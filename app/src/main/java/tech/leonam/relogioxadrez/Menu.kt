package tech.leonam.relogioxadrez

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import tech.leonam.relogioxadrez.databinding.ActivityMenuBinding

class Menu : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window!!.statusBarColor = Color.BLACK
        supportActionBar!!.hide()
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        povoarSpinnerPresetTempo()
        setarPadraoSeExistir()
        povoarSpinnerMelhorDeTanto()
        clickInit()
    }

    private fun povoarSpinnerMelhorDeTanto() {
        val spinnerArray = arrayOf(
            "3",
            "5",
            "7",
            "11",
            "13",
            "15",
            "17",
            "19",
            "21"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.melhorDe.adapter = adapter
    }

    private fun setarPadraoSeExistir() {
        binding.spinnerPreset.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val seg = binding.spinnerPreset.selectedItem.toString()

                val regex = """(\d{2})\|(\d{2})""".toRegex()
                val matchResults = regex.findAll(seg)

                matchResults.forEach {
                    val (minutos, adicional) = it.destructured
                    binding.min.setText(minutos)
                    binding.editTextSegundos.setText(adicional)
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }
    }

    private fun povoarSpinnerPresetTempo() {
        val spinnerArray = arrayOf(
            "01|00",
            "02|01",
            "03|00",
            "03|02",
            "05|00",
            "05|03",
            "10|00",
            "10|05",
            "15|10",
            "30|00",
            "30|20",
            "60|00"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerArray)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        binding.spinnerPreset.adapter = adapter
    }

    private fun clickInit() {
        binding.init.setOnClickListener {
            val min = binding.min.text.toString()
            val seg = binding.seg.text.toString()
            val adicional = binding.editTextSegundos.text.toString()
            val melhorDe = binding.melhorDe.selectedItem.toString().toInt()

            if (min.isBlank()) binding.min.setText("0")
            if (seg.isBlank()) binding.seg.setText("0")
            if (adicional.isBlank()) binding.editTextSegundos.setText("0")

            val minutos = binding.min.text.toString().toDouble() * 60.0
            val segundos = binding.seg.text.toString().toDouble() + minutos

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("seg", segundos)
            intent.putExtra("ad", adicional.toDouble())
            intent.putExtra("melhor", melhorDe)

            startActivity(intent)
        }
    }
}