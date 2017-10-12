package com.example.united.mrk;



public class Data_Submenu {
    private String namasubmenu;
    private String img,note,harga;
    private String codemenu;
    private int count;
    private String qty,fkitchen_id;




    public void setidsubmenu(String idsubmenu){String idsubmenu1 = idsubmenu;}


    public void setnamasubmenu(String namasubmenu) {this.namasubmenu = namasubmenu;}
    public String getnamasubmenu()

    {
        return namasubmenu;
    }

    public void setimg(String img) {this.img = img;}
    String getImg() {
        return img;
    }



    public void setCount(int count) {
        this.count = count;
    }
    public int getCount() {
        return count ;
    }


    public void setcodemenu(String codemenu) {
        this.codemenu = codemenu;
    }

    public String getcodemenu() {
        return codemenu;
    }


    public void setqty(String qty) {
        this.qty = qty;
    }
    public String getqty() {
        return qty ;
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


    public void setfkitchen_id(String fkitchen_id) {
        this.fkitchen_id = fkitchen_id;
    }

    public String getfkitchen_id() {
        return fkitchen_id;
    }
}

