package com.example.trackrrv1

import android.content.ContentResolver
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask


class FirebaseStorageViewModel: ViewModel() {
    lateinit var mImageUri: Uri

    var mStorageRef: StorageReference =  FirebaseStorage.getInstance().getReference("uploads")
    var mDatabaseRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("uploads")




}