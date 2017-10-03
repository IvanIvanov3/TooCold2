package com.bytebazar.toocold2.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bytebazar.toocold2.R;
import com.bytebazar.toocold2.Utils;
import com.bytebazar.toocold2.model.realm.HourlyWeatherRealm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

final class TodayHourlyAdapter extends RecyclerView.Adapter<TodayHourlyAdapter.Holder> {

    private List<HourlyWeatherRealm> weatherList = new ArrayList<>();

    public class Holder extends RecyclerView.ViewHolder {

        final private ImageView icon;
        final private TextView temp;
        final private TextView time;

        public Holder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            temp = (TextView) itemView.findViewById(R.id.temp);
            time = (TextView) itemView.findViewById(R.id.time);
        }

        void bind(HourlyWeatherRealm weather) {
            icon.setImageResource(Utils.getIconFromDescription(weather.getId()));
            temp.setText(weather.getFormattedTemp());
            time.setText(formatTime(weather.getTime()));
        }
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hourly, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.bind(weatherList.get(position));
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public void setData(List<HourlyWeatherRealm> list) {
        weatherList = list;
    }


    private String formatTime(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return dateFormat.format(time);
    }
}
