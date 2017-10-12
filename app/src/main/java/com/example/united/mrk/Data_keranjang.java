package com.example.united.mrk;

/**
 * Created by united on 08/06/2017.
 */

class Data_keranjang {
    String nama_order,jumlah_order,code_produk,note,harga,total,fkitchen_id;


    public void setnama_order(String nama_order) {
        this.nama_order = nama_order;
    }
    public String getnama_order() {
        return nama_order;
    }



    public void setjumlah_order(String jumlah_order) {this.jumlah_order = jumlah_order;}
    public String getjumlah_order() {
        return jumlah_order;
    }

    public void setcode_produk(String code_produk) {
        this.code_produk = code_produk;
    }

    public String getcode_produk() {
        return code_produk;
    }

    public void setnote(String note) {
        this.note = note;
    }

    public String getnote() {
        return note;
    }

    public void setharga(String harga) {
        this.harga = harga;
    }

    public String getharga() {
        return harga;
    }

    public void settotal(String total) {
        this.total = total;
    }

    public String gettotal() {
        return total;
    }


    public void setfkitchen_id(String fkitchen_id) {
        this.fkitchen_id = fkitchen_id;
    }

    public String getfkitchen_id() {
        return fkitchen_id;
    }
}
