package ifsp.pdm.julia.moviesmanager.model.dao

import ifsp.pdm.julia.moviesmanager.model.entity.Contact

interface ContactDao {
    fun createContact(contact: Contact): Int
    fun retrieveContact(id: Int): Contact?
    fun retrieveContacts(): MutableList<Contact>
    fun updateContact(contact: Contact): Int
    fun deleteContact(id: Int): Int
}