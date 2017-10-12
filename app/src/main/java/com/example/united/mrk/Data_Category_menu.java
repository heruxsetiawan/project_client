package com.example.united.mrk;


class Data_Category_menu {


    private String id_category;
    private String nama_category;
    private String img;
    private String posisi;

    void setid_category(String id_category) {
        this.id_category = id_category;
    }
    String getIdcategory() {
        return id_category;
    }

    void setnama_category(String nama_category) {
        this.nama_category = nama_category;
    }
    String getcategory() {
        return nama_category;
    }

    void setImg(String img) {
        this.img = img;
    }
    String getImg() {
        return img;
    }


    public void setPosisi(String posisi) {
        this.posisi = posisi;
    }
    String getposisi() {
        return posisi;
    }
}
