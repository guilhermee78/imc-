package com.example.calculadoradeimc.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calculadoradeimc.model.AppDatabase
import com.example.calculadoradeimc.model.AtualizarUsuario
import com.example.calculadoradeimc.dao.UsuarioDao
import com.example.calculadoradeimc.databinding.ContatoItemAgBinding
import com.example.calculadoradeimc.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContatoAdapter(
    private val context: Context,
    private var listaUsuarios: MutableList<Usuario> // Mude para 'var' se for modificar diretamente
) : RecyclerView.Adapter<ContatoAdapter.ContatoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContatoViewHolder {
        val itemLista = ContatoItemAgBinding.inflate(LayoutInflater.from(context), parent, false)
        return ContatoViewHolder(itemLista)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ContatoViewHolder, position: Int) {
        val usuario = listaUsuarios[position]
        holder.txtNome.text = usuario.nome
        holder.txtSobrenome.text = usuario.sobrenome
        holder.txtIdade.text = usuario.idade
        holder.txtcelular.text = usuario.celular

        holder.btAtualizar.setOnClickListener {
            val intent = Intent(context, AtualizarUsuario::class.java)
            intent.putExtra("nome", usuario.nome)
            intent.putExtra("sobrenome", usuario.sobrenome)
            intent.putExtra("idade", usuario.idade)
            intent.putExtra("celular", usuario.celular)
            intent.putExtra("uid", usuario.uid)
            context.startActivity(intent)
        }

        holder.btDeletar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val usuarioDao: UsuarioDao = AppDatabase.getInstance(context).usuarioDao()
                usuarioDao.deletar(usuario.uid)
                listaUsuarios.removeAt(position)

                withContext(Dispatchers.Main) {
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun getItemCount() = listaUsuarios.size

    /**
     * Atualiza a lista de usuários do adapter com uma nova lista e notifica o RecyclerView.
     * @param newList A nova lista de usuários a ser exibida.
     */
    @SuppressLint("NotifyDataSetChanged") // Suprime o aviso para notifyDataSetChanged
    fun updateList(newList: MutableList<Usuario>) {
        this.listaUsuarios.clear() // Limpa a lista existente
        this.listaUsuarios.addAll(newList) // Adiciona todos os itens da nova lista
        // notifyDataSetChanged() será chamado pelo LiveData observer na Activity,
        // mas é bom ter aqui caso você queira chamar updateList de outros lugares.
        // Se a chamada do notifyDataSetChanged na Activity já for suficiente, você pode remover esta linha.
        notifyDataSetChanged()
    }

    inner class ContatoViewHolder(binding: ContatoItemAgBinding) : RecyclerView.ViewHolder(binding.root) {
        val txtNome = binding.txtNome
        val txtSobrenome = binding.txtSobrenome
        val txtIdade = binding.txtIdade
        val txtcelular = binding.txtTelefone
        val btAtualizar = binding.btAtualizar
        val btDeletar = binding.btdeletar
    }
}