package com.tkkim.whereisit.list_location;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tkkim.whereisit.R;
import com.tkkim.whereisit.add_location.data.MyLocation;
import com.tkkim.whereisit.detail_location.DetailLocation;
import com.tkkim.whereisit.z_etc.DBHelper;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;

public class ListLocationFragment extends Fragment implements AbsListView.OnScrollListener {

    private DBHelper dbHelper;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ListLocAdapter mAdapter;
    private TextView tvNoData;
    private EditText etSearch;
    private Button btnSearch;

    private Boolean mLock;
    private ArrayList<MyLocation> items;

    public ListLocationFragment() {
    }

    public static ListLocationFragment newInstance() {
        ListLocationFragment fragment = new ListLocationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoc(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getContext(), "onResume", Toast.LENGTH_SHORT).show();
//        mAdapter.updateData(items);
//        getLoc();

//        mAdapter.delete();
//        getLoc();
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.detach(this).attach(this).commit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_location, container, false);

        mLock = true;


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
//        색상 변경
//        swipeRefreshLayout.setColorSchemeResources(
//                android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.scrollToPosition(0);
//        recyclerView.smoothScrollToPosition(items.size());

        mAdapter = new ListLocAdapter(items, getContext(),recyclerView);
        mAdapter.setOnLoadMoreListener(new ListLocAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

            }
        });

//        페이드 모션
//        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
//        alphaAdapter.setFirstOnly(false);
//        recyclerView.setAdapter(alphaAdapter);


//        페이드& 아래서 위로 & 속도 & 가속도
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
        alphaAdapter.setFirstOnly(false);
        alphaAdapter.setDuration(500);
        recyclerView.setAdapter(new SlideInBottomAnimationAdapter(alphaAdapter));

//        recyclerView.setAdapter(mAdapter);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvNoData = (TextView) view.findViewById(R.id.tvNoData);
//        etSearch = (EditText) view.findViewById(R.id.etSearch);
//        btnSearch = (Button) view.findViewById(R.id.btnSearch);

        if (items.isEmpty() || items == null) {
            tvNoData.setVisibility(View.VISIBLE);
        } else {
            tvNoData.setVisibility(View.GONE);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Toast.makeText(getContext(), "onAttach", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Toast.makeText(getContext(), "onDetach", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int first, int visible, int total) {
// 현재 가장 처음에 보이는 셀번호와 보여지는 셀번호를 더한값이
        // 전체의 숫자와 동일해지면 가장 아래로 스크롤 되었다고 가정합니다.
        int count = total - visible;

        if (first >= count && total != 0 && mLock == false) {
            Log.i("load", "Loading next items");
            getLoc(items.size());
        }
    }

    public void getLoc(final int start) {
        mLock=true;

        if (dbHelper == null) {
            dbHelper = new DBHelper(getContext(), dbHelper.DB_NAME, null, 1);
        }
        items = dbHelper.getLocList(start);

        mLock = false;

        Runnable run = new Runnable() {
            @Override
            public void run() {

            }
        };

        // 속도의 딜레이를 구현하기 위한 꼼수
        Handler handler = new Handler();
        handler.postDelayed(run, 5000);

    }

    public void getSearchList() {
        if (dbHelper == null) {
            dbHelper = new DBHelper(getContext(), dbHelper.DB_NAME, null, 1);
        }
        items = dbHelper.getSearch("이", 0);
    }

    public void goToDetailLoc(int position) {

        Intent intent = new Intent(getContext(), DetailLocation.class);
        intent.putExtra(DetailLocation.PUT_LOC_NO, items.get(position).getLoc_no() + "");
        intent.putExtra(DetailLocation.PUT_LOC_NAME, items.get(position).getLoc_name());
        intent.putExtra(DetailLocation.PUT_LOC_COMMENT, items.get(position).getLoc_comment());
        intent.putExtra(DetailLocation.PUT_LOC_IMGPATH, items.get(position).getLoc_imgpath());
        startActivityForResult(intent, 0);

//        Bundle b = new Bundle();
//        b.putString(DetailLocation.PUT_LOC_NO, items.get(position).getLoc_no()+"");
//        b.putString(DetailLocation.PUT_LOC_NAME, items.get(position).getLoc_name());

//        Fragment fragment = new DetailLocation();
//        fragment.setArguments(b);
//        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
//        fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
//        fragmentTransaction.commitAllowingStateLoss();
    }



//    private class ListLocAdapter extends RecyclerView.Adapter<ListLocationFragment.MyViewHolder> {
//
//        private Context context;
//        private final ArrayList<MyLocation> items;
//
//        private int visibleThreshold = 1;
//        private int lastVisibleItem, totalItemCount;
//
//        public ListLocAdapter(ArrayList<MyLocation> items, Context context, RecyclerView rv) {
//            this.items = items;
//            this.context = context;
//
//            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                    super.onScrolled(recyclerView, dx, dy);
//                    final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                    totalItemCount = linearLayoutManager.getItemCount();
//                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
//                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
//                        loading = true;
//                        // End has been reached
//                        // Do something
//                        Log.i("AdapterScrolled", "onScrolled: End reached");
//                        if (onLoadMoreListener != null) {
//                            onLoadMoreListener.onLoadMore();
//                        }
//
//                    }
//                }
//            });
//        }
//        public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
//            this.onLoadMoreListener = onLoadMoreListener;
//        }
//        public interface OnLoadMoreListener {
//            void onLoadMore();
//        }
//
//        public void add(MyLocation item) {
//            this.items.add(item);
//            notifyDataSetChanged();
//        }
//
//        public void reFresh() {
//            notifyDataSetChanged();
//        }
//
//        public void clear() {
//            items.clear();
//            notifyDataSetChanged();
//        }
//
//        public void delete(int position) {
//            items.remove(position);
//            notifyDataSetChanged();
//        }
//
//        public void updateData(ArrayList<MyLocation> items) {
//            items.clear();
//            items.addAll(items);
//            notifyDataSetChanged();
//        }
//
//        @Override
//        public ListLocationFragment.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_holder_list_location, viewGroup, false);
//            return new ListLocationFragment.MyViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(final ListLocationFragment.MyViewHolder viewHolder, final int position) {
//
//
//            String imgPath = items.get(position).getLoc_imgpath();
//            if (imgPath == null) {
//                viewHolder.ivLocImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_android_black_24dp));
//            } else {
//                viewHolder.ivLocImg.setImageURI(Uri.fromFile(new File(imgPath)));
//            }
////            if (imgPath!= null) {
////                viewHolder.ivLocImg.setImageURI(Uri.fromFile(new File(imgPath)));
////            }
//
//            viewHolder.tvLocName.setText(items.get(position).getLoc_name());
//            viewHolder.tvLocComment.setText(items.get(position).getLoc_comment());
//            viewHolder.tvLocDate.setText(items.get(position).getLoc_date());
//            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    goToDetailLoc(position);
//                }
//            });
//        }
//
//        @Override
//        public int getItemCount() {
//            return items.size();
//        }
//    }
//
//    private class MyViewHolder extends RecyclerView.ViewHolder {
//
//        private ImageView ivLocImg;
//        private TextView tvLocName;
//        private TextView tvLocComment;
//        private TextView tvLocDate;
//        private CardView cardView;
//
//        public MyViewHolder(View itemView) {
//            super(itemView);
//            ivLocImg = (ImageView) itemView.findViewById(R.id.ivLocImg);
//            tvLocName = (TextView) itemView.findViewById(R.id.tvLocName);
//            tvLocComment = (TextView) itemView.findViewById(R.id.tvLocComment);
//            tvLocDate = (TextView) itemView.findViewById(R.id.tvLocDate);
//            cardView = (CardView) itemView.findViewById(R.id.cardview);
//        }
//    }
}

