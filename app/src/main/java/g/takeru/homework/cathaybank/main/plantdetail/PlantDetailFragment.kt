package g.takeru.homework.cathaybank.main.plantdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import g.takeru.homework.cathaybank.R
import g.takeru.homework.cathaybank.databinding.FragmentPlantDetailBinding
import g.takeru.homework.cathaybank.datamodel.Plant
import g.takeru.homework.cathaybank.main.MainActivity
import g.takeru.homework.cathaybank.main.MainRepositoryImpl
import g.takeru.homework.cathaybank.main.MainViewModel
import g.takeru.homework.cathaybank.main.MainViewModelFactory
import g.takeru.homework.cathaybank.utils.toHttps
import kotlinx.coroutines.Dispatchers

class PlantDetailFragment : Fragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentPlantDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlantDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup MVVM
        val repository = MainRepositoryImpl(Dispatchers.IO)
        viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory(
            requireActivity().application, repository)
        )[MainViewModel::class.java]

        // get args
        val plantId = arguments?.getInt("plantId") ?: 0
        val plant = viewModel.getPlain(plantId)

        // UI
        setupTitle()
        binding.apply {
            plant?.let {
                plantImg.load(plant.F_Pic01_URL.toHttps())
                plantDesc.text = getDesc(plant)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupTitle() {
        (activity as MainActivity).supportActionBar?.title = getString(R.string.plant_data)
    }

    private fun getDesc(plant: Plant): String {
        val builder = StringBuilder()
        builder
            .appendLine(plant.F_AlsoKnown)
            .appendLine("")
            .appendLine(plant.F_Family)
            .appendLine("")
            .appendLine(plant.F_Genus)
            .appendLine("")
            .appendLine(plant.F_Feature)
            .appendLine("")
            .appendLine(plant.F_Brief)

            return builder.toString()
    }
}