package com.bytebazar.toocold2.model.mapper;

import android.support.annotation.NonNull;

import com.bytebazar.toocold2.model.realm.DailyWeatherRealm;
import com.bytebazar.toocold2.model.realm.HourlyWeatherRealm;
import com.bytebazar.toocold2.model.realm.WeatherRealm;
import com.bytebazar.toocold2.model.response.Description;
import com.bytebazar.toocold2.model.response.HourlyWeather;
import com.bytebazar.toocold2.model.response.WeatherResponse;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import io.reactivex.functions.Function;
import io.realm.RealmList;

import static com.bytebazar.toocold2.Utils.getDayOfWeek;
import static com.bytebazar.toocold2.Utils.getIconFromDescription;

public final class RealmMapper implements Function<WeatherResponse, WeatherRealm> {
    @Override
    public WeatherRealm apply(final WeatherResponse weatherResponse) throws Exception {
        return convertResponseToRealmModel(weatherResponse);
    }

    @NonNull
    private HourlyWeatherRealm convertResponseToHourlyWeatherModel(HourlyWeather hourlyWeather, int offset) {

        final HourlyWeatherRealm hourlyWeatherRealm = new HourlyWeatherRealm();
        hourlyWeatherRealm.setTime(hourlyWeather.time * 1000);
        hourlyWeatherRealm.setTemp((int) Math.round(hourlyWeather.temp.temp));
        final Description description = hourlyWeather.weather.get(0);
        hourlyWeatherRealm.setId(description.id);
        hourlyWeatherRealm.setMain(description.main);
        hourlyWeatherRealm.setDescription(description.description);
        hourlyWeatherRealm.setIcon(description.icon);

        return hourlyWeatherRealm;
    }

    private WeatherRealm convertResponseToRealmModel(WeatherResponse weatherResponse) {

        final WeatherRealm weatherRealm = createWeatherRealmModel(weatherResponse);

        RealmList<DailyWeatherRealm> dailyWeatherRealms = new RealmList<>();
        RealmList<HourlyWeatherRealm> hourlyWeatherRealms = new RealmList<>();
        DailyWeatherRealm dailyWeatherRealm = new DailyWeatherRealm();

        long endDay = 0;

        final TimeZone timeZone = TimeZone.getDefault();
        final int offset = timeZone.getOffset(new Date().getTime());

        Calendar calendar = Calendar.getInstance(timeZone);

        if (weatherResponse.list.size() > 0) {
            endDay = setEndDay(calendar, weatherResponse.list.get(0).time * 1000, offset);
            dailyWeatherRealm.setHourlyWeather(hourlyWeatherRealms);
        }

        int averageTemp = 0;
        int counter = 0;

        for (HourlyWeather hourlyWeather : weatherResponse.list) {
            if (hourlyWeather.time * 1000 > endDay) {
                dailyWeatherRealm.setDayTempAverage(averageTemp / counter);
                averageTemp = 0;
                counter = 0;
                dailyWeatherRealms.add(dailyWeatherRealm);

                dailyWeatherRealm = new DailyWeatherRealm();
                hourlyWeatherRealms = new RealmList<>();
                HourlyWeatherRealm hourlyWeatherRealm
                        = convertResponseToHourlyWeatherModel(hourlyWeather, offset);
                averageTemp += hourlyWeatherRealm.getTemp();

                hourlyWeatherRealms.add(hourlyWeatherRealm);
                dailyWeatherRealm.setHourlyWeather(hourlyWeatherRealms);

                endDay = setEndDay(calendar, hourlyWeatherRealm.getTime(), offset);
            } else {
                final HourlyWeatherRealm hourlyWeatherRealm
                        = convertResponseToHourlyWeatherModel(hourlyWeather, offset);
                hourlyWeatherRealms.add(hourlyWeatherRealm);
                averageTemp += hourlyWeatherRealm.getTemp();
            }
            counter++;
        }
        dailyWeatherRealm.setDayTempAverage(averageTemp / counter);
        dailyWeatherRealms.add(dailyWeatherRealm);

        setAverageValues(dailyWeatherRealms);

        weatherRealm.setDailyWeatherRealm(dailyWeatherRealms);
        return weatherRealm;
    }

    @NonNull
    private WeatherRealm createWeatherRealmModel(WeatherResponse weatherResponse) {
        final WeatherRealm weatherRealm = new WeatherRealm();

        weatherRealm.setCreateTime(System.currentTimeMillis());
        weatherRealm.setCityName(weatherResponse.city.name);
        weatherRealm.setCityId(weatherResponse.city.id);
        weatherRealm.setLatitude(weatherResponse.city.coord.latitude);
        weatherRealm.setLongitude(weatherResponse.city.coord.longitude);
        return weatherRealm;
    }

    private long setEndDay(Calendar calendar, long time, int offset) {
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTimeInMillis();
    }

    private void setAverageValues(List<DailyWeatherRealm> list) {
        for (DailyWeatherRealm dailyWeather : list) {
            final HourlyWeatherRealm hourlyWeatherRealm = dailyWeather.getHourlyWeather().get(0);
            dailyWeather.setDay(getDayOfWeek(hourlyWeatherRealm.getTime()));
            dailyWeather.setIcon(getIconFromDescription(hourlyWeatherRealm.getId()));
        }
    }
}
