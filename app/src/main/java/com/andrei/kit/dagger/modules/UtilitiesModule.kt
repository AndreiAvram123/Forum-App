package com.andrei.kit.dagger.modules

import android.content.Context
import android.content.SharedPreferences
import com.andrei.kit.R
import com.andrei.kit.models.User
import com.andrei.dataLayer.dataMappers.toUser
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.InternalCoroutinesApi
import pl.aprilapps.easyphotopicker.ChooserType
import pl.aprilapps.easyphotopicker.EasyImage

@InternalCoroutinesApi
@InstallIn(ActivityComponent::class)
@Module
class UtilitiesModule {

    @Provides
    fun getSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(context.getString(R.string.key_preferences), Context.MODE_PRIVATE)
    }


    @Provides
    fun user(): User = FirebaseAuth.getInstance().currentUser!!.toUser()


    @Provides
    fun getEasyImage (@ApplicationContext context: Context) : EasyImage {
        return  EasyImage.Builder(context) // Chooser only
                // Will appear as a system chooser title, DEFAULT empty string
                //.setChooserTitle("Pick media")
                // Will tell chooser that it should show documents or gallery apps
                //.setChooserType(ChooserType.CAMERA_AND_DOCUMENTS)  you can use this or the one below
                //.setChooserType(ChooserType.CAMERA_AND_GALLERY)
                // Setting to true will cause taken pictures to show up in the device gallery, DEFAULT false
                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
                .setCopyImagesToPublicGalleryFolder(true) // Sets the name for images stored if setCopyImagesToPublicGalleryFolder = true
                .setFolderName("EasyImage sample") // Allow multiple picking
                .allowMultiple(true)
                .build()
    }
}
