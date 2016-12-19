package com.tkkim.whereisit.add_stuff;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkkim.whereisit.R;
import com.tkkim.whereisit.add_location.data.MyLocation;
import com.tkkim.whereisit.z_etc.DBHelper;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by conscious on 2016-12-06.
 */

public class ChoiceLocation extends AppCompatActivity {

    private DBHelper dbHelper;

    private TextView tvNoData;
    private RecyclerView recyclerView;
    private ArrayList<MyLocation> items;

    public static final String EXTRA_LOC_NO = "locNo";
    private String LocNo;
    private String LocName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_location);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.nav_choice_loc);

        tvNoData = (TextView)findViewById(R.id.tvNoData);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        getLoc();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // finish the activity
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getLoc() {
        if (dbHelper == null) {
            dbHelper = new DBHelper(this, "WhereIsIt", null, 1);
        }
        items = dbHelper.getLocList(0);
        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        recyclerView.setAdapter(new MyAdapter(items));

        if (items.isEmpty()||items==null) {
            tvNoData.setVisibility(View.VISIBLE);
        } else {
            tvNoData.setVisibility(View.GONE);
        }
    }

    private void goBack(String locNo, String locName) {
        Intent intent = new Intent();
        intent.putExtra(AddStuff.PUT_LOC_NO, locNo);
        intent.putExtra(AddStuff.PUT_LOC_NAME, locName);
        setResult(AddStuff.RESULT_OK, intent);
        finish();
    }

    private class MyAdapter extends RecyclerView.Adapter<ChoiceLocation.MyViewHolder> {

        private final ArrayList<MyLocation> items;

        public MyAdapter(ArrayList<MyLocation> items) {
            this.items = items;
        }

        @Override
        public ChoiceLocation.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_holder_choice_location, viewGroup, false);
            return new ChoiceLocation.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ChoiceLocation.MyViewHolder viewHolder, int position) {

            if (items.get(position).getLoc_imgpath() != null) {
                String imgPath = items.get(position).getLoc_imgpath();
                viewHolder.ivLocImg.setImageURI(Uri.fromFile(new File(imgPath)));
            }

            viewHolder.tvLocName.setText(items.get(position).getLoc_name());
            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LocNo = items.get(viewHolder.getAdapterPosition()).getLoc_no() + "";
                    LocName = items.get(viewHolder.getAdapterPosition()).getLoc_name();
                    goBack(LocNo, LocName);
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivLocImg;
        private TextView tvLocName;
        private CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivLocImg = (ImageView) itemView.findViewById(R.id.ivLocImg);
            tvLocName = (TextView) itemView.findViewById(R.id.tvLocName);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
        }
    }
}
