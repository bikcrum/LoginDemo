package com.bikcrum.logindemo.utility;

/**
 * Created by Rakesh Pandit on 11/27/2017.
 */

public class Util {


    public static boolean isValidEmail(String target) {
        if (target == null) return false;
        target = target.trim();
        return !target.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
