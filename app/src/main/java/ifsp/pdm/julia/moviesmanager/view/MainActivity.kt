package ifsp.pdm.julia.moviesmanager.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.AdapterContextMenuInfo
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ifsp.pdm.julia.moviesmanager.R
import ifsp.pdm.julia.moviesmanager.adapter.MoviesAdapter
import ifsp.pdm.julia.moviesmanager.controller.MoviesRoomController
import ifsp.pdm.julia.moviesmanager.databinding.ActivityMainBinding
import ifsp.pdm.julia.moviesmanager.model.Constant.EXTRA_MOVIE
import ifsp.pdm.julia.moviesmanager.model.Constant.VIEW_CONTACT
import ifsp.pdm.julia.moviesmanager.model.entity.Movie

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val movieList: MutableList<Movie> = mutableListOf()

    private lateinit var movieAdapter: MoviesAdapter

    private lateinit var carl: ActivityResultLauncher<Intent>

    private val movieController: MoviesRoomController by lazy {
        MoviesRoomController(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        movieAdapter = MoviesAdapter(this, movieList)
        amb.moviesLv.adapter = movieAdapter

        carl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val movie = result.data?.getParcelableExtra<Movie>(EXTRA_MOVIE)

                movie?.let { _movie->
                    if (_movie.id != null) {
                        val position = movieList.indexOfFirst { it.id == _movie.id }
                        if (position != -1) {
                            movieController.editMovie(_movie)
                        }
                    }
                    else {
                        movieController.insertMovie(_movie)
                    }
                }
            }
        }

        registerForContextMenu(amb.moviesLv)

        amb.moviesLv.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val movie = movieList[position]
                val movieIntent = Intent(this@MainActivity, MovieActivity::class.java)
                movieIntent.putExtra(EXTRA_MOVIE, movie)
                movieIntent.putExtra(VIEW_CONTACT, true)
                startActivity(movieIntent)
            }

        movieController.getMovies()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.addMovieMi -> {
                carl.launch(Intent(this, MovieActivity::class.java))
                true
            }
            else -> { false }
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context_menu_main, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as AdapterContextMenuInfo).position
        val movie = movieList[position]
        return when(item.itemId) {
            R.id.removeMovieMi -> {
                // Remove o contato
                movieController.removeMovie(movie)
                true
            }
            R.id.editMovieMi -> {
                // Chama a tela para editar o contato
                val movieIntent = Intent(this, MovieActivity::class.java)
                movieIntent.putExtra(EXTRA_MOVIE, movie)
                movieIntent.putExtra(VIEW_CONTACT, false)
                carl.launch(movieIntent)
                true
            }
            else -> { false }
        }
    }

    fun updateMovieList(_movieList: MutableList<Movie>) {
        movieList.clear()
        movieList.addAll(_movieList)
        movieAdapter.notifyDataSetChanged()
    }
}