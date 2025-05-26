package com.example.calculadoradeimc

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calculadoradeimc.adapter.ContatoAdapter
import com.example.calculadoradeimc.dao.UsuarioDao
import com.example.calculadoradeimc.databinding.ActivityListaUsuariosBinding
import com.example.calculadoradeimc.model.AppDatabase
import com.example.calculadoradeimc.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext // Importar withContext

class ListaUsuariosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaUsuariosBinding
    private lateinit var usuarioDao: UsuarioDao
    private lateinit var contatoAdapter: ContatoAdapter

    private val _listaUsuario = MutableLiveData<MutableList<Usuario>>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListaUsuariosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicialize o DAO aqui uma vez
        usuarioDao = AppDatabase.getInstance(this).usuarioDao()

        val recyclerViewContato = binding.recyclerViewUsuarios
        recyclerViewContato.layoutManager = LinearLayoutManager(this)
        recyclerViewContato.setHasFixedSize(true)
        contatoAdapter = ContatoAdapter(this, mutableListOf())
        recyclerViewContato.adapter = contatoAdapter

        _listaUsuario.observe(this, Observer { listaUsuario ->
            contatoAdapter.updateList(listaUsuario)
        })

        // Chame getContatos dentro de uma corrotina
        CoroutineScope(Dispatchers.IO).launch {
            getContatos()
        }
    }

    override fun onResume() {
        super.onResume()
        // Chame getContatos dentro de uma corrotina para garantir que os dados são atualizados ao voltar para a tela
        CoroutineScope(Dispatchers.IO).launch {
            getContatos()
        }
    }

    // A função getContatos precisa ser uma função suspend
    private suspend fun getContatos() { // ADICIONADO 'suspend' AQUI
        val listaUsuarios: MutableList<Usuario> = usuarioDao.get()
        // Atualize o LiveData na thread principal
        withContext(Dispatchers.Main) {
            _listaUsuario.postValue(listaUsuarios)
        }
    }
}