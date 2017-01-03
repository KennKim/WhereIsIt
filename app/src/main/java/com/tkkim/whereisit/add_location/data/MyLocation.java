package com.tkkim.whereisit.add_location.data;

import java.io.Serializable;


public class MyLocation implements Serializable {

    private static final long serialVersionUID = 1L;

    private int loc_no;
    private String loc_name;
    private String loc_comment;
    private String loc_imgpath;
    private String loc_thumbpath;
    private String loc_date;

    @Override
    public String toString() {
        return "MyLocation{" +
                "loc_comment='" + loc_comment + '\'' +
                ", loc_no=" + loc_no +
                ", loc_name='" + loc_name + '\'' +
                ", loc_imgpath='" + loc_imgpath + '\'' +
                ", loc_thumbpath='" + loc_thumbpath + '\'' +
                ", loc_date='" + loc_date + '\'' +
                '}';
    }

    public String getLoc_comment() {
        return loc_comment;
    }

    public void setLoc_comment(String loc_comment) {
        this.loc_comment = loc_comment;
    }

    public String getLoc_date() {
        return loc_date;
    }

    public void setLoc_date(String loc_date) {
        this.loc_date = loc_date;
    }

    public String getLoc_imgpath() {
        return loc_imgpath;
    }

    public void setLoc_imgpath(String loc_imgpath) {
        this.loc_imgpath = loc_imgpath;
    }

    public String getLoc_name() {
        return loc_name;
    }

    public void setLoc_name(String loc_name) {
        this.loc_name = loc_name;
    }

    public int getLoc_no() {
        return loc_no;
    }

    public void setLoc_no(int loc_no) {
        this.loc_no = loc_no;
    }

    public String getLoc_thumbpath() {
        return loc_thumbpath;
    }

    public void setLoc_thumbpath(String loc_thumbpath) {
        this.loc_thumbpath = loc_thumbpath;
    }
}
