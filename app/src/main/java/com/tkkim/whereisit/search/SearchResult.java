package com.tkkim.whereisit.search;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkkim.whereisit.R;
import com.tkkim.whereisit.add_location.data.MyLocation;

import java.io.File;
import java.util.ArrayList;


public class SearchResult extends Fragment {

    public SearchResult() {
    }

    public static SearchResult newInstance(String param1, String param2) {
        SearchResult fragment = new SearchResult();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private class MyAdapter extends RecyclerView.Adapter<SearchResult.MyViewHolder>{

        private final ArrayList<MyLocation> items;

        public MyAdapter(ArrayList<MyLocation> items) {
            this.items = items;
        }

        @Override
        public SearchResult.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_search, parent, false);
            return new SearchResult.MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(final SearchResult.MyViewHolder viewHolder, final int position) {


            String imgPath = items.get(position).getLoc_imgpath();
            if (imgPath == null) {
                viewHolder.ivLocImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_android_black_24dp));
            } else {
                viewHolder.ivLocImg.setImageURI(Uri.fromFile(new File(imgPath)));
            }
//            if (imgPath!= null) {
//                viewHolder.ivLocImg.setImageURI(Uri.fromFile(new File(imgPath)));
//            }

            viewHolder.tvLocName.setText(items.get(position).getLoc_name());
            viewHolder.tvLocComment.setText(items.get(position).getLoc_comment());
            viewHolder.tvLocDate.setText(items.get(position).getLoc_date());
            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    goToDetailLoc(position);
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
        private TextView tvLocComment;
        private TextView tvLocDate;
        private CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivLocImg = (ImageView) itemView.findViewById(R.id.ivLocImg);
            tvLocName = (TextView) itemView.findViewById(R.id.tvLocName);
            tvLocComment = (TextView) itemView.findViewById(R.id.tvLocComment);
            tvLocDate = (TextView) itemView.findViewById(R.id.tvLocDate);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
        }
    }
}
