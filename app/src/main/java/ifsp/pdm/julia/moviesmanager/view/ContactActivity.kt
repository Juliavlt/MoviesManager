package ifsp.pdm.julia.moviesmanager.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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
                    generoEt.setSelection(genero.toInt())
                    if(!assistido){
                        notaEt.setVisibility(View.GONE)
                        assistidoEt.setChecked(false)
                    } else{
                        notaEt.setVisibility(View.VISIBLE)
                        notaEt.setText(nota.toString())
                        assistidoEt.setChecked(true)
                    }
                }
            }
        }
        val viewContact = intent.getBooleanExtra(VIEW_CONTACT, false)
        if (viewContact) {
            acb.nameEt.isEnabled = false
            acb.anoLancamentoEt.isEnabled = false
            acb.produtoraEt.isEnabled = false
            acb.duracaoEt.isEnabled = false
            acb.generoEt.isEnabled = false
            acb.assistidoEt.isEnabled = false
            acb.saveBt.visibility = View.GONE
            if (acb.assistidoEt.isChecked()) {
                acb.notaEt.isEnabled = false
                acb.notaEt.setVisibility(View.VISIBLE)
            } else {
                acb.notaEt.isEnabled = false
                acb.notaEt.setVisibility(View.GONE)
            }
        }

        acb.saveBt.setOnClickListener {
            val contact = Contact(
                id = receivedContact?.id,
                nomeFilme = acb.nameEt.text.toString(),
                anoLancamento = acb.anoLancamentoEt.text.toString(),
                produtora = acb.produtoraEt.text.toString(),
                duracao = acb.duracaoEt.text.toString().toInt(),
                assistido = (if (acb.assistidoEt.isChecked()) true else false),
                nota = if (acb.notaEt.text.toString().toInt() > 10) 10 else acb.notaEt.text.toString().toInt(),
                genero = acb.generoEt.selectedItemPosition.toString(),
            )
            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_CONTACT, contact)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        acb.assistidoEt.setOnCheckedChangeListener(
            object : CompoundButton.OnCheckedChangeListener {
                override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
                    if (acb.assistidoEt.isChecked()) {
                        acb.notaEt.setVisibility(View.VISIBLE)
                    } else {
                        acb.notaEt.setVisibility(View.GONE)
                    }
                }
            })
    }

}