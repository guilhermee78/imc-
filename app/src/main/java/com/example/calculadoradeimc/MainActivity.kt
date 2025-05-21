package com.example.calculadoradeimc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_FORMULARIO = 1
        const val REQUEST_CODE_EDITAR = 2
    }

    private lateinit var btnAbrirFormulario: Button
    private lateinit var recyclerUsuarios: RecyclerView
    private lateinit var adapter: UsuarioIMCAdapter
    private val listaUsuarios = mutableListOf<UsuarioIMC>()
    private var usuarioEditandoPosicao: Int = -1 // Para saber quem está sendo editado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAbrirFormulario = findViewById(R.id.btnAbrirFormulario)
        recyclerUsuarios = findViewById(R.id.recyclerUsuarios)

        adapter = UsuarioIMCAdapter(
            listaUsuarios,
            onEditarClick = { position -> editarUsuario(position) },
            onExcluirClick = { position -> excluirUsuario(position) }
        )

        recyclerUsuarios.layoutManager = LinearLayoutManager(this)
        recyclerUsuarios.adapter = adapter

        btnAbrirFormulario.setOnClickListener {
            val intent = Intent(this, FormularioIMCActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_FORMULARIO)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val peso = data?.getStringExtra("peso") ?: return
            val altura = data.getStringExtra("altura") ?: return

            val pesoDouble = peso.toDoubleOrNull() ?: return
            val alturaDouble = altura.toDoubleOrNull() ?: return
            val imcCalculado = calcularIMC(pesoDouble, alturaDouble)

            if (requestCode == REQUEST_CODE_FORMULARIO) {
                val novoUsuario = UsuarioIMC(peso = peso, altura = altura, imc = "%.2f".format(imcCalculado))
                listaUsuarios.add(novoUsuario)
                adapter.notifyItemInserted(listaUsuarios.size - 1)
            } else if (requestCode == REQUEST_CODE_EDITAR) {
                if (usuarioEditandoPosicao != -1) {
                    val usuarioEditado = listaUsuarios[usuarioEditandoPosicao]
                    usuarioEditado.peso = peso
                    usuarioEditado.altura = altura
                    usuarioEditado.imc = "%.2f".format(imcCalculado)
                    adapter.notifyItemChanged(usuarioEditandoPosicao)
                }
            }
        }
    }

    private fun editarUsuario(position: Int) {
        val usuario = listaUsuarios[position]
        usuarioEditandoPosicao = position

        val intent = Intent(this, FormularioIMCActivity::class.java)
        intent.putExtra("peso_existente", usuario.peso)
        intent.putExtra("altura_existente", usuario.altura)
        intent.putExtra("imc_existente", usuario.imc)
        startActivityForResult(intent, REQUEST_CODE_EDITAR)
    }

    private fun excluirUsuario(position: Int) {
        listaUsuarios.removeAt(position)
        adapter.notifyItemRemoved(position)
    }

    private fun calcularIMC(peso: Double, altura: Double): Double {
        return peso / (altura * altura)
    }
}
