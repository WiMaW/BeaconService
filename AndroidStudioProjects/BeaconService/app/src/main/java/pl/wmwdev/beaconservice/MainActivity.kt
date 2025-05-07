package pl.wmwdev.beaconservice

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
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
            lifecycleScope.launch {
                try {
                    val actionList = RetrofitRepo.api.getElements()
                    binding.recyclerView.adapter = ActionAdapter(actionList)
                    Log.d("API", "Pobrano: ${actionList.size} elementów")
                } catch (e: Exception) {
                    Log.e("API", "Bład: ${e.message}")
                }
            }
        }
    }
}