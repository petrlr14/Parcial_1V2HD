package com.pdm00057616.contactsmanager.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.karan.churi.PermissionManager.PermissionManager;
import com.pdm00057616.contactsmanager.R;
import com.pdm00057616.contactsmanager.adapter.RecyclerViewIndividaulContactAdapter;
import com.pdm00057616.contactsmanager.model.Address;
import com.pdm00057616.contactsmanager.model.Contacts;
import com.pdm00057616.contactsmanager.model.Email;
import com.pdm00057616.contactsmanager.model.PhoneNumber;

import java.util.ArrayList;

public class ViewInfoActivity extends AppCompatActivity {

    //Views
    ImageView imageViewPhoto;
    TextView textViewName, textViewPhone, textViewEmail, textViewBirthday, textViewAddress, textViewBirthdayLable;
    RecyclerView recyclerViewNumbers, recyclerViewEmail, recyclerViewAddress;
    Activity activity = this;
    Toolbar toolbar;
    ImageButton imageButtonShare;
    //
    Contacts contact;
    RecyclerViewIndividaulContactAdapter adapter;
    PermissionManager permissionManager;
    int type, indice;
    boolean resume = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_info);
        permissionManager = new PermissionManager() {
        };
        Intent intent = getIntent();
        bindViews();
        setInfo(intent);
        initComponentsNumber();
        initComponentsEmail();
        initComponentsAddress();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit, menu);
        return true;
    }

    //al regresar de la pantalla de edit se vuelven a cargar los elementos textview
    @Override
    protected void onResume() {
        super.onResume();
        if (resume) {
            restoreInfo();
        }
        resume = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                opneEditActivity();
                return true;
        }
    }

    private void bindViews() {
        imageViewPhoto = findViewById(R.id.image_view_photo);
        textViewName = findViewById(R.id.textview_name_contact);
        textViewPhone = findViewById(R.id.textview_phone_contact);
        textViewEmail = findViewById(R.id.textview_email_contact);
        textViewBirthdayLable = findViewById(R.id.textview_birthday);
        textViewBirthday = findViewById(R.id.textview_birthday_contact);
        textViewAddress = findViewById(R.id.textview_address_contact);
        imageButtonShare = findViewById(R.id.shareButton);
        imageButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
        recyclerViewNumbers = findViewById(R.id.phones_container);
        recyclerViewEmail = findViewById(R.id.email_container);
        recyclerViewAddress = findViewById(R.id.address_container);
        toolbar = findViewById(R.id.toolbar_view_info);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    //Se inicializa los recyclerview
    private void initComponentsNumber() {
        recyclerViewNumbers.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        adapter = new RecyclerViewIndividaulContactAdapter(contact, 1) {
            @Override
            public void setOnclickPhone(View v, int position) {
                makeCall(position);
            }
        };
        recyclerViewNumbers.setAdapter(adapter);
        recyclerViewNumbers.setLayoutManager(layoutManager);
    }
    private void initComponentsEmail() {
        recyclerViewEmail.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        adapter = new RecyclerViewIndividaulContactAdapter(contact, 2) {
            @Override
            public void setOnclickPhone(View v, int position) {
            }
        };
        recyclerViewEmail.setAdapter(adapter);
        recyclerViewEmail.setLayoutManager(layoutManager);
    }
    private void initComponentsAddress() {
        recyclerViewAddress.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        adapter = new RecyclerViewIndividaulContactAdapter(contact, 3) {
            @Override
            public void setOnclickPhone(View v, int position) {
            }
        };
        recyclerViewAddress.setAdapter(adapter);
        recyclerViewAddress.setLayoutManager(layoutManager);
    }

    //setea la informacion en los campos
    private void setInfo(Intent intent) {
        Bundle bundle = intent.getExtras();
        type = bundle.getInt("type");
        System.out.println(type);
        indice = bundle.getInt("contact");
        contact = MainActivity.contacts.get((getInternalFilterContact(type, indice)));
        if (contact.getPhoto() != null) {
            imageViewPhoto.setImageBitmap(contact.getPhoto());
        } else {
            imageViewPhoto.setImageResource(R.drawable.user);
        }
        textViewName.setText(setName());
        textViewBirthday.setText(setBirthday());
    }
    private String setBirthday() {
        String birthday = "";
        if (contact.getBirthday().equals("")) {
            birthday = "no birthday set";
        } else {
            birthday = contact.getBirthday();
        }
        return birthday;

    }
    private String setName() {
        String name = "";
        ArrayList<String> nameArray = new ArrayList<>();
        nameArray.add(contact.getName().getFirstName());
        nameArray.add(contact.getName().getMiddleName());
        nameArray.add(contact.getName().getLastName());
        for (String x : nameArray) {
            if (x != null) {
                if (!x.equals("")) {
                    name += x + " ";
                }
            }
        }
        return name;
    }
    //inicia intent para llamada
    private void makeCall(int position) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact.getNumbers().get(position).getNumber()));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) ==
                PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        } else {
            permissionManager.checkAndRequestPermissions(this);
        }
    }

    //abre actividad edit
    private void opneEditActivity() {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("contact", MainActivity.contacts.indexOf(contact));
        resume = true;
        startActivity(intent);
    }

    //filtra la lista de contacto
    public int getInternalFilterContact(int type, int index) {
        ArrayList<Contacts> contactsFav = new ArrayList<>();
        if (type == 1) {
            return index;
        } else {
            for (int i = 0; i < MainActivity.contacts.size(); i++) {
                if (MainActivity.contacts.get(i).getFav()) {
                    contactsFav.add(MainActivity.contacts.get(i));
                }
            }
            return MainActivity.contacts.indexOf((contactsFav.get(index)));
        }
    }

    //se vuelve a cargar la informacion
    private void restoreInfo() {
        textViewName.setText(setName());
        imageViewPhoto.setImageBitmap(contact.getPhoto());
        recyclerViewNumbers.getAdapter().notifyDataSetChanged();
        recyclerViewEmail.getAdapter().notifyDataSetChanged();
        recyclerViewAddress.getAdapter().notifyDataSetChanged();
        textViewBirthday.setText(setBirthday());
    }

    //intent para compartir
    private void share() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, getShareInfo());
        intent.setType("text/plain");
        startActivity(intent);
    }

    private String getShareInfo() {
        String info="", name = "", phone="";
        if (contact.getName().getFirstName() != null) {
            name += contact.getName().getFirstName();
        }
        if (contact.getName().getMiddleName() != null) {
            name += contact.getName().getMiddleName();
        }
        if (contact.getName().getLastName() != null) {
            name += contact.getName().getLastName();
        }
        info+=name+"\n";
        info+="PHONES\n";
        if(contact.getNumbers()!=null){
            if(contact.getNumbers().size()>0){
                for(PhoneNumber x:contact.getNumbers()){
                    info+=x.getNumber()+"\n";
                }
            }
        }
        info+="Email\n";
        if(contact.getEmail()!=null){
            if(contact.getEmail().size()>0){
                for(Email x:contact.getEmail()){
                    info+=x.getEmail()+"\n";
                }
            }
        }
        info+="Address\n";
        if(contact.getAddress()!=null){
            if(contact.getAddress().size()>0){
                for(Address x:contact.getAddress()){
                    info+=x.getAddress()+"\n";
                }
            }
        }
        info+="Birthday\n";
        if(contact.getBirthday()!=null){
            info+=contact.getBirthday();
        }
        return info;
    }


}
