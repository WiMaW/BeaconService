package pl.wmwdev.beaconservice

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import pl.wmwdev.beaconservice.data.Action
import pl.wmwdev.beaconservice.data.Block
import pl.wmwdev.beaconservice.data.Block1
import pl.wmwdev.beaconservice.data.Block2
import pl.wmwdev.beaconservice.databinding.ActivityEditBinding

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private val blocks = mutableListOf<Block>()
    private lateinit var adapter: BlockAdapter

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val element = intent.getParcelableExtra("element", Action::class.java)

        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        displayElementItems(binding, element)

        //recyclerView
        adapter = BlockAdapter(blocks) { block ->
            showAddItemDialog(block, this, adapter, element)
        }

        binding.blocksRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.blocksRecyclerView.adapter = adapter


        //Cancel and save buttons
        binding.cancelButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.addBlock1Button.setOnClickListener {
            adapter.addBlock(Block1())
        }


        binding.addBlock2Button.setOnClickListener {
            adapter.addBlock(Block2())
        }
    }


}

fun displayElementItems(binding: ActivityEditBinding, element: Action?) {
    binding.textTextView.text = element?.tekst
    binding.imageTextView.text = element?.image
    binding.videoTextView.text = element?.video
    binding.audioTextView.text = element?.audio
}

private fun showAddItemDialog(block: Block, context: Context, adapter: BlockAdapter, element: Action?) {
    val editText = EditText(context).apply {
        setText(block.content)
    }

    AlertDialog.Builder(context)
        .setTitle("Wprowadź treść lub URL")

        .setView(editText)
        .setPositiveButton("Zapisz") { _, _ ->
            block.content = editText.text.toString()
            adapter.notifyDataSetChanged()
        }
        .setNegativeButton("Anuluj", null)
        .show()
}
