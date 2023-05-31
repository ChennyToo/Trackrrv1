package com.example.trackrrv1

class FirebaseUpload() {
    lateinit var mName : String
    lateinit var mImageUrl: String

    fun Upload(name : String, imageUrl : String) {
        var name2 = name
        if (name.trim().equals("")) {
            name2 = "No Name";
        }

        mName = name;
        mImageUrl = imageUrl;
    }

    fun getName()  : String{
        return mName;
    }

    fun setName(name : String) {
        mName = name;
    }

    fun getImageUrl() : String{
        return mImageUrl;
    }
}