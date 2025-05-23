package com.example.calculadoradeimc

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calculadoradeimc.adapter.ContatoAdapter
import com.example.calculadoradeimc.dao.UsuarioDao
import com.example.calculadoradeimc.databinding.ActivityMainAgBinding // <-- CORRIGIDO AQUI
import com.example.calculadoradeimc.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.calculadoradeimc.model.AppDatabase
import kotlinx.coroutines.withContext

class CadastroUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainAgBinding // <-- CORRIGIDO AQUI
    private lateinit var usuarioDao: UsuarioDao
    private lateinit var contatoAdapter: ContatoAdapter

    private val _listaUsuario = MutableLiveData<MutableList<Usuario>>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainAgBinding.inflate(layoutInflater) // <-- CORRIGIDO AQUI
        setContentView(binding.root)

        // Initialize the RecyclerView and adapter once
        val recyclerViewContato = binding.recyclerViewContatos // <-- CORRIGIDO AQUI
        recyclerViewContato.layoutManager = LinearLayoutManager(this)
        recyclerViewContato.setHasFixedSize(true)
        contatoAdapter = ContatoAdapter(this, mutableListOf())
        recyclerViewContato.adapter = contatoAdapter

        // Observe changes in _listaUsuario LiveData
        _listaUsuario.observe(this, Observer { listaUsuario -> // <-- CORRIGIDO AQUI (_listaUsuarioS para _listaUsuario)
            contatoAdapter.updateList(listaUsuario)
            // contatoAdapter.notifyDataSetChanged() // Geralmente não é necessário aqui se updateList já chama notifyDataSetChanged()
        })

        // Fetch contacts when the activity is created
        CoroutineScope(Dispatchers.IO).launch {
            getContatos()
        }

        // Set up the click listener for the register button
        binding.btCadastrar.setOnClickListener {
            val navegarTelaCadastro = Intent(this, CadastroUsuarios::class.java)
            startActivity(navegarTelaCadastro)
        }
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.IO).launch {
            getContatos()
        }
    }

    private fun getContatos() {
        usuarioDao = AppDatabase.getInstance(this).usuarioDao()
        val listaUsuarios: MutableList<Usuario> = usuarioDao.get()
        _listaUsuario.postValue(listaUsuarios)
    }
}