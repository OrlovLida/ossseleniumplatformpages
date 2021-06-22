package com.oss.pages.bigdata.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConstantsDfe {

    private static String seleniumName;

    public ConstantsDfe() {
    }

    public static String createName() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String date = simpleDateFormat.format(new Date());
        seleniumName = "Selenium_" + date;
        return seleniumName;
    }
}
