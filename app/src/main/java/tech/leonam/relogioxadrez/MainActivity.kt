package tech.leonam.relogioxadrez

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import tech.leonam.relogioxadrez.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var jogadorDeBaixo: Thread? = Thread {
        decairTempoJogadorDeBaixo()
    }
    private var jogadorDeCima: Thread? = null
    private lateinit var binding: ActivityMainBinding
    private var tempoJogadorDeCima = 10
    private var tempoJogadorDeBaixo = 10
    private var esgotouOTempo = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        window.navigationBarColor = Color.BLACK
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.tempoInferior.text = tempoJogadorDeBaixo.toString()
        binding.tempoSuperior.text = tempoJogadorDeCima.toString()
        setContentView(binding.root)
        jogadorDeBaixo?.start()
        jogadorDeBaixoClica()
        jogadorDeCimaClica()
    }

    private fun jogadorDeCimaClica() {
        binding.jogadorSuperior.setOnClickListener {
            jogadorDeCima?.interrupt()
            jogadorDeBaixo = Thread {
                decairTempoJogadorDeBaixo()
            }
            jogadorDeBaixo?.start()
        }
    }



    private fun jogadorDeBaixoClica() {
        binding.jogadorInferior.setOnClickListener {
            jogadorDeBaixo?.interrupt()
            jogadorDeCima = Thread {
                decairTempoJogadorDeCima()
            }
            jogadorDeCima?.start()
        }
    }

    private fun decairTempoJogadorDeBaixo() {
        try {
            while (!esgotouOTempo) {
                Thread.sleep(1000)
                tempoJogadorDeBaixo--
                runOnUiThread {
                    binding.tempoInferior.text = tempoJogadorDeBaixo.toString()
                }
                if (tempoJogadorDeBaixo == 0) {
                    esgotouOTempo = true
                    runOnUiThread(){
                        quemGanhouNoTempo("Pretas")
                    }
                }
            }
        } catch (ignored: InterruptedException) {}
    }

    private fun decairTempoJogadorDeCima() {
        try {
            while (!esgotouOTempo) {
                Thread.sleep(1000)
                tempoJogadorDeCima--
                runOnUiThread {
                    binding.tempoSuperior.text = tempoJogadorDeCima.toString()
                }
                if (tempoJogadorDeCima == 0) {
                    esgotouOTempo = true
                    runOnUiThread {
                        quemGanhouNoTempo("Brancas")
                    }
                }
            }
        } catch (ignored: InterruptedException) {}
    }
    private fun quemGanhouNoTempo(quemGanhou:String){
        val alert = AlertDialog.Builder(this)
        alert.setMessage("Jogador de $quemGanhou foi campeão por tempo")
        alert.setTitle(getString(R.string.tempos_um_campe_o))
        alert.create().show()
    }
}
