package pl.wmwdev.beaconservice

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import pl.wmwdev.beaconservice.data.remote.ApiResponse
import pl.wmwdev.beaconservice.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: BeaconServiceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.addActionButton.setOnClickListener {
            viewModel.loadElements()
            showElementsDialog(this@MainActivity)
        }
    }

    fun showElementsDialog(context: Context) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_elements, null)
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.recycler_view)
        val progressBar = dialogView.findViewById<ProgressBar>(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(context)

        AlertDialog.Builder(context)
            .setTitle("Elementy z Api")
            .setView(dialogView)
            .show()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.elements.collect { response ->
                    when(response) {
                        ApiResponse.Loading -> {
                            progressBar.visibility = View.VISIBLE
                        }

                        is ApiResponse.Success -> {
                            recyclerView.adapter = ActionAdapter(response.data)
                            progressBar.visibility = View.GONE
                        }

                        is ApiResponse.Error -> {
                            progressBar.visibility = View.GONE
                            Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                            Log.e("API", response.message)
                        }
                    }
                }
            }
        }
    }
}