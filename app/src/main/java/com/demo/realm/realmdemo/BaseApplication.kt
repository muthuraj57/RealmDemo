package com.demo.realm.realmdemo

import android.app.Application
import android.content.Context
import android.util.Base64
import io.realm.Realm
import io.realm.RealmConfiguration
import java.security.SecureRandom

/**
 * Created by muthuraj on 11/10/17.
 */

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initRealm()
    }

    private fun initRealm() {
        Realm.init(this)

        val path = getExternalFilesDir(null)
        val config = RealmConfiguration.Builder()
//                .deleteRealmIfMigrationNeeded()
                .directory(path!!)
                .name("RealmDemo.realm")
//                .encryptionKey(getRealmKey())
//                .schemaVersion(1L)
//                .migration(RealmMigrations())
                .build()
        Realm.setDefaultConfiguration(config)
    }


//             <========================== Helper Methods =========================>

    private fun getRealmKey(): ByteArray {

        val storedKey: String? = getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)
                .getString(REALM_KEY, null)

        if (storedKey == null) {
            val byteArray = ByteArray(64)
            SecureRandom().nextBytes(byteArray)
            storeRealmKey(byteArray)
            log("realmKey: ${byteArray.toHexString()}")
            return byteArray
        }
        val byteArray = Base64.decode(storedKey, Base64.NO_WRAP)
        log("realmKey: ${byteArray.toHexString()}")

        return byteArray
    }

    private fun storeRealmKey(key: ByteArray) {
        val keyString = Base64.encodeToString(key, Base64.NO_WRAP)
        log("storeRealmKey byte[]: $key, string: $keyString")
        getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)
                .edit()
                .putString(REALM_KEY, Base64.encodeToString(key, Base64.NO_WRAP))
                .apply()
    }

    private fun ByteArray.toHexString(): String {
        val builder = StringBuilder()
        for (byte in this) {
            builder.append(String.format("%02x", byte))
        }
        return builder.toString()
    }

    companion object {
        const val PREF_FILE = "pref_file"
        const val REALM_KEY = "realm_key"
    }
}
