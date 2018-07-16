package com.demo.realm.realmdemo

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import com.bumptech.glide.Glide
import com.demo.realm.realmdemo.model.Result
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults
import kotlinx.android.synthetic.main.people_item.view.*

/**
 * Created by muthuraj on 11/10/17.
 */
class PeopleAdapter(context: Context,
                    private val results: RealmResults<Result>)
    : RealmRecyclerViewAdapter<Result, PeopleViewHolder>(results, true) {

    private val inflater = LayoutInflater.from(context)

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        holder.bind(results[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PeopleViewHolder {
        val itemView = inflater.inflate(R.layout.people_item, parent, false)
        return PeopleViewHolder(itemView)
    }

}


class PeopleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(result: Result) {
        result.picture
                ?.large
                ?.takeIf { URLUtil.isValidUrl(it) }
                ?.let {
                    Glide.with(itemView.context)
                            .load(it)
                            .into(itemView.user_image)
                }
        itemView.user_name.text = result.name.toString()
        itemView.user_email.text = result.email
    }
}