package com.pdm00057616.contactsmanager.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.karan.churi.PermissionManager.PermissionManager;
import com.pdm00057616.contactsmanager.R;
import com.pdm00057616.contactsmanager.adapter.ViewPagerAdapter;
import com.pdm00057616.contactsmanager.fragment.ContactsShowFragment;
import com.pdm00057616.contactsmanager.model.Contacts;
import com.pdm00057616.contactsmanager.util.ContactGetter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Views
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SearchView searchView;

    //Other stuffs
    public static ViewPagerAdapter viewPagerAdapter;
    public static ArrayList<Contacts> contacts;
    private Activity activity=this;
    private PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionManager=new PermissionManager(){};
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)==
                PackageManager.PERMISSION_GRANTED){
            bindViews();
            init();
        }else{
            permissionManager.checkAndRequestPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionManager.checkResult(requestCode,permissions,grantResults);
        if(permissionManager.getStatus().get(0).granted.contains(Manifest.permission.READ_CONTACTS)){
            bindViews();
            init();
            viewPagerAdapter.notifyDataSetChanged();
        }else{
            permissionManager.checkAndRequestPermissions(activity);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TransitionManager.beginDelayedTransition(toolbar);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        final MenuItem actionItem=menu.findItem(R.id.search_action);
        searchView=(SearchView) actionItem.getActionView();
        ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text))
                .setHintTextColor(getResources().getColor(R.color.white));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!searchView.isIconified()){
                    searchView.setIconified(true);
                }
                actionItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ContactsShowFragment page=(ContactsShowFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
                final ArrayList<Contacts>filt=filter(page.getList(viewPager.getCurrentItem()), newText);
                page.setListFilter(filt);
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)==PackageManager.PERMISSION_GRANTED){
            viewPagerAdapter.notifyDataSetChanged();
        }else{
            permissionManager.checkAndRequestPermissions(this);
        }
    }

    private void bindViews(){
        toolbar=findViewById(R.id.toolbar);
        fab=findViewById(R.id.fab);
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.viewpager);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityAdd();
            }
        });
    }

    private void init(){
        contacts= ContactGetter.getContacts(activity);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(ContactsShowFragment.newInstance(1, contacts), "Contactos");
        viewPagerAdapter.addFragment(ContactsShowFragment.newInstance(2, contacts), "Fav");
        setSupportActionBar(toolbar);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    private void startActivityAdd(){
        Intent intent=new Intent(activity, AddContactActivity.class);
        startActivity(intent);
    }

    private ArrayList<Contacts> filter(ArrayList<Contacts> contacts, String query){
        query=query.toLowerCase();
        final ArrayList<Contacts> filter=new ArrayList<>();
        for(Contacts c:contacts){
            if(getName(c).startsWith(query)){
                filter.add(c);
            }
        }
        return filter;
    }
    private String getName(Contacts c){
        String name="";
        ArrayList<String> arrayList=new ArrayList<>();
        arrayList.add(c.getName().getFirstName());
        arrayList.add(c.getName().getMiddleName());
        arrayList.add(c.getName().getLastName());
        for(String x:arrayList){
            if(x!=null){
                name+=x+" ";
            }
        }
        return name;
    }
}
