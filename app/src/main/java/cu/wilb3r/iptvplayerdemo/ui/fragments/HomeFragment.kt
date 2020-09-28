package cu.wilb3r.iptvplayerdemo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import cu.wilb3r.iptvplayerdemo.R
import cu.wilb3r.iptvplayerdemo.data.M3UItem
import cu.wilb3r.iptvplayerdemo.databinding.FragmentHomeBinding
import cu.wilb3r.iptvplayerdemo.ui.PlayerActivity
import cu.wilb3r.iptvplayerdemo.ui.adapters.ItemAdapter
import cu.wilb3r.iptvplayerdemo.ui.vm.HomeViewModel
import cu.wilb3r.iptvplayerdemo.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment @Inject constructor(
    val itemAdapter: ItemAdapter
) : Fragment() {
    private val vm: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        initRecyclerView()
        initViewModel()
        itemAdapter.setOnItemClickListener {
            onItemTap(it)
        }
        download("https://pastebin.com/raw/89S0bBCp")
        return binding.root
    }

    private fun initRecyclerView() {
        binding.incRecycler.recyclerview.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            binding.incRecycler.recyclerview.adapter = itemAdapter
        }
    }

    private fun initViewModel() {
        vm.playListLiveData.observe(viewLifecycleOwner, Observer {
            itemAdapter.items = it.m3UItems
            itemAdapter.notifyDataSetChanged()
            binding.progressBar.invisible()

        })
        vm.errorLiveData.observe(viewLifecycleOwner, Observer { it ->
            it.getContentIfNotHandled()?.let {
                Coroutines.main {
                    context?.also { result ->
                        snack(result.getString(R.string.unable_fetch_data), binding.root)
                    }
                    binding.progressBar.invisible()
                }
            }
        })

    }

    private fun download(url: String) = Coroutines.main {
        vm.download(url)
        binding.progressBar.visible()
    }


    fun onItemTap(item: M3UItem) {
        PlayerActivity.newIntent(requireContext(), item).also {
            startActivity(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}