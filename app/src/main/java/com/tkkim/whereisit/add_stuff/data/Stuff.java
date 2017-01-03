package com.tkkim.whereisit.add_stuff.data;

import java.io.Serializable;


public class Stuff implements Serializable {

    private static final long serialVersionUID = 1L;

    private int stu_no;
    private String loc_no;
    private String stu_name;
    private String stu_comment;
    private String stu_imgpath;
    private String stu_thumbpath;
    private String stu_date;

    @Override
    public String toString() {
        return "Stuff{" +
                "loc_no='" + loc_no + '\'' +
                ", stu_no=" + stu_no +
                ", stu_name='" + stu_name + '\'' +
                ", stu_comment='" + stu_comment + '\'' +
                ", stu_imgpath='" + stu_imgpath + '\'' +
                ", stu_thumbpath='" + stu_thumbpath + '\'' +
                ", stu_date='" + stu_date + '\'' +
                '}';
    }

    public String getLoc_no() {
        return loc_no;
    }

    public void setLoc_no(String loc_no) {
        this.loc_no = loc_no;
    }

    public String getStu_comment() {
        return stu_comment;
    }

    public void setStu_comment(String stu_comment) {
        this.stu_comment = stu_comment;
    }

    public String getStu_date() {
        return stu_date;
    }

    public void setStu_date(String stu_date) {
        this.stu_date = stu_date;
    }

    public String getStu_imgpath() {
        return stu_imgpath;
    }

    public void setStu_imgpath(String stu_imgpath) {
        this.stu_imgpath = stu_imgpath;
    }

    public String getStu_name() {
        return stu_name;
    }

    public void setStu_name(String stu_name) {
        this.stu_name = stu_name;
    }

    public int getStu_no() {
        return stu_no;
    }

    public void setStu_no(int stu_no) {
        this.stu_no = stu_no;
    }

    public String getStu_thumbpath() {
        return stu_thumbpath;
    }

    public void setStu_thumbpath(String stu_thumbpath) {
        this.stu_thumbpath = stu_thumbpath;
    }
}
