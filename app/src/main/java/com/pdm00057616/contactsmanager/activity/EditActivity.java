package com.pdm00057616.contactsmanager.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.pdm00057616.contactsmanager.util.ImagePicker;
import com.pdm00057616.contactsmanager.util.VerifyNewContact;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {

    //Views
    ImageView imageViewPhoto;
    ImageButton imageButtonAddPhoto, imageButtonAddPhone, imageButtonAddEmail, imageButtonAddAddress;
    LinearLayout linearLayoutPhones, linearLayoutEmail, linearLayoutAddress;
    Toolbar toolbar;
    TextView textViewBirthday;
    EditText fName, lName, mName;
    //
    PermissionManager permissionManager;
    Bitmap newpic;
    Activity activity = this;
    Contacts contact, newContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        bindViews();
        setInfo(getIntent());
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


    private void bindViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        imageViewPhoto = findViewById(R.id.image_view_photo);
        imageButtonAddPhoto = findViewById(R.id.image_button_add_photo);
        imageButtonAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.pickImage(activity);
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
        linearLayoutPhones = findViewById(R.id.linear_layout_phone);
        linearLayoutEmail = findViewById(R.id.linear_layout_email);
        linearLayoutAddress = findViewById(R.id.linear_layout_address);
        textViewBirthday = findViewById(R.id.textview_birthday);
        fName = findViewById(R.id.edit_text_fname);
        lName = findViewById(R.id.edit_text_lname);
        mName = findViewById(R.id.edit_text_mname);
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

    private void setInfo(Intent intent) {
        Bundle bundle=intent.getExtras();
        contact = MainActivity.contacts.get(bundle.getInt("contact"));
        setInfoInViews(contact);
    }

    //Reciving info and setting it into fields
    private void setName(Contacts contact) {
        fName.setText(contact.getName().getFirstName());
        lName.setText(contact.getName().getLastName());
    }

    private void setImage(Contacts contact) {
        newpic = contact.getPhoto();
        if (newpic == null) {
            imageViewPhoto.setImageResource(R.drawable.user);
        } else {
            imageViewPhoto.setImageBitmap(newpic);
        }
    }

    private void setPhones(Contacts contact) {
        int cantidad = contact.getNumbers().size();
        LinearLayout linearLayout;
        EditText editText;
        for (int i = 0; i < cantidad; i++) {
            addView(1);
            linearLayout = (LinearLayout) linearLayoutPhones.getChildAt(linearLayoutPhones.getChildCount() - 2);
            editText = (EditText) linearLayout.getChildAt(0);
            editText.setText(contact.getNumbers().get(i).getNumber());
        }
    }

    private void setEmail(Contacts contact) {
        int cantidad = contact.getEmail().size();
        LinearLayout linearLayout;
        EditText editText;
        for (int i = 0; i < cantidad; i++) {
            addView(2);
            linearLayout = (LinearLayout) linearLayoutEmail.getChildAt(linearLayoutEmail.getChildCount() - 2);
            editText = (EditText) linearLayout.getChildAt(0);
            editText.setText(contact.getEmail().get(i).getEmail());
        }
    }

    private void setAddress(Contacts contact) {
        int cantidad = 0;
        if (contact.getAddress() != null) {
            cantidad = contact.getAddress().size();
        }
        LinearLayout linearLayout;
        EditText editText;
        for (int i = 0; i < cantidad; i++) {
            addView(3);
            linearLayout = (LinearLayout) linearLayoutAddress.getChildAt(linearLayoutAddress.getChildCount() - 2);
            editText = (EditText) linearLayout.getChildAt(0);
            editText.setText(contact.getAddress().get(i).getAddress());
        }
    }

    private void setInfoInViews(Contacts contact) {
        setName(contact);
        setImage(contact);
        setPhones(contact);
        setEmail(contact);
        setAddress(contact);
    }

    //preparing new views
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
                linearLayoutPhones.addView(newRow, linearLayoutPhones.getChildCount() - 1);
                linear = (LinearLayout) linearLayoutPhones.getChildAt(linearLayoutPhones.getChildCount() - 2);
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

    private void setUpNewViews(EditText editText, Spinner spinner, ImageView icon, LinearLayout linear, ArrayList<Integer> types, int type) {
        editText = (EditText) linear.getChildAt(0);
        editText.setHint(types.get(0));
        spinner = (Spinner) linear.getChildAt(1);
        spinner.setAdapter(setArrayAdapter(type));
        spinner.setSelection(type);
        icon = (ImageView) linear.getChildAt(2);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout ln = (LinearLayout) v.getParent();
                ln.setVisibility(View.GONE);
            }
        });
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

    private void setExtraFieldIconsAndHint(int type, ArrayList<Integer> types) {
        switch (type) {
            case 1:
                types.add(R.string.phone_number_hint);
                break;
            case 2:
                types.add(R.string.email_hint);
                break;
            case 3:
                types.add(R.string.address_hint);
                break;
        }
    }

    //saving contact
    private ArrayList<PhoneNumber> getPhones() {
        int cant = linearLayoutPhones.getChildCount();
        ArrayList<PhoneNumber> numbers = new ArrayList<>();
        System.out.println(cant);
        if (cant != 1) {
            for (int i = 0; i < cant; i++) {
                if (linearLayoutPhones.getChildAt(i) instanceof LinearLayout && linearLayoutPhones.getVisibility() == View.VISIBLE) {
                    LinearLayout layout = (LinearLayout) linearLayoutPhones.getChildAt(i);
                    String numero = ((EditText) layout.getChildAt(0)).getText().toString();
                    int type = getItemTypeSpinnerPhone(i, layout);
                    numbers.add(new PhoneNumber(numero, type));
                }
            }
        }
        return numbers;
    }
    private ArrayList<Email> getEmail() {
        int cant = linearLayoutEmail.getChildCount();
        ArrayList<Email> emails = new ArrayList<>();
        if (cant != 1) {
            for (int i = 0; i < cant; i++) {
                if (linearLayoutEmail.getChildAt(i) instanceof LinearLayout && linearLayoutEmail.getVisibility() == View.VISIBLE) {
                    LinearLayout layout = (LinearLayout) linearLayoutEmail.getChildAt(i);
                    String email = ((EditText) layout.getChildAt(0)).getText().toString();
                    int type = getItemTypeSpinnerEmail(i, layout);
                    emails.add(new Email(email, type));
                }
            }
        }
        return emails;
    }
    private ArrayList<Address> getAddress() {
        int cant = linearLayoutAddress.getChildCount();
        ArrayList<Address> addresses = new ArrayList<>();
        if (cant != 1) {
            for (int i = 0; i < cant; i++) {
                if (linearLayoutAddress.getChildAt(i) instanceof LinearLayout && linearLayoutAddress.getChildAt(i).getVisibility() == View.VISIBLE) {
                    LinearLayout layout = (LinearLayout) linearLayoutAddress.getChildAt(i);
                    String address = ((EditText) layout.getChildAt(0)).getText().toString();
                    int type = getItemTypeSpinnerAddress(i, layout);
                    addresses.add(new Address(address, type));
                }
            }
        }
        return addresses;
    }
    public void saveContact() {
        String auxName = fName.getText().toString() + mName.getText().toString() + lName.getText().toString();
        String birthday = textViewBirthday.getText().toString();
        if (VerifyNewContact.isReady(auxName, birthday, getPhones(), getEmail(), getAddress(), activity)) {

            replaceFields(fName.getText().toString(),  mName.getText().toString(), lName.getText().toString(),
                    birthday);
            Toast.makeText(activity, "Contact edited", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    public void replaceFields(String fname, String mname, String lname, String birthday) {
        MainActivity.contacts.get(MainActivity.contacts.indexOf(contact)).setName(setName(fname, mname, lname));
        MainActivity.contacts.get(MainActivity.contacts.indexOf(contact)).setNumbers(getPhones());
        MainActivity.contacts.get(MainActivity.contacts.indexOf(contact)).setEmail(getEmail());
        MainActivity.contacts.get(MainActivity.contacts.indexOf(contact)).setAddress(getAddress());
        MainActivity.contacts.get(MainActivity.contacts.indexOf(contact)).setPhoto(newpic);
    }
    public Name setName(String fname, String mname, String lname){
        Name names=new Name(fname, mname, lname);
        return names;
    }
    private int getItemTypeSpinnerPhone(int i, LinearLayout layout) {
        String item = ((Spinner) (layout).getChildAt(1)).getSelectedItem().toString();
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
    private int getItemTypeSpinnerEmail(int i, LinearLayout layout) {
        String item = ((Spinner) (layout).getChildAt(1)).getSelectedItem().toString();
        if (item.equals("Home")) {
            return 1;
        } else if (item.equals("Work")) {
            return 2;
        } else {
            return 4;
        }
    }
    private int getItemTypeSpinnerAddress(int i, LinearLayout layout) {
        String item = ((Spinner) (layout).getChildAt(1)).getSelectedItem().toString();
        if (item.equals("Home")) {
            return 1;
        } else {
            return 2;
        }
    }
}
