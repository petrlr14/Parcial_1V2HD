package com.pdm00057616.contactsmanager.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import com.pdm00057616.contactsmanager.model.Contacts;
import com.pdm00057616.contactsmanager.model.Email;
import com.pdm00057616.contactsmanager.model.PhoneNumber;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ContactGetter {

    public static ArrayList<Contacts> getContacts(Context context) {
        ArrayList<Contacts> list = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
                        ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id)));
                Bitmap photo = null;
                if (inputStream != null) {
                    photo = BitmapFactory.decodeStream(inputStream);
                }
                if(cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    List<Integer> aux=new ArrayList<>();
                    int auxEntero;
                    while (cursorInfo.moveToNext()) {
                        Contacts info = new Contacts(null, null, null, null, null, false, null, null);
                        if(!aux.contains(Integer.parseInt(id))){
                            info.setId(id);
                            info.setName(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                            info.setNumbers(getContactPhones(context, id));
                            info.setPhoto(photo);
                            info.setEmail(getContactEmail(context, id));
                            info.setBirthday(getBirthday(context, id));
                            list.add(info);
                        }
                        auxEntero=Integer.parseInt(id);
                        aux.add(auxEntero);
                    }
                }
                cursorInfo.close();
            }
            cursor.close();
        }
        return list;
    }

    public static ArrayList<PhoneNumber> getContactPhones(Context context, String id) {
        ArrayList<PhoneNumber> phoneList = new ArrayList<>();
        Uri phonesUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor phones = context.getContentResolver().query(phonesUri, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
        while (phones.moveToNext()) {
            String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));
            int type = phones.getInt(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
            phoneList.add(new PhoneNumber(number, type));
        }
        phones.close();
        return phoneList;
    }

    public static ArrayList<Email> getContactEmail(Context context, String id){
        ArrayList<Email> emailList = new ArrayList<>();
        Uri emailUri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        Cursor email = context.getContentResolver().query(emailUri, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
        while (email.moveToNext()) {
            String mail = email.getString(email.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            int type = email.getInt(email.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
            emailList.add(new Email(mail, type));
        }
        email.close();
        return emailList;
    }

    public static String getBirthday(Context context, String id){
        String birthday="";
        Cursor bdc = context.getContentResolver().query(android.provider.ContactsContract.Data.CONTENT_URI, new String[] { ContactsContract.CommonDataKinds.Contactables.TYPE }, android.provider.ContactsContract.Data.CONTACT_ID+" = "+id+" AND "+ ContactsContract.Data.MIMETYPE+" = '"+ ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE+"' AND "+ ContactsContract.CommonDataKinds.Event.TYPE+" = "+ ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY, null, android.provider.ContactsContract.Data.DISPLAY_NAME);
        if (bdc.getCount() > 0) {
            while (bdc.moveToNext()) {
                birthday = bdc.getString(0);
            }
        }
        bdc.close();
        return birthday;
    }

}
