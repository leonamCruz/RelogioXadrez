package tech.leonam.relogioxadrez

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
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
        povoarSpinner()
        clickInit()
    }

    private fun povoarSpinner() {
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
            val adicional = binding.textAdicionalLances.text.toString()

            if (min.isBlank()) binding.min.setText("0")
            if (seg.isBlank()) binding.seg.setText("0")
            if (adicional.isBlank()) binding.textAdicionalLances.text = "0"

            val minutos = binding.min.text.toString().toInt() * 60
            val segundos = binding.seg.text.toString().toInt() + minutos
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("seg", segundos)
            startActivity(intent)
        }
    }
}