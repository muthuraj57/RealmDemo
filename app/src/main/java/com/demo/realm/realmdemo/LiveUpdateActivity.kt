package com.demo.realm.realmdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.demo.realm.realmdemo.model.Result
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_live_update.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by muthuraj on 11/10/17.
 */
class LiveUpdateActivity : AppCompatActivity() {

    private val compositeDisposable by lazy { CompositeDisposable() }
    private val realm by lazy { Realm.getDefaultInstance() }
    private val realmResults by lazy {
        realm.where(Result::class.java)
                .findAllSorted("name.first")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_live_update)

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = PeopleAdapter(this, realmResults)

        val rand = Random()
        val disposable = Observable.interval(1, TimeUnit.SECONDS)
                .map { rand.nextInt(3) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    when (it) {
                        1 -> addData()
                        2 -> addData(true)
                        3 -> deleteData()
                    }
                }
        compositeDisposable.add(disposable)
    }

    private fun addData(many: Boolean = false) {
        realm.executeTransaction {
            if (many) {
                val data = (0 until 3).map { Result.generateDummyResult() }
                it.insertOrUpdate(data)
            } else {
                it.insertOrUpdate(Result.generateDummyResult())
            }
        }
    }

    private fun deleteData() {
        realm.executeTransaction {
            it.where(Result::class.java)
                    .findFirst()
                    ?.deleteFromRealm()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
        realm.close()
    }
}