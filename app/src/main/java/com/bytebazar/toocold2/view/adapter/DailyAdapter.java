package com.bytebazar.toocold2.view.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bytebazar.toocold2.R;
import com.bytebazar.toocold2.model.CurrentValues;
import com.bytebazar.toocold2.model.realm.DailyWeatherRealm;
import com.bytebazar.toocold2.model.realm.HourlyWeatherRealm;
import com.bytebazar.toocold2.model.realm.WeatherRealm;

public final class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.BaseHolder> {

    private WeatherRealm weatherRealm;
    final private TodayHourlyAdapter todayHourlyAdapter;

    private static final int VIEW_TYPE_DAILY = 0;
    private static final int VIEW_TYPE_TODAY = 1;

    public DailyAdapter(WeatherRealm weatherRealm) {
        this.weatherRealm = weatherRealm;
        todayHourlyAdapter = new TodayHourlyAdapter();
    }

    abstract class BaseHolder extends RecyclerView.ViewHolder {

        public BaseHolder(View itemView) {
            super(itemView);
        }

        public abstract void bind(DailyWeatherRealm weatherRealm);

    }

    class DailyHolder extends BaseHolder {

        final private TextView dayOfWeek;
        final private TextView temp;
        final private ImageView icon;

        public DailyHolder(View itemView) {
            super(itemView);
            dayOfWeek = itemView.findViewById(R.id.dayOfWeek);
            temp = (TextView) itemView.findViewById(R.id.temp);
            icon = (ImageView) itemView.findViewById(R.id.icon);
        }

        @Override
        public void bind(DailyWeatherRealm weather) {
            dayOfWeek.setText(weather.getDay());
            temp.setText(weather.getDayTempAverage());
            icon.setImageResource(weather.getIcon());
        }
    }

    class TodayHolder extends BaseHolder {

        final private RecyclerView recyclerView;
        final private TextView city;
        final private TextView description;
        final private TextView temp;

        public TodayHolder(View itemView) {
            super(itemView);
            city = (TextView) itemView.findViewById(R.id.city);
            description = (TextView) itemView.findViewById(R.id.description);
            temp = (TextView) itemView.findViewById(R.id.temp);

            recyclerView = (RecyclerView) itemView.findViewById(R.id.todayRecyclerView);
            recyclerView.setHasFixedSize(true);

            final LinearLayoutManager layoutManager =
                    new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false) {
                        @Override
                        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                            super.onLayoutChildren(recycler, state);

//                    final int first = findFirstVisibleItemPosition();
//                    final int last = findLastVisibleItemPosition();

//                    if (first == 0 && last == dailyWeatherRealm.getHourlyWeather().size() - 1) {
//
//                    })
                        }
                    };

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(todayHourlyAdapter);
        }

        @Override
        public void bind(DailyWeatherRealm d) {
            city.setText(weatherRealm.getCityName());
            CurrentValues<String, String> currentValues = d.getCurrentValues();
            description.setText(currentValues.currentHourlyDescription);
            temp.setText(currentValues.currentHourlyTemp);
            todayHourlyAdapter.setData(d.getHourlyWeather());
        }
    }


    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_DAILY) {
            return new DailyHolder(inflater.inflate(R.layout.item_daily, parent, false));
        } else {
            return new TodayHolder(inflater.inflate(R.layout.item_today, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        holder.bind(weatherRealm.getDailyWeatherRealm().get(position));
    }

    @Override
    public int getItemViewType(int position) {
        final DailyWeatherRealm dailyWeatherRealm = weatherRealm.getDailyWeatherRealm().get(position);
        final HourlyWeatherRealm hourlyWeatherRealm = dailyWeatherRealm.getHourlyWeather().get(0);
        final long time = hourlyWeatherRealm.getTime();
        return DateUtils.isToday(time) ? VIEW_TYPE_TODAY : VIEW_TYPE_DAILY;
    }

    @Override
    public int getItemCount() {
        return weatherRealm.getDailyWeatherRealm().size();
    }

    public void setData(WeatherRealm weatherRealm) {
        this.weatherRealm = weatherRealm;
    }
}
