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
import pl.wmwdev.beaconservice.data.Action
import pl.wmwdev.beaconservice.data.Block
import pl.wmwdev.beaconservice.data.Block1
import pl.wmwdev.beaconservice.data.Block2
import pl.wmwdev.beaconservice.data.Block3
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
            showAddItemDialog(block, this, adapter, extractOptionFromElement(element))
        }

        val layoutManager = GridLayoutManager(this, 2) // 2 kolumny

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    BlockAdapter.TYPE_BLOCK1 -> 2 // zajmuje cały rząd
                    BlockAdapter.TYPE_BLOCK2 -> 1 // połowa rzędu
                    else -> 2
                }
            }
        }
        binding.blocksRecyclerView.layoutManager = layoutManager
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
            adapter.addBlock(Block2())
        }

        binding.addBlock3Button.setOnClickListener {
            adapter.addBlock(Block3())
        }
    }


}

fun displayElementItems(binding: ActivityEditBinding, element: Action?) {
    if (!element?.tekst.isNullOrBlank()) binding.textTextView.text = "Tekst"
    if (!element?.image.isNullOrBlank()) binding.imageTextView.text = "Zdjęcie"
    if (!element?.video.isNullOrBlank()) binding.videoTextView.text = "Film"
    if (!element?.audio.isNullOrBlank()) binding.audioTextView.text = "Dźwięk"
}

private fun showAddItemDialog(block: Block, context: Context, adapter: BlockAdapter, options: List<String>) {
    val editText = EditText(context).apply {
        setText(block.content)
    }

    AlertDialog.Builder(context)
        .setTitle("Wprowadź treść lub URL")
        .setItems(options.toTypedArray()) { _, index ->
            block.content = options[index].substringAfter(" ")
            adapter.notifyDataSetChanged()
        }
        .setPositiveButton("Zapisz") { _, _ ->
            block.content = editText.text.toString()
            adapter.notifyDataSetChanged()
        }
        .setNegativeButton("Anuluj", null)
        .show()
}

fun extractOptionFromElement(element: Action?) : List<String> {
    if(element == null) return emptyList()

    val options: List<String> = listOf(
        element.tekst,
        element.image,
        element.video,
        element.audio
    ).filter { it.isNotBlank() }

    return options
}
