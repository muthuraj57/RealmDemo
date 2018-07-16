package com.demo.realm.realmdemo

import com.demo.realm.realmdemo.model.Result
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken

import java.lang.reflect.Type

import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by muthuraj on 11/10/17.
 */

object RetrofitUtil {

    val retrofit: Retrofit by lazy {

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

        Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://randomuser.me/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(gsonConverterFactory)
                .build()
    }


    private val gson by lazy { Gson() }

    private val gsonConverterFactory by lazy {
        val gson = GsonBuilder()
                .registerTypeAdapter(object : TypeToken<Array<Result>>() {

                }.type, ResultDeserializer())
                .create()
        GsonConverterFactory.create(gson)
    }

    private class ResultDeserializer : JsonDeserializer<Array<Result>> {

        @Throws(JsonParseException::class)
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Array<Result> {
            val results = json.asJsonObject.get("results").asJsonArray
            return gson.fromJson(results, Array<Result>::class.java)
        }
    }
}
