package com.example.faithconx.signup.viewmodel

import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.faithconx.util.Constants
import com.google.firebase.storage.FirebaseStorage

class ImageStorageViewModel {
    private val isImageSaved= MutableLiveData<Boolean>()
    private val downloadedUrl = MutableLiveData<Uri>()


    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveToDb(firstName:String,lastName:String, email:String, number:String,filePathUri: Uri?) {
//        upload image first
        uploadProfileImage(firstName,lastName,email,number,filePathUri)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun uploadProfileImage(
        firstName: String,
        lastName: String,
        email: String,
        number: String,
        filePathUri: Uri?
    ) {
        val storageReference =
            FirebaseStorage.getInstance().getReference(Constants.PROFILE_IMAGES_ROOT_KEY)
                .child(Constants.IMAGE_URL_PREFIX + firebaseAuth.currentUser?.uid)
        filePathUri?.let { filePathUri ->
            storageReference.putFile(filePathUri)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.i("TAG", "Image uploaded successfully.")
                        storageReference.downloadUrl.addOnSuccessListener { uri ->
                            //Save complete user cred
                            isImageSaved.postValue(true)
                            downloadedUrl.postValue(uri)
                            saveUserCredential( firstName,lastName,email,number,uri.toString())
                            Log.i("TAG", "Downloaded img uri successfully: ${uri.toString()}")
                        }
                    } else {
                        isImageSaved.postValue(false)
                    }
                }
        }
    }
}