package com.practicum.randommovierating


import android.text.Layout
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val title: TextView = itemView.findViewById(R.id.title)
    private val image: ImageView = itemView.findViewById(R.id.poster)

    fun bind(movie: MovieResponse.Movie) {
        title.text = movie.title
        Glide.with(itemView)
            .load(movie.image)
            .transform(RoundedCorners(convertDpToPx(6)))
            .into(image)
    }


    private fun convertDpToPx(dp: Int): Int {
        val displayMetrics = itemView.resources.displayMetrics;
        return ((dp * displayMetrics.density) + 0.5).toInt()
    }
}