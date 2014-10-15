package com.utils;

import java.util.concurrent.TimeUnit;

/**
 * Created by Denys Kovalenko on 10/15/2014.
 */
public class Utils {

    public static void pause(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
        }
    }
}
