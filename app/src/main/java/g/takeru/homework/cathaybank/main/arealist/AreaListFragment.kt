package g.takeru.homework.cathaybank.main.arealist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import g.takeru.homework.cathaybank.databinding.FragmentAreaListBinding
import g.takeru.homework.cathaybank.main.MainRepositoryImpl
import g.takeru.homework.cathaybank.main.MainViewModel
import g.takeru.homework.cathaybank.main.MainViewModelFactory
import kotlinx.coroutines.Dispatchers

class AreaListFragment : Fragment() {

    private var _binding: FragmentAreaListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var viewModel: MainViewModel

    private val listAdapter: AreaListAdapter by lazy {
        AreaListAdapter { _, area -> gotoAreaDetailPage(area.e_no) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAreaListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setuo MVVM
        val repository = MainRepositoryImpl(Dispatchers.IO)
        viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory(
            requireActivity().application, repository)
        )[MainViewModel::class.java]
        observeData()

        // ui
        initRefreshLayout()
        setupRecyclerView()

        // get data from api call
        viewModel.getAreaList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeData() {
        viewModel.areaList.observe(viewLifecycleOwner) {
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

    private fun initRefreshLayout() {
        binding.refreshLayout.apply {
            setOnRefreshListener { viewModel.getAreaList() }
        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = listAdapter
                binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean = false
                    override fun onQueryTextChange(newText: String?): Boolean {
                        (adapter as Filterable).filter.filter(newText)
                        return false
                    }
                })
            }
        }
    }

    private fun gotoAreaDetailPage(areaNo: String) {
        val action = AreaListFragmentDirections.actionFirstFragmentToSecondFragment(areaNo)
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