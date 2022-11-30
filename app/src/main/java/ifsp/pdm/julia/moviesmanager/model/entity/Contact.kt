package ifsp.pdm.julia.moviesmanager.model.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Contact(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    @NonNull
    var nomeFilme: String,
    @NonNull
    var anoLancamento: String,
    @NonNull
    var produtora: String,
    @NonNull
    var duracao: Int,
    @NonNull
    var nota: Int,
    @NonNull
    var genero: String,
    @NonNull
    var assistido: Boolean,
): Parcelable
