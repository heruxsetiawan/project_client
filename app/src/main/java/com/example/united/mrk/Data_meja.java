package com.example.united.mrk;

/**
 * Created by united on 31/08/2017.
 */

public class Data_meja {

    String nama_meja,fpkey,ftablekey,ftype;


    public void setnama_meja(String nama_meja) {
        this.nama_meja = nama_meja;
    }

    public void setfpkey(String fpkey) {
        this.fpkey = fpkey;
    }

    public void setftablekey(String ftablekey) {
        this.ftablekey = ftablekey;
    }

    public void setftype(String ftype) {
        this.ftype = ftype;
    }

    public String getnama_meja() {
        return nama_meja;
    }

    public String getfpkey() {
        return fpkey;
    }

  
    public String getftype() {
        return ftype;
    }

    public String getftablekey() {
        return ftablekey;
    }
}
