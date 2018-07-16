package com.demo.realm.realmdemo.model

import io.realm.RealmObject
import java.util.*

/**
 * Created by muthuraj on 11/10/17.
 */

open class Result : RealmObject() {
    var gender: String? = null
    var email: String? = null
    var phone: String? = null
    var nat: String? = null
    var name: Name? = null
    var picture: Picture? = null
//    var age: Long = 0

    override fun toString(): String {
        return "Result(gender=$gender, email=$email, phone=$phone, nat=$nat, name=$name, picture=$picture)"
    }

    companion object {
        fun generateDummyResult() =
                Result().apply {
                    name = Name.generateName()
                    picture = Picture.generateDummyPicture()
                }
    }
}

open class Name : RealmObject() {
    var title: String? = null
    var first: String? = null
    var last: String? = null

    override fun toString() = "$title. $first $last"

    companion object {
        private const val lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890"

        private val rand = java.util.Random()

        // consider using a Map<String,Boolean> to say whether the identifier is being used or not
        private val identifiers: Set<String> = HashSet()

        private fun randomString(): String {
            var builder = StringBuilder()
            while (builder.toString().isEmpty()) {
                val length = rand.nextInt(5) + 5
                for (i in 0 until length) {
                    builder.append(lexicon[rand.nextInt(lexicon.length)])
                }
                if (identifiers.contains(builder.toString())) {
                    builder = StringBuilder()
                }
            }
            return builder.toString()
        }

        fun generateName() =
                Name().apply {
                    title = "Mr"
                    first = randomString()
                    last = randomString()
                }
    }
}

open class Picture : RealmObject() {
    var large: String? = null

    override fun toString() = "Picture(large=$large)"

    companion object {
        fun generateDummyPicture(): Picture {
            val picNum = Random().nextInt(100)
            return Picture().apply {
                large = "https://randomuser.me/api/portraits/med/men/$picNum.jpg"
            }
        }
    }
}
