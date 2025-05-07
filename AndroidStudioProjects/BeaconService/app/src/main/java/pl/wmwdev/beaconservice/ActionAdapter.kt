package pl.wmwdev.beaconservice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.wmwdev.beaconservice.data.Action
import pl.wmwdev.beaconservice.databinding.ActionItemLayoutBinding

class ActionAdapter(private val items: List<Action>) : RecyclerView.Adapter<ActionAdapter.ElementViewHolder>() {


   inner class ElementViewHolder(val binding: ActionItemLayoutBinding) :
       RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElementViewHolder {
        val binding = ActionItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ElementViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size


    override fun onBindViewHolder(holder: ElementViewHolder, position: Int) {
        val item = items[position]
        holder.binding.nameTextView.text = item.name
    }
}