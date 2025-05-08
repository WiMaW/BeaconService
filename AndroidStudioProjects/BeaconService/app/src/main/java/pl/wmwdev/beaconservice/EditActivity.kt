package pl.wmwdev.beaconservice

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.set
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pl.wmwdev.beaconservice.data.Action
import pl.wmwdev.beaconservice.databinding.ActivityEditBinding

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val element = intent.getParcelableExtra("element", Action::class.java)

        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        displayElementItems(binding, element)

    }
}

fun displayElementItems(binding: ActivityEditBinding, element: Action?) {
    binding.textTextView.text = element?.text
    binding.imageTextView.text = element?.image
    binding.videoTextView.text = element?.video
    binding.audioTextView.text = element?.audio
}