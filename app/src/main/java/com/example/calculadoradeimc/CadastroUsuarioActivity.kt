// app/src/main/java/com/example/calculadoradeimc/CadastroUsuarioActivity.kt

package com.example.calculadoradeimc

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.calculadoradeimc.dao.UsuarioDao
import com.example.calculadoradeimc.databinding.ActivityCadastroUsuarioFormBinding // Certifique-se de que é o binding do formulário
import com.example.calculadoradeimc.model.AppDatabase
import com.example.calculadoradeimc.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext // Adicionado para atualizar a UI

class CadastroUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroUsuarioFormBinding // Binding do layout do formulário
    private lateinit var usuarioDao: UsuarioDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCadastroUsuarioFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usuarioDao = AppDatabase.getInstance(this).usuarioDao()

        binding.buttonSalvarCadastro.setOnClickListener { // ID do botão de salvar
            val nome = binding.editTextNome.text.toString()
            val sobrenome = binding.editTextSobrenome.text.toString()
            val idade = binding.editTextIdade.text.toString() // Agora é String, como no modelo Usuario
            val celular = binding.editTextCelular.text.toString() // Agora é String, como no modelo Usuario

            if (nome.isNotEmpty() && sobrenome.isNotEmpty() && idade.isNotEmpty() && celular.isNotEmpty()) {
                val novoUsuario = Usuario(nome, sobrenome, idade, celular) // Agora passamos todos os 4 parâmetros String
                // O UID é auto-gerado, então não precisamos passá-lo ao criar o objeto para inserção

                CoroutineScope(Dispatchers.IO).launch {
                    usuarioDao.inserir(novoUsuario) // Chamando o método 'inserir' do DAO com um único Usuario
                    withContext(Dispatchers.Main) { // Voltar para a thread principal para Toast e finish()
                        Toast.makeText(this@CadastroUsuarioActivity, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                        finish() // Fecha esta Activity e retorna para a anterior (HomeActivity)
                    }
                }
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }
        // REMOVA QUALQUER OUTRO setOnClickListener AQUI QUE ESTEJA TENTANDO INICIAR CadastroUsuarios::class.java
        // E REMOVA TAMBÉM QUALQUER LÓGICA DE RecyclerView OU DAO.GET() DESSA ACTIVITY, POIS ELAS DEVEM ESTAR NA LISTAUSUARIOSACTIVITY
    }
    // REMOVA onResume(), getContatos() e qualquer outra lógica de listagem daqui
}