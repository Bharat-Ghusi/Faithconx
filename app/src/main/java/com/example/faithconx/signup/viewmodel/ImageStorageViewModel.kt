package com.example.faithconx.signup.viewmodel

import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.faithconx.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class ImageStorageViewModel {
    private val firebaseAuth =  FirebaseAuth.getInstance()
    private val isImageSaved= MutableLiveData<Boolean>()
    private val imageUri = MutableLiveData<Uri>()

    fun isImageSaved():LiveData<Boolean> = isImageSaved
    fun getDownloadedImageUri():LiveData<Uri> = imageUri
 

    @RequiresApi(Build.VERSION_CODES.O)
     fun uploadProfileImage( filePathUri: Uri?) {
        val storageReference =
            FirebaseStorage.getInstance().getReference(Constants.PROFILE_IMAGES_ROOT_KEY)
                .child(Constants.IMAGE_URL_PREFIX + firebaseAuth.currentUser?.uid)
        filePathUri?.let { filePathUri ->
            storageReference.putFile(filePathUri)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        isImageSaved.postValue(true)
                        Log.i("TAG", "Image uploaded successfully.")
                        storageReference.downloadUrl.addOnSuccessListener { uri ->
                            //Save complete user cred

                            imageUri.postValue(uri)
//                            saveUserCredential( firstName,lastName,email,number,uri.toString())
                            Log.i("TAG", "Downloaded img uri successfully: ${uri.toString()}")
                        }
                    } else {
                        isImageSaved.postValue(false)
                        Log.i("TAG", "Image upload failed.")
                    }
                }
        }
    }
}