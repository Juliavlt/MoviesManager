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
import ifsp.pdm.julia.moviesmanager.adapter.ContactAdapter
import ifsp.pdm.julia.moviesmanager.controller.ContactRoomController
import ifsp.pdm.julia.moviesmanager.databinding.ActivityMainBinding
import ifsp.pdm.julia.moviesmanager.model.Constant.EXTRA_CONTACT
import ifsp.pdm.julia.moviesmanager.model.Constant.VIEW_CONTACT
import ifsp.pdm.julia.moviesmanager.model.entity.Contact

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Data source
    private val contactList: MutableList<Contact> = mutableListOf()

    // Adapter
    private lateinit var contactAdapter: ContactAdapter

    private lateinit var carl: ActivityResultLauncher<Intent>

    // Controller
    private val contactController: ContactRoomController by lazy {
        ContactRoomController(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        contactAdapter = ContactAdapter(this, contactList)
        amb.contactsLv.adapter = contactAdapter

        carl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val contact = result.data?.getParcelableExtra<Contact>(EXTRA_CONTACT)

                contact?.let { _contact->
                    if (_contact.id != null) {
                        val position = contactList.indexOfFirst { it.id == _contact.id }
                        if (position != -1) {
                            // Alterar na posição
                            contactController.editContact(_contact)
                        }
                    }
                    else {
                        contactController.insertContact(_contact)
                    }
                }
            }
        }

        registerForContextMenu(amb.contactsLv)

        amb.contactsLv.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val contact = contactList[position]
                val contactIntent = Intent(this@MainActivity, ContactActivity::class.java)
                contactIntent.putExtra(EXTRA_CONTACT, contact)
                contactIntent.putExtra(VIEW_CONTACT, true)
                startActivity(contactIntent)
            }

        // Buscando contatos no banco
        contactController.getContacts()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.addContactMi -> {
                carl.launch(Intent(this, ContactActivity::class.java))
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
        val contact = contactList[position]
        return when(item.itemId) {
            R.id.removeContactMi -> {
                // Remove o contato
                contactController.removeContact(contact)
                true
            }
            R.id.editContactMi -> {
                // Chama a tela para editar o contato
                val contactIntent = Intent(this, ContactActivity::class.java)
                contactIntent.putExtra(EXTRA_CONTACT, contact)
                contactIntent.putExtra(VIEW_CONTACT, false)
                carl.launch(contactIntent)
                true
            }
            else -> { false }
        }
    }

    fun updateContactList(_contactList: MutableList<Contact>) {
        contactList.clear()
        contactList.addAll(_contactList)
        contactAdapter.notifyDataSetChanged()
    }
}