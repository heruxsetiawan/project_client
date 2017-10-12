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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class Adapter_keranjang extends RecyclerView.Adapter<Adapter_keranjang.ViewHolder> {
    private ArrayList<Data_keranjang> rvsubmenu;
    private Handler repeatUpdateHandler = new Handler();
    private boolean mAutoIncrement = false;
    private boolean mAutoDecrement = false;
    private final long REP_DELAY = 150;
    Context context3;
    private DataHelper myDb;
    private Keranjang keranjang2;
    String note;
    private Timer timer = new Timer();
    private final long DELAY = 900;

    Adapter_keranjang(Context context2, ArrayList<Data_keranjang> inputData, Keranjang kr) {
        rvsubmenu = inputData;
        context3 = context2;
        keranjang2=kr;

    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvnama_order, txtCount,tvharga,hargatotal,fkitchen_id;
        RelativeLayout buttonInc, buttonDec;
        CardView cvMain;
        EditText mEditText;
        ViewHolder(View v) {
            super(v);
            tvnama_order = (TextView) v.findViewById(R.id.tv_submenu_keranjang);
            hargatotal = (TextView) v.findViewById(R.id.harga_total);
            cvMain = (CardView) v.findViewById(R.id.cv_keranjang);
            txtCount = (TextView) v.findViewById(R.id.tv_qty_keranjang);
            buttonInc = (RelativeLayout) v.findViewById(R.id.btn_plus_keranjang);
            buttonDec = (RelativeLayout) v.findViewById(R.id.btn_minus_keranjang);
            mEditText = (EditText) v.findViewById(R.id.add_note);
            tvharga=(TextView)v.findViewById(R.id.tv_harga_keranjang);
            fkitchen_id=(TextView)v.findViewById(R.id.fkitchen_id);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_keranjang_item, parent, false);
        myDb = new DataHelper(parent.getContext());
        return new ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Data_keranjang dk = rvsubmenu.get(position);
        final String code_produk = dk.getcode_produk();
        final String nama_order = dk.getnama_order();
        final String harga=dk.getharga();
        final String total=dk.gettotal();
        final String fkitchen_id=dk.getfkitchen_id();
        //ftotal,kitchenid
        holder.hargatotal.setText(total);
        holder.fkitchen_id.setText(fkitchen_id);
        holder.tvnama_order.setText(nama_order);
        holder.txtCount.setText(dk.getjumlah_order());
        holder.mEditText.setText(dk.getnote());

        holder.fkitchen_id.setVisibility(View.GONE);
        holder.hargatotal.setVisibility(View.GONE);

        holder.buttonInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double tempHarga = Double.parseDouble(harga);
                final String hargamenu = String.valueOf(Math.round(tempHarga));
                int hasil = 1 + Integer.parseInt(holder.txtCount.getText().toString());
                int total=hasil*Integer.parseInt(hargamenu);
                final String getnote = String.valueOf(holder.mEditText.getText());
                final Integer jumlah = Integer.valueOf(holder.txtCount.getText().toString());
                final String fkitchen_id = String.valueOf(holder.fkitchen_id.getText());
                if (jumlah == 0){
                    holder.txtCount.setText(hasil + "");
                    boolean isInserted = myDb.insertData(code_produk, nama_order, hasil+"", getnote, hargamenu, total+"",fkitchen_id);
                    myDb.getjumlah(code_produk);
                    myDb.getnote(code_produk);
                    keranjang2.getTotal();

                    if (isInserted) {
                        Log.e("SQLITE", "SUKSES");
                    } else {
                        Log.e("SQLITE", "Gagal");
                    }
                }else{
                    holder.txtCount.setText(hasil + "");
                    boolean isInserted = myDb.updateData(code_produk, nama_order, hasil+"", hargamenu, total+"",fkitchen_id);
                    myDb.getjumlah(code_produk);
                    keranjang2.getTotal();
                    if (isInserted) {
                        Log.e("SQLITE", "SUKSES");
                    } else {
                        Log.e("SQLITE", "Gagal");
                    }
                }

            }
        });
        holder.buttonDec.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                double tempHarga = Double.parseDouble(harga);
                final String hargamenu = String.valueOf(Math.round(tempHarga));
                final Integer aa2 = Integer.valueOf(holder.txtCount.getText().toString());
                int hasil = Integer.parseInt(holder.txtCount.getText().toString()) - 1;
                int total=hasil*Integer.parseInt(hargamenu);
                final String fkitchen_id = String.valueOf(holder.fkitchen_id.getText());
                if (aa2 > 0) {
                    holder.txtCount.setText(hasil + "");
                    boolean isInserted = myDb.updateData(code_produk, nama_order, hasil+"", hargamenu, total+"",fkitchen_id);
                    keranjang2.getTotal();
                    Log.e("total ",myDb.getTotal());
                    if (isInserted) {
                        Log.e("SQLITE", "SUKSES");
                    } else {
                        Log.e("SQLITE", "Gagal");
                    }
                    final Integer aa3 = Integer.valueOf(holder.txtCount.getText().toString());

                    if (aa3 == 0) {
                        Integer del_id = Integer.valueOf(code_produk);
                        myDb.deleteData(del_id);
                        keranjang2.getTotal();
                    }
                }
            }

        });

        holder.mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final int jumlah = Integer.valueOf(myDb.getjumlah(code_produk));

                final String test_note= s+"";
                note=test_note;
                timer.cancel();
                timer = new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                // TODO: do what you need here (refresh list)
                                // you will probably need to use runOnUiThread(Runnable action) for some specific actions
                                if(jumlah == 0){

                                }else{
                                    myDb.updateNote(code_produk, test_note);

                                }


                            }
                        },
                        DELAY
                );
            }
        });


        class RptUpdater implements Runnable {
            public void run() {
                if (mAutoIncrement) {
                    increment();
                    repeatUpdateHandler.postDelayed(new RptUpdater(), REP_DELAY);
                    keranjang2.getTotal();
                } else if (mAutoDecrement) {
                    final Integer aa2 = Integer.valueOf(holder.txtCount.getText().toString());
                    keranjang2.getTotal();
                    if (aa2 > 0) {
                        decrement();
                        repeatUpdateHandler.postDelayed(new RptUpdater(), REP_DELAY);

                    }
                }
            }

            private void increment() {
                int hasil = 1 + Integer.parseInt(holder.txtCount.getText().toString());
                holder.txtCount.setText(hasil + "");
            }
            private void decrement() {
                int hasil = Integer.parseInt(holder.txtCount.getText().toString()) - 1;
                holder.txtCount.setText(hasil + "");
                final Integer jumlah = Integer.valueOf(holder.txtCount.getText().toString());

                if (jumlah == 0) {
                    Integer del_id = Integer.valueOf(code_produk);
                    myDb.deleteData(del_id);
                    Log.e("delete Nama Menu", String.valueOf(del_id));
                }


        }
        }

        holder.buttonInc.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View arg0) {
                final int hasil = 1 + Integer.parseInt(holder.txtCount.getText().toString());
                final int jumlah = Integer.valueOf(myDb.getjumlah(code_produk));
                int total=hasil*Integer.parseInt(harga);
                final String getnote = String.valueOf(holder.mEditText.getText());
                final String fkitchen_id = String.valueOf(holder.fkitchen_id.getText());
                if (jumlah == 0) {
                    mAutoIncrement = true;
                    repeatUpdateHandler.post(new RptUpdater());
                    myDb.insertData(code_produk, nama_order, hasil+"", getnote, harga, total+"",fkitchen_id);
                    return false;

                }else if (jumlah > 0)
                {
                    mAutoIncrement = true;
                    repeatUpdateHandler.post(new RptUpdater());
                    myDb.updateData(code_produk, nama_order, hasil+"", harga, total+"",fkitchen_id);
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
            String value = harga;
            value = value.replace(".","").replace(",","");
            double amount = Double.parseDouble(value);
            DecimalFormat formatter = null;

            if (value != null && !value.equals("")) {
                formatter = new DecimalFormat("#,###");
            }
            if (!value.equals(""))
                holder.tvharga.setText(formatter.format(amount));

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
