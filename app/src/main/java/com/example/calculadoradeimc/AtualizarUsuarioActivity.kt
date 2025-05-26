package com.example.calculadoradeimc // PACOTE CORRETO AGORA

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.calculadoradeimc.dao.UsuarioDao
import com.example.calculadoradeimc.databinding.ActivityAtualizarUsuarioAgBinding
import com.example.calculadoradeimc.model.AppDatabase // Importe AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AtualizarUsuarioActivity : AppCompatActivity() { // NOME DA CLASSE CORRETO AGORA

    private lateinit var binding: ActivityAtualizarUsuarioAgBinding
    private lateinit var usuarioDao: UsuarioDao // Inicialize uma vez

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAtualizarUsuarioAgBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa o DAO uma vez quando a Activity é criada
        usuarioDao = AppDatabase.getInstance(this).usuarioDao()

        val nomeRecuperado = intent.extras?.getString("nome")
        val sobrenomeRecuperado = intent.extras?.getString("sobrenome")
        val idadeRecuperada = intent.extras?.getString("idade")
        val celularRecuperado = intent.extras?.getString("celular")
        val uid = intent.extras!!.getInt("uid") // UID é um Int

        binding.editNome.setText(nomeRecuperado)
        binding.editSobrenome.setText(sobrenomeRecuperado)
        binding.editIdade.setText(idadeRecuperada)
        binding.editCelular.setText(celularRecuperado)


        binding.btAtualizar.setOnClickListener {
            // Captura os valores dos campos
            val nome = binding.editNome.text.toString()
            val sobrenome = binding.editSobrenome.text.toString()
            val idade = binding.editIdade.text.toString()
            val celular = binding.editCelular.text.toString()

            // Verifica se os campos estão preenchidos antes de iniciar a corrotina
            if (nome.isNotEmpty() && sobrenome.isNotEmpty() && idade.isNotEmpty() && celular.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch { // Inicia a corrotina no Dispatchers.IO
                    // Chama a função suspend atualizarContato dentro da corrotina
                    atualizarContato(uid, nome, sobrenome, idade, celular)

                    withContext(Dispatchers.Main) { // Volta para a thread principal para exibir o Toast
                        Toast.makeText(
                            this@AtualizarUsuarioActivity, // Contexto correto
                            "Atualizado com sucesso!!",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish() // Finaliza a Activity após a atualização
                    }
                }
            } else {
                Toast.makeText( // Exibe o Toast na thread principal (já estamos nela)
                    this@AtualizarUsuarioActivity,
                    "Preencher todos os campos!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // A função atualizarContato agora é suspend
    private suspend fun atualizarContato(uid: Int, nome: String, sobrenome: String, idade: String, celular: String){
        // usuarioDao já foi inicializado no onCreate, não precisa fazer de novo aqui
        usuarioDao.atualizar(uid, nome, sobrenome, idade, celular)
    }
}