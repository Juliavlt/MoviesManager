package ifsp.pdm.julia.moviesmanager.controller
import ifsp.pdm.julia.moviesmanager.model.dao.ContactDao
import ifsp.pdm.julia.moviesmanager.model.database.ContactDaoSqlite
import ifsp.pdm.julia.moviesmanager.model.entity.Contact
import ifsp.pdm.julia.moviesmanager.view.MainActivity

class ContactController(private val mainActivity: MainActivity) {
    private val contactDaoImpl: ContactDao = ContactDaoSqlite(mainActivity)

    fun insertContact(contact: Contact) = contactDaoImpl.createContact(contact)
    fun getContact(id: Int) = contactDaoImpl.retrieveContact(id)
    fun getContacts() {
        Thread {
                val returnedList = contactDaoImpl.retrieveContacts()
                mainActivity.updateContactList(returnedList)
        }.start()
    }
    fun editContact(contact: Contact) = contactDaoImpl.updateContact(contact)
    fun removeContact(id: Int) = contactDaoImpl.deleteContact(id)
}