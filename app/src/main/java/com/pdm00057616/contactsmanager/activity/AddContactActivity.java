package com.pdm00057616.contactsmanager.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.karan.churi.PermissionManager.PermissionManager;
import com.pdm00057616.contactsmanager.R;
import com.pdm00057616.contactsmanager.model.Address;
import com.pdm00057616.contactsmanager.model.Contacts;
import com.pdm00057616.contactsmanager.model.Email;
import com.pdm00057616.contactsmanager.model.Name;
import com.pdm00057616.contactsmanager.model.PhoneNumber;
import com.pdm00057616.contactsmanager.util.ContactGetter;
import com.pdm00057616.contactsmanager.util.DatePick;
import com.pdm00057616.contactsmanager.util.ImagePicker;
import com.pdm00057616.contactsmanager.util.VerifyNewContact;

import java.util.ArrayList;
import java.util.Calendar;

public class AddContactActivity extends AppCompatActivity {

    //views
    ImageView imageViewPhoto;
    ImageButton imageButtonAddPhoto, imageButtonAddPhone, imageButtonAddEmail, imageButtonAddAddress;
    EditText editTextFName, editTextMName, editTextLName, editTextPhone, editTextEmail, editTextAddress;
    LinearLayout linearLayoutPhone, linearLayoutEmail, linearLayoutAddress;
    Toolbar toolbar;
    Spinner spinnerPhones, spinnerEmail, spinnerAddress;
    TextView textViewBirthday;
    //extras
    Activity activity = this;
    Bitmap newpic;
    String nameAux, birthdayAux;
    PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        permissionManager = new PermissionManager() {
        };
        bindViews();
    }

    private void bindViews() {
        toolbar = findViewById(R.id.toolbar_add_contact);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        imageViewPhoto = findViewById(R.id.image_view_photo);

        imageButtonAddPhoto = findViewById(R.id.image_button_add_photo);
        imageButtonAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                PackageManager.PERMISSION_GRANTED) {
                    ImagePicker.pickImage(activity);
                } else {
                    permissionManager.checkAndRequestPermissions(activity);
                }
            }
        });

        imageButtonAddPhone = findViewById(R.id.image_button_add_phone);
        imageButtonAddPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addView(1);
            }
        });
        imageButtonAddEmail = findViewById(R.id.image_button_add_email);
        imageButtonAddEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addView(2);
            }
        });
        imageButtonAddAddress = findViewById(R.id.image_button_add_address);
        imageButtonAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addView(3);
            }
        });

        editTextPhone = findViewById(R.id.edit_text_phone);
        editTextFName = findViewById(R.id.edit_text_fname);
        editTextLName = findViewById(R.id.edit_text_lname);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextAddress = findViewById(R.id.edit_text_address);

        textViewBirthday = findViewById(R.id.textview_birthday);
        textViewBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR), month = c.get(Calendar.MONTH), day = c.get(Calendar.DAY_OF_MONTH);
                final DatePickerDialog dialog = new DatePickerDialog(v.getContext(), new DatePick((TextView) v), year, month, day);
                dialog.show();
            }
        });

        linearLayoutPhone = findViewById(R.id.linear_layout_phone);
        linearLayoutEmail = findViewById(R.id.linear_layout_email);
        linearLayoutAddress = findViewById(R.id.linear_layout_address);

        spinnerPhones = findViewById(R.id.spinner_phone);
        spinnerEmail = findViewById(R.id.spinner_email);
        spinnerAddress = findViewById(R.id.spinner_address);
        setAdapters();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1) {
            final Bundle extras = data.getExtras();
            if (extras != null) {
                newpic = extras.getParcelable("data");
                imageViewPhoto.setImageBitmap(newpic);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_contact_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                saveContact();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionManager.checkResult(requestCode, permissions, grantResults);
        if (permissionManager.getStatus().get(0).granted.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                permissionManager.getStatus().get(0).granted.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ImagePicker.pickImage(this);
        }
    }

    //se seta adapter para spinner
    private void setAdapters() {
        spinnerPhones.setAdapter(setArrayAdapter(1));
        spinnerEmail.setAdapter(setArrayAdapter(2));
        spinnerAddress.setAdapter(setArrayAdapter(3));
    }
    private ArrayAdapter<CharSequence> setArrayAdapter(int type) {
        ArrayAdapter<CharSequence> adapter;
        switch (type) {
            case 1:
                adapter = ArrayAdapter.createFromResource(this, R.array.spinner_phones_types, android.R.layout.simple_spinner_item);
                break;
            case 2:
                adapter = ArrayAdapter.createFromResource(this, R.array.spinner_email_types, android.R.layout.simple_spinner_item);
                break;
            default:
                adapter = ArrayAdapter.createFromResource(this, R.array.spinner_address_types, android.R.layout.simple_spinner_item);
                break;
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    //se agrega un layout a otro layout padre para tener campos nuevos al presionar boton
    private void addView(int type) {
        ArrayList<Integer> types = new ArrayList<>();
        setExtraFieldIconsAndHint(type, types);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View newRow = inflater.inflate(R.layout.contact_info_extra_field, null);
        Spinner spinner = null;
        LinearLayout linear;
        ImageView icon = null;
        EditText editText = null;
        switch (type) {
            case 1:
                linearLayoutPhone.addView(newRow, linearLayoutPhone.getChildCount() - 1);
                linear = (LinearLayout) linearLayoutPhone.getChildAt(linearLayoutPhone.getChildCount() - 2);
                setUpNewViews(editText, spinner, icon, linear, types, type);
                break;
            case 2:
                linearLayoutEmail.addView(newRow, linearLayoutEmail.getChildCount() - 1);
                linear = (LinearLayout) linearLayoutEmail.getChildAt(linearLayoutEmail.getChildCount() - 2);
                setUpNewViews(editText, spinner, icon, linear, types, type);
                break;
            case 3:
                linearLayoutAddress.addView(newRow, linearLayoutAddress.getChildCount() - 1);
                linear = (LinearLayout) linearLayoutAddress.getChildAt(linearLayoutAddress.getChildCount() - 2);
                setUpNewViews(editText, spinner, icon, linear, types, type);
                break;
        }
    }

    //se configura los nuevos campos en los linear
    private void setExtraFieldIconsAndHint(int type, ArrayList<Integer> types) {
        switch (type) {
            case 1:
                types.add(R.string.phone_number_hint);
                types.add(R.drawable.phone_call);
                break;
            case 2:
                types.add(R.string.email_hint);
                types.add(R.drawable.email_icon);
                break;
            case 3:
                types.add(R.string.address_hint);
                types.add(R.drawable.direction_icon);
                break;
        }
    }

    private void setUpNewViews(EditText editText, Spinner spinner, ImageView icon, LinearLayout linear, ArrayList<Integer> types, int type) {
        editText = (EditText) linear.getChildAt(0);
        editText.setHint(types.get(0));
        spinner = (Spinner) linear.getChildAt(1);
        spinner.setAdapter(setArrayAdapter(type));
        icon = (ImageView) linear.getChildAt(2);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout ln = (LinearLayout) v.getParent();
                ln.setVisibility(View.GONE);
            }
        });
    }


    private void getAllInfo() {
        nameAux = editTextFName.getText().toString() + " " + editTextLName.getText().toString();
        birthdayAux = textViewBirthday.getText().toString();
    }

    private void saveContact() {
        getAllInfo();
        if (VerifyNewContact.isReady(nameAux, birthdayAux, getPhones(), getEmial(), getAddress(), this)) {
            Contacts contact = new Contacts(getName(), getPhones(), getEmial(), newpic, ContactGetter.getLastID(this), false, getAddress(), birthdayAux);
            MainActivity.contacts.add(contact);
            Toast.makeText(activity, "Contact Added", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private ArrayList<PhoneNumber> getPhones() {
        ArrayList<PhoneNumber> numbers = new ArrayList<>();
        String number;
        int type;
        for (int i = 0; i < linearLayoutPhone.getChildCount(); i++) {
            if (linearLayoutPhone.getChildAt(i) instanceof LinearLayout && linearLayoutPhone.getChildAt(i).getVisibility() == View.VISIBLE) {
                number = ((EditText) ((LinearLayout) linearLayoutPhone.getChildAt(i)).getChildAt(0)).getText().toString();
                type = getItemTypeSpinnerPhone(i);
                if (!number.equals("")) {
                    PhoneNumber phone = new PhoneNumber(number, type);
                    numbers.add(phone);
                }
            }
        }
        return numbers;
    }

    private ArrayList<Email> getEmial() {
        ArrayList<Email> emails = new ArrayList<>();
        String mail;
        int type;
        for (int i = 0; i < linearLayoutEmail.getChildCount(); i++) {
            if (linearLayoutEmail.getChildAt(i) instanceof LinearLayout) {
                mail = ((EditText) ((LinearLayout) linearLayoutEmail.getChildAt(i)).getChildAt(0)).getText().toString();
                type = getItemTypeSpinnerEmail(i);
                if (!mail.equals("")) {
                    Email email = new Email(mail, type);
                    emails.add(email);
                }
            }
        }
        return emails;
    }

    private ArrayList<Address> getAddress() {
        ArrayList<Address> address = new ArrayList<>();
        String addressget;
        int type;
        for (int i = 0; i < linearLayoutAddress.getChildCount(); i++) {
            if (linearLayoutAddress.getChildAt(i) instanceof LinearLayout) {
                addressget = ((EditText) ((LinearLayout) linearLayoutAddress.getChildAt(i)).getChildAt(0)).getText().toString();
                type = getItemTypeSpinnerAddress(i);
                if (!addressget.equals("")) {
                    Address ad = new Address(addressget, type);
                    address.add(ad);
                }
            }
        }
        return address;
    }

    private Name getName() {
        return new Name(editTextFName.getText().toString(), null, editTextLName.getText().toString());
    }

    private int getItemTypeSpinnerPhone(int i) {
        String item = ((Spinner) ((LinearLayout) linearLayoutPhone.getChildAt(i)).getChildAt(1)).getSelectedItem().toString();
        if (item.equals("Home")) {
            return 1;
        } else if (item.equals("Mobile")) {
            return 2;
        } else if (item.equals("Work")) {
            return 3;
        } else {
            return 4;
        }
    }

    private int getItemTypeSpinnerEmail(int i) {
        String item = ((Spinner) ((LinearLayout) linearLayoutPhone.getChildAt(i)).getChildAt(1)).getSelectedItem().toString();
        if (item.equals("Home")) {
            return 1;
        } else if (item.equals("Work")) {
            return 2;
        } else {
            return 4;
        }
    }

    private int getItemTypeSpinnerAddress(int i) {
        String item = ((Spinner) ((LinearLayout) linearLayoutPhone.getChildAt(i)).getChildAt(1)).getSelectedItem().toString();
        if (item.equals("Home")) {
            return 1;
        } else {
            return 2;
        }
    }


}
