import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrew.bookapp.Adapters.CustomDivider
import com.andrew.bookapp.Adapters.HomeAdapter
import com.andrew.bookapp.R
import com.andrew.bookapp.databinding.LayoutHomeFragmentBinding
import com.andrew.bookapp.viewModels.ViewModelPost
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class HomeFragment : Fragment() {
    lateinit var binding: LayoutHomeFragmentBinding
    private val viewModelPost: ViewModelPost by activityViewModels()
    private val homeAdapter: HomeAdapter by lazy {
        HomeAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_home_fragment, container, false);
        initializeUI()
        attachObserver()
        return binding.root
    }


    private fun initializeUI() {
        binding.homeSwipeRefreshLayout.setOnRefreshListener {
            viewModelPost.fetchNewPosts()
            binding.homeSwipeRefreshLayout.isRefreshing = false
        }
        initializeRecyclerView()
        binding.addPostButton.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeToFragmentAddPost()
            findNavController().navigate(action)
        }
    }

    private fun initializeRecyclerView() {
        with(binding.recyclerViewHome) {
            adapter = homeAdapter
            addItemDecoration(CustomDivider(20))
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun attachObserver() {
        viewModelPost.recentPosts.observe(viewLifecycleOwner, Observer {
            homeAdapter.submitList(it)
        })

    }


}