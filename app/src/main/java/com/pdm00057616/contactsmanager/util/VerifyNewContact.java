package com.pdm00057616.contactsmanager.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.pdm00057616.contactsmanager.R;
import com.pdm00057616.contactsmanager.model.Address;
import com.pdm00057616.contactsmanager.model.Email;
import com.pdm00057616.contactsmanager.model.PhoneNumber;

import java.util.ArrayList;

public class VerifyNewContact {

    public static boolean isReady(String name, String birthday, ArrayList<PhoneNumber> phones, ArrayList<Email> emails, ArrayList<Address> addresses, Context context){
        if(name.equals("")&&birthday.equals("")&&phones.size()==0&&emails.size()==0&&addresses.size()==0){
            Toast.makeText(context, context.getResources().getString(R.string.toast1), Toast.LENGTH_SHORT).show();
            return false;
        }else{
            if(!name.equals("")){
                System.out.println(phones.size()+"  "+emails.size()+"   "+addresses.size());
                if(phones.size()>0||emails.size()>0||addresses.size()>0){
                    return true;
                }else{
                    Toast.makeText(context, context.getResources().getString(R.string.toast2), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else{
                Toast.makeText(context, context.getResources().getString(R.string.toast3), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

}
