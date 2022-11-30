package ifsp.pdm.julia.moviesmanager.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ifsp.pdm.julia.moviesmanager.R
import ifsp.pdm.julia.moviesmanager.model.entity.Contact

class ContactAdapter(
    context: Context,
    private val contactList: MutableList<Contact>
) : ArrayAdapter<Contact>(context, R.layout.tile_contact, contactList) {
    private data class TileContactHolder(val nameTv: TextView)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val contact = contactList[position]
        var contactTileView = convertView
        if (contactTileView == null) {
            // Inflo uma nova célula
            contactTileView =
                (context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                    R.layout.tile_contact,
                    parent,
                    false
                )

            val tileContactHolder = TileContactHolder(
                contactTileView.findViewById(R.id.nomeFilmeTv),
            )
            contactTileView.tag = tileContactHolder
        }

        with(contactTileView?.tag as TileContactHolder) {
            nameTv.text = contact.nomeFilme
        }

        return contactTileView
    }
}