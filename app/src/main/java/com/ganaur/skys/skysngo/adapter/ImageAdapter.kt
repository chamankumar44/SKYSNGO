package ngo.ganaur.skys.skysngo.adapter

import android.content.Context
import android.content.Intent
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ngo.ganaur.skys.skysngo.view.ImageActivity
import ngo.ganaur.skys.skysngo.R
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_view.view.*

/**
 * Created by apple on 22/04/20.
 */


class ImageAdapter(val mContext: Context,private val myDataset: ArrayList<String>) :
        RecyclerView.Adapter<ImageAdapter.MyViewHolder>() {

    class MyViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ImageAdapter.MyViewHolder {

        val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_view, parent, false) as CardView


        return MyViewHolder(textView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val imageUrl = myDataset.get(position)

        holder.cardView.setOnClickListener {
            onlickImage(imageUrl)
        }

        Picasso.with(holder.cardView.card_image.context)
                .load(imageUrl)
                .fit()
                .placeholder(R.drawable.skys)
                .into(holder.cardView.card_image ,  object : Callback {
                    override fun onSuccess() {
                        holder.cardView. progress_image. visibility = View.GONE
                    }
                    override fun onError() {
                        holder.cardView. progress_image. visibility = View.VISIBLE
                    }

                })
    }

    override fun getItemCount() = myDataset.size


   private fun onlickImage(url: String){

       mContext.startActivity(Intent(mContext, ImageActivity::class.java)
               .putExtra("url",url))




   }
}