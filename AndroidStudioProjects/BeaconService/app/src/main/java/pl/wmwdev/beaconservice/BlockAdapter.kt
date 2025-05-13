package pl.wmwdev.beaconservice

import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pl.wmwdev.beaconservice.data.Block
import pl.wmwdev.beaconservice.data.Block1
import pl.wmwdev.beaconservice.data.Block2
import pl.wmwdev.beaconservice.data.Block3
import pl.wmwdev.beaconservice.databinding.BlockItemLayoutBinding

class BlockAdapter(
    private val blockList: MutableList<Block>,
    private val onClick: (Block) -> Unit
) : RecyclerView.Adapter<BlockAdapter.BlockViewHolder>() {

    companion object {
        const val TYPE_BLOCK1 = 1
        const val TYPE_BLOCK2 = 2
    }

    inner class BlockViewHolder(val binding: BlockItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockViewHolder {
        val binding = BlockItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BlockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BlockViewHolder, position: Int) {
        val block = blockList[position]
        val binding = holder.binding

        binding.root.layoutParams = binding.root.layoutParams.apply {
            width = block.width
            height = block.height
        }

        binding.root.setBackgroundColor(block.color)

        binding.textView.visibility = View.GONE
        binding.imageView.visibility = View.GONE
        binding.videoView.visibility = View.GONE
        binding.audioControlButton.visibility = View.GONE

        if (block.content.isBlank()) {
            binding.textView.visibility = View.VISIBLE
            binding.textView.text = "Kliknij, aby dodać zawartość"
        } else {
            when {
                isImage(block.content) -> {
                    binding.imageView.visibility = View.VISIBLE
                    Glide.with(binding.imageView.context)
                        .load(block.content)
                        .into(binding.imageView)
                }

                isAudio(block.content) -> {
                    binding.audioControlButton.visibility = View.VISIBLE
                    val mediaPlayer = MediaPlayer()
                    mediaPlayer.setDataSource(block.content)
                    mediaPlayer.prepare()

                    var isPlaying = false

                    binding.audioControlButton.text = "▶"

                    binding.audioControlButton.setOnClickListener {
                        if (isPlaying) {
                            mediaPlayer.pause()
                            binding.audioControlButton.text = "▶"
                        } else {
                            mediaPlayer.start()
                            binding.audioControlButton.text = "⏸"
                        }
                        isPlaying = !isPlaying
                    }
                }

                isVideo(block.content) -> {
                    binding.videoView.visibility = View.VISIBLE
                    binding.videoView.setVideoURI(Uri.parse(block.content))

                    binding.videoView.setOnPreparedListener { mediaPlayer ->
                        val videoWidth = mediaPlayer.videoWidth
                        val videoHeight = mediaPlayer.videoHeight

                        val layoutParams = binding.videoView.layoutParams
                        val containerWidth = block.width

                        val scale = videoHeight.toFloat() / videoWidth.toFloat()
                        layoutParams.width = containerWidth
                        layoutParams.height = (containerWidth * scale).toInt()
                        binding.videoView.layoutParams = layoutParams

                        // Dodaj MediaController
                        val mediaController = MediaController(binding.videoView.context)
                        mediaController.setAnchorView(binding.videoView)
                        binding.videoView.setMediaController(mediaController)

                        mediaController.show() // pokazuje od razu (opcjonalne)
                        binding.videoView.start()
                    }
                }


                else -> {
                    binding.textView.visibility = View.VISIBLE
                    binding.textView.text = block.content
                }
            }
        }
        binding.root.setOnClickListener {
            onClick(block)
        }
    }

    fun addBlock(block: Block) {
        blockList.add(block)
        notifyItemInserted(blockList.size - 1)
    }

    private fun isImage(content: String): Boolean = content.matches(Regex("https?://.*\\.(jpg|jpeg|png|webp)", RegexOption.IGNORE_CASE))

    private fun isVideo(content: String): Boolean =
        content.matches(Regex(""".*\.mp4(\?.*)?$""", RegexOption.IGNORE_CASE))

    private fun isAudio(content: String): Boolean =
        content.matches(Regex(""".*\.mp3(\?.*)?$""", RegexOption.IGNORE_CASE))

    override fun getItemCount(): Int = blockList.size

    override fun getItemViewType(position: Int): Int {
        return when (blockList[position]) {
            is Block1 -> TYPE_BLOCK1
            is Block2 -> TYPE_BLOCK2
            is Block3 -> TYPE_BLOCK1
        }
    }
}