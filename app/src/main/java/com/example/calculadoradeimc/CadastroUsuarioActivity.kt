package com.example.calculadoradeimc

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calculadoradeimc.adapter.ContatoAdapter
import com.example.calculadoradeimc.dao.UsuarioDao
import com.example.calculadoradeimc.databinding.ActivityMainBinding
import com.example.calculadoradeimc.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CadastroUsuarioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var usuarioDao: UsuarioDao
    private lateinit var contatoAdapter: ContatoAdapter
    private val _listaUsuario = MutableLiveData<MutableList<Usuario>>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.IO).launch {
             getContatos()

            withContext(Dispatchers.Main){

                _listaUsuario.observe(this@CadastroUsuarioActivity){ listaUsuario ->

                val recyclerViewContato = binding.recyclerViewContatos
                recyclerViewContato.layoutManager = LinearLayoutManager(this@CadastroUsuarioActivity)
                recyclerViewContato.setHasFixedSize(true)
                contatoAdapter = ContatoAdapter(this@CadastroUsuarioActivity, listaUsuario)
                recyclerViewContato.adapter = contatoAdapter
                    contatoAdapter.notifyDataSetChanged()
            }
            }
        }

        binding.btCadastrar.setOnClickListener {
            val navegarTelaCadastro = Intent(this, CadastroUsuarios::class.java)
            startActivity(navegarTelaCadastro)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()

        CoroutineScope(Dispatchers.IO).launch {
            getContatos()

            withContext(Dispatchers.Main){

                _listaUsuario.observe(this@CadastroUsuarioActivity){ listaUsuario ->

                    val recyclerViewContato = binding.recyclerViewContatos
                    recyclerViewContato.layoutManager = LinearLayoutManager(this@CadastroUsuarioActivity)
                    recyclerViewContato.setHasFixedSize(true)
                    contatoAdapter = ContatoAdapter(this@CadastroUsuarioActivity, listaUsuario)
                    recyclerViewContato.adapter = contatoAdapter
                    contatoAdapter.notifyDataSetChanged()
                }
            }
        }

    }

    private fun getContatos(){
        usuarioDao = AppDatabase.getInstance(this).usuarioDao()
        val listaUsuarios: MutableList<Usuario> = usuarioDao.get()
        _listaUsuario.postValue(listaUsuarios)
    }
}