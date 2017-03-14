package com.tkkim.whereisit.add_stuff;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tkkim.whereisit.R;
import com.tkkim.whereisit.add_location.data.MyLocation;
import com.tkkim.whereisit.z_etc.DBHelper;

import java.io.File;
import java.util.ArrayList;


public class ChoiceLocation extends AppCompatActivity {

    private DBHelper dbHelper;

    private TextView tvNoData;
    private RecyclerView recyclerView;
    private Button btn;
    private ArrayList<MyLocation> items;
    private ArrayList<MyLocation> arrLocNo;
    private SparseBooleanArray boolLocNo;
    private Boolean boolReEnter = false;

    public static final String EXTRA_LOC_NO = "locNo";
    private String LocNo;
    private String LocName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_location);


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.nav_choice_loc);


        tvNoData = (TextView) findViewById(R.id.tvNoData);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        items = (ArrayList<MyLocation>)getIntent().getSerializableExtra(AddStuff.PUT_ITEM);
        boolLocNo = new SparseBooleanArray();


        if (getIntent().getBooleanExtra(AddStuff.PUT_CHOICED, false)) {
            boolReEnter = true;
            arrLocNo = (ArrayList<MyLocation>) getIntent().getSerializableExtra(AddStuff.PUT_ARR_LOC_NO);
        }


        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (boolLocNo != null) {
                    arrLocNo = new ArrayList<MyLocation>();
                    int i;
                    for (i = 0; i < boolLocNo.size(); i++) {

                        if (boolLocNo.get(i)) {
                            MyLocation myLoc = new MyLocation();
                            myLoc.setLoc_no(items.get(i).getLoc_no());
                            myLoc.setLoc_name(items.get(i).getLoc_name());
                            arrLocNo.add(myLoc);
                        }


                        Log.d("mybool", "keyAt : " + boolLocNo.keyAt(i));
                        Log.d("mybool", String.valueOf(boolLocNo.get(i)));
//                        Log.d("mybool", items.get(boolLocNo.keyAt(i)).getLoc_no() + " : getlocNo");
                    }
                    goBack(arrLocNo);
                } else {
                    Toast.makeText(getApplicationContext(), "위치가 선택되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(new MyAdapter(items));

        if (items.isEmpty() || items == null) {
            tvNoData.setVisibility(View.VISIBLE);
        } else {
            tvNoData.setVisibility(View.GONE);
        }


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
        items = dbHelper.getLocAll();

    }

    private void goBack(ArrayList<MyLocation> arrLocNo) {
        Intent intent = new Intent();
        intent.putExtra(AddStuff.PUT_ARR_LOC_NO, arrLocNo);
        setResult(AddStuff.RESULT_OK, intent);
        finish();
    }

    private class MyAdapter extends RecyclerView.Adapter<ChoiceLocation.MyViewHolder> {

        private final ArrayList<MyLocation> items;

        MyAdapter(ArrayList<MyLocation> items) {
            this.items = items;
        }

        public void setCheck(int position) {

        }

        @Override
        public ChoiceLocation.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_holder_choice_location, viewGroup, false);
            return new ChoiceLocation.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ChoiceLocation.MyViewHolder viewHolder, final int position) {

            if (items.get(position).getLoc_imgpath() != null) {
                String imgPath = items.get(position).getLoc_imgpath();
                viewHolder.ivLocImg.setImageURI(Uri.fromFile(new File(imgPath)));
            }

            viewHolder.tvLocName.setText(items.get(position).getLoc_name());


            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(),"list "+position,Toast.LENGTH_SHORT).show();
                    if(viewHolder.checkBox.isChecked()){
                        viewHolder.checkBox.setChecked(false);
                    }else{
                        viewHolder.checkBox.setChecked(true);
                    }
                }
            });


            boolLocNo.append(position, false);

            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Toast.makeText(getApplicationContext(), "checked " + items.get(position).getLoc_no() + "-" + position, Toast.LENGTH_SHORT).show();
//                    arrLocNo.add(items.get(position).getLoc_no());
//                        sparseLocNo.append(position,items.get(position).getLoc_no());
                        boolLocNo.put(position, true);


                    } else {
//                        arrLocNo.remove(items.get(position).getLoc_no());
//                        sparseLocNo.removeAt(position);
                        boolLocNo.put(position, false);
                        Toast.makeText(getApplicationContext(), "Unchecked " + items.get(position).getLoc_no() + "-" + position, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            if (boolReEnter) {
                for (MyLocation data : arrLocNo) {
                    if (data.getLoc_no() == items.get(position).getLoc_no()) {
                        viewHolder.checkBox.setChecked(true);
                    }
                }
            }
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
        private CheckBox checkBox;

        MyViewHolder(View itemView) {
            super(itemView);
            ivLocImg = (ImageView) itemView.findViewById(R.id.ivLocImg);
            tvLocName = (TextView) itemView.findViewById(R.id.tvLocName);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        }
    }
}
