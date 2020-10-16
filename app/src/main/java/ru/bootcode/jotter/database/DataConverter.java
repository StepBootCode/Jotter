/*
 *
 *  Created by Sergey Stepchenkov on 02.10.20 17:10
 *  Copyright (c) 2020. All rights reserved.
 *  More info on www.bootcode.ru
 *  Last modified 02.10.20 17:10
 *
 */

package ru.bootcode.jotter.database;

import androidx.room.TypeConverter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataConverter {
    // Вспомогательный класс, который используется в классах @Entity Room
    // нужен для преобразования типов JAVA в типы хранимые в БД

    // Область конвертировани даты и времени-------------------------------------------------
    static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
    //---------------------------------------------------------------------------------------

}
