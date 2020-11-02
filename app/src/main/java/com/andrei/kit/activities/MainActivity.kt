package com.andrei.kit.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
import android.os.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.andrei.kit.R
import com.andrei.kit.databinding.DrawerHeaderBinding
import com.andrei.kit.databinding.LayoutMainActivityBinding
import com.andrei.kit.services.*
import com.andrei.kit.user.UserAccountManager
import com.andrei.kit.viewModels.ViewModelChat
import com.andrei.dataLayer.dataMappers.toUser
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

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
            FirebaseAuth.getInstance().currentUser?.let{
              //  val userIDMessage = Message.obtain(null, new_user_id_message, it.userID, 0)
              //  serviceMessenger.send(userIDMessage)
            }
            viewModelChat.chatLink.observe(this@MainActivity, {
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.layout_main_activity)
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
        AppBarConfiguration(navController.graph, drawer)
        binding.navView.setupWithNavController(navController)


        val headerBinding = DrawerHeaderBinding.bind(binding.navView.getHeaderView(0))
        headerBinding.user = FirebaseAuth.getInstance().currentUser!!.toUser()

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