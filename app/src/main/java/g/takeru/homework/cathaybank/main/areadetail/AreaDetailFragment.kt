package g.takeru.homework.cathaybank.main.areadetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import g.takeru.homework.cathaybank.databinding.FragmentAreaDetailBinding
import g.takeru.homework.cathaybank.datamodel.Area
import g.takeru.homework.cathaybank.main.MainActivity
import g.takeru.homework.cathaybank.main.MainRepositoryImpl
import g.takeru.homework.cathaybank.main.MainViewModel
import g.takeru.homework.cathaybank.main.MainViewModelFactory
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class AreaDetailFragment : Fragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentAreaDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel

    private val listAdapter: PlantListAdapter by lazy {
        PlantListAdapter(area) { _, plant -> gotoPlantDetailPage(plant._id) }
    }

    private var area: Area? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAreaDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup MVVM
        val repository = MainRepositoryImpl(Dispatchers.IO)
        viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory(
            requireActivity().application, repository)
        )[MainViewModel::class.java]
        observeData()

        // get args
        val areaNo = arguments?.getString("areaNo") ?: ""
        area = viewModel.getArea(areaNo)

        // UI
        setupRecyclerView()
        initRefreshLayout()
        setupTitle()

        // get data from api call
        area?.let { viewModel.getPlantList(area!!.e_name) }
    }

    private fun setupTitle() {
        (activity as MainActivity).supportActionBar?.title = area?.e_name
    }


    private fun observeData() {
        viewModel.plantList.observe(viewLifecycleOwner) {
            it?.let { listAdapter.submitData(it) }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            it?.let { displayLoading(it) }
        }
        viewModel.serverError.observe(viewLifecycleOwner) {
            showErrorEsg(it)
        }
        viewModel.connectError.observe(viewLifecycleOwner) {
            showErrorEsg(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setupRecyclerView() {
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = listAdapter
        }
    }

    private fun initRefreshLayout() {
        binding.refreshLayout.apply {
            setOnRefreshListener { viewModel.getPlantList(area!!.e_name) }
        }
    }

    private fun gotoPlantDetailPage(plantId: Int) {
        val action = AreaDetailFragmentDirections.actionSecondFragmentToThirdFragment(plantId)
        findNavController().navigate(action)
    }

    private fun displayLoading(show: Boolean) {
        binding.refreshLayout.isRefreshing = show
    }

    private fun showErrorEsg(msg: String) {
        Snackbar.make(requireActivity().findViewById(android.R.id.content),
            msg, Snackbar.LENGTH_LONG).show();
    }
}