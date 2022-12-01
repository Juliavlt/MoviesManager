package ifsp.pdm.julia.moviesmanager.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ifsp.pdm.julia.moviesmanager.model.dao.MovieRoomDao
import ifsp.pdm.julia.moviesmanager.model.entity.Movie

@Database(entities = [Movie::class], version = 2)
abstract class MovieRoomDaoDatabase: RoomDatabase() {
    abstract fun getMovieRoomDao(): MovieRoomDao
}