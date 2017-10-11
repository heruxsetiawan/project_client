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
            fragmentParent.tampilfromtxt(CreateFileJson.getData(getApplicationContext(), "menu"));
        }


        fragmentParent.viewPager.setCurrentItem(0);

        toolbar = (Toolbar) findViewById(R.id.toolbar_tes);
        setSupportActionBar(toolbar);
        // navigationView = (NavigationView) findViewById(R.id.navigation_view);
       /* navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Memeriksa apakah item tersebut dalam keadaan dicek  atau tidak,
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                //Menutup  drawer item klik


                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.navigation1:
                        fragmentParent.tampilfromtxt(CreateFileJson.getData(getApplicationContext(),"menu"), "2");
                        return true;
                    case R.id.navigation2:
                        fragmentParent.tampilfromtxt(CreateFileJson.getData(getApplicationContext(),"menu"), "11");
                        return true;
                    case R.id.navigation3:
                        fragmentParent.tampilfromtxt(CreateFileJson.getData(getApplicationContext(),"menu"), "6");
                        return true;
                    case R.id.navigation4:
                        fragmentParent.tampilfromtxt(CreateFileJson.getData(getApplicationContext(),"menu"), "13");
                        return true;
                    case R.id.navigation5:
                        fragmentParent.tampilfromtxt(CreateFileJson.getData(getApplicationContext(),"menu"), "10");
                        return true;
                    case R.id.navigation6:
                        fragmentParent.tampilfromtxt(CreateFileJson.getData(getApplicationContext(),"menu"), "4");
                        return true;
                    case R.id.navigation7:
                        fragmentParent.tampilfromtxt(CreateFileJson.getData(getApplicationContext(),"menu"), "7");
                        return true;
                    case R.id.navigation8:
                        fragmentParent.tampilfromtxt(CreateFileJson.getData(getApplicationContext(),"menu"), "8");
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Kesalahan Terjadi ", Toast.LENGTH_SHORT).show();
                        return true;
                }
            }

        });
*/

       /* drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                // Kode di sini akan merespons setelah drawer menutup disini kita biarkan kosong
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //  Kode di sini akan merespons setelah drawer terbuka disini kita biarkan kosong
                super.onDrawerOpened(drawerView);
            }
        };
        //Mensetting actionbarToggle untuk drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        //memanggil synstate
        actionBarDrawerToggle.syncState();*/

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
