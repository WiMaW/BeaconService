package pl.wmwdev.beaconservice

import android.media.Image
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore.Audio.Media
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.Orientation
import com.bumptech.glide.Glide
import pl.wmwdev.beaconservice.data.Block
import pl.wmwdev.beaconservice.data.Block1
import pl.wmwdev.beaconservice.data.Block2
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
        binding.audioButton.visibility = View.GONE

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
//        when(block) {
//            is Block1 -> blockList.add(block)
//            is Block2 -> {
//                blockList.add(block)
//                blockList.add(block)
//            }
//        }
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
        }
    }
}