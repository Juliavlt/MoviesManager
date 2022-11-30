package ifsp.pdm.julia.moviesmanager.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ifsp.pdm.julia.moviesmanager.model.dao.ContactRoomDao
import ifsp.pdm.julia.moviesmanager.model.entity.Contact

@Database(entities = [Contact::class], version = 2)
abstract class ContactRoomDaoDatabase: RoomDatabase() {
    abstract fun getContactRoomDao(): ContactRoomDao
}