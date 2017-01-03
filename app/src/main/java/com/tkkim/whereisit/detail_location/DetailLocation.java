package com.tkkim.whereisit.detail_location;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkkim.whereisit.R;
import com.tkkim.whereisit.add_location.AddLocation;
import com.tkkim.whereisit.add_location.data.MyLocation;
import com.tkkim.whereisit.add_stuff.AddStuff;
import com.tkkim.whereisit.add_stuff.data.Stuff;
import com.tkkim.whereisit.detail_stuff.DetailStuff;
import com.tkkim.whereisit.z_etc.DBHelper;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by conscious on 2016-12-05.
 */

public class DetailLocation extends AppCompatActivity {

    private DBHelper dbHelper;

    private RecyclerView recyclerView;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolBar;

    private ImageView ivBackground;
    private TextView tvNoData;
    private FloatingActionButton fabDetailLoc;
    private ArrayList<Stuff> stuItems;
//    private ArrayList<MyLocation> locItems;

    public static final String PUT_LOC_NO = "locNo";
    public static final String PUT_LOC_NAME = "locName";
    public static final String PUT_LOC_COMMENT = "locComment";
    public static final String PUT_LOC_IMGPATH = "locImgpath";
    private String locNo;
    private String locName;
    private String locComment;
    private String locImgpath;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_location);

        locNo = getIntent().getStringExtra(PUT_LOC_NO);
        locName = getIntent().getStringExtra(PUT_LOC_NAME);
        locComment = getIntent().getStringExtra(PUT_LOC_COMMENT);
        locImgpath = getIntent().getStringExtra(PUT_LOC_IMGPATH);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        toolBar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout.setTitle(locName);

        ivBackground = (ImageView) findViewById(R.id.ivBackground);
        if (getIntent().getStringExtra(PUT_LOC_IMGPATH) != null) {
            ivBackground.setImageURI(Uri.fromFile(new File(locImgpath)));
        }


        if (dbHelper == null) {
            dbHelper = new DBHelper(this, dbHelper.DB_NAME, null, 1);
        }
        stuItems = dbHelper.getStuAll(locNo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(stuItems));

        tvNoData = (TextView) findViewById(R.id.tvNoData);
        if (!stuItems.isEmpty()) {
            tvNoData.setVisibility(View.GONE);
        }

        fabDetailLoc = (FloatingActionButton) findViewById(R.id.fabDetailLoc);
        fabDetailLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddStuff();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
//        ArrayList<MyLocation> items = getLocItem(locNo);
        getSupportActionBar().setTitle(locName);
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


    private void goToAddStuff() {
        ArrayList<MyLocation> arrLocNo = new ArrayList<>();
        MyLocation myLoc = new MyLocation();
        myLoc.setLoc_no(Integer.parseInt(locNo));
        myLoc.setLoc_name(locName);
        arrLocNo.add(myLoc);

        Intent intent = new Intent(DetailLocation.this, AddStuff.class);
        intent.putExtra(AddStuff.PUT_FROM_Detail_Loc, true);
        intent.putExtra(AddStuff.PUT_ARR_LOC_NO, arrLocNo);
        startActivity(intent);
    }

    private void updateData() {
        Intent intent = new Intent(this, AddLocation.class);
        intent.putExtra(PUT_LOC_NO, locNo);
        intent.putExtra(PUT_LOC_NAME, locName);
        intent.putExtra(PUT_LOC_COMMENT, locComment);
        intent.putExtra(PUT_LOC_IMGPATH, locImgpath);
        startActivity(intent);
    }

    private void deleteData() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("삭제하기");
        ab.setMessage("이 위치안의 물건들도 모두 삭제됩니다.\n삭제 하실래요?");
        ab.setIcon(getResources().getDrawable(R.drawable.ic_check_box_black_24dp));
        ab.setCancelable(false);
        ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                if (dbHelper == null) {
                    dbHelper = new DBHelper(getApplicationContext(), dbHelper.DB_NAME, null, 1);
                }
                dbHelper.deleteLoc(locNo);
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
    }

//    private ArrayList<MyLocation> getLocItem(String locNo) {
//        if (dbHelper == null) {
//            dbHelper = new DBHelper(this, dbHelper.DB_NAME, null, 1);
//        }
//        locItems = dbHelper.getLocItem(locNo);
//        return locItems;
//    }


    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private final ArrayList<Stuff> stuItems;

        public MyAdapter(ArrayList<Stuff> items) {
            this.stuItems = items;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_holder_detail_location, viewGroup, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder viewHolder, int position) {

            if (stuItems.get(position).getStu_imgpath() != null) {
                String imgPath = stuItems.get(position).getStu_imgpath();
                viewHolder.ivStuImg.setImageURI(Uri.fromFile(new File(imgPath)));
            }

            viewHolder.tvStuName.setText(stuItems.get(position).getStu_name());
            viewHolder.tvStuComment.setText(stuItems.get(position).getStu_comment());
            viewHolder.tvStuDate.setText(stuItems.get(position).getStu_date());
            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(getApplicationContext(), "carView " + viewHolder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DetailLocation.this, DetailStuff.class);
                    intent.putExtra(DetailStuff.PUT_STU_NO, stuItems.get(viewHolder.getAdapterPosition()).getStu_no()+"");
                    intent.putExtra(DetailStuff.PUT_STU_NAME, stuItems.get(viewHolder.getAdapterPosition()).getStu_name());
                    intent.putExtra(DetailStuff.PUT_STU_COMMENT, stuItems.get(viewHolder.getAdapterPosition()).getStu_comment());
                    intent.putExtra(DetailStuff.PUT_STU_DATE, stuItems.get(viewHolder.getAdapterPosition()).getStu_date());
                    intent.putExtra(DetailStuff.PUT_STU_IMGPATH, stuItems.get(viewHolder.getAdapterPosition()).getStu_imgpath());
                    intent.putExtra(PUT_LOC_NO, locNo);
                    startActivity(intent);
//                    overridePendingTransition(android.R.anim.slide_out_right, R.anim.hold);
                    overridePendingTransition(R.anim.slide_left, R.anim.hold);
                }
            });
        }

        @Override
        public int getItemCount() {
            return stuItems.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivStuImg;
        private TextView tvStuName;
        private TextView tvStuComment;
        private TextView tvStuDate;
        private CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivStuImg = (ImageView) itemView.findViewById(R.id.ivStuImg);
            tvStuName = (TextView) itemView.findViewById(R.id.tvStuName);
            tvStuComment = (TextView) itemView.findViewById(R.id.tvStuComment);
            tvStuDate = (TextView) itemView.findViewById(R.id.tvStuDate);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
        }
    }
}
