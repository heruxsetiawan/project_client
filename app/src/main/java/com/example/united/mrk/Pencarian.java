package com.example.united.mrk;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.united.mrk.koneksi.RegisterUserClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by DAT on 9/1/2015.
 */
public class Pencarian extends AppCompatActivity implements MenuItemCompat.OnActionExpandListener {
    static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    // private String url = "http://192.168.0.12/solis/main_menu_2.php";
    // private static String url = BuildConfig.API + "main_menu_2.php";
    private static String url = BuildConfig.Main_menu;
 //   private static String url = "http://192.168.0.86/solis/main_menu_tes_android.php";
    String id;
    private RecyclerView rvView;
    private ArrayList<Data_Submenu> dataList = new ArrayList<>();
    private ArrayList<Data_Category_menu> dataList2 = new ArrayList<>();
    private Adapter_pencarian mAdapter;

    Context context;
    private SearchView.OnQueryTextListener queryTextListener;
    DataHelper myDb;
    boolean cari = true;
    FragmentParent fp;
    FragmentChild fragmentchild;
    RecyclerView.LayoutManager layoutManager;
    public final static String LIST_STATE_KEY = "recycler_list_state";
    Parcelable listState;

    private ProgressDialog pDialog;
    // public String menuJson;
    private String posisi, submenu;
    public static final String SUBMENU_PUTEXTRA = "submenu";
    private ProgressDialog pd = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FragmentManager fm = getSupportFragmentManager();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //fp = (FragmentParent) fm.findFragmentById(R.id.fragmentParent);
        setContentView(R.layout.activity_pencarian);
        rvView = (RecyclerView) findViewById(R.id.rv_pencarian);
        rvView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(context, 1);
        rvView.setLayoutManager(layoutManager);
        dataList = new ArrayList<Data_Submenu>();
        myDb = new DataHelper(Pencarian.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        all_menu();
        // GetDataJsonfilter("pencarian");
        //  GetDataJson_dial("menu", "stadion");

        //fromtxt_filter();

        rvView.setItemViewCacheSize(dataList.size());
    }

    public void fromtxt_filter() {

        //dataList.clear();

        try {

            String s = CreateFileJson.getData(Pencarian.this, "all_menu");
            JSONObject jsonObj = new JSONObject(s);
            JSONArray contacts = jsonObj.getJSONArray("all_menu");
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject c = contacts.getJSONObject(i);
                JSONArray produk = c.getJSONArray("menu");
                for (int y = 0; y < produk.length(); y++) {
                    JSONObject detailProduk = produk.getJSONObject(y);
                    String codesubmenu = detailProduk.getString("fcode");
                    String namasubmenu = detailProduk.getString("nama");
                    String hargamenu = detailProduk.getString("harga");
                    Log.e("nama tes cari", namasubmenu);

                           /* Data_Submenu_dial ds = new Data_Submenu_dial();
                            ds.setcodemenu(codesubmenu);
                            ds.setnamasubmenu(namasubmenu);
                            ds.setharga(hargamenu);
                            ds.setqty(myDb.getjumlah(codesubmenu));
                            ds.setnote(myDb.getnote(codesubmenu));
                            dataList_dial.add(ds);*/
                  /*  Data_Submenu ds = new Data_Submenu();
                    ds.setcodemenu(codesubmenu);
                    ds.setnamasubmenu(namasubmenu);
                    ds.setharga(hargamenu);
                    ds.setqty(myDb.getjumlah(codesubmenu));
                    ds.setnote(myDb.getnote(codesubmenu));
                    dataList.add(ds);*/
                }


            }
        /*    rvView.setItemViewCacheSize(dataList.size());
            mAdapter = new Adapter_pencarian(context, dataList, fp);
            rvView.setAdapter(mAdapter);*/
        } catch (JSONException e) {
            Log.e("JSONException detail", e.getMessage());
        }
    }


    void GetDataJsonfilter(String kata) {
        dataList.clear();

        class a extends AsyncTask<String, Void, String> {
            private RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(Pencarian.this);
                pDialog.setMessage("Loading...");
                pDialog.setCancelable(false);
                pDialog.show();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject jsonObj = new JSONObject(s);
                    JSONArray contacts = jsonObj.getJSONArray("all_menu");
                    CreateFileJson.saveData(Pencarian.this, s, "all_menu");
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        JSONArray produk = c.getJSONArray("menu");
                        for (int y = 0; y < produk.length(); y++) {
                            JSONObject detailProduk = produk.getJSONObject(y);
                            String codesubmenu = detailProduk.getString("fpkey");
                            String namasubmenu = detailProduk.getString("nama");
                            String hargamenu = detailProduk.getString("harga");

                            Data_Submenu ds = new Data_Submenu();
                            ds.setcodemenu(codesubmenu);
                            ds.setnamasubmenu(namasubmenu);
                            ds.setharga(hargamenu);
                            ds.setqty(myDb.getjumlah(codesubmenu));
                            ds.setnote(myDb.getnote(codesubmenu));
                            dataList.add(ds);
                        }


                    }
                } catch (final JSONException e) {
                    Log.e("TAG", "Json parsing error parent: " + e.getMessage());
                }


            }

            @Override
            protected String doInBackground(String... params) {
                Log.e("ket", "memproses parent");
                HashMap<String, String> data = new HashMap<>();
                data.put("operasi", params[0]);
                return ruc.sendPostRequest(url, data, "POST");
            }
        }
        a ru = new a();
        ru.execute(kata);
    }

    void GetDataJson_dial(String kata, String cabang) {
        dataList.clear();

        class a extends AsyncTask<String, Void, String> {
            private RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(Pencarian.this);
                pDialog.setMessage("Loading...");
                pDialog.setCancelable(false);
                pDialog.show();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    JSONObject jsonObj = new JSONObject(s);
                    JSONArray contacts = jsonObj.getJSONArray("all_menu");
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        JSONArray produk = c.getJSONArray("menu");
                        for (int y = 0; y < produk.length(); y++) {
                            JSONObject detailProduk = produk.getJSONObject(y);
                            String codesubmenu = detailProduk.getString("fpkey");
                            String namasubmenu = detailProduk.getString("nama");
                            String hargamenu = detailProduk.getString("harga");

                           /* Data_Submenu_dial ds = new Data_Submenu_dial();
                            ds.setcodemenu(codesubmenu);
                            ds.setnamasubmenu(namasubmenu);
                            ds.setharga(hargamenu);
                            ds.setqty(myDb.getjumlah(codesubmenu));
                            ds.setnote(myDb.getnote(codesubmenu));
                            dataList_dial.add(ds);*/
                            Data_Submenu ds = new Data_Submenu();
                            ds.setcodemenu(codesubmenu);
                            ds.setnamasubmenu(namasubmenu);
                            ds.setharga(hargamenu);
                            ds.setqty(myDb.getjumlah(codesubmenu));
                            ds.setnote(myDb.getnote(codesubmenu));
                            dataList.add(ds);
                        }


                    }
                    rvView.setItemViewCacheSize(dataList.size());
                    mAdapter = new Adapter_pencarian(context, dataList, fp);
                    rvView.setAdapter(mAdapter);
                    pDialog.dismiss();
                } catch (final JSONException e) {
                    Log.e("TAG", "Json parsing error parent: " + e.getMessage());
                    Toast.makeText(Pencarian.this, "Tidak Dapat Terhubung ke Server ", Toast.LENGTH_LONG).show();
                    pDialog.dismiss();
                }


            }

            @Override
            protected String doInBackground(String... params) {
                Log.e("ket", "memproses parent");
                HashMap<String, String> data = new HashMap<>();
                data.put("operasi", params[0]);
                data.put("cabang", params[1]);
                return ruc.sendPostRequest(url, data, "POST");
            }
        }
        a ru = new a();
        ru.execute(kata, cabang);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pencarian, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search2);
        searchItem.expandActionView();
        SearchManager searchManager = (SearchManager) Pencarian.this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            MenuItemCompat.setOnActionExpandListener(searchItem, this);
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(Pencarian.this.getComponentName()));


            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    final ArrayList<Data_Submenu> filteredModelList = filter(dataList, newText);
                    if (filteredModelList.size() > 0) {
                        if (newText.length() > 0) {
                            cari = false;
                            mAdapter.setFilter(filteredModelList);
                        } else {
                            cari = true;

                        }
                    }

                    return false;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        return super.onCreateOptionsMenu(menu);
    }

    private ArrayList<Data_Submenu> filter(ArrayList<Data_Submenu> models, String query) {
        query = query.toLowerCase();
        final ArrayList<Data_Submenu> filteredModelList = new ArrayList<>();
        for (Data_Submenu model : models) {
            final String text = model.getnamasubmenu().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Pencarian.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        new Pindah_Home().execute();
        return true;
    }

    public void setArguments(Bundle bundle) {

    }

    class Pindah_Home extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Pencarian.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        protected String doInBackground(String... args) {
            Intent intent = new Intent(Pencarian.this, MainActivity.class);
            startActivity(intent);
            finish();
            return null;

        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();


        }

    }

    public void all_menu() {

        dataList.clear();
        Log.e("lokal","jalan");
        try {
            String s = CreateFileJson.getData(Pencarian.this, "all_menu");
            JSONObject jsonObj = new JSONObject(s);
            JSONArray contacts = jsonObj.getJSONArray("all_menu");

            for (int i = 0; i < contacts.length(); i++) {
                JSONObject c = contacts.getJSONObject(i);
                JSONArray produk = c.getJSONArray("menu");
                for (int y = 0; y < produk.length(); y++) {
                    JSONObject detailProduk = produk.getJSONObject(y);

                    String fpkey = detailProduk.getString("fpkey");
                    String nama = detailProduk.getString("nama");
                    String harga = detailProduk.getString("harga");
                    String fkitchen_id = detailProduk.getString("fkitchen_id");
                    Data_Submenu ds = new Data_Submenu();
                    Log.e("nama",nama);
                    ds.setcodemenu(fpkey);
                    ds.setnamasubmenu(nama);
                    ds.setharga(harga);
                  //  ds.setfkitchen_id(fkitchen_id);
                    ds.setqty(myDb.getjumlah(fpkey));
                    ds.setnote(myDb.getnote(fpkey));
                    dataList.add(ds);

                }


            }
            rvView.setItemViewCacheSize(dataList.size());
            mAdapter = new Adapter_pencarian(context, dataList, fp);
            rvView.setAdapter(mAdapter);
        } catch (JSONException e) {
            Log.e("JSONException detail", e.getMessage());
        }
    }

}
