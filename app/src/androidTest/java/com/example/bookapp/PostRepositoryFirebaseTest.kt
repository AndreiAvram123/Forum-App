package com.example.bookapp

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.example.bookapp.models.Post
import com.example.dataLayer.models.PostDTO
import com.example.dataLayer.models.UserDTO
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Assert.assertNotNull
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import java.io.File
import java.lang.Exception
import java.util.*


class PostRepositoryFirebaseTest {
    private val tag = PostRepositoryFirebaseTest::class.simpleName
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: StorageReference

    private val collectionName = "posts"
    private lateinit var context: Context

    @Before
    fun setUp() {
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance().reference
        context = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun shouldPushedDataBeReturned() {
        runBlocking {
            val testPost = PostDTO(id = 1, title = "testTitle", image = "image",
                    content = "test", date = Calendar.getInstance().timeInMillis / 1000,
                    author = UserDTO(userID = 109, username = "Andrei",
                            email = "cactus@gmail.com", profilePicture = ""))
            try {
                db.collection(collectionName).add(testPost).await()
                val data = db.collection(collectionName).whereEqualTo(PostDTO::id.name, testPost.id).get().await().documents
                data.map { it.toObject(PostDTO::class.java) }.find { it?.id == testPost.id }.also {
                    if (it == null) {
                        fail()
                    } else {
                        assert(true)
                    }
                }

            } catch (e: Exception) {
                Log.d("error", e.printStackTrace().toString())
                fail()
            }
        }
    }

    @Test
    fun shouldRemoveAddedPost() {
        runBlocking {
            val testPost = Post(id = Calendar.getInstance().timeInMillis.toInt(), title = "testTitle", image = "image",
                    content = "test", date = Calendar.getInstance().timeInMillis / 1000, authorID = 109)
            try {
                //add post
                val id = db.collection(collectionName).add(testPost).await().id

                //remove post
                db.collection(collectionName).document(id).delete().await()

                //query the post
                val data = db.collection(collectionName).whereEqualTo(Post::id.name, testPost.id).get().await().documents
                data.map { it.toObject(Post::class.java) }.find { it?.id == testPost.id }.also {
                    if (it == null) {
                        assert(true)
                    }
                }

            } catch (e: Exception) {
                Log.d("error", e.printStackTrace().toString())
                fail()
            }
        }
    }


    @Test
    fun shouldReturnRecentPosts() = runBlocking {
        val fetchedData = db.collection(collectionName).orderBy(Post::date.name, Query.Direction.DESCENDING).limit(10).get().await()
        val posts = fetchedData.documents.map { it.toObject(Post::class.java) }
        assertNotNull(posts)
    }

    @Test
    fun shouldGetImage() = runBlocking<Unit> {
        val localFile: File = File.createTempFile("images", "jpg")
        val reference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://freelanceproject-f7aef.appspot.com/background.png")
        reference.getFile(localFile).await()
        val uri = Uri.fromFile(localFile)
        val drawable = uri.toDrawable(context)
        Log.d(tag, drawable.toString())
        assertNotNull(drawable)
    }

    @Test
    fun shouldDownloadVideo() = runBlocking<Unit> {
        val localFile: File = File.createTempFile("videos", "wmw")
        val url = "gs://freelanceproject-f7aef.appspot.com/app movie.wmv"
        val reference = FirebaseStorage.getInstance().getReferenceFromUrl(url)
        reference.getFile(localFile).await()
        Log.d(tag, localFile.absolutePath)
    }

    @Test
    fun shouldUploadFile() = runBlocking<Unit> {
        val localFile: File = File.createTempFile("images", "jpg")
        val reference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://freelanceproject-f7aef.appspot.com/background.png")
        reference.getFile(localFile).await()
        storage.child("testImage").putFile(Uri.fromFile(localFile)).await()
    }

    @Test
    fun shouldUploadVideo() = runBlocking<Unit> {
        val localFile: File = File.createTempFile("images", "mp4")
        val url = "gs://freelanceproject-f7aef.appspot.com/testVideo.mp4"
        val reference = FirebaseStorage.getInstance().getReferenceFromUrl(url)
        reference.getFile(localFile).await()
        storage.child("testVideo").putFile(Uri.fromFile(localFile)).await()
    }

}