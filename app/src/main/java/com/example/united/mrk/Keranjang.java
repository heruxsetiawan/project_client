package com.example.united.mrk;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.united.mrk.koneksi.RegisterUserClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class Keranjang extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private DataHelper myDb;
    ArrayList<Data_keranjang> dataList;
    ArrayList<Data_meja> dataList_meja;
    private Adapter_keranjang mAdapter;
    private RecyclerView rvView;
    Context context;
    public RelativeLayout btnorder;
    public TextView total;
    private static String solis_tes = BuildConfig.Main_menu;
    private static String url_split = BuildConfig.Solis_order;
    ArrayList<String> data_set_meja = new ArrayList<>();
    ArrayList<String> data_set_meja_filter = new ArrayList<>();
    RelativeLayout container, rl_total;
    Spinner spinner, spinner2;
    int b, d, status_table;
    String c;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    EditText txt_nama, txt_pass;
    String pilihmeja, fpkey, ftablekey, ftype, nomor_meja;
    String format = "";
    String user, pass;
    int respon_meja;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);
        spinner = (Spinner) findViewById(R.id.mySpinner2);
        spinner2 = (Spinner) findViewById(R.id.mySpinner3);
        btnorder = (RelativeLayout) findViewById(R.id.rl_total);
        rl_total = (RelativeLayout) findViewById(R.id.rl_total);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setTitle("Order List");
        context = getApplicationContext();
        rvView = (RecyclerView) findViewById(R.id.rv_submenu_keranjang);
        total = (TextView) findViewById(R.id.tvtotal);
        rvView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 1);
        rvView.setLayoutManager(layoutManager);
        myDb = new DataHelper(getApplicationContext());
        dataList = new ArrayList<>();
        dataList_meja = new ArrayList<>();
        String menuJson = CreateFileJson.getData(getApplicationContext(), "meja");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        jsonmeja("meja");
        Getfromdata();
        getjumlahdata();

        rvView.setItemViewCacheSize(dataList.size());
        getTotal();

        btnorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Dialog_login();
                Cek_Status_Meja("cek1", ftablekey);

            }
        });

    }

    private void showDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        alertDialogBuilder.setTitle("Hapus data pesanan");
        alertDialogBuilder
                .setMessage("Apakah anda akan menghapus data pesanan ?")
                .setIcon(R.drawable.mrk)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        total.setText("0");
                        c = total.getText().toString();
                        d = Integer.parseInt(c);
                        myDb.deleteAll();
                        Refresh();
                        if (d == 0) {
                            rl_total.setVisibility(View.GONE);
                        }
                        Intent i = new Intent(Keranjang.this, MainActivity.class);
                        startActivity(i);
                        finish();

                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    public void getTotal() {
        if (myDb.getTotal() == null) {
            total.setText("0");
            rl_total.setVisibility(View.GONE);
        } else if (myDb.getTotal().equalsIgnoreCase("0")) {
            total.setText("0");
            rl_total.setVisibility(View.GONE);
        } else
            rl_total.setVisibility(View.VISIBLE);
        try {
            String value = myDb.getTotal();
            value = value.replace(".", "").replace(",", "");
            double amount = Double.parseDouble(value);
            DecimalFormat formatter = null;

            if (value != null && !value.equals("")) {
                formatter = new DecimalFormat("#,###");
            }
            if (!value.equals(""))
                total.setText(formatter.format(amount));
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void kosong() {
        txt_nama.setText(null);
        txt_pass.setText(null);
    }


    void Refresh() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 1);
        rvView.setLayoutManager(layoutManager);
        myDb = new DataHelper(getApplicationContext());
        dataList = new ArrayList<>();
        Getfromdata();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.keranjang, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_delete:
                showDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    void setSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, data_set_meja);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    void setSpinner2() {
        //  ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, data_set_meja_filter);
        // adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        //  spinner2.setAdapter(adapter);
        // spinner2.setOnItemSelectedListener(this);
        Adapter_spinner_meja customAdapter = new Adapter_spinner_meja(context, dataList_meja);
        spinner2.setAdapter(customAdapter);
        spinner2.setOnItemSelectedListener(this);
    }

    void post_data() {

        String note, harga, nama_produk, qty, code_produk, total, fkitchen_id;
        Cursor res = myDb.getjumlah2();
        Integer jml = Integer.valueOf(getjumlahdata());

        String split = "";
        Integer i = 1;
        while (res.moveToNext()) {
            code_produk = res.getString(1);
            qty = res.getString(3);
            note = res.getString(4);


            if (i == 1) {

                split = "" + format;

            } else {

                split = "+" + format;

            }
            if (jml > 1) {
                format = code_produk + "." + note + "." + qty + "." + pilihmeja + "." + split;
            } else {
                format = code_produk + "." + note + "." + qty + "." + pilihmeja;
            }


            i++;


        }
        Log.e("format_post_data", format);

        res.close();

    }


    void Getfromdata() {

        String note, harga, nama_produk, qty, code_produk, total, fkitchen_id;
        Cursor res = myDb.getjumlah2();
        Integer jml = Integer.valueOf(getjumlahdata());

        String split = "";
        Integer i = 1;
        while (res.moveToNext()) {
            code_produk = res.getString(1);
            nama_produk = res.getString(2);
            qty = res.getString(3);
            note = res.getString(4);
            harga = res.getString(5);
            total = res.getString(6);
            fkitchen_id = res.getString(7);
//            Log.e("code_produk",code_produk);
//            Log.e("qty",qty);
//            Log.e("harga",harga);
//            Log.e("total",total);
//            Log.e("note",note);
//            Log.e("fkitchen_id",fkitchen_id);
            Data_keranjang dk = new Data_keranjang();
            dk.setcode_produk(code_produk);
            dk.setnama_order(nama_produk);
            dk.setjumlah_order(qty);
            dk.setnote(note);
            dk.setharga(harga);
            dk.settotal(total);
            dk.setfkitchen_id(fkitchen_id);
            Log.e("tes",nama_produk);
          /*  if (i == 1) {

                split = "" + format;

            } else {

                split = "+" + format;

            }
            if (jml > 1) {
                format = code_produk + "." + note + "." +  qty + "." +nomor_meja + "."+ split;
            } else {
                format = code_produk + "." + note + "." + qty + "." + nomor_meja;
            }
*/

            i++;

            try {
                dataList.add(dk);
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }

        }
        // Log.e("post_data_pesnanan", format);
        mAdapter = new Adapter_keranjang(context, dataList, Keranjang.this);
        rvView.setAdapter(mAdapter);

        res.close();

    }

    public String getjumlahdata() {
        String jumlah = null;
        Cursor res = myDb.hitungjumlahdata();
        while (res.moveToNext()) {
            jumlah = res.getString(0);

        }
        // Log.e("jumlah data", jumlah);
        res.close();

        return jumlah;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.mySpinner3) {

//            pilihmeja = parent.getItemAtPosition(position).toString();
            pilihmeja = ((TextView) view.findViewById(R.id.nama_meja)).getText().toString();
            fpkey = ((TextView) view.findViewById(R.id.fpkey)).getText().toString();
            ftablekey = ((TextView) view.findViewById(R.id.ftable_key)).getText().toString();
            ftype = ((TextView) view.findViewById(R.id.ftype)).getText().toString();
            Log.e("pilih meja", pilihmeja);

        } else if (spinner.getId() == R.id.mySpinner2) {
            // Log.e("click spinner 1", parent.getItemAtPosition(position).toString());
            //jsonmeja_filter(CreateFileJson.getData(this, "meja"), parent.getItemAtPosition(position).toString());
            jsonmeja_filter("meja", parent.getItemAtPosition(position).toString());
        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    void jsonmeja(String operasi) {

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
                    CreateFileJson.saveData(getApplicationContext(), s, "meja");
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String nama_meja = c.getString("meja_kategori");
                        //  Data_keranjang dk = new Data_keranjang();
                        //  dk.setnama_meja(nama_meja);
                        //  dataList.add(dk);
                        data_set_meja.add(nama_meja);


                    }

                } catch (final JSONException e) {
                    Log.e("TAG", "Json meja parsing error: " + e.getMessage());
                  //  Toast.makeText(Keranjang.this, "Gagal tidak terhubung ke server ", Toast.LENGTH_LONG).show();
                    jsonmeja("meja");
                }

                setSpinner();



            }

            @Override
            protected String doInBackground(String... params) {
                Log.e("ket", "memproses parent");
                HashMap<String, String> data = new HashMap<>();
                data.put("operasi", params[0]);
                return ruc.sendPostRequest(url_split, data, "POST");
            }
        }
        a ru = new a();
        ru.execute(operasi);
    }

    void jsonmeja_filter(String operasi, final String inputan) {
        dataList_meja.clear();
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
                        if (c.getString("meja_kategori").equalsIgnoreCase(inputan)) {
                            JSONArray produk = c.getJSONArray("detail");
                            for (int y = 0; y < produk.length(); y++) {
                                JSONObject detailProduk = produk.getJSONObject(y);
                                String fname = detailProduk.getString("fname");
                                String fpkey = detailProduk.getString("fpkey");
                                String ftablekey = detailProduk.getString("ftablekey");
                                String ftype = detailProduk.getString("ftype");
                                Data_meja dk = new Data_meja();
                                dk.setnama_meja(fname);
                                dk.setfpkey(fpkey);
                                dk.setftablekey(ftablekey);
                                dk.setftype(ftype);
                                dataList_meja.add(dk);
                                Log.e("nomor meja",fname);
                                //  data_set_meja_filter.add(fname);


                            }
                        }


                    }


                } catch (final JSONException e) {
                    Log.e("TAG", "jsonmeja_filter: " + e.getMessage());

                }
                setSpinner2();

            }

            @Override
            protected String doInBackground(String... params) {
                Log.e("ket", "jsonmeja_filter");
                HashMap<String, String> data = new HashMap<>();
                data.put("operasi", params[0]);
                return ruc.sendPostRequest(url_split, data, "POST");
            }


        }
        a ru = new a();
        ru.execute(operasi);
    }

    private void showDialog_meja() {
        String title = null, message = null;

        Log.e("showDialog_meja =", status_table + "");
        if (status_table == 1) {
            title = "Meja " + pilihmeja + " sudah di pakai";
            message = "Apakah anda tetap memilih meja " + pilihmeja+" ?";
        }
        if (status_table > 1) {
            title ="Pesanan di meja " + pilihmeja + " sudah ada";
            message = "Apakah anda akan menambah pesanan ?";
        }
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Keranjang.this);
       // alertDialogBuilder.setTitle("Pesanan di meja " + pilihmeja + " sudah ada");
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
               // .setMessage("Apakah anda akan menambah pesanan?")
                .setMessage(message)
                .setIcon(R.drawable.mrk)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Dialog_login();

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

    void Cek_Status_Meja(String operasi, final String String_order) {
        //1

        Log.e("Cek_Status_Meja", "jalan");
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


                    respon_meja = Integer.parseInt(s);
                    status_table = Integer.parseInt(s);


                    if (respon_meja < 1) {
                        //eksekui
                        Dialog_login();
                    } else {

                        showDialog_meja();
                    }

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Tidak Terhubung ke server ", Toast.LENGTH_LONG).show();
                }


            }

            @Override
            protected String doInBackground(String... params) {
                Log.e("ket", "memproses parent");
                HashMap<String, String> data = new HashMap<>();
                data.put("operasi", params[0]);
                data.put("ftablekey", params[1]);


                return ruc.sendPostRequest(url_split, data, "POST");
            }
        }
        a ru = new a();
        ru.execute(operasi, String_order);


    }

    private void Dialog_login() {
        //2
        Log.e("Dialog_login", "jalan");
        dialog = new AlertDialog.Builder(Keranjang.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.mrk);
        dialog.setTitle("Login");

        txt_nama = (EditText) dialogView.findViewById(R.id.txt_nama);
        txt_pass = (EditText) dialogView.findViewById(R.id.txt_pass);


        kosong();

        dialog.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                user = ((EditText) dialogView.findViewById(R.id.txt_nama)).getText().toString();
                pass = ((EditText) dialogView.findViewById(R.id.txt_pass)).getText().toString();
                Log.e("user", user);
                Log.e("pass", pass);
                if (user.equalsIgnoreCase("") && pass.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "User harus di isi ", Toast.LENGTH_LONG).show();
                } else {
                    login("login", user, pass);
                }


            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    void login(String operasi, final String user, final String pass) {
        //3
        Log.e("login", "jalan");
        class a extends AsyncTask<String, Void, String> {
            private RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.e("respon login", s);
                if (s.equalsIgnoreCase("berhasil_login")) {
                    Log.e("berhasil", "login");
                    checkin("checkin", ftablekey, ftype);
                } else {
                    Toast.makeText(getApplicationContext(), "User atau Password salah ", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {
                Log.e("ket", "memproses parent");
                HashMap<String, String> data = new HashMap<>();
                data.put("operasi", params[0]);
                data.put("user", params[1]);
                data.put("pass", params[2]);


                return ruc.sendPostRequest(url_split, data, "POST");
            }
        }
        a ru = new a();
        ru.execute(operasi, user, pass);
        //ru.execute(operasi, post_data_pesnanan);
    }

    void checkin(String operasi, final String ftablekey, final String ftype) {
        //4
        Log.e("checkin", "jalan");
        class a extends AsyncTask<String, Void, String> {
            private RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.e("checkin respon from php", s);
                if (s.equalsIgnoreCase("berhasil checkin")) {
                    Toast.makeText(getApplicationContext(), "Berhasil Checkin ", Toast.LENGTH_LONG).show();
                    post_data();
                    cek_Nomor_meja("check_meja", format);

                } else {
                    Toast.makeText(getApplicationContext(), "Gagal checkin tidak terhubung ke server ", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {
                Log.e("ket", "memproses parent");
                HashMap<String, String> data = new HashMap<>();
                data.put("operasi", params[0]);
                data.put("ftablekey", params[1]);
                data.put("ftype", params[2]);


                return ruc.sendPostRequest(url_split, data, "POST");
            }
        }
        a ru = new a();
        ru.execute(operasi, ftablekey, ftype);
        //ru.execute(operasi, post_data_pesnanan);
    }

    void cek_Nomor_meja(String operasi, final String String_order) {
        //5
        //cek nomor meja apakah kosong
        Log.e("cek_transheader_meja", "jalan");
        class a extends AsyncTask<String, Void, String> {
            private RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.e("respon check meja", s);
                if (s.equalsIgnoreCase("completed")) {
                    post_data_pesnanan("order", format, ftablekey, ftype, user, pass);
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal tidak terhubung ke server ", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {
                Log.e("ket", "memproses parent");
                HashMap<String, String> data = new HashMap<>();
                data.put("operasi", params[0]);
                data.put("format_order", params[1]);


                return ruc.sendPostRequest(url_split, data, "POST");
            }
        }
        a ru = new a();
        ru.execute(operasi, String_order);
        //ru.execute(operasi, post_data_pesnanan);
    }

    void post_data_pesnanan(String operasi, String format_post, String ftablekey, String ftype, String user, String pass) {
        //6
        // void post_data_pesnanan(String operasi, String nomor_meja, String ftablekey, String ftype) {
        //post data pesanan
        Log.e("post_data_pesnanan", "jalan");
        class a extends AsyncTask<String, Void, String> {
            private RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(Keranjang.this);
                pDialog.setMessage("Loading...");
                pDialog.setCancelable(false);
                pDialog.show();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.e("split from php", s);
                if (s.equalsIgnoreCase("selesai")) {
                    pDialog.dismiss();
                    myDb.deleteAll();
                    dataList.clear();
                    Intent intent = new Intent(Keranjang.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            protected String doInBackground(String... params) {
                Log.e("ket", "memproses parent");
                HashMap<String, String> data = new HashMap<>();

                data.put("operasi", params[0]);
                data.put("format_order", params[1]);
                data.put("ftablekey", params[2]);
                data.put("ftype", params[3]);
                data.put("user", params[4]);
                data.put("pass", params[5]);

                return ruc.sendPostRequest(url_split, data, "POST");
            }
        }
        a ru = new a();
        //ru.execute(operasi, nomor_meja, ftablekey, ftype);
        ru.execute(operasi, format_post, ftablekey, ftype, user, pass);
    }


}
