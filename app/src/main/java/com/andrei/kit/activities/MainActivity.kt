package com.andrei.kit.activities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.andrei.dataLayer.dataMappers.toUser
import com.andrei.dataLayer.engineUtils.Status
import com.andrei.kit.R
import com.andrei.kit.databinding.DrawerHeaderBinding
import com.andrei.kit.databinding.LayoutMainActivityBinding
import com.andrei.kit.models.User
import com.andrei.kit.services.*
import com.andrei.kit.user.UserAccountManager
import com.andrei.kit.utils.*
import com.andrei.kit.viewModels.ViewModelChat
import com.andrei.kit.viewModels.ViewModelUser
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource
import javax.inject.Inject

private const val requestCodePermissionCamera = 44
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var userAccountManager: UserAccountManager

    @Inject
    lateinit var user:User


    @Inject
    lateinit var easyImage: EasyImage
    @Inject
    lateinit var permissionManager: PermissionManager

    private val viewModelChat: ViewModelChat by viewModels()
    private val viewModelUser: ViewModelUser by viewModels()

    private var mBound: Boolean = false
    private lateinit var serviceMessenger: Messenger
    private lateinit var binding: LayoutMainActivityBinding

    /** Defines callbacks for service binding, passed to bindService()  */
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            mBound = true
            serviceMessenger = Messenger(service)

            viewModelChat.chatLink.reObserve(this@MainActivity, {
                it?.let { link ->
                    val message = Message.obtain(null, new_chat_link_message)
                    val bundle = Bundle().apply { putString(key_chats_link, link) }
                    message.data = bundle
                    serviceMessenger.send(message)
                }
            })
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.layout_main_activity)
        configureNavigation()


    }



    private fun startMessengerService() {
        Intent(this, MessengerService::class.java).also {
            startService(it)
        }
    }

    private fun bindToMessengerService() {
        Intent(this, MessengerService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        if (mBound) {
            unbindService(connection)
            mBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        startMessengerService()
        bindToMessengerService()
    }



    private fun configureNavigation() {

        val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment?

        val navController = navHostFragment!!.navController

        NavigationUI.setupWithNavController(binding.bottomNavigation,
                navController)


        configureDrawer(navController)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        easyImage.handleActivityResult(requestCode, resultCode, data, this, object : DefaultCallback() {
            override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
              if(imageFiles.isNotEmpty()){
                      val imageData = imageFiles.first().file.toUri().toDrawable(this@MainActivity).toBase64()
                      viewModelUser.changeProfilePicture(imageData,
                              FirebaseAuth.getInstance().currentUser!!).observeRequest(this@MainActivity,{
                              if(it.status == Status.SUCCESS){
                                  Snackbar.make(binding.root,"Profile image changed, please restart the app",Snackbar.LENGTH_SHORT).show()
                            }
                      })
              }
            }

            override fun onImagePickerError(error: Throwable, source: MediaSource) {
                //Some error handling
                error.printStackTrace()
            }

            override fun onCanceled(@NonNull source: MediaSource) {
                //Not necessary to remove any files manually anymore
            }
        })
    }

    private fun configureDrawer(navController: NavController) {
        val drawer = binding.drawerLayout
        AppBarConfiguration(navController.graph, drawer)
        binding.navView.setupWithNavController(navController)

        val headerBinding = DrawerHeaderBinding.bind(binding.navView.getHeaderView(0))
        headerBinding.user = user
        headerBinding.navHeaderImageView.setOnClickListener {
           if(permissionManager.hasPermission(android.Manifest.permission.CAMERA)) {
               easyImage.openChooser(this)
           }else{
               requestSinglePermission(android.Manifest.permission.CAMERA, requestCodePermissionCamera)
           }

       }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == requestCodePermissionCamera){
            if(permissionManager.isPermissionGranted(grantResults)){
                easyImage.openChooser(this)
            }
        }
    }
}