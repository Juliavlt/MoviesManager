package ifsp.pdm.julia.moviesmanager.model.dao

import androidx.room.*
import ifsp.pdm.julia.moviesmanager.model.entity.Movie

@Dao
interface MovieRoomDao {
    companion object Constant {
        const val MOVIE_DATABASE_FILE = "movie_room"
        const val MOVIE_TABLE = "movie"
        const val ID_COLUMN = "id"
        const val NAME_COLUMN = "nomeFilme"
    }

    @Insert
    fun createMovie(movie: Movie)

    @Query("SELECT * FROM $MOVIE_TABLE WHERE $ID_COLUMN = :id")
    fun retrieveMovie(id: Int): Movie?

    @Query("SELECT * FROM $MOVIE_TABLE ORDER BY $NAME_COLUMN")
    fun retrieveMovies(): MutableList<Movie>

    @Update
    fun updateMovie(movie: Movie): Int

    @Delete
    fun deleteMovie(movie: Movie): Int
}