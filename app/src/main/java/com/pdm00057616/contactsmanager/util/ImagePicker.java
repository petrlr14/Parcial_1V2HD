package com.pdm00057616.contactsmanager.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;

public class ImagePicker {
    public static void pickImage(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        System.out.println(getMaxSize(activity));
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("outputX",getMaxSize(activity));
        intent.putExtra("outputY", getMaxSize(activity));
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, 1);
    }

    private static int getMaxSize(Context context){
        Uri uri=ContactsContract.DisplayPhoto.CONTENT_MAX_DIMENSIONS_URI;
        final String[] projection = new String[] { ContactsContract.DisplayPhoto.DISPLAY_MAX_DIM };
        final Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
        try {
            c.moveToFirst();
            return c.getInt(0);
        } finally {
            c.close();
        }
    }
}
