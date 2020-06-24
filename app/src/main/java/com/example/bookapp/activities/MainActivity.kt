package com.example.bookapp.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.os.*
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.bookapp.R
import com.example.bookapp.dagger.MyApplication
import com.example.bookapp.databinding.DrawerHeaderBinding
import com.example.bookapp.databinding.LayoutMainActivityBinding
import com.example.bookapp.services.*
import com.example.bookapp.user.UserAccountManager
import com.example.bookapp.viewModels.ViewModelChat
import com.example.bookapp.viewModels.ViewModelComments
import com.example.bookapp.viewModels.ViewModelPost
import com.example.bookapp.viewModels.ViewModelUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@InternalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var userAccountManager: UserAccountManager


    private val viewModelChat: ViewModelChat by viewModels()
    private var mBound: Boolean = false
    private lateinit var serviceMessenger: Messenger
    private lateinit var binding: LayoutMainActivityBinding

    /** Defines callbacks for service binding, passed to bindService()  */
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            mBound = true
            serviceMessenger = Messenger(service)
            stopNotificationSound()

            userAccountManager.user.value?.let {
                val userIDMessage = Message.obtain(null, new_user_id_message, it.userID, 0)
                serviceMessenger.send(userIDMessage)
            }
            viewModelChat.chatLink.observe(this@MainActivity, Observer {
                it?.let { link ->
                    val message = Message.obtain(null, new_chat_link_message)
                    Bundle().apply { putString(key_chats_link, link) }.also { bundle -> message.data = bundle }
                    serviceMessenger.send(message)
                }
            })
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(this, 0, intent, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.layout_main_activity)

        startDagger()
        createMessageNotificationChannel()
        configureNavigation()


    }


    override fun onStop() {
        super.onStop()
        if (mBound) {
            playNotificationOnNewMessage()
            unbindService(connection)
            mBound = false
        }
    }

    private fun playNotificationOnNewMessage() {
        val playNotification = Message.obtain(null, play_notification_message, 0, 0)
        serviceMessenger.send(playNotification)

    }

    private fun stopNotificationSound() {
        if (mBound) {
            val stopNotification = Message.obtain(null, stop_notification_message, 0, 0)
            serviceMessenger.send(stopNotification)
        }
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

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
        mBound = false
    }


    override fun onStart() {
        super.onStart()
        //fetch new notifications every time on start is called
        createMessageNotificationChannel()
        startMessengerService()
        bindToMessengerService()
        stopNotificationSound()
    }

    private fun startDagger() {
//        (application as MyApplication).appComponent = DaggerAppComponent.factory().create((application as MyApplication)
//                , viewModelPost.viewModelScope)
//
//        val appComponent = (application as MyApplication).appComponent



//        appComponent.inject(viewModelPost)
//        appComponent.inject(viewModelUser)
//        appComponent.inject(viewModelChat)
//        appComponent.inject(viewModelComment)


    }


    private fun configureNavigation() {

        val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment?

        val navController = navHostFragment!!.navController

        NavigationUI.setupWithNavController(binding.bottomNavigation,
                navController)

        showNotifications()

        configureDrawer(navController)

    }


    private fun configureDrawer(navController: NavController) {
        val drawer = binding.drawerLayout
        val appBarConfiguration = AppBarConfiguration(navController.graph, drawer)
        binding.navView.setupWithNavController(navController)


        val headerBinding = DrawerHeaderBinding.bind(binding.navView.getHeaderView(0))
        headerBinding.user = userAccountManager.user.value

    }

    private fun showNotifications() {
        val chatBadge = binding.bottomNavigation.getOrCreateBadge(
                R.id.friends
        )
        viewModelChat.lastMessageChats.observe(this, Observer {
            if (it.isNotEmpty()) {
                chatBadge.number = it.size
                chatBadge.isVisible = true
            } else {
                chatBadge.isVisible = false
            }
        })
    }


    private fun createMessageNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.message_channel)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(getString(R.string.message_channel_id), name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }
}