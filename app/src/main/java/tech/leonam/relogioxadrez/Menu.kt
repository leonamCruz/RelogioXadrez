package tech.leonam.relogioxadrez

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
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
        clickInit()
    }

    private fun clickInit() {
        binding.init.setOnClickListener {
            if (binding.min.text.toString().isBlank()) {
                binding.min.setText("0")
            } else if (binding.seg.text.toString().isBlank()) {
                binding.seg.setText("0")
            }
                val minutos = binding.min.text.toString().toInt() * 60
                val segundos = binding.seg.text.toString().toInt() + minutos
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("seg", segundos)
                startActivity(intent)
        }
    }
}