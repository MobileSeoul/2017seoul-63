package com.noti.sns.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.noti.sns.R;
import com.noti.sns.activity.SettingActivity;
import com.noti.sns.listitem.HomeCardListItem;
import com.noti.sns.utility.Listsave;
import com.noti.sns.viewadapter.HomeItemTouchHelperView;
import com.noti.sns.viewadapter.HomeViewAdapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements HomeViewAdapter.OnStartDragListener {

    public static ItemTouchHelper mItemTouchHelper;
    static RecyclerView recyclerView;
    static HomeViewAdapter adapter;
    static ArrayList<HomeCardListItem> contacts;
    static Context homeFragment;
    static HomeFragment this_home;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.frg_nemu_home, container, false);
        Intent intent_settitng = new Intent(getActivity(), SettingActivity.class);
        homeFragment = getActivity();
        this_home = this;
        recyclerView = rootView.findViewById(R.id.recyclerView);
        contacts = Listsave.HomeCardList.get_Home_List();
        TextView non = rootView.findViewById(R.id.non_home);
        if (contacts.size() == 0)
            non.setVisibility(View.VISIBLE);
        else
            non.setVisibility(View.GONE);
        adapter = new HomeViewAdapter(getActivity(), contacts, this);
        HomeItemTouchHelperView mCallback = new HomeItemTouchHelperView(adapter);
        mItemTouchHelper = new ItemTouchHelper(mCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        ImageView goToSetting_Home = rootView.findViewById(R.id.goToSetting_Home);
        goToSetting_Home.setOnClickListener(view -> startActivity(intent_settitng));

        return rootView;
    }

    public static void refresh() {
        contacts = Listsave.HomeCardList.get_Home_List();
        adapter = new HomeViewAdapter(homeFragment, contacts, this_home);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStartDrag(HomeViewAdapter.Holder holder) {
        mItemTouchHelper.startDrag(holder);
    }
}
