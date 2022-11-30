package ifsp.pdm.julia.moviesmanager.model.database

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import ifsp.pdm.julia.moviesmanager.R
import ifsp.pdm.julia.moviesmanager.model.dao.ContactDao
import ifsp.pdm.julia.moviesmanager.model.entity.Contact
import java.sql.SQLException

class ContactDaoSqlite(context: Context) : ContactDao {
    companion object Constant {
        private const val CONTACT_DATABASE_FILE = "contacts"
        private const val CONTACT_TABLE = "contact"
        private const val ID_COLUMN = "id"
        private const val MOVIE_NAME_COLUMN = "movie_name"
        private const val RELEASE_YEAR_COLUMN = "release_year"
        private const val PRODUCER_COLUMN = "producer"
        private const val TIME_COLUMN = "time"
        private const val GRADE_COLUMN = "grade"
        private const val GENDER_COLUMN = "gender"
        private const val WATCHED_COLUMN = "watched"

        private const val CREATE_CONTACT_TABLE_STATEMENT =
            "CREATE TABLE IF NOT EXISTS $CONTACT_TABLE ( " +
                    "$ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$MOVIE_NAME_COLUMN TEXT NOT NULL, " +
                    "$RELEASE_YEAR_COLUMN TEXT NOT NULL, " +
                    "$PRODUCER_COLUMN TEXT NOT NULL, " +
                    "$TIME_COLUMN TEXT NOT NULL, " +
                    "$GRADE_COLUMN TEXT NOT NULL, " +
                    "$GENDER_COLUMN TEXT NOT NULL, " +
                    "$WATCHED_COLUMN TEXT NOT NULL );"
    }

    // Referência para o banco de dados
    private val contactSqliteDatabase: SQLiteDatabase

    init {
        // Criando ou abrindo o banco
        contactSqliteDatabase = context.openOrCreateDatabase(
            CONTACT_DATABASE_FILE,
            MODE_PRIVATE,
            null
        )
        try {
            contactSqliteDatabase.execSQL(CREATE_CONTACT_TABLE_STATEMENT)
        } catch (se: SQLException) {
            Log.e(context.getString(R.string.app_name), se.toString())
        }
    }

    private fun Contact.toContentValues() = with(ContentValues()) {
        put(MOVIE_NAME_COLUMN, nomeFilme)
        put(RELEASE_YEAR_COLUMN, anoLancamento)
        put(PRODUCER_COLUMN, produtora)
        put(TIME_COLUMN, duracao)
        put(GRADE_COLUMN, nota)
        put(GENDER_COLUMN, genero)
        put(WATCHED_COLUMN, assistido)
        this
    }

    private fun contactToContentValues(contact: Contact) = with(ContentValues()) {
        put(MOVIE_NAME_COLUMN, contact.nomeFilme)
        put(RELEASE_YEAR_COLUMN, contact.anoLancamento)
        put(PRODUCER_COLUMN, contact.produtora)
        put(TIME_COLUMN, contact.duracao)
        put(GRADE_COLUMN, contact.nota)
        put(GENDER_COLUMN, contact.genero)
        put(WATCHED_COLUMN, contact.assistido)
        this
    }

    private fun Cursor.rowToContact() = Contact(
        getInt(getColumnIndexOrThrow(ID_COLUMN)),
        getString(getColumnIndexOrThrow(MOVIE_NAME_COLUMN)),
        getString(getColumnIndexOrThrow(RELEASE_YEAR_COLUMN)),
        getString(getColumnIndexOrThrow(PRODUCER_COLUMN)),
        getString(getColumnIndexOrThrow(TIME_COLUMN)).toInt(),
        getString(getColumnIndexOrThrow(GRADE_COLUMN)).toInt(),
        getString(getColumnIndexOrThrow(GENDER_COLUMN)),
        getString(getColumnIndexOrThrow(WATCHED_COLUMN)).toBoolean(),
    )

    override fun createContact(contact: Contact) = contactSqliteDatabase.insert(
        CONTACT_TABLE,
        null,
        contactToContentValues(contact)
    ).toInt()


    override fun retrieveContact(id: Int): Contact? {
        val cursor = contactSqliteDatabase.rawQuery(
            "SELECT * FROM $CONTACT_TABLE WHERE $ID_COLUMN = ?",
            arrayOf(id.toString())
        )
        val contact = if (cursor.moveToFirst()) cursor.rowToContact() else null

        cursor.close()
        return contact
    }

    override fun retrieveContacts(): MutableList<Contact> {
        val contactList = mutableListOf<Contact>()
        val cursor = contactSqliteDatabase.rawQuery(
            "SELECT * FROM $CONTACT_TABLE ORDER BY $MOVIE_NAME_COLUMN",
            null
        )
        while (cursor.moveToNext()) {
            contactList.add(cursor.rowToContact())
        }
        cursor.close()
        return contactList
    }

    override fun updateContact(contact: Contact) = contactSqliteDatabase.update(
        CONTACT_TABLE,
        contact.toContentValues(),
        "$ID_COLUMN = ?",
        arrayOf(contact.id.toString())
    )

    override fun deleteContact(id: Int) =
        contactSqliteDatabase.delete(
            CONTACT_TABLE,
            "$ID_COLUMN = ?",
            arrayOf(id.toString())
        )
}