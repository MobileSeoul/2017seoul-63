package com.noti.sns.viewadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.noti.sns.listitem.TimeLineListItem;
import com.noti.sns.R;

import java.util.ArrayList;
import java.util.List;


public class TimeLineViewAdapter extends RecyclerView.Adapter<TimeLineViewAdapter.Holder> {

    private Context context;
    private List<TimeLineListItem> list = new ArrayList<>();

    public TimeLineViewAdapter(Context context, List<TimeLineListItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        //여기에 세부설정


        holder.titleText_t.setText(list.get(position).title);
        holder.subTitleText_t.setText(list.get(position).subtitle);
        if (position == 0) {

            holder.up_Line.setVisibility(View.INVISIBLE);
            holder.down_Line.setVisibility(View.VISIBLE);
            holder.circle_t.setBackgroundResource(R.drawable.ic_panorama_fish_eye_black_24dp);
        } else if (position == 1) {
            holder.up_Line.setVisibility(View.VISIBLE);
            holder.down_Line.setVisibility(View.VISIBLE);
            holder.circle_t.setBackgroundResource(R.drawable.ic_adjust_black_24dp);
        } else {
            holder.up_Line.setVisibility(View.VISIBLE);
            holder.down_Line.setVisibility(View.VISIBLE);
            holder.circle_t.setBackgroundResource(R.drawable.ic_brightness_30dp);
        }
        if (position == list.size() - 1) {
            holder.up_Line.setVisibility(View.VISIBLE);
            if (position == 0) {
                holder.up_Line.setVisibility(View.INVISIBLE);
            }
            holder.down_Line.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public TextView titleText_t;
        public TextView subTitleText_t;
        public ImageView circle_t;
        public View up_Line;
        public View down_Line;
        public RelativeLayout view_t;


        public Holder(View view) {
            super(view);
            titleText_t = view.findViewById(R.id.td_title);
            subTitleText_t = view.findViewById(R.id.td_subtitle);
            circle_t = view.findViewById(R.id.td_circle);
            up_Line = view.findViewById(R.id.td_upline);
            down_Line = view.findViewById(R.id.td_downline);
            view_t = view.findViewById(R.id.td_view);
        }
    }
}
