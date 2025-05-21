package com.example.calculadoradeimc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UsuarioIMCAdapter(
    private val usuarios: List<UsuarioIMC>,
    private val onEditarClick: (Int) -> Unit,
    private val onExcluirClick: (Int) -> Unit
) : RecyclerView.Adapter<UsuarioIMCAdapter.UsuarioIMCViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioIMCViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_usuario_imc, parent, false)
        return UsuarioIMCViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioIMCViewHolder, position: Int) {
        val usuario = usuarios[position]
        holder.bind(usuario)

        holder.btnEditar.setOnClickListener {
            onEditarClick(position)
        }

        holder.btnExcluir.setOnClickListener {
            onExcluirClick(position)
        }
    }

    override fun getItemCount(): Int = usuarios.size

    class UsuarioIMCViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtInfoUsuario: TextView = itemView.findViewById(R.id.txtInfoUsuario)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditar)
        val btnExcluir: Button = itemView.findViewById(R.id.btnExcluir)

        fun bind(usuario: UsuarioIMC) {
            txtInfoUsuario.text = """
                ID: ${usuario.id.take(8)}...
                Peso: ${usuario.peso} kg
                Altura: ${usuario.altura} m
                IMC: ${usuario.imc}
            """.trimIndent()
        }
    }
}
