package tech.leonam.relogioxadrez

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import tech.leonam.relogioxadrez.databinding.ActivityMainBinding

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private var adicional: Double = 0.0
    private var jogadorDeBaixo: Thread? = null
    private var jogadorDeCima: Thread? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var xadrez: Xadrez
    private var tempoJogadorDeCima: Double = 0.0
    private var tempoJogadorDeBaixo: Double = 0.0
    private var esgotouOTempo = false
    private var movCima: Int = 0
    private var movBaixo: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        window.navigationBarColor = Color.BLACK
        binding = ActivityMainBinding.inflate(layoutInflater)
        xadrez = intent.extras?.getParcelable("xadrez")!!
        zerar()

        jogadorDeBaixo = Thread {
            decairTempoJogadorDeBaixo()
        }
        binding.jogadorInferior.isEnabled = false
        binding.jogadorSuperior.isEnabled = false

        clicaNoStart()

        jogadorDeBaixoClica()
        jogadorDeCimaClica()

        clicaNoRestart()
        abandonarPartida()

        setContentView(binding.root)
    }

    private fun abandonarPartida() {
        binding.jogadorInferior.setOnLongClickListener{
            jogadorDeCima?.interrupt()
            jogadorDeBaixo?.interrupt()
            quemAbandonou("Rosa")
            true
        }
        binding.jogadorSuperior.setOnLongClickListener{
            jogadorDeCima?.interrupt()
            jogadorDeBaixo?.interrupt()
            quemAbandonou("Azul")
            true
        }
    }

    private fun clicaNoRestart() {
        binding.restart.setOnClickListener {
            recreate()
        }
    }

    private fun clicaNoStart() {
        binding.startM.setOnClickListener {
            jogadorDeBaixo?.start()
            binding.jogadorInferior.isEnabled = true
            binding.startM.isEnabled = false
        }
    }

    private fun jogadorDeCimaClica() {
        binding.jogadorSuperior.setOnClickListener {
            tempoJogadorDeCima += adicional
            movCima++
            binding.movAzul.text = movCima.toString()
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
            movBaixo++
            binding.movRosa.text = movBaixo.toString()

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
                        quemGanhouNoTempo("Azul")
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
                        quemGanhouNoTempo("Rosa")
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
        alert.setPositiveButton("Ok") { _, _ ->

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }
        alert.create().show()
    }

    private fun quemAbandonou (quemAbandonou: String){
        val alert = AlertDialog.Builder(this)
        alert.setMessage("Jogador $quemAbandonou abandonou porque não dá mais.")
        alert.setTitle(getString(R.string.abandono))
        alert.setPositiveButton("Ok") { _, _ ->

           recreate()

        }
        alert.create().show()
    }

    private fun zerar(){
        tempoJogadorDeCima = xadrez.segOne
        tempoJogadorDeBaixo = xadrez.segTwo
        adicional = xadrez.adicional

        converterPadraoDeTempoParaView(true, tempoJogadorDeCima)
        converterPadraoDeTempoParaView(false, tempoJogadorDeBaixo)

        movCima = 0
        movBaixo = 0

        binding.movAzul.text = movCima.toString()
        binding.movRosa.text = movBaixo.toString()
    }

}
