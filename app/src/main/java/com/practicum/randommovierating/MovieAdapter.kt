package com.practicum.randommovierating

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class MovieAdapter(private val movieList: ArrayList<MovieResponse.Movie>) :
    RecyclerView.Adapter<MessageViewHolder>() {

    private var typeCurrentViewHolder: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val holderTypeId =
            if (typeCurrentViewHolder == 0) R.layout.movie_preview
            else R.layout.movie_view
        val view =
            LayoutInflater.from(parent.context)
                .inflate(holderTypeId, parent, false)
        return MessageViewHolder(view)
    }

    override fun getItemCount() = movieList.size

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {

        with(holder) {
            bind(movieList[position])
            itemView.setOnClickListener {
                typeCurrentViewHolder = if (itemViewType == 0) 1 else 0
                Toast.makeText(
                    holder.itemView.context,
                    "$itemViewType, $typeCurrentViewHolder",
                    Toast.LENGTH_SHORT
                ).show()
                notifyItemChanged(adapterPosition)
            }
        }
    }
}
