package com.monke.machnomusic3.data.remote.storage

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class Storage @Inject constructor(
    private val storage: FirebaseStorage
) {

    suspend fun uploadFileWithUri(uri: Uri, path: String): Result<Any?> {
        try {
            val trackRef = storage.reference.child(path)
            val result = trackRef.putFile(uri).await()
            Log.d("Storage", "File $path has been uploaded")
            return Result.success(null)
        } catch (exception: Exception) {
            exception.printStackTrace()
            return Result.failure(exception)
        }
    }

    suspend fun getDownloadUrl(path: String): Result<String> {
        try {
            val ref = storage.getReference(path)
            val url = ref.downloadUrl.await()
            return Result.success(url.toString())
        } catch (exception: Exception) {
            exception.printStackTrace()
            return Result.failure(exception)
        }
    }
}