package com.demo.realm.realmdemo

import com.demo.realm.realmdemo.model.Result

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by muthuraj on 11/10/17.
 */

interface NetworkService {

    @GET("api")
    fun getPeopleResults(@Query("results") results: Int): Single<Array<Result>>
}
