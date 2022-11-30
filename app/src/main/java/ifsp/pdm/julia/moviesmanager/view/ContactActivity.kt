package ifsp.pdm.julia.moviesmanager.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ifsp.pdm.julia.moviesmanager.databinding.ActivityContactBinding
import ifsp.pdm.julia.moviesmanager.model.Constant.EXTRA_CONTACT
import ifsp.pdm.julia.moviesmanager.model.Constant.VIEW_CONTACT
import ifsp.pdm.julia.moviesmanager.model.entity.Contact

class ContactActivity : AppCompatActivity() {
    private val acb: ActivityContactBinding by lazy {
        ActivityContactBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(acb.root)

        val receivedContact = intent.getParcelableExtra<Contact>(EXTRA_CONTACT)
        receivedContact?.let{ _receivedContact ->
            with(acb) {
                with(_receivedContact) {
                    nameEt.setText(nomeFilme)
                    anoLancamentoEt.setText(anoLancamento)
                    produtoraEt.setText(produtora)
                    duracaoEt.setText(duracao.toString())
                    notaEt.setText(nota.toString())
                    generoEt.setText(genero)
                    assistidoEt.setText(assistido.toString())
                }
            }
        }
        val viewContact = intent.getBooleanExtra(VIEW_CONTACT, false)
        if (viewContact) {
            acb.nameEt.isEnabled = false
            acb.anoLancamentoEt.isEnabled = false
            acb.produtoraEt.isEnabled = false
            acb.duracaoEt.isEnabled = false
            acb.notaEt.isEnabled = false
            acb.generoEt.isEnabled = false
            acb.assistidoEt.isEnabled = false
            acb.saveBt.visibility = View.GONE
        }

        acb.saveBt.setOnClickListener {
            val contact = Contact(
                id = receivedContact?.id,
                nomeFilme = acb.nameEt.text.toString(),
                anoLancamento = acb.anoLancamentoEt.text.toString(),
                produtora = acb.produtoraEt.text.toString(),
                duracao = acb.duracaoEt.text.toString().toInt(),
                nota = acb.notaEt.text.toString().toInt(),
                genero = acb.generoEt.text.toString(),
                assistido = acb.assistidoEt.text.toString().toBoolean(),

            )
            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_CONTACT, contact)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}