// app/src/main/java/com.example.calculadoradeimc/dao/UsuarioDao.kt

package com.example.calculadoradeimc.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.calculadoradeimc.model.Usuario

@Dao
interface UsuarioDao {

    @Insert // Mantenha a anotação @Insert
    suspend fun inserir(usuario: Usuario) // Corrigido para receber UM Usuario e ser suspend

    @Query("SELECT * FROM tabela_usuarios ORDER BY nome ASC")
    suspend fun get(): MutableList<Usuario> // Boas práticas: torne o 'get' suspend também para operações de IO

    @Query("UPDATE tabela_usuarios SET nome = :novoNome, sobrenome = :novoSobrenome, idade = :novaIdade, celular = :novoCelular " +
            "WHERE UID = :id")
    suspend fun atualizar( // Torne o 'atualizar' suspend
        id: Int,
        novoNome: String,
        novoSobrenome: String,
        novaIdade: String,
        novoCelular: String
    )
    @Query("DELETE FROM tabela_usuarios WHERE uid = :id")
    suspend fun deletar(id: Int) // Torne o 'deletar' suspend
}