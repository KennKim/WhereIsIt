package com.tkkim.whereisit.detail_stuff;

import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkkim.whereisit.R;
import com.tkkim.whereisit.detail_location.DetailLocation;
import com.tkkim.whereisit.z_etc.DBHelper;

import java.io.File;


public class DetailStuff extends AppCompatActivity {

    private DBHelper dbHelper;

    private ImageView ivStuImg;
    private TextView tvStuName;
    private TextView tvStuComment;
    private TextView tvStuDate;

    public static final String PUT_STU_NO = "stuNo";
    public static final String PUT_STU_NAME = "stuName";
    public static final String PUT_STU_COMMENT = "stuComment";
    public static final String PUT_STU_DATE = "stuDate";
    public static final String PUT_STU_IMGPATH = "stuImgpath";

    private String locNo;
    private String stuNo;
    private String stuName;
    private String stuComment;
    private String stuDate;
    private String stuImgpath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locNo=getIntent().getStringExtra(DetailLocation.PUT_LOC_NO);
        stuNo=getIntent().getStringExtra(PUT_STU_NO);
        stuName=getIntent().getStringExtra(PUT_STU_NAME);
        stuComment=getIntent().getStringExtra(PUT_STU_COMMENT);
        stuDate=getIntent().getStringExtra(PUT_STU_DATE);
        stuImgpath=getIntent().getStringExtra(PUT_STU_IMGPATH);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(stuName);

        setContentView(R.layout.activity_detail_stuff);
        ivStuImg=(ImageView)findViewById(R.id.ivStuImg);
        tvStuName=(TextView)findViewById(R.id.tvStuName);
        tvStuComment=(TextView)findViewById(R.id.tvStuComment);
        tvStuDate=(TextView)findViewById(R.id.tvStuDate);

        init();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // finish the activity
                onBackPressed();
                return true;
            case R.id.updateData:
                updateData();
                return true;
            case R.id.deleteData:
                deleteData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init() {

        if (stuImgpath != null) {
            ivStuImg.setImageURI(Uri.fromFile(new File(stuImgpath)));
        }

        tvStuName.setText(stuName);
        tvStuComment.setText(stuComment);
        tvStuDate.setText(stuDate);
    }
    private void deleteData() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("삭제하기");
        ab.setMessage("'"+stuName + "'을 \n삭제 할까요?");
        ab.setIcon(getResources().getDrawable(R.drawable.ic_check_box_black_24dp));
        ab.setCancelable(false);
        ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                if (dbHelper == null) {
                    dbHelper = new DBHelper(getApplicationContext(), dbHelper.DB_NAME, null, 1);
                }
                dbHelper.deleteStuff(stuNo,locNo);
                finish();
            }
        });

        ab.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });
        AlertDialog dialog = ab.create();
        dialog.show();
        infoSnackbar();
    }

    private void updateData() {
        infoSnackbar();
    }
    private void infoSnackbar() {
        View wView = getWindow().getDecorView().getRootView();
        Snackbar snackbar = Snackbar.make(wView, "test", Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.CYAN)
                .setAction("GOOD", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        View snackView = snackbar.getView();
        snackView.setBackgroundColor(getResources().getColor(R.color.colorFab));
        snackbar.show();
    }
}
