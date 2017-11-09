package com.example.united.mrk;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by DAT on 9/1/2015.
 */
public class FragmentChild extends Fragment implements MenuItemCompat.OnActionExpandListener {
    static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    String id;
    private RecyclerView rvView;
    private ArrayList<Data_Submenu> dataList = new ArrayList<>();
    private ArrayList<Data_Category_menu> dataList2 = new ArrayList<>();
    private Adapter_Child mAdapter;
    Context context;
    private SearchView.OnQueryTextListener queryTextListener;
    boolean cari = true;
    FragmentParent fp;
    FragmentChild fragmentchild;
    RecyclerView.LayoutManager layoutManager;
    public final static String LIST_STATE_KEY = "recycler_list_state";
    Parcelable listState;
    TabLayout tabLayout;
    ViewPager viewPager;
    private ProgressDialog pDialog;
    // public String menuJson;
    private String posisi, submenu;
    public static final String SUBMENU_PUTEXTRA = "submenu";
    ProgressDialog progress;
    public DataHelper myDb;
    public static AlertDialog.Builder dialog;
    public  LayoutInflater inflater;
    public   View dialogView;
    public FragmentChild() {
    }


    public static FragmentChild newInstance(int page, String category, String posisi, String submenu) {
            Bundle args = new Bundle();
            args.putInt(ARG_PAGE, page);
            args.putString(SUBMENU_PUTEXTRA, submenu);
            FragmentChild fragment = new FragmentChild();
            fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        submenu = getArguments().getString(SUBMENU_PUTEXTRA);


        myDb = new DataHelper(getActivity());

        fp = (FragmentParent) getFragmentManager().findFragmentById(R.id.fragmentParent);



        setRetainInstance(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        final View view = inflater.inflate(R.layout.fragmentchild, container, false);
        rvView = (RecyclerView) view.findViewById(R.id.rv_submenu);
        tabLayout = (TabLayout) view.findViewById(R.id.my_tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.my_viewpager);
        rvView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(context, 1);
        rvView.setLayoutManager(layoutManager);
        dataList = new ArrayList<Data_Submenu>();
        myDb = new DataHelper(getActivity());
        fromtxt_filter(submenu);
        setHasOptionsMenu(true);
        rvView.setItemViewCacheSize(dataList.size());

        return view;
    }

    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        listState = layoutManager.onSaveInstanceState();
        state.putParcelable(LIST_STATE_KEY, listState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null)
            listState = savedInstanceState.getParcelable(LIST_STATE_KEY);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_home, menu);
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


    private void delete_dialog() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());

        alertDialogBuilder.setTitle("Hapus data pesanan");
        alertDialogBuilder
                .setMessage("Apakah anda akan menghapus data pesanan ?")
                .setIcon(R.drawable.mrk)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        myDb.deleteAll();
                        Refresh();

                        Intent i = new Intent(getActivity(), MainActivity.class);
                        startActivity(i);
                        getActivity().finish();

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_keranjang:
                Cursor res = myDb.getAllData();
                if (res.getCount() == 0) {
                    showMessage("Data Kosong", "Nothing found");
                    return true;
                }

                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                    //buffer.append("nomor :" + res.getString(0) + "\n");
                    buffer.append("Name :" + res.getString(2) + "\n");
                    buffer.append("Jumlah :" + res.getString(3) + "\n");
                    buffer.append("Note :" + res.getString(4) + "\n");
                }

                showMessage("Order List", buffer.toString());
                return true;
            case R.id.action_refresh:
                showDialog();

                return true;
            case R.id.action_delete:
                Cursor res2 = myDb.getAllData();
                if (res2.getCount() == 0) {
                    showMessage("Data Kosong", "Nothing found");
                    return true;
                } else {
                    delete_dialog();
                    return true;
                }
            case R.id.action_search:
              /*  Intent intent = new Intent(getActivity(), Main_cari.class);
                startActivity(intent);*/


             new Pindah_pencarian().execute();


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class Pindah_pencarian extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        protected String doInBackground(String... args) {
            Intent intent = new Intent(getActivity(), Pencarian.class);
            startActivity(intent);
            getActivity().finish();
            return null;

        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();


        }

    }


    private void showDialog() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Update Data");
        alertDialogBuilder
                .setMessage("Apakah anda akan mengupdate data?")
                .setIcon(R.drawable.mrk)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        new CreateNewPosting().execute();


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

    class CreateNewPosting extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
//            pDialog.show();


        }

        protected String doInBackground(String... args) {
            //letak dimana progress berjalan
            //biasanya koneksi ke server maupun proses yang membutuhkan waktu

            fp.refresh_update_data("menu", "stadion");


            return null;

        }

        protected void onPostExecute(String file_url) {
            //tempat dimana progress telah selesai
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 1);
            rvView.setLayoutManager(layoutManager);
            dataList = new ArrayList<>();
            rvView.setItemViewCacheSize(dataList.size());

            Intent i = getActivity().getPackageManager().
                    getLaunchIntentForPackage(getActivity().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            pDialog.dismiss();


        }

    }

    public void Refresh() {
        try {
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 1);
            rvView.setLayoutManager(layoutManager);
            dataList = new ArrayList<>();
            rvView.setItemViewCacheSize(dataList.size());
            //Getfromtxt(CreateFileJson.getData(getContext(), id));
            //---------------------> Getfromjson("menu", id);

        } catch (Exception e) {
            Log.e("eror", e + "");
        }


    }


    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }


    public void fromtxt_filter(String list) {

        dataList.clear();

        try {
            String s = CreateFileJson.getData(getActivity(), "menu");
            JSONObject jsonObj = new JSONObject(s);
            JSONArray contacts = jsonObj.getJSONArray("data");
            for (int i = 0; i < contacts.length(); i++) {

                JSONObject c = contacts.getJSONObject(i);
                if (c.getString("category").equalsIgnoreCase(list)) {

                    JSONArray produk = c.getJSONArray("menu");
                    for (int y = 0; y < produk.length(); y++) {
                        JSONObject detailProduk = produk.getJSONObject(y);
                        String codesubmenu = detailProduk.getString("fpkey");
                        String namasubmenu = detailProduk.getString("nama");
                        String hargamenu = detailProduk.getString("harga");
                        String fkitchen_id = detailProduk.getString("fkitchen_id");
                        String img = detailProduk.getString("img");
                        String pedas=detailProduk.getString("spicy");
                        String new_menu=detailProduk.getString("new_menu");
                        String recomended=detailProduk.getString("recomended");
                        String favorit=detailProduk.getString("favorit");


                        Data_Submenu ds = new Data_Submenu();
                        ds.setcodemenu(codesubmenu);
                        ds.setnamasubmenu(namasubmenu);
                        ds.setharga(hargamenu);
                        ds.setqty(myDb.getjumlah(codesubmenu));
                        ds.setnote(myDb.getnote(codesubmenu));
                        ds.setfkitchen_id(fkitchen_id);
                        ds.setimg(img);
                        ds.setpedas(pedas);
                        ds.setnew_menu(new_menu);
                        ds.setrecomended(recomended);
                        ds.setfavorit(favorit);
                        dataList.add(ds);

                    }
                }
            }
            rvView.setItemViewCacheSize(dataList.size());
            mAdapter = new Adapter_Child(getActivity(), dataList, fp);
            rvView.setAdapter(mAdapter);
        } catch (JSONException e) {
            Log.e("JSONException detail", e.getMessage());
            Toast.makeText(getContext(), "json error "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        Refresh();
        return true;
    }
}
