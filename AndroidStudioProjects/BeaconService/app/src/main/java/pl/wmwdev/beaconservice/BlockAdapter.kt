package pl.wmwdev.beaconservice

import android.media.Image
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore.Audio.Media
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pl.wmwdev.beaconservice.data.Block
import pl.wmwdev.beaconservice.databinding.BlockItemLayoutBinding

class BlockAdapter(
    private val blockList: MutableList<Block>,
    private val onClick: (Block) -> Unit
) : RecyclerView.Adapter<BlockAdapter.BlockViewHolder>() {

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
                    binding.audioButton.visibility = View.VISIBLE
                    binding.audioButton.setOnClickListener {
                        val mediaPlayer = MediaPlayer().apply {
                            setDataSource(block.content)
                            prepare()
                            start()
                        }
                    }
                }

                isVideo(block.content) -> {
                    binding.videoView.visibility = View.VISIBLE
                    binding.videoView.setVideoURI(Uri.parse(block.content))
                    binding.videoView.setOnPreparedListener { it.isLooping = true }
                    binding.videoView.start()
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

    private fun isImage(content: String): Boolean = content.endsWith(".jpg", true) || content.endsWith(".png", true)

    private fun isAudio(content: String): Boolean = content.endsWith(".mp3", true)

    private fun isVideo(content: String): Boolean = content.endsWith("mp4", true )

    override fun getItemCount(): Int = blockList.size
}