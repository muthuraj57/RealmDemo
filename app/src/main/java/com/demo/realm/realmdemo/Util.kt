package com.demo.realm.realmdemo

import android.util.Log

/**
 * Created by muthuraj on 11/10/17.
 */

fun Any.log(message: String) {
    if (BuildConfig.DEBUG) {
        Log.d(this::class.java.simpleName, message)
    }
}