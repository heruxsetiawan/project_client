package com.example.united.mrk;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class Adapter_pencarian extends RecyclerView.Adapter<Adapter_pencarian.ViewHolder> {
    private ArrayList<Data_pencarian> rvsubmenu;
    private Handler repeatUpdateHandler = new Handler();
    private boolean mAutoIncrement = false;
    private boolean mAutoDecrement = false;
    private final long REP_DELAY = 150;
    Context context3;
    private DataHelper myDb;
    Pencarian fragmentparent;

    private Timer timer = new Timer();
    private final long DELAY = 900;
    String note;


    Adapter_pencarian(Context context2, ArrayList<Data_pencarian> inputData, Pencarian fp) {
        rvsubmenu = inputData;
        context3 = context2;
        fragmentparent = fp;
    }

    void setFilter(ArrayList<Data_pencarian> menu) {
        rvsubmenu = new ArrayList<>();
        rvsubmenu.addAll(menu);
        notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvnamasubmenu, txtCount, editText,fkitchen_id;
        ImageView img_submenu,pedas,new_menu,favorit,recomended;
        RelativeLayout buttonInc, buttonDec;
        CardView cvMain;
        public EditText mEditText;
        RelativeLayout rl_minus,rl_plus;
        RelativeLayout note,rladd,view2;

        ViewHolder(View v) {
            super(v);
            tvnamasubmenu = (TextView) v.findViewById(R.id.tv_submenu);
            fkitchen_id = (TextView) v.findViewById(R.id.fkitchen_id);
            img_submenu = (ImageView) v.findViewById(R.id.img_submenu_);
            cvMain = (CardView) v.findViewById(R.id.cv_submenu);
            txtCount = (TextView) v.findViewById(R.id.tv_qty);
            buttonInc = (RelativeLayout) v.findViewById(R.id.btn_plus);
            buttonDec = (RelativeLayout) v.findViewById(R.id.btn_minus);
            mEditText = (EditText) v.findViewById(R.id.notes);
            editText = (TextView) v.findViewById(R.id.tv_harga);
            note=(RelativeLayout)v.findViewById(R.id.rl_notes);
            rladd=(RelativeLayout)v.findViewById(R.id.relativeLayout2);
            pedas=(ImageView) v.findViewById(R.id.pedas);
            new_menu=(ImageView) v.findViewById(R.id.new_menu);
            favorit=(ImageView) v.findViewById(R.id.favorit);
            recomended=(ImageView) v.findViewById(R.id.recomend);




        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_food_item, parent, false);
        myDb = new DataHelper(parent.getContext());
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Data_pencarian ds = rvsubmenu.get(position);
        final String namasubmenu = ds.getnamasubmenu();
        final String codemenu = ds.getcodemenu();
        final String hargamenu = ds.getharga();
        final String fkitchen_id=ds.getfkitchen_id();
        final String pedas=ds.getpedas();
        final String new_menu=ds.getnew_menu();
        final String recomended=ds.getrecomended();
        final String favorit=ds.getfavorit();
        holder.fkitchen_id.setText(fkitchen_id);
        holder.tvnamasubmenu.setText(namasubmenu);
        holder.txtCount.setText(ds.getqty());
        holder.mEditText.setText(ds.getnote());
        holder.rladd.setVisibility(View.GONE);
        holder.pedas.setVisibility(View.GONE);
        holder.new_menu.setVisibility(View.GONE);
        holder.recomended.setVisibility(View.GONE);
        holder.favorit.setVisibility(View.GONE);
        if (pedas.equalsIgnoreCase("1")){
            holder.pedas.setVisibility(View.VISIBLE);
        }
        if (new_menu.equalsIgnoreCase("1")){
            holder.new_menu.setVisibility(View.VISIBLE);
        }
        if (recomended.equalsIgnoreCase("1")){
            holder.recomended.setVisibility(View.VISIBLE);
        }
        if (favorit.equalsIgnoreCase("1")){
            holder.favorit.setVisibility(View.VISIBLE);
        }

        Picasso.with(context3)
                .load(ds.getImg())
                .into(holder.img_submenu);
        holder.img_submenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog_gambar mydialog = new dialog_gambar(context3,ds.getImg());
                mydialog.show();

            }
        });
       /* final Integer jumlah = Integer.valueOf(holder.txtCount.getText().toString());
        if(jumlah==0){
            holder.note.setVisibility(View.GONE);
            holder.rl1.setVisibility(View.GONE);
            holder.rladd.setVisibility(View.VISIBLE);
            holder.view2.setVisibility(View.GONE);

        }*/
        holder.rladd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double tempHarga = Double.parseDouble(hargamenu);
                final String harga = String.valueOf(Math.round(tempHarga));
                holder.rladd.setVisibility(View.GONE);
              //  holder.rl1.setVisibility(View.VISIBLE);
                final Integer jumlah = Integer.valueOf(holder.txtCount.getText().toString());
                final String getnote = String.valueOf(holder.mEditText.getText());
                final int hasil = 1 + Integer.parseInt(holder.txtCount.getText().toString());
                final int total = hasil * Integer.parseInt(harga);
                final String fkitchen_id = String.valueOf(holder.fkitchen_id.getText());

                if (jumlah == 0) {
                    holder.txtCount.setText(hasil + "");
                    boolean isInserted2 = myDb.insertData(codemenu, namasubmenu, hasil + "", getnote, harga, total + "",fkitchen_id);
                    fragmentparent.getTotal();
                    holder.note.setVisibility(View.VISIBLE);
                  //  holder.rl1.setVisibility(View.VISIBLE);
                    holder.rladd.setVisibility(View.GONE);
                    //holder.view2.setVisibility(View.VISIBLE);
                    if (isInserted2) {
                        Log.e("SQLITE", "SUKSES");
                    } else {
                        Log.e("SQLITE", "Gagal");
                    }
                }

            }
        });


        class RptUpdater implements Runnable {
            public void run() {
                if (mAutoIncrement) {
                    increment();
                    repeatUpdateHandler.postDelayed(new RptUpdater(), REP_DELAY);
                    fragmentparent.getTotal();
                } else if (mAutoDecrement) {
                    final Integer aa2 = Integer.valueOf(holder.txtCount.getText().toString());
                    fragmentparent.getTotal();
                    if (aa2 > 0) {
                        decrement();
                        repeatUpdateHandler.postDelayed(new RptUpdater(), REP_DELAY);

                    }
                }
            }

            private void increment() {
                int hasil = 1 + Integer.parseInt(holder.txtCount.getText().toString());
                holder.txtCount.setText(hasil + "");
                holder.note.setVisibility(View.VISIBLE);
            //    holder.rl1.setVisibility(View.VISIBLE);
                holder.rladd.setVisibility(View.GONE);

            }

            private void decrement() {
                int hasil = Integer.parseInt(holder.txtCount.getText().toString()) - 1;
                holder.txtCount.setText(hasil + "");
                final Integer jumlah = Integer.valueOf(holder.txtCount.getText().toString());
                holder.note.setVisibility(View.VISIBLE);
                if (jumlah == 0) {
                    Integer del_id = Integer.valueOf(codemenu);
                    myDb.deleteData(del_id);
                    holder.note.setVisibility(View.GONE);
                  //  holder.rl1.setVisibility(View.GONE);
                   // holder.rladd.setVisibility(View.VISIBLE);
                    Log.e("delete Nama Menu", String.valueOf(del_id));
                }
            }

        }

        holder.mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final int jumlah = Integer.valueOf(myDb.getjumlah(codemenu));
                final String test_note = s + "";
                note = test_note;
                timer.cancel();
                timer = new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                // TODO: do what you need here (refresh list)
                                // you will probably need to use runOnUiThread(Runnable action) for some specific actions
                                if (jumlah == 0) {

                                } else {
                                    myDb.updateNote(codemenu, test_note);
                                }
                            }
                        },
                        DELAY
                );
            }
        });


        holder.buttonInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double tempHarga = Double.parseDouble(hargamenu);
                final String harga = String.valueOf(Math.round(tempHarga));
                final Integer jumlah = Integer.valueOf(holder.txtCount.getText().toString());
                final String getnote = String.valueOf(holder.mEditText.getText());
                final int hasil = 1 + Integer.parseInt(holder.txtCount.getText().toString());
                final int total = hasil * Integer.parseInt(harga);
                final String fkitchen_id = String.valueOf(holder.fkitchen_id.getText());

                if (jumlah == 0) {
                    holder.txtCount.setText(hasil + "");
                    boolean isInserted2 = myDb.insertData(codemenu, namasubmenu, hasil + "", getnote, harga, total + "",fkitchen_id);
                    fragmentparent.getTotal();
                    holder.note.setVisibility(View.VISIBLE);
//                    holder.rl1.setVisibility(View.VISIBLE);
                    holder.rladd.setVisibility(View.GONE);
                    if (isInserted2) {
                        Log.e("SQLITE", "SUKSES");
                        Log.e("tes pedas",pedas);
                    } else {
                        Log.e("SQLITE", "Gagal");
                    }
                } else {
                    holder.txtCount.setText(hasil + "");
                    boolean isUpdate = myDb.updateData(codemenu, namasubmenu, hasil + "", harga, total + "",fkitchen_id);
                    fragmentparent.getTotal();
                    holder.note.setVisibility(View.VISIBLE);
                  //  holder.rl1.setVisibility(View.VISIBLE);
                    holder.rladd.setVisibility(View.GONE);
                    if (isUpdate) {
                        Log.e("SQLITE", "SUKSES");
                        Log.e("getnote", getnote);
                    } else {
                        Log.e("SQLITE", "Gagal");
                    }
                }
            }
        });
        holder.buttonInc.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View arg0) {
                double tempHarga = Double.parseDouble(hargamenu);
                final String harga = String.valueOf(Math.round(tempHarga));
                final int hasil = 1 + Integer.parseInt(holder.txtCount.getText().toString());
                final int jumlah = Integer.valueOf(myDb.getjumlah(codemenu));
                final int total = Integer.parseInt(harga) * hasil;
                final String getnote = String.valueOf(holder.mEditText.getText());
                final String fkitchen_id = String.valueOf(holder.fkitchen_id.getText());


                if (jumlah == 0) {
                    mAutoIncrement = true;
                    repeatUpdateHandler.post(new RptUpdater());
                    myDb.insertData(codemenu, namasubmenu, hasil + "", getnote, harga, total + "",fkitchen_id);
                    return false;
                } else if (jumlah > 0) {
                    mAutoIncrement = true;
                    repeatUpdateHandler.post(new RptUpdater());
                    myDb.updateData(codemenu, namasubmenu, hasil + "", harga, total + "",fkitchen_id);
                    return false;
                }
                return false;
            }
        });
        holder.buttonInc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) && mAutoIncrement) {
                    mAutoIncrement = false;
                }
                return false;
            }
        });


        holder.buttonDec.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                double tempHarga = Double.parseDouble(hargamenu);
                final String harga = String.valueOf(Math.round(tempHarga));
                final Integer aa2 = Integer.valueOf(holder.txtCount.getText().toString());
                int hasil = Integer.parseInt(holder.txtCount.getText().toString()) - 1;
                final int total = Integer.parseInt(harga) * hasil;
                String aa = holder.txtCount.getText().toString();
                final String fkitchen_id = String.valueOf(holder.fkitchen_id.getText());


                if (aa2 > 0) {
                    holder.txtCount.setText(hasil + "");
                    boolean isUpdate = myDb.updateData(codemenu, namasubmenu, hasil + "", harga, total + "",fkitchen_id);
                    fragmentparent.getTotal();
                    holder.note.setVisibility(View.VISIBLE);
                //    holder.rl1.setVisibility(View.VISIBLE);
                    holder.rladd.setVisibility(View.GONE);
                    if (isUpdate) {
                        Log.e("SQLITE", "SUKSES");
                    } else {
                        Log.e("SQLITE", "Gagal");
                    }
                    final Integer aa3 = Integer.valueOf(holder.txtCount.getText().toString());

                    if (aa3 == 0) {
                        Integer del_id = Integer.valueOf(codemenu);
                        myDb.deleteData(del_id);
                        fragmentparent.getTotal();
                        holder.note.setVisibility(View.GONE);
                     //   holder.rl1.setVisibility(View.GONE);
                     //   holder.rladd.setVisibility(View.VISIBLE);
                        Log.e("delete Nama Menu", String.valueOf(del_id));
                    }


                }
            }

        });
        holder.buttonDec.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View arg0) {

                mAutoDecrement = true;
                repeatUpdateHandler.post(new RptUpdater());

                return false;
            }
        });
        holder.buttonDec.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                        && mAutoDecrement) {
                    mAutoDecrement = false;
                }
                return false;
            }
        });
        try {
            double tempHarga = Double.parseDouble(hargamenu);
            String value = String.valueOf(Math.round(tempHarga));
            value = value.replace(".", "").replace(",", "");
            double amount = Double.parseDouble(value);
            DecimalFormat formatter = null;

            if (value != null && !value.equals("")) {
                formatter = new DecimalFormat("#,###");
            }
            if (!value.equals(""))
                holder.editText.setText(formatter.format(amount));
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return rvsubmenu.size();
    }


}
