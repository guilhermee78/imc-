package com.example.calculadoradeimc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.calculadoradeimc.databinding.ActivityMainAgBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainAgBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Usando ViewBinding para acessar os componentes do XML
        binding = ActivityMainAgBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Botão que leva à tela de cálculo de IMC
        binding.btCadastrarImc.setOnClickListener {
            val intent = Intent(this, CalculoImcActivity::class.java)
            startActivity(intent)
        }

        // Botão que leva à tela de cadastro de usuários
        binding.btCadastrar.setOnClickListener {
            val intent = Intent(this, CadastroUsuarioActivity::class.java)
            startActivity(intent)
        }
    }
}
