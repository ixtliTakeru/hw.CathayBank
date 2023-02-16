package g.takeru.homework.cathaybank.main.areadetail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import g.takeru.homework.cathaybank.databinding.ListitemAreaHeaderBinding
import g.takeru.homework.cathaybank.databinding.ListitemPlantBinding
import g.takeru.homework.cathaybank.datamodel.Area
import g.takeru.homework.cathaybank.datamodel.Plant
import g.takeru.homework.cathaybank.main.arealist.AdapterHolderClickListener
import g.takeru.homework.cathaybank.utils.toHttps

class PlantListAdapter (private val area: Area?,
                        private val holderListener: AdapterHolderClickListener<Plant>)
    : RecyclerView.Adapter<ViewHolder>() {

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_ITEM = 1
    }

    private var plantList = listOf<Plant>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(list: List<Plant>) {
        plantList = list
        notifyDataSetChanged()
    }

    class PlantHolder(val binding: ListitemPlantBinding) :
        ViewHolder(binding.root)

    class HeaderHolder(val binding: ListitemAreaHeaderBinding) :
        ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == TYPE_HEADER) {
            HeaderHolder(
                ListitemAreaHeaderBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        } else {
            PlantHolder(
                ListitemPlantBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 0) {
            holder as HeaderHolder
            holder.binding.apply {
                areaImg.load(area?.e_pic_url?.toHttps())
                areaDesc.text = area?.e_info
            }
        } else {
            val plant = plantList[position-1]
            holder as PlantHolder
            holder.binding.apply {
                plantImage.load(plant.F_Pic01_URL.toHttps())
                // Unable to parse F_Name_Ch(Plant api) for Plant class due to encoding issue
                plantName.text = plant.F_Name_Ch
                plantDesc.text = plant.F_AlsoKnown
            }
            holder.itemView.setOnClickListener { holderListener.onItemClicked(position, plant) }
        }
    }

    override fun getItemCount(): Int =
        plantList.size + 1

    override fun getItemViewType(position: Int): Int =
        if (position == 0) TYPE_HEADER else TYPE_ITEM

}