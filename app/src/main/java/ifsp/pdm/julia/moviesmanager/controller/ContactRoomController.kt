package ifsp.pdm.julia.moviesmanager.controller

import android.os.AsyncTask
import androidx.room.Room
import ifsp.pdm.julia.moviesmanager.model.dao.ContactRoomDao
import ifsp.pdm.julia.moviesmanager.model.dao.ContactRoomDao.Constant.CONTACT_DATABASE_FILE
import ifsp.pdm.julia.moviesmanager.model.database.ContactRoomDaoDatabase
import ifsp.pdm.julia.moviesmanager.model.entity.Contact
import ifsp.pdm.julia.moviesmanager.view.MainActivity

class ContactRoomController(private val mainActivity: MainActivity) {
    private val contactDaoImpl: ContactRoomDao by lazy {
        Room.databaseBuilder(
            mainActivity,
            ContactRoomDaoDatabase::class.java,
            CONTACT_DATABASE_FILE
        ).fallbackToDestructiveMigration()
            .build().getContactRoomDao()
    }

    fun insertContact(contact: Contact) {
        Thread {
            contactDaoImpl.createContact(contact)
            getContacts()
        }.start()
    }

    fun getContact(id: Int) = contactDaoImpl.retrieveContact(id)
    fun getContacts() {
        object: AsyncTask<Unit, Unit, MutableList<Contact>>(){
            override fun doInBackground(vararg params: Unit?): MutableList<Contact> {
                val returnList = mutableListOf<Contact>()
                returnList.addAll(contactDaoImpl.retrieveContacts())
                return returnList
            }

            override fun onPostExecute(result: MutableList<Contact>?) {
                super.onPostExecute(result)
                if (result != null){
                    mainActivity.updateContactList(result)
                }
            }
        }.execute()
    }

    fun editContact(contact: Contact) {
        Thread {
            contactDaoImpl.updateContact(contact)
            getContacts()
        }.start()
    }

    fun removeContact(contact: Contact) {
        Thread {
            contactDaoImpl.deleteContact(contact)
            getContacts()
        }.start()
    }
}
