package com.pdm00057616.contactsmanager.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.pdm00057616.contactsmanager.R;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton fab;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void bindViews(){
        toolbar=findViewById(R.id.toolbar);
        fab=findViewById(R.id.fab);
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.viewpager);
    }
}
