package g.takeru.homework.cathaybank.main.arealist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import coil.load
import g.takeru.homework.cathaybank.databinding.ListitemAreaBinding
import g.takeru.homework.cathaybank.datamodel.Area
import g.takeru.homework.cathaybank.utils.toHttps

fun interface AdapterHolderClickListener<T> {
    fun onItemClicked(position: Int, data: T)
}

class AreaListAdapter (private val holderListener: AdapterHolderClickListener<Area>) :
    RecyclerView.Adapter<AreaListAdapter.AreaHolder>(), Filterable {

    private var areaList = listOf<Area>()
    private var filterAreaList = listOf<Area>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(list: List<Area>) {
        areaList = list
        filterAreaList = list
        notifyDataSetChanged()
    }

    class AreaHolder(val binding: ListitemAreaBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaHolder =
        AreaHolder(
            ListitemAreaBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: AreaHolder, position: Int) {
        val area = filterAreaList[position]
        holder.binding.apply {
            areaImage.load(area.e_pic_url.toHttps())
            areaName.text = area.e_name
            areaDesc.text = area.e_info
            areaHoliday.text = area.e_memo
        }
        holder.itemView.setOnClickListener { holderListener.onItemClicked(position, area) }
    }

    override fun getItemCount(): Int = filterAreaList.size

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            return FilterResults().apply {
                this.values = areaList.filter { it.e_name.contains(constraint ?: "", ignoreCase = true) }
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filterAreaList = listOf<Area>()
            filterAreaList = results?.values as? List<Area> ?: emptyList()
            notifyDataSetChanged()
        }
    }
}