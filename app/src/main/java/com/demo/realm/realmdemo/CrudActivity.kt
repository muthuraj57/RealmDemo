package com.demo.realm.realmdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.demo.realm.realmdemo.model.Result
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import java.util.*

/**
 * Created by muthuraj on 11/10/17.
 */
class CrudActivity : AppCompatActivity() {

    private val compositeDisposable by lazy { CompositeDisposable() }
    private val realm by lazy { Realm.getDefaultInstance() }

    private val realmResults by lazy {
        realm.where(Result::class.java)
            .findAllAsync()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud)

        realmResults.addChangeListener { updatedResults ->
            log("changeListener realmResults size: ${realmResults.size}")
        }
    }


    /*
    * Fetch data from service and add it to realm
    * */
    fun addData(view: View) {
        val disposable = RetrofitUtil.retrofit
            .create(NetworkService::class.java)
            .getPeopleResults(10)
            .doAfterSuccess { results ->
                Realm.getDefaultInstance()
                    .use {
                        it.executeTransaction { realm ->
                            realm.insertOrUpdate(results.toList())
                        }
                    }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { results ->
                    log("getPeopleResults: " + Arrays.toString(results))
                    Toast.makeText(this, "Added ${results.size} results", Toast.LENGTH_SHORT).show()
                },
                { it.printStackTrace() })
        compositeDisposable.add(disposable)
    }

    /*
    * Delete first queried data
    * */
    fun deleteData(view: View) {
        realm.executeTransaction {
            it.where(Result::class.java)
                .findFirst()
                ?.deleteFromRealm()
        }
    }

    /*
    * Update first queried data with nat as "IN"
    * */
    fun updateData(view: View) {
        realm.executeTransaction {
            val firstData = it.where(Result::class.java)
                .findFirst() ?: return@executeTransaction
            firstData.nat = "IN"
        }
    }

    /*
    * Read all the results and toast the size of the results
    * */
    fun readData(view: View) {
        Toast.makeText(this, "Result size ${realmResults.size}", Toast.LENGTH_SHORT).show()

//        val results = realm.where(Result::class.java).findAll()
//        log("readData: $results")
//        Toast.makeText(this, "Result size ${results.size}", Toast.LENGTH_SHORT).show()
    }


    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
        realmResults.removeAllChangeListeners()
        realm.close()
    }
}