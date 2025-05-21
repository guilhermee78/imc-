package com.example.calculadoradeimc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FormularioIMCActivity : AppCompatActivity() {

    private lateinit var editPeso: EditText
    private lateinit var editAltura: EditText
    private lateinit var txtResultadoIMC: TextView
    private lateinit var btnCalcular: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario)

        editPeso = findViewById(R.id.editPeso)
        editAltura = findViewById(R.id.editAltura)
        txtResultadoIMC = findViewById(R.id.txtResultadoIMC)
        btnCalcular = findViewById(R.id.btnCalcular)

        // Verificar se recebeu dados
        val pesoExistente = intent.getStringExtra("peso_existente")
        val alturaExistente = intent.getStringExtra("altura_existente")
        val imcExistente = intent.getStringExtra("imc_existente")

        if (pesoExistente != null && alturaExistente != null && imcExistente != null) {
            editPeso.setText(pesoExistente)
            editAltura.setText(alturaExistente)
            txtResultadoIMC.text = "IMC Atual: $imcExistente"
        }

        btnCalcular.setOnClickListener {
            val peso = editPeso.text.toString()
            val altura = editAltura.text.toString()

            val resultIntent = Intent()
            resultIntent.putExtra("peso", peso)
            resultIntent.putExtra("altura", altura)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
