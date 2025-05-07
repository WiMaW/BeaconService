package pl.wmwdev.beaconservice

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.elements.collect { response ->
                        when(response) {
                            ApiResponse.Loading -> binding.progressBar.visibility = View.VISIBLE

                            is ApiResponse.Success -> {
                                binding.progressBar.visibility = View.GONE
                                binding.recyclerView.adapter = ActionAdapter(response.data)
                            }

                            is ApiResponse.Error -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(this@MainActivity, response.message, Toast.LENGTH_SHORT).show()
                            }
                        }

                    }
                }
            }
        }
    }
}