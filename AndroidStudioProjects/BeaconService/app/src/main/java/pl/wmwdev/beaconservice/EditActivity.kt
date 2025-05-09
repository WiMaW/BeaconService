package pl.wmwdev.beaconservice

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
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

        binding.cancelButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.block1preview.setOnClickListener {

        }
    }
}

fun displayElementItems(binding: ActivityEditBinding, element: Action?) {
    binding.textTextView.text = element?.tekst
    binding.imageTextView.text = element?.image
    binding.videoTextView.text = element?.video
    binding.audioTextView.text = element?.audio
}