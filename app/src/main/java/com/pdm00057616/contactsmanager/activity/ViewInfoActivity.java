package com.pdm00057616.contactsmanager.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pdm00057616.contactsmanager.R;
import com.pdm00057616.contactsmanager.adapter.RecyclerViewIndividaulContactAdapter;
import com.pdm00057616.contactsmanager.model.Contacts;
import com.pdm00057616.contactsmanager.util.ImagePicker;

public class ViewInfoActivity extends AppCompatActivity {

    //Views
    ImageView imageViewPhoto;
    TextView textViewName, textViewPhone, textViewEmail, textViewBirthday;
    RecyclerView recyclerViewNumbers, recyclerViewEmail, recyclerViewAddress;
    Activity activity=this;
    Toolbar toolbar;
    //
    Contacts contact;
    RecyclerViewIndividaulContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_info);
        Intent intent=getIntent();
        bindViews();
        setInfo(intent);
        initComponentsNumber();
        initComponentsEmail();
        initComponentsAddress();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void bindViews() {
        imageViewPhoto=findViewById(R.id.image_view_photo);
        textViewName=findViewById(R.id.textview_name_contact);
        textViewPhone=findViewById(R.id.textview_phone_contact);
        textViewEmail=findViewById(R.id.textview_email_contact);
        textViewBirthday=findViewById(R.id.textview_birthday_contact);
        recyclerViewNumbers =findViewById(R.id.phones_container);
        recyclerViewEmail =findViewById(R.id.email_container);
        toolbar=findViewById(R.id.toolbar_view_info);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    private void initComponentsNumber(){
        recyclerViewNumbers.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        adapter= new RecyclerViewIndividaulContactAdapter(contact, 1) {
            @Override
            public void setOnclickPhone(View v, int position) {
                makeCall(position);
            }
        };
        recyclerViewNumbers.setAdapter(adapter);
        recyclerViewNumbers.setLayoutManager(layoutManager);
    }
    private void initComponentsEmail(){
        recyclerViewEmail.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        adapter= new RecyclerViewIndividaulContactAdapter(contact, 2) {
            @Override
            public void setOnclickPhone(View v, int position) {
                ImagePicker.pickImage(activity);
            }
        };
        recyclerViewEmail.setAdapter(adapter);
        recyclerViewEmail.setLayoutManager(layoutManager);
    }
    private void initComponentsAddress(){
        recyclerViewAddress.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        adapter= new RecyclerViewIndividaulContactAdapter(contact, 3) {
            @Override
            public void setOnclickPhone(View v, int position) {
            }
        };
        recyclerViewAddress.setAdapter(adapter);
        recyclerViewAddress.setLayoutManager(layoutManager);
    }

    private void setInfo(Intent intent){
        Bundle bundle=intent.getExtras();
        contact=bundle.getParcelable("contact");
        if(contact.getPhoto()!=null){
            imageViewPhoto.setImageBitmap(contact.getPhoto());
        }else{
            imageViewPhoto.setImageResource(R.drawable.user);
        }
        textViewName.setText(contact.getName());
        textViewBirthday.setText(setBirthday());

    }

    private String setBirthday(){
        String birthday="";
        if(contact.getBirthday().equals("")){
            birthday="no textViewBirthday set";
        }else{
            birthday=contact.getBirthday();
        }
        return birthday;

    }

    private void makeCall(int position){
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact.getNumbers().get(position).getNumber()));
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
            startActivity(intent);
        }
    }


}
