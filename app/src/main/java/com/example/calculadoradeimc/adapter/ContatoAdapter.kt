// app/src/main/java/com/example/calculadoradeimc/adapter/ContatoAdapter.kt
package com.example.calculadoradeimc.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calculadoradeimc.AtualizarUsuarioActivity // CORREÇÃO DO IMPORT AQUI
import com.example.calculadoradeimc.dao.UsuarioDao
import com.example.calculadoradeimc.databinding.ContatoItemAgBinding // Seu Binding do item da lista
import com.example.calculadoradeimc.model.AppDatabase
import com.example.calculadoradeimc.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContatoAdapter(
    private val context: Context,
    private var listaUsuarios: MutableList<Usuario> // É 'var' porque será reatribuída no updateList
) : RecyclerView.Adapter<ContatoAdapter.ContatoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContatoViewHolder {
        val itemLista = ContatoItemAgBinding.inflate(LayoutInflater.from(context), parent, false)
        return ContatoViewHolder(itemLista)
    }

    @SuppressLint("NotifyDataSetChanged") // Manter por enquanto, otimizar com DiffUtil se a lista for muito grande
    override fun onBindViewHolder(holder: ContatoViewHolder, position: Int) {
        val usuario = listaUsuarios[position]
        holder.txtNome.text = "Nome: ${usuario.nome}" // Adicione "Nome: " para melhor exibição
        holder.txtSobrenome.text = "Sobrenome: ${usuario.sobrenome}"
        holder.txtIdade.text = "Idade: ${usuario.idade}"
        holder.txtCelular.text = "Celular: ${usuario.celular}"

        holder.btAtualizar.setOnClickListener {
            val intent = Intent(context, AtualizarUsuarioActivity::class.java)
            // Passa todos os dados do usuário para a tela de atualização
            intent.putExtra("uid", usuario.uid) // UID é importante para saber qual usuário atualizar
            intent.putExtra("nome", usuario.nome)
            intent.putExtra("sobrenome", usuario.sobrenome)
            intent.putExtra("idade", usuario.idade)
            intent.putExtra("celular", usuario.celular)
            context.startActivity(intent)
        }

        holder.btDeletar.setOnClickListener {
            // Lógica de deleção
            val usuarioDao: UsuarioDao = AppDatabase.getInstance(context).usuarioDao()
            CoroutineScope(Dispatchers.IO).launch {
                usuarioDao.deletar(usuario.uid) // Deleção no banco de dados

                withContext(Dispatchers.Main) {
                    // Remove o item da lista local e notifica o adapter
                    // IMPORTANTE: Isso assume que o `getContatos()` na `ListaUsuariosActivity` será chamado
                    // no `onResume()` para re-sincronizar a lista com o banco.
                    // Caso contrário, você pode precisar de um callback para notificar a Activity.
                    listaUsuarios.removeAt(position)
                    notifyItemRemoved(position) // Notifica a remoção de um item específico
                    // O notifyItemRangeChanged(position, listaUsuarios.size) pode ser útil se a remoção
                    // causar um reordenamento visual de vários itens que subiram de posição.
                    // Por simplicidade, notifyDataSetChanged também funcionaria aqui, mas é menos eficiente.
                    // Se você não quiser refazer o fetch da Activity no onResume:
                    // notifyItemRangeChanged(position, listaUsuarios.size)
                }
            }
        }
    }

    override fun getItemCount() = listaUsuarios.size

    /**
     * Atualiza a lista de usuários do adapter com uma nova lista e notifica o RecyclerView.
     * @param newList A nova lista de usuários a ser exibida.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: MutableList<Usuario>) {
        this.listaUsuarios.clear()
        this.listaUsuarios.addAll(newList)
        notifyDataSetChanged() // Notifica que os dados mudaram para redesenhar a lista
    }

    // ViewHolder para os itens da lista
    inner class ContatoViewHolder(binding: ContatoItemAgBinding) : RecyclerView.ViewHolder(binding.root) {
        val txtNome = binding.txtNome
        val txtSobrenome = binding.txtSobrenome
        val txtIdade = binding.txtIdade
        val txtCelular = binding.txtTelefone // ID do layout é txtTelefone, nome da variável txtCelular
        val btAtualizar = binding.btAtualizar
        val btDeletar = binding.btdeletar
    }
}