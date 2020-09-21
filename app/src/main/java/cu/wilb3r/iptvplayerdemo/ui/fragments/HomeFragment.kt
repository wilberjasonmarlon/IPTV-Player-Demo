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
import cu.wilb3r.iptvplayerdemo.ui.adapters.M3UItemAdapter
import cu.wilb3r.iptvplayerdemo.ui.vm.HomeViewModel
import cu.wilb3r.iptvplayerdemo.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment(), M3UItemAdapter.AdapterListener {
    private val vm: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var items = ArrayList<M3UItem>()
    private var adapter: M3UItemAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        initRecyclerView()
        initViewModel()
        download("https://pastebin.com/raw/ZzGTySZE")
        return view
    }

    private fun initRecyclerView() {
        adapter = M3UItemAdapter(items, requireActivity(), this)
        // binding.incRecycler.recyclerview.layoutManager = LinearLayoutManager(requireActivity())
        binding.incRecycler.recyclerview.layoutManager = GridLayoutManager(requireActivity(), 3)
        binding.incRecycler.recyclerview.adapter = adapter

    }

    private fun initViewModel() {
        vm.playListLiveData.observe(viewLifecycleOwner, Observer {

            items.clear()
            items.addAll(it.m3UItems)
            adapter!!.notifyDataSetChanged()
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


    override fun onItemTap(position: Int) {
        println(items[position].mStreamURL)
        PlayerActivity.newIntent(requireContext(), items[position].mStreamURL).also {
            startActivity(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}