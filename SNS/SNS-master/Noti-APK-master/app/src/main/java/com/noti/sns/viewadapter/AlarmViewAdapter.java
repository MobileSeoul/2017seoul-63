package com.noti.sns.viewadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.noti.sns.fragment.LunchFragment;
import com.noti.sns.listitem.AlarmListItem;
import com.noti.sns.R;
import com.noti.sns.server.Connection;
import com.noti.sns.utility.Listsave;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.noti.sns.activity.MainActivity.pref;


public class AlarmViewAdapter extends RecyclerView.Adapter<AlarmViewAdapter.Holder> {

    private Context context;
    private List<AlarmListItem> list = new ArrayList<>();

    public AlarmViewAdapter(Context context, List<AlarmListItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        //여기에 세부설정

        holder.titleText_a.setText(list.get(position).wmeal);
        String min = list.get(position).minute < 10 ? "0" + list.get(position).minute : String.valueOf(list.get(position).minute);
        holder.timeText_a.setText(list.get(position).hour + ":" + min);

        holder.close_a.setOnClickListener(view -> {
            /*
                alarm_remove_failed:nonexistent //계정없음
				alarm_remove_failed:password
				alarm_remove_success
        	 */
            JSONObject jo = new JSONObject();
            try {
                jo.put("pw", pref.getString("save_pw", ""));
                jo.put("type", list.get(position).wmeal);
                jo.put("id", pref.getString("save_id", ""));
                String[] response = Connection.sendJSON("jaeheon.com/alarmr/", jo.toString());
                if (response[1].equals("alarm_remove_success")) {
                    // 이런식으로
                    list.remove(position);
                    Listsave.MealAlamList.put_Alam_List((ArrayList<AlarmListItem>) list);
                    LunchFragment.refresh();
                } else {
                    Toast.makeText(context, "일시적인 오류입니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(context, "인터넷 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
            }

        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public TextView titleText_a;
        public TextView timeText_a;
        public ImageView close_a;
        public RelativeLayout bac;


        public Holder(View view) {
            super(view);
            titleText_a = view.findViewById(R.id.title_meal);
            timeText_a = view.findViewById(R.id.time_meal);
            close_a = view.findViewById(R.id.close_al);
            bac = view.findViewById(R.id.cv_alam);
        }
    }
}
