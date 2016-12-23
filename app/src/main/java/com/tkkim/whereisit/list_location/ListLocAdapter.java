package com.tkkim.whereisit.list_location;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkkim.whereisit.R;
import com.tkkim.whereisit.add_location.data.MyLocation;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by conscious on 2016-12-23.
 */

class ListLocAdapter extends RecyclerView.Adapter<ListLocAdapter.MyViewHolder> {

    private Context context;
    private final ArrayList<MyLocation> items;

    private OnLoadMoreListener onLoadMoreListener;
    private int visibleThreshold = 1;
    private int last, total;
    private Boolean mLoad;

    public ListLocAdapter(ArrayList<MyLocation> items, Context context, RecyclerView recyclerView) {
        this.items = items;
        this.context = context;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                total = linearLayoutManager.getItemCount();
                last = linearLayoutManager.findLastVisibleItemPosition();
                if (!mLoad && total <= (last + visibleThreshold)) {
                    mLoad = true;
                    // End has been reached
                    // Do something
                    Log.i("AdapterScrolled", "onScrolled: End reached");
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }

                }
            }
        });
    }

    public void setOnLoadMoreListener(ListLocAdapter.OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void add(MyLocation item) {
        this.items.add(item);
        notifyDataSetChanged();
    }

    public void reFresh() {
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void delete(int position) {
        items.remove(position);
        notifyDataSetChanged();
    }

    public void updateData(ArrayList<MyLocation> items) {
        items.clear();
        items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_holder_list_location, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {


        String imgPath = items.get(position).getLoc_imgpath();
        if (imgPath == null) {
//            viewHolder.ivLocImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_android_black_24dp));
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
                ListLocationFragment.newInstance().goToDetailLoc(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

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

