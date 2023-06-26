package tech.leonam.relogioxadrez

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import tech.leonam.relogioxadrez.databinding.ActivityMenuBinding

class Menu : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clickInit()
    }

    private fun clickInit() {
        binding.init.setOnClickListener{
            val minutos = binding.min.text.toString().toInt() * 60
            val segundos = binding.seg.text.toString().toInt() + minutos

            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("seg",segundos)
            startActivity(intent)
        }
    }
}