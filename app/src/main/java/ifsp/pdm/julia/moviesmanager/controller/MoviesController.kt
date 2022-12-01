package ifsp.pdm.julia.moviesmanager.controller
import ifsp.pdm.julia.moviesmanager.model.dao.MoviesDao
import ifsp.pdm.julia.moviesmanager.model.database.MoviesDaoSqlite
import ifsp.pdm.julia.moviesmanager.model.entity.Movie
import ifsp.pdm.julia.moviesmanager.view.MainActivity

class MoviesController(private val mainActivity: MainActivity) {
    private val contactDaoImpl: MoviesDao = MoviesDaoSqlite(mainActivity)

    fun insertMovie(movie: Movie) = contactDaoImpl.createMovie(movie)
    fun getMovie(id: Int) = contactDaoImpl.retrieveMovie(id)
    fun getMovies() {
        Thread {
                val returnedList = contactDaoImpl.retrieveMovies()
                mainActivity.updateMovieList(returnedList)
        }.start()
    }
    fun editMovie(movie: Movie) = contactDaoImpl.updateMovie(movie)
    fun removeMovie(id: Int) = contactDaoImpl.deleteMovie(id)
}