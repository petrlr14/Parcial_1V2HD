package com.pdm00057616.contactsmanager.model

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class Contact(var name:String?, var number:String?, @Transient var photo:Bitmap?, @Transient var photoUri: Uri?, var id:String?, var fav:Boolean?):Serializable, Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Bitmap::class.java.classLoader),
            parcel.readParcelable(Uri::class.java.classLoader),
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(number)
        parcel.writeParcelable(photo, flags)
        parcel.writeParcelable(photoUri, flags)
        parcel.writeString(id)
        parcel.writeValue(fav)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Contact> {
        override fun createFromParcel(parcel: Parcel): Contact {
            return Contact(parcel)
        }

        override fun newArray(size: Int): Array<Contact?> {
            return arrayOfNulls(size)
        }
    }
}
