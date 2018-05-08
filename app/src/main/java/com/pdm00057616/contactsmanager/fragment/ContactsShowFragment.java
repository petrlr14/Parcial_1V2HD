package com.pdm00057616.contactsmanager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pdm00057616.contactsmanager.R;
import com.pdm00057616.contactsmanager.activity.MainActivity;
import com.pdm00057616.contactsmanager.activity.ViewInfoActivity;
import com.pdm00057616.contactsmanager.adapter.RecyclerViewAdapter;
import com.pdm00057616.contactsmanager.model.Contacts;

import java.util.ArrayList;

public class ContactsShowFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    int type;
    ArrayList<Contacts> contacts;
    ArrayList<Contacts> contactsFav;


    public static ContactsShowFragment newInstance(int type, ArrayList<Contacts> contacts) {
        ContactsShowFragment fragment = new ContactsShowFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putParcelableArrayList("list", contacts);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contacts_view_fragment, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        adapter = new RecyclerViewAdapter(getInternalFilterContact()) {
            @Override
            public void setOnClickCardViewAction(View view, int position) {
                openViewActivity(position);
            }

            @Override
            public void setOnClickButtonAction(View view, int position) {
                setFav(position);
                MainActivity.viewPagerAdapter.notifyDataSetChanged();
            }
        };
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt("type");
            contacts=MainActivity.contacts;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(type==1){
            contacts=MainActivity.contacts;
            contactsFav=getInternalFilterContact();
        }
    }

    public ArrayList<Contacts> getInternalFilterContact() {
        contactsFav = new ArrayList<>();
        if (type == 1) {
            return contacts;
        } else {
            for (Contacts c : contacts) {
                if (c.getFav()) {
                    contactsFav.add(c);
                }
            }
            return contactsFav;
        }
    }

    private void setFav(int position) {
        if (type == 1) {
            boolean fav = contacts.get(position).getFav();
            if (fav) {
                contacts.get(position).setFav(false);
                System.out.println();
            } else {
                contacts.get(position).setFav(true);
            }
        } else {
            boolean fav = contactsFav.get(position).getFav();
            if (fav) {
                contactsFav.get(position).setFav(false);
            } else {
                contactsFav.get(position).setFav(true);
            }
        }
    }

    public void openViewActivity(int position) {
        Intent intent = new Intent(getContext(), ViewInfoActivity.class);
        if (type == 1) {
            intent.putExtra("contact", position);
            intent.putExtra("type", 1);
            System.out.println(MainActivity.contacts.get(position).getFav());
        } else {
            intent.putExtra("contact", contactsFav.indexOf(contactsFav.get(position)));
            intent.putExtra("type", 2);
        }
        startActivity(intent);
    }

    public void setListFilter(ArrayList<Contacts> fiter) {
        adapter.setFilter(fiter);
    }

    public ArrayList<Contacts> getList(int type){
        switch (type){
            case 0:return contacts;
            default:return contactsFav;
        }
    }

}
