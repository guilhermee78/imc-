package com.example.calculadoradeimc

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer // Import Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calculadoradeimc.adapter.ContatoAdapter
import com.example.calculadoradeimc.dao.UsuarioDao
import com.example.calculadoradeimc.databinding.ActivityMainBinding
import com.example.calculadoradeimc.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Assuming AppDatabase and UsuarioDao are correctly defined elsewhere in your project.
// For example:
// import com.example.calculadoradeimc.database.AppDatabase // Adjust this import if needed
// import com.example.calculadoradeimc.dao.UsuarioDao // Adjust this import if needed

// Assuming CadastroActivity is the name of your new activity for user registration.
// You will need to create a new Kotlin file named CadastroActivity.kt and define the class.
// For example:
// class CadastroActivity : AppCompatActivity() { /* ... */ }


class CadastroUsuarioActivity : AppCompatActivity() {

    // Using lateinit for binding is common practice with View Binding
    private lateinit var binding: ActivityMainBinding
    private lateinit var usuarioDao: UsuarioDao
    private lateinit var contatoAdapter: ContatoAdapter

    // MutableLiveData to hold the list of users, making it observable
    private val _listaUsuario = MutableLiveData<MutableList<Usuario>>()

    @SuppressLint("NotifyDataSetChanged") // Suppress lint warning for notifyDataSetChanged
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the RecyclerView and adapter once
        // It's good practice to initialize the adapter with an empty list first
        // The actual data will be set when the LiveData observes changes
        val recyclerViewContato = binding.recyclerUsuarios
        recyclerViewContato.layoutManager = LinearLayoutManager(this)
        recyclerViewContato.setHasFixedSize(true)
        contatoAdapter = ContatoAdapter(this, mutableListOf()) // Initialize with an empty list
        recyclerViewContato.adapter = contatoAdapter

        // Observe changes in _listaUsuario LiveData
        // This observer will update the RecyclerView whenever _listaUsuario changes
        _listaUsuario.observe(this, Observer { listaUsuario ->
            // Update the adapter's data and notify it of the changes
            contatoAdapter.updateList(listaUsuario) // Assuming you add an updateList method to your adapter
            contatoAdapter.notifyDataSetChanged()
        })

        // Fetch contacts when the activity is created
        // This will trigger the LiveData observer to update the UI
        CoroutineScope(Dispatchers.IO).launch {
            getContatos()
        }

        // Set up the click listener for the register button
        binding.btCadastrar.setOnClickListener {
            // Ensure 'CadastroActivity' is the correct name for your registration activity
            val navegarTelaCadastro = Intent(this, CadastroUsuarioActivity::class.java)
            startActivity(navegarTelaCadastro)
        }
    }

    // onResume is called when the activity comes to the foreground
    // We fetch contacts again here to ensure the list is up-to-date
    // (e.g., after returning from the CadastroActivity)
    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.IO).launch {
            getContatos()
        }
    }

    /**
     * Fetches the list of users from the database and posts it to _listaUsuario LiveData.
     * This method runs on an IO dispatcher (background thread).
     */
    private fun getContatos() {
        // Get an instance of the database and the DAO
        usuarioDao = AppDatabase.getInstance(this).usuarioDao()
        // Retrieve the list of users
        val listaUsuarios: MutableList<Usuario> = usuarioDao.get()
        // Post the value to LiveData, which will then trigger the observer on the main thread
        _listaUsuario.postValue(listaUsuarios)
    }
}

// You might need to add an updateList method to your ContatoAdapter:
/*
// In ContatoAdapter.kt
class ContatoAdapter(private val context: Context, private var listaUsuarios: MutableList<Usuario>) :
    RecyclerView.Adapter<ContatoAdapter.ContatoViewHolder>() {

    // ... (your existing adapter code) ...

    fun updateList(newList: MutableList<Usuario>) {
        this.listaUsuarios = newList
        // notifyDataSetChanged() will be called by the LiveData observer
    }

    // ... (rest of your adapter methods like onCreateViewHolder, onBindViewHolder, getItemCount) ...
}
*/