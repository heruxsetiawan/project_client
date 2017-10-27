package com.example.united.mrk;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity  {
    FragmentParent fragmentParent;
    public boolean doubleTapParam = false;
    private SearchView.OnQueryTextListener queryTextListener;
    private ArrayList<Data_Submenu> dataList = new ArrayList<>();
    boolean cari = true;
    private Adapter_Child mAdapter;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getIDs();

        String menuJson = CreateFileJson.getData(getApplicationContext(), "menu");
        if (menuJson == null) {
            fragmentParent.GetDataJsonfilter("menu", "stadion");
            // fragmentParent.tampilfromtxt(CreateFileJson.getData(getApplicationContext(),"menu"));
        } else {
            fragmentParent.GetDataJsonfilter("menu", "stadion");
           // fragmentParent.tampilfromtxt(CreateFileJson.getData(getApplicationContext(), "menu"));
        }


        fragmentParent.viewPager.setCurrentItem(0);

        toolbar = (Toolbar) findViewById(R.id.toolbar_tes);
        setSupportActionBar(toolbar);

    }

    private void restartprogam() {
        Intent i = MainActivity.this.getPackageManager().
                getLaunchIntentForPackage(MainActivity.this.getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }

    private void getIDs() {
        fragmentParent = (FragmentParent) this.getSupportFragmentManager().findFragmentById(R.id.fragmentParent);
    }

    @Override
    public void onBackPressed() {
        if (doubleTapParam) {
            super.onBackPressed();
            return;
        }

        this.doubleTapParam = true;
        Toast.makeText(this, "Tap sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();
        fragmentParent.viewPager.setCurrentItem(0);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleTapParam = false;

            }
        }, 3000);
    }

    @Override
    public void onResume() {
        super.onResume();
        fragmentParent.viewPager.setCurrentItem(0);
        new Runnable() {
            @Override
            public void run() {
                fragmentParent.viewPager.setCurrentItem(0);

            }
        };
    }


}
