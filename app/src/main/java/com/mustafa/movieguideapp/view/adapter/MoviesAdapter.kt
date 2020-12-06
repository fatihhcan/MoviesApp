package com.mustafa.movieguideapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mustafa.movieguideapp.R
import com.mustafa.movieguideapp.databinding.ItemMovieBinding
import com.mustafa.movieguideapp.models.Movie
import com.mustafa.movieguideapp.utils.MovieDiffUtilCallBack

class MoviesAdapter(
    private val dataBindingComponent: DataBindingComponent,
    private val movieOnClickCallback: ((Movie) -> Unit)?
) : PagingDataAdapter<Movie, MoviesAdapter.MoviesViewHolder>(MovieDiffUtilCallBack()) {

    companion object {
        private const val NETWORK_VIEW_TYPE = 1
        private const val WALLPAPER_VIEW_TYPE = 2
    }

    class MoviesViewHolder(private val binding: ItemMovieBinding) : ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.movie = movie
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) {
            NETWORK_VIEW_TYPE
        } else {
            WALLPAPER_VIEW_TYPE
        }
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val binding: ItemMovieBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_movie,
            parent,
            false,
            dataBindingComponent
        )

        binding.root.setOnClickListener {
            binding.movie?.let {
                movieOnClickCallback?.invoke(it)
            }
        }

        return MoviesViewHolder(binding)
    }
}