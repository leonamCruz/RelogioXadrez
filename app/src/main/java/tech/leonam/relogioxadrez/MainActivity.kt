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
    private var tempoJogadorDeCima = 10.0
    private var tempoJogadorDeBaixo = 10.0
    private var esgotouOTempo = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        window.navigationBarColor = Color.BLACK
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.tempoInferior.text = String.format("%.2f",tempoJogadorDeBaixo)
        binding.tempoSuperior.text = String.format("%.2f",tempoJogadorDeCima)
        setContentView(binding.root)
        jogadorDeBaixo?.start()
        jogadorDeBaixoClica()
        jogadorDeCimaClica()
    }

    private fun jogadorDeCimaClica() {

        binding.jogadorSuperior.setOnClickListener {
            jogadorDeCima?.interrupt()
            runOnUiThread {
                binding.jogadorSuperior.isEnabled = false
                binding.jogadorInferior.isEnabled = true
            }
            jogadorDeBaixo = Thread {
                decairTempoJogadorDeBaixo()
            }
            jogadorDeBaixo?.start()
        }
    }


    private fun jogadorDeBaixoClica() {
        binding.jogadorInferior.setOnClickListener {
            jogadorDeBaixo?.interrupt()
            runOnUiThread {
                binding.jogadorSuperior.isEnabled = true
                binding.jogadorInferior.isEnabled = false
            }
            jogadorDeCima = Thread {
                decairTempoJogadorDeCima()
            }
            jogadorDeCima?.start()
        }
    }

    private fun decairTempoJogadorDeBaixo() {
        try {
            while (!esgotouOTempo) {
                Thread.sleep(1)
                tempoJogadorDeBaixo -= 0.001
                runOnUiThread {
                    binding.tempoInferior.text = String.format("%.2f",tempoJogadorDeBaixo)
                }
                if (tempoJogadorDeBaixo <= 0.01) {
                    esgotouOTempo = true
                    runOnUiThread {
                        binding.tempoInferior.text = "0"
                        quemGanhouNoTempo("Pretas")
                    }
                }
            }
        } catch (ignored: InterruptedException) {
        }
    }

    private fun decairTempoJogadorDeCima() {
        try {
            while (!esgotouOTempo) {
                Thread.sleep(1)
                tempoJogadorDeCima -= 0.001
                runOnUiThread {
                    binding.tempoSuperior.text = String.format("%.2f",tempoJogadorDeCima)
                }
                if (tempoJogadorDeCima <= 0.01) {
                    esgotouOTempo = true
                    runOnUiThread {
                        binding.tempoSuperior.text = "0"
                        quemGanhouNoTempo("Brancas")
                    }
                }
            }
        } catch (ignored: InterruptedException) {
        }
    }

    private fun quemGanhouNoTempo(quemGanhou: String) {
        val alert = AlertDialog.Builder(this)
        alert.setMessage("Jogador de $quemGanhou foi campeão por tempo")
        alert.setTitle(getString(R.string.tempos_um_campe_o))
        alert.create().show()
    }
}
