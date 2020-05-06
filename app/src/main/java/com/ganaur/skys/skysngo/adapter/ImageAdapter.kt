package com.ganaur.skys.skysngo.adapter

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ganaur.skys.skysngo.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_view.view.*

/**
 * Created by apple on 22/04/20.
 */


class ImageAdapter(private val myDataset: ArrayList<String>) :
        RecyclerView.Adapter<ImageAdapter.MyViewHolder>() {

    class MyViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ImageAdapter.MyViewHolder {

        val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_view, parent, false) as CardView


        return MyViewHolder(textView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Picasso.with(holder.cardView.card_image.context).load(myDataset
                .get(position)).into(holder.cardView.card_image );

    }

    override fun getItemCount() = myDataset.size
}