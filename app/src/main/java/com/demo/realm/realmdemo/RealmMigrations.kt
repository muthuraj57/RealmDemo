package com.demo.realm.realmdemo

import io.realm.DynamicRealm
import io.realm.RealmMigration

/**
 * Created by muthuraj on 16/10/17.
 */
class RealmMigrations : RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVers: Long, newVersion: Long) {
        val schema = realm.schema
        var oldVersion = oldVers

        if (oldVersion == 0L) {
            val userSchema = schema.get("Result")
            userSchema?.addField("age", Int::class.java)
            oldVersion++
        }

        if (oldVersion == 1L) {
            //migration code
        }
    }
}