package com.oss.pages.bigdata.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConstantsDfe {

    private ConstantsDfe() {
        throw new IllegalStateException("Utility class");
    }

    public static String createName() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String date = simpleDateFormat.format(new Date());
        return "Selenium_" + date;
    }
}
