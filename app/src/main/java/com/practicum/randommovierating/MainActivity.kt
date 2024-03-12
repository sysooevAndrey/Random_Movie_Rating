package com.practicum.randommovierating

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("UNUSED_EXPRESSION")
class MainActivity : AppCompatActivity() {

    companion object {
        private const val SEARCH_TEXT_DEF: String = ""
        private const val BASE_URL: String = "https://tv-api.com/"
        private const val NOT_FOUND_TOAST: String = "Ничего не нашлось"
        private const val NETWORK_PROBLEM_TOAST: String = "Проверьте подключение"
        val moviesList = ArrayList<MovieResponse.Movie>()
    }

    private var searchText: String = SEARCH_TEXT_DEF

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iMbdApiSearchService: IMbdApiService =
        retrofit.create(IMbdApiService::class.java)

    private lateinit var clearButton: ImageView

    private lateinit var searchEditText: EditText

    private lateinit var searchButton: ImageView

    private val movieAdapter = MovieAdapter(moviesList)

    private lateinit var progressBar: ProgressBar

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progress_bar)

        searchButton = findViewById(R.id.search_button)

        clearButton = findViewById(R.id.clear_text)

        searchEditText = findViewById(R.id.search_edit_text)

        searchEditText.setText(searchText)

        clearButton.setOnClickListener {
            searchEditText.setText(SEARCH_TEXT_DEF)
            moviesList.clear()
            movieAdapter.notifyDataSetChanged()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager
                ?.hideSoftInputFromWindow(searchEditText.windowToken, 0)
        }

        searchButton.setOnClickListener {
            createRequest()
        }

        val searchEditTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                clearButton.isVisible = clearButtonVisibility(s)
                searchText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        searchEditText.addTextChangedListener(searchEditTextWatcher)
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                createRequest()
            }
            false
        }

        val searchRecyclerView = findViewById<RecyclerView>(R.id.search_recycler_view)

        searchRecyclerView.adapter = movieAdapter

    }

    private fun createRequest() {
        progressBar.isVisible = true
        moviesList.clear()
        iMbdApiSearchService
            .search(searchText)
            .enqueue(object : Callback<MovieResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<MovieResponse>,
                    response: Response<MovieResponse>
                ) {
                    if (response.code() == 200) {
                        progressBar.isVisible = false
                        moviesList.addAll(response.body()?.results!!)
                        movieAdapter.notifyDataSetChanged()
                        if (moviesList.isEmpty()) {
                            makeToast(NOT_FOUND_TOAST)
                        }
                    } else {
                        makeToast(NETWORK_PROBLEM_TOAST)
                    }
                }

                override fun onFailure(
                    call: Call<MovieResponse>,
                    t: Throwable
                ) {
                    makeToast(NETWORK_PROBLEM_TOAST)
                }
            })
        true
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }

    private fun makeToast(text: String) =
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

