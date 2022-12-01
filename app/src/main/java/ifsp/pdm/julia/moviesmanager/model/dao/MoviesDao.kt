package ifsp.pdm.julia.moviesmanager.model.dao

import ifsp.pdm.julia.moviesmanager.model.entity.Movie

interface MoviesDao {
    fun createMovie(movie: Movie): Int
    fun retrieveMovie(id: Int): Movie?
    fun retrieveMovies(): MutableList<Movie>
    fun updateMovie(movie: Movie): Int
    fun deleteMovie(id: Int): Int
}