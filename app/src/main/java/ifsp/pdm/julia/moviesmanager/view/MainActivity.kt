package ifsp.pdm.julia.moviesmanager.view

import android.content.Intent
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

    // Data source
    private val movieList: MutableList<Movie> = mutableListOf()

    // Adapter
    private lateinit var contactAdapter: MoviesAdapter

    private lateinit var carl: ActivityResultLauncher<Intent>

    // Controller
    private val contactController: MoviesRoomController by lazy {
        MoviesRoomController(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        contactAdapter = MoviesAdapter(this, movieList)
        amb.contactsLv.adapter = contactAdapter

        carl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val movie = result.data?.getParcelableExtra<Movie>(EXTRA_MOVIE)

                movie?.let { _contact->
                    if (_contact.id != null) {
                        val position = movieList.indexOfFirst { it.id == _contact.id }
                        if (position != -1) {
                            // Alterar na posição
                            contactController.editMovie(_contact)
                        }
                    }
                    else {
                        contactController.insertMovie(_contact)
                    }
                }
            }
        }

        registerForContextMenu(amb.contactsLv)

        amb.contactsLv.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val contact = movieList[position]
                val contactIntent = Intent(this@MainActivity, MovieActivity::class.java)
                contactIntent.putExtra(EXTRA_MOVIE, contact)
                contactIntent.putExtra(VIEW_CONTACT, true)
                startActivity(contactIntent)
            }

        // Buscando contatos no banco
        contactController.getMovies()
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
        val contact = movieList[position]
        return when(item.itemId) {
            R.id.removeMovieMi -> {
                // Remove o contato
                contactController.removeMovie(contact)
                true
            }
            R.id.editMovieMi -> {
                // Chama a tela para editar o contato
                val contactIntent = Intent(this, MovieActivity::class.java)
                contactIntent.putExtra(EXTRA_MOVIE, contact)
                contactIntent.putExtra(VIEW_CONTACT, false)
                carl.launch(contactIntent)
                true
            }
            else -> { false }
        }
    }

    fun updateMovieList(_movieList: MutableList<Movie>) {
        movieList.clear()
        movieList.addAll(_movieList)
        contactAdapter.notifyDataSetChanged()
    }
}