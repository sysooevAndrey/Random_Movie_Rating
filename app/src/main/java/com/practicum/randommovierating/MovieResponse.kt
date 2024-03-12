package com.practicum.randommovierating

class MovieResponse(

    val results: List<Movie>
) {
    data class Movie(
        val title: String,
        val image: String,
    )
}

