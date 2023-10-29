package tech.leonam.relogioxadrez

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import tech.leonam.relogioxadrez.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var jogadorDeBaixo: Thread? = Thread {
        decairTempoJogadorDeBaixo()
    }
    private var jogadorDeCima: Thread? = null
    private lateinit var binding: ActivityMainBinding
    private var tempoJogadorDeBaixo: Double = 0.0
    private var tempoJogadorDeCima: Double = 0.0
    private var esgotouOTempo = false
    private var adicional: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()

        adicional = intent.extras!!.getDouble("ad")
        tempoJogadorDeBaixo = intent.extras!!.getDouble("seg")
        tempoJogadorDeCima = intent.extras!!.getDouble("seg")

        Toast.makeText(this, adicional.toString(),Toast.LENGTH_SHORT).show()
        window.navigationBarColor = Color.BLACK

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        converterPadraoDeTempoParaView(true, tempoJogadorDeBaixo)
        converterPadraoDeTempoParaView(false, tempoJogadorDeCima)
        clicaNoStart()
        clicaNoRestart()
    }

    private fun clicaNoRestart() {
        binding.restart.setOnClickListener {
            recreate()
        }
    }

    private fun clicaNoStart() {
        binding.startM.setOnClickListener {
            jogadorDeBaixo?.start()
            jogadorDeBaixoClica()
            jogadorDeCimaClica()
            binding.startM.isEnabled = false
        }
    }

    private fun jogadorDeCimaClica() {
        binding.jogadorSuperior.setOnClickListener {
            tempoJogadorDeCima += adicional
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
            tempoJogadorDeBaixo += adicional
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
                    converterPadraoDeTempoParaView(false, tempoJogadorDeBaixo)
                }
                if (tempoJogadorDeBaixo <= 0.001) {
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
                    converterPadraoDeTempoParaView(true, tempoJogadorDeCima)
                }
                if (tempoJogadorDeCima <= 0.001) {
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

    private fun converterPadraoDeTempoParaView(isDeCima: Boolean, tempoEmSegundos: Double) {
        runOnUiThread {
            val minutos = (tempoEmSegundos / 60).toInt()
            val segundos = (tempoEmSegundos % 60).toInt()
            val milesimos = ((tempoEmSegundos - (tempoEmSegundos.toInt())) * 100).toInt()
            val tempoConvertido = String.format("%02d:%02d:%02d", minutos, segundos, milesimos)

            if (isDeCima) binding.tempoSuperior.text =
                tempoConvertido else binding.tempoInferior.text = tempoConvertido
        }
    }

    private fun quemGanhouNoTempo(quemGanhou: String) {
        val alert = AlertDialog.Builder(this)
        alert.setMessage("Jogador de $quemGanhou foi campeão por tempo")
        alert.setTitle(getString(R.string.tempos_um_campe_o))
        alert.create().show()
    }

}
