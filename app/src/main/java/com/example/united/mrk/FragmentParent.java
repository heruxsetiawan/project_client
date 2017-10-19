package com.example.united.mrk;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class FragmentParent extends Fragment {
    private TabLayout tabLayout;
    public ViewPager viewPager;
    public ViewPagerAdapter adapter;
    int selectedTabPosition;
    //  public String url = BuildConfig.API + "main_menu_2.php";
    private static String url2 = BuildConfig.Main_menu;
    private static String url = BuildConfig.Main_menu_2;
    // private static String url = "http://192.168.0.86/solis/main_menu_tes_android.php";
    public TextView Rp, total;
    public DataHelper myDb;
    private ArrayList<Data_Category_menu> dataList;
    ViewGroup container2;
    LayoutInflater layoutInflater2;
    private RelativeLayout get_total;
    View addView2;
    public String jumlah_data;
    public ProgressDialog pDialog2;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent, container, false);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter("com.wirasetiawan.BroadcastReceiver"));
        pDialog2 = new ProgressDialog(getActivity());
        getIDs(view);
        setEvents();
        dataList = new ArrayList<>();
        container2 = (RelativeLayout) view.findViewById(R.id.container2);
        myDb = new DataHelper(getActivity());
        setRetainInstance(true);
        getinflate();
        getTotal();

        //all_menu("menu","stadion");
        return view;
    }


    public void getinflate() {
        layoutInflater2 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addView2 = layoutInflater2.inflate(R.layout.inflate_total, null);
        Rp = (TextView) addView2.findViewById(R.id.rp);


        total = (TextView) addView2.findViewById(R.id.total);
        container2.addView(addView2);
        get_total = (RelativeLayout) addView2.findViewById(R.id.rl_total);

        get_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Keranjang.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

    }

    public void getTotal() {


        if (myDb.getTotal() == null) {
            Rp.setText("0");
            hideinflate();
        } else if (myDb.getTotal().equalsIgnoreCase("0")) {
            Rp.setText("0");
            hideinflate();
        } else {
            try {
                showinflate();
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

    public void hideinflate() {
        container2.setVisibility(View.GONE);
    }

    public void showinflate() {
        container2.setVisibility(View.VISIBLE);
    }


    @Override
    public void onResume() {
        super.onResume();
        final int pos = 0;
        viewPager.postDelayed(new Runnable() {

            @Override
            public void run() {
                viewPager.setCurrentItem(pos);

            }
        }, 0);

    }

    public void getIDs(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.my_viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.my_tab_layout);
        adapter = new ViewPagerAdapter(getFragmentManager(), getActivity(), viewPager, tabLayout);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

    }


    public boolean setEvents() {
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
               /* viewPager.setCurrentItem(tab.getPosition());
                selectedTabPosition = viewPager.getCurrentItem();*/
                // Log.e("onTabSelected", "onTabSelected " + tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                //Log.e("Unselected", "Unselected " + tab.getPosition());
            }
        });
        return true;
    }


    public void addPage() {
        for (int i = 0; i < dataList.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putString("submenu", dataList.get(i).getcategory());
            FragmentChild fragmentChild = new FragmentChild();
            fragmentChild.setArguments(bundle);
            adapter.addFrag(fragmentChild, dataList.get(i).getcategory());
            adapter.notifyDataSetChanged();
            if (adapter.getCount() > 0) tabLayout.setupWithViewPager(viewPager);
            viewPager.setCurrentItem(adapter.getCount() - 1);
            setupTabLayout();
            //-------------------->  setupTabLayout();
        }
        viewPager.setCurrentItem(0);

    }

    public void setupTabLayout() {
        selectedTabPosition = viewPager.getCurrentItem();
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setCustomView(adapter.getTabView(i));
        }
    }


    void tampilfromtxt(String menu) {
        dataList.clear();
        adapter.mFragmentTitleList.clear();
        adapter.mFragmentList.clear();
        adapter.notifyDataSetChanged();
        Log.e("data from txt parent", "jalan");

        if (menu != null) {

            try {
                JSONObject jsonObj = new JSONObject(menu);
                JSONArray contacts = jsonObj.getJSONArray("data");
                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);
                    String namasubmenu = c.getString("category");
                    Data_Category_menu ds = new Data_Category_menu();
                    ds.setnama_category(namasubmenu);
                    dataList.add(ds);


                }
            } catch (final JSONException e) {
                Log.e("TAG", "Json from txt erorr parent: " + e.getMessage());
            }
        } else {
            Log.e("TAG", "Json from txt error parent: ");
        }
        addPage();

    }


    void GetDataJsonfilter(String kata, String cabang) {
        dataList.clear();
        pDialog2 = new ProgressDialog(getContext());
        pDialog2.setMessage("Loading...");
        pDialog2.setCancelable(false);
        pDialog2.show();

        class a extends AsyncTask<String, Void, String> {
            private RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();


            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.e("s",s);
                if (s.equalsIgnoreCase("gagal_total")) {
                    Toast.makeText(getContext(), "Pindah Koneksi", Toast.LENGTH_SHORT).show();
                    getserver_backup("menu", "stadion");

                } else {
                    try {
                        JSONObject jsonObj = new JSONObject(s);
                        JSONArray contacts = jsonObj.getJSONArray("data");
                        CreateFileJson.saveData(getActivity(), s, "menu");
                        for (int i = 0; i < contacts.length(); i++) {
                            JSONObject c = contacts.getJSONObject(i);
                            String namasubmenu = c.getString("category");
                            Data_Category_menu ds = new Data_Category_menu();
                            ds.setnama_category(namasubmenu);
                            dataList.add(ds);


                        }

                    } catch (final JSONException e) {
                        Log.e("TAG", "Json pertama error : " + e.getMessage());
                        //  GetDataJsonfilter("menu", "stadion");
                        // showDialog_error();
                    }

                }
                addPage();
                viewPager.setCurrentItem(0);
                pDialog2.dismiss();

            }

            @Override
            protected String doInBackground(String... params) {
                Log.e("ket", "memproses parent");
                HashMap<String, String> data = new HashMap<>();
                data.put("operasi", params[0]);
                data.put("cabang", params[1]);

                return ruc.sendPostRequest(url2, data, "POST");
            }
        }
        a ru = new a();
        ru.execute(kata, cabang);
    }

    void getserver(String kata, String cabang) {
        dataList.clear();
        class a extends AsyncTask<String, Void, String> {
            private RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();


            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    JSONObject jsonObj = new JSONObject(s);
                    JSONArray contacts = jsonObj.getJSONArray("data");
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String namasubmenu = c.getString("category");
                        Data_Category_menu ds = new Data_Category_menu();
                        ds.setnama_category(namasubmenu);
                        dataList.add(ds);


                    }
                    CreateFileJson.saveData(getActivity(), s, "menu");
                } catch (final JSONException e) {
                    Log.e("TAG", "Json server sebenarnya salah : " + e.getMessage());
                    //  GetDataJsonfilter("menu", "stadion");
                    showDialog_error();
                }


                addPage();
                viewPager.setCurrentItem(0);

            }

            @Override
            protected String doInBackground(String... params) {
                Log.e("ket", "memproses server sebenaranya");
                HashMap<String, String> data = new HashMap<>();
                data.put("operasi", params[0]);
                data.put("cabang", params[1]);

                return ruc.sendPostRequest(url, data, "POST");
            }
        }
        a ru = new a();
        ru.execute(kata, cabang);
    }

    void all_menu(String kata, String cabang) {
        Log.e("allmenu", "jalan");

        class a extends AsyncTask<String, Void, String> {
            private RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    JSONObject jsonObj = new JSONObject(s);
                    JSONArray contacts = jsonObj.getJSONArray("all_menu");
                    CreateFileJson.saveData(getActivity(), s, "all_menu");
                    for (int i = 0; i < contacts.length(); i++) {
                       /* JSONObject c = contacts.getJSONObject(i);
                        JSONArray produk = c.getJSONArray("menu");
                        for (int y = 0; y < produk.length(); y++) {
                            JSONObject detailProduk = produk.getJSONObject(y);


                        }*/


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
                data.put("cabang", params[1]);
                return ruc.sendPostRequest(url2, data, "POST");
            }
        }
        a ru = new a();
        ru.execute(kata, cabang);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.wirasetiawan.BroadcastReceiver")) {
                GetDataJsonfilter("menu", "stadion");
                Intent i = context.getPackageManager().
                        getLaunchIntentForPackage(getActivity().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        }
    };


    private void showDialog_error() {
        String title = null, message = null;


        title = "Aplikasi Tidak Terhubung ke Server";
        message = "Coba Lagi Hubungkan ke Server ?";

        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getContext());

        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
                .setMessage(message)
                .setIcon(R.drawable.mrk)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // GetDataJsonfilter("menu", "stadion");
                        getserver_backup("menu", "stadion");


                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        android.app.AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    void getserver_backup(String kata, String cabang) {
        dataList.clear();
        class a extends AsyncTask<String, Void, String> {
            private RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();


            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    JSONObject jsonObj = new JSONObject(s);
                    JSONArray contacts = jsonObj.getJSONArray("data");
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String namasubmenu = c.getString("category");
                        Data_Category_menu ds = new Data_Category_menu();
                        ds.setnama_category(namasubmenu);
                        dataList.add(ds);


                    }
                    CreateFileJson.saveData(getActivity(), s, "menu");
                } catch (final JSONException e) {
                    Log.e("TAG", "Json server sebenarnya salah : " + e.getMessage());
                    //  GetDataJsonfilter("menu", "stadion");
                    showDialog_error();
                }


                addPage();
                viewPager.setCurrentItem(0);
                pDialog2.dismiss();
            }

            @Override
            protected String doInBackground(String... params) {
                Log.e("ket", "memproses server sebenaranya");
                HashMap<String, String> data = new HashMap<>();
                data.put("operasi", params[0]);
                data.put("cabang", params[1]);
                pDialog2.dismiss();
                return ruc.sendPostRequest(url, data, "POST");
            }
        }
        a ru = new a();
        ru.execute(kata, cabang);
    }


}
