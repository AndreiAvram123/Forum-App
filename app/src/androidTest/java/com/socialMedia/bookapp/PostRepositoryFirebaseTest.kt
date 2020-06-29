package com.socialMedia.bookapp

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.socialMedia.bookapp.models.Post
import com.socialMedia.dataLayer.models.PostDTO
import com.socialMedia.dataLayer.models.UserDTO
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
            val testPost = PostDTO(title = "testTitle", image = "gs://freelanceproject-f7aef.appspot.com/testImage",
                    content = "test", date = Calendar.getInstance().timeInMillis / 1000,
                    author = UserDTO(userID = 109, username = "Andrei",
                            email = "cactus@gmail.com", profilePicture = ""))
            try {
                db.collection(collectionName).add(testPost).await()
                val data = db.collection(collectionName).whereEqualTo(PostDTO::date.name, testPost.date).get().await().documents
                if (data.isEmpty()) {
                    fail()
                } else {
                    assert(true)
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
            val testPost = PostDTO(title = "testTitle", image = "https://firebasestorage.googleapis.com/v0/b/freelanceproject-f7aef.appspot.com/o/background.png?alt=media&token=6a78ce1f-ca80-4108-a1f2-8e3ac07dcd7d",
                    content = "test", date = Calendar.getInstance().timeInMillis / 1000, author = UserDTO())
            try {
                //add post
                val id = db.collection(collectionName).add(testPost).await().id

                //remove post
                db.collection(collectionName).document(id).delete().await()

                //query the post
                val data = db.collection(collectionName).whereEqualTo(Post::date.name, testPost.date).get().await().documents
                if (data.isNullOrEmpty()) {
                    fail()
                } else {
                    assert(true)
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
        //create temporary file
        val localFilePath = Uri.fromFile(File.createTempFile("images", "jpg"))
        //get a file from firebase
        val reference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://freelanceproject-f7aef.appspot.com/background.png")
        reference.getFile(localFilePath).await()

        //upload the same file
        val uploadTask = storage.child("images/${localFilePath.lastPathSegment}").putFile(localFilePath).await()
        //get a download url
        val path = FirebaseStorage.getInstance().getReference("testImage").downloadUrl.await().toString()
        print(path)
    }

    @Test
    fun shouldUploadVideo() = runBlocking<Unit> {
        val localFile: File = File.createTempFile("images", "mp4")
        val url = "gs://freelanceproject-f7aef.appspot.com/testVideo.mp4"
        val reference = FirebaseStorage.getInstance().getReferenceFromUrl(url)
        reference.getFile(localFile).await()
        storage.child("testVideo").putFile(Uri.fromFile(localFile)).await()
    }

    @Test
    fun shouldUploadFileAndDownloadViaURL() = runBlocking<Unit> {
        //create temp file and download from known path in firebase
        val localFilePath = Uri.fromFile(File.createTempFile("images", "jpg"))
        //get a file from firebase
        val reference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://freelanceproject-f7aef.appspot.com/background.png")
        reference.getFile(localFilePath).await()

        //upload the file
        //upload the same file
        val uploadPath = "images/${localFilePath.lastPathSegment}"
        val uploadTask = storage.child(uploadPath).putFile(localFilePath).await()

        //get full url path
        val path = FirebaseStorage.getInstance().getReference(uploadPath).downloadUrl.await()
        //create a temp file and download via full url
        val temp = Uri.fromFile(File.createTempFile("images", "jpg"))
        val ref = FirebaseStorage.getInstance().getReferenceFromUrl(path.toString())
        ref.getFile(temp).await()
    }

    @Test
    fun shouldFetchPostByUID() = runBlocking<Unit>{
        val uid = "FFxaWf9XcaipO9OYfwYC"
        val post = db.collection(collectionName).document(uid).get().await().toObject(PostDTO::class.java)
        assertNotNull(post)
    }

}