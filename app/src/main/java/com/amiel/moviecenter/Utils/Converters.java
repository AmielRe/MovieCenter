package com.amiel.moviecenter.Utils;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        return value == null ? null : df.parse(df.format(new Date(value)));
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
