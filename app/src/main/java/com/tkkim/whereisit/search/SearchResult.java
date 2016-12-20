package com.tkkim.whereisit.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkkim.whereisit.R;
import com.tkkim.whereisit.add_location.data.MyLocation;

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
        public void onBindViewHolder(MyViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }


    private class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
