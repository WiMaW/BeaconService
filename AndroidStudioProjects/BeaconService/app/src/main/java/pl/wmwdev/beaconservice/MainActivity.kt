package pl.wmwdev.beaconservice

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import pl.wmwdev.beaconservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            try {
                val actionList = RetrofitRepo.api.getElements()
                binding.recyclerView.adapter = ElementAdapter(actionList)
                Log.d("API", "Pobrano: ${actionList.size} elementów")
            } catch (e: Exception) {
                Log.e("API", "Bład: ${e.message}")
            }
        }
    }
}