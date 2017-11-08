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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.united.mrk.koneksi.RegisterUserClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by DAT on 9/1/2015.
 */
public class Pencarian extends AppCompatActivity implements MenuItemCompat.OnActionExpandListener {
    static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    private static String url = BuildConfig.Main_menu;
    String id;
    private RecyclerView rvView;
    private ArrayList<Data_pencarian> dataList = new ArrayList<>();
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
    LayoutInflater layoutInflater2;
    private RelativeLayout get_total;
    View addView2;
    public TextView Rp, total;
    ViewGroup container2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FragmentManager fm = getSupportFragmentManager();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //fp = (FragmentParent) fm.findFragmentById(R.id.fragmentParent);
        setContentView(R.layout.activity_pencarian);
        container2 = (RelativeLayout) findViewById(R.id.container_pencarian);
        rvView = (RecyclerView) findViewById(R.id.rv_pencarian);
        rvView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(context, 1);
        rvView.setLayoutManager(layoutManager);
        dataList = new ArrayList<Data_pencarian>();
        myDb = new DataHelper(Pencarian.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        all_menu();
        getinflate();
        getTotal();

        rvView.setItemViewCacheSize(dataList.size());
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
                    final ArrayList<Data_pencarian> filteredModelList = filter(dataList, newText);
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

    private ArrayList<Data_pencarian> filter(ArrayList<Data_pencarian> models, String query) {
        query = query.toLowerCase();
        final ArrayList<Data_pencarian> filteredModelList = new ArrayList<>();
        for (Data_pencarian model : models) {
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
        Log.e("lokal", "jalan");
        try {
            String s = CreateFileJson.getData(Pencarian.this, "menu");
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
                    String img = detailProduk.getString("img");
                    String pedas=detailProduk.getString("spicy");
                    String new_menu=detailProduk.getString("new_menu");
                    String recomended=detailProduk.getString("recomended");
                    String favorit=detailProduk.getString("favorit");

                    Data_pencarian ds = new Data_pencarian();
                    ds.setcodemenu(fpkey);
                    ds.setnamasubmenu(nama);
                    ds.setharga(harga);
                    ds.setqty(myDb.getjumlah(fpkey));
                    ds.setnote(myDb.getnote(fpkey));
                    ds.setfkitchen_id(fkitchen_id);
                    ds.setimg(img);
                    ds.setpedas(pedas);
                    ds.setnew_menu(new_menu);
                    ds.setrecomended(recomended);
                    ds.setfavorit(favorit);
                    dataList.add(ds);
                    Log.e("pedas",nama);
                    Log.e("pedas",pedas);

                }


            }
            rvView.setItemViewCacheSize(dataList.size());
            mAdapter = new Adapter_pencarian(context, dataList, Pencarian.this);
            rvView.setAdapter(mAdapter);
        } catch (JSONException e) {
            Log.e("JSONException detail", e.getMessage());
        }
    }

    public void getinflate() {
        layoutInflater2 = (LayoutInflater) Pencarian.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addView2 = layoutInflater2.inflate(R.layout.inflate_total, null);
        Rp = (TextView) addView2.findViewById(R.id.rp);
        total = (TextView) addView2.findViewById(R.id.total);
        container2.addView(addView2);
        get_total = (RelativeLayout) addView2.findViewById(R.id.rl_total);

        get_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pencarian.this, Keranjang.class);
                startActivity(intent);
                Pencarian.this.finish();
            }
        });

    }

    public void getTotal() {
        if (myDb.getTotal() == null) {
            Rp.setText("0");
            // hideinflate();
        } else if (myDb.getTotal().equalsIgnoreCase("0")) {
            Rp.setText("0");
            // hideinflate();
        } else {
            try {
                //    showinflate();
                String value = myDb.getTotal();
                value = value.replace(".", "").replace(",", "");
                double amount = Double.parseDouble(value);
                DecimalFormat formatter = null;

                if (value != null && !value.equals("")) {
                    formatter = new DecimalFormat("#,###");
                }
                if (!value.equals(""))
                    Rp.setText(formatter.format(amount));

                return;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


    }

}
