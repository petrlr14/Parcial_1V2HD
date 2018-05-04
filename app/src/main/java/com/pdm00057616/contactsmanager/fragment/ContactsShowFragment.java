package com.pdm00057616.contactsmanager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pdm00057616.contactsmanager.model.Contact;

import java.util.ArrayList;

public class ContactsShowFragment extends Fragment {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public static ContactsShowFragment newInstance(int type, ArrayList<Contact> contacts) {
        ContactsShowFragment fragment = new ContactsShowFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("list", contacts);
        fragment.setArguments(args);
        return fragment;
    }

}
