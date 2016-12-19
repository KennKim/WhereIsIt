package com.tkkim.whereisit.add_stuff.data;

/**
 * Created by conscious on 2016-12-04.
 */

public class Tag {
    private int tag_no;
    private String stu_no;

    @Override
    public String toString() {
        return "Tag{" +
                "stu_no='" + stu_no + '\'' +
                ", tag_no=" + tag_no +
                ", tag_name='" + tag_name + '\'' +
                '}';
    }

    public String getStu_no() {
        return stu_no;
    }

    public void setStu_no(String stu_no) {
        this.stu_no = stu_no;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public int getTag_no() {
        return tag_no;
    }

    public void setTag_no(int tag_no) {
        this.tag_no = tag_no;
    }

    private String tag_name;
}
