package com.demo.realm.realmdemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.realm.Realm
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun openCrudActivity(view: View) {
        startActivity(Intent(this, CrudActivity::class.java))
    }

    fun openLiveUpdateActivity(view: View) {
        startActivity(Intent(this, LiveUpdateActivity::class.java))
    }
}
