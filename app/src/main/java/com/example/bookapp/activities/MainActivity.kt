package com.example.bookapp.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.bookapp.AppUtilities
import com.example.bookapp.NavGraphMainActivityDirections
import com.example.bookapp.R
import com.example.bookapp.dagger.*
import com.example.bookapp.fragments.MessagesFragment
import com.example.bookapp.models.MessageDTO
import com.example.bookapp.models.User
import com.example.bookapp.services.MessengerService
import com.example.bookapp.viewModels.ViewModelChat
import com.example.bookapp.viewModels.ViewModelPost
import com.example.bookapp.viewModels.ViewModelUser
import com.example.dataLayer.interfaces.dao.ChatDao
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.layout_main_activity.view.*
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@InternalCoroutinesApi
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val viewModelPost: ViewModelPost by viewModels()
    private val viewModelUser: ViewModelUser by viewModels()
    private val viewModelChat: ViewModelChat by viewModels()
    private var mBound: Boolean = false
    private lateinit var mService: MessengerService

    /** Defines callbacks for service binding, passed to bindService()  */
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as MessengerService.LocalBinder
            mService = binder.getService()
            (application as MyApplication).appComponent.inject(mService)
            mBound = true
            mService.pendingIntent = getPendingIntent()
            mService.shouldPlayNotification = false

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

        executeDefaultOperations()
        startDagger(getCurrentUser())
        createMessageNotificationChannel()

        startMessengerService()

        setContentView(R.layout.layout_main_activity)
        configureNavigationView()


        viewModelChat.chatLink.observe(this, Observer {
            mService.chatLink = it.hubURL
        })

    }


    override fun onStop() {
        super.onStop()
        mBound = false
        if (this::mService.isInitialized) {
            mService.shouldPlayNotification = true
        }
    }


    private fun startMessengerService() {
        Intent(this, MessengerService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
        mBound = false
    }

    private fun executeDefaultOperations() {
        sharedPreferences = getSharedPreferences(getString(R.string.key_preferences), Context.MODE_PRIVATE)
    }

    override fun onStart() {
        super.onStart()
        //fetch new notifications every time on start is called
        viewModelChat.fetchChatNotifications.value = true
        createMessageNotificationChannel()
    }

    private fun startDagger(user: User) {
        (application as MyApplication).appComponent = DaggerAppComponent.factory().create(applicationContext, viewModelPost.viewModelScope, user)
        val appComponent = (application as MyApplication).appComponent

        viewModelChat.user.value = user

        appComponent.inject(this)
        appComponent.inject(viewModelPost)
        appComponent.inject(viewModelUser)
        appComponent.inject(viewModelChat)


    }


    private fun getCurrentUser(): User {
        val userID = sharedPreferences.getInt(getString(R.string.key_user_id), 0)
        return User(userID = userID,
                username = sharedPreferences.getStringNotNull(R.string.key_email),
                email = sharedPreferences.getStringNotNull(R.string.key_username),
                profilePicture = "")
    }


    private fun startWelcomeActivity() {
        val intent = Intent(this, WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }


    private fun configureNavigationView() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        val navController = navHostFragment!!.navController

        NavigationUI.setupWithNavController(bottomNavigationView,
                navController)
        showNotifications(bottomNavigationView)

    }

    private fun showNotifications(bottomNavigationView: BottomNavigationView) {
        val chatBadge = bottomNavigationView.getOrCreateBadge(
                R.id.friends
        )
        viewModelChat.chatNotifications.observe(this, Observer {
            if (it.isNotEmpty()) {
                chatBadge.number = it.size
                chatBadge.isVisible = true
            } else {
                chatBadge.isVisible = false
            }
        })
        viewModelChat.fetchChatNotifications.value = true
    }


    private fun SharedPreferences.getStringNotNull(keyID: Int
    ): String {
        val value = getString(getString(keyID), "unknown")
        value?.let { return it }
        return "Unknown"
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 1) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try { // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
            } catch (e: ApiException) { // Google Sign In failed, update UI appropriately
// ...
//     Snackbar.make(findViewById(R.id.nav_graph_main_activity), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
            }
        }
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