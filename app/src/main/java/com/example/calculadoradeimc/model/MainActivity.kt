package com.example.calculadoradeimc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_ag)

        val btnIrParaIMC = findViewById<Button>(R.id.btCadastrarImc)
        val btnIrParaCadastro = findViewById<Button>(R.id.btCadastrar)

        btnIrParaIMC.setOnClickListener {
            val intent = Intent(this, CalculoImcActivity::class.java) // renomeie a antiga MainActivity de IMC se necessário
            startActivity(intent)
        }

        btnIrParaCadastro.setOnClickListener {
            val intent = Intent(this, CadastroUsuarioActivity::class.java)
            startActivity(intent)
        }
    }
}
