package com.kojikoji.gpt.data.types.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName DateUtils
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/12/28 20:56
 * @Version
 */

public class DateUtils {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Date parseDate(String date) throws ParseException {
        return formatter.parse(date);
    }
}
