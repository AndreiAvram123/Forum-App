import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookapp.Adapters.CustomDivider
import com.example.bookapp.Adapters.HomeAdapter
import com.example.bookapp.R
import com.example.bookapp.databinding.LayoutHomeFragmentBinding
import com.example.bookapp.viewModels.ViewModelPost
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class HomeFragment : Fragment(), HomeAdapter.Callback {
    lateinit var binding: LayoutHomeFragmentBinding
    private val viewModelPost: ViewModelPost by activityViewModels()
    private val homeAdapter: HomeAdapter = HomeAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_home_fragment, container, false);
        attachObserver()
        initializeRefreshLayout()
        initializeRecyclerView()
        val layoutManager: LinearLayoutManager = binding.recyclerViewHome.layoutManager as LinearLayoutManager
        layoutManager.scrollToPosition(viewModelPost.lastSeenPostPosition)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        val layoutManager: LinearLayoutManager = binding.recyclerViewHome.layoutManager as LinearLayoutManager
        viewModelPost.lastSeenPostPosition = layoutManager.findFirstVisibleItemPosition()
    }

    private fun initializeRefreshLayout() {
        binding.homeSwipeRefreshLayout.setOnRefreshListener {
            binding.homeSwipeRefreshLayout.isRefreshing = false
        }
    }

    private fun initializeRecyclerView() {
        binding.recyclerViewHome.adapter = homeAdapter;
        binding.recyclerViewHome.addItemDecoration(CustomDivider(20));
        binding.recyclerViewHome.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun attachObserver() {
        viewModelPost.getFirstPagePosts().observe(viewLifecycleOwner, Observer { posts ->
            homeAdapter.addData(ArrayList(posts));
        })

    }

    override fun requestMoreData() {
        viewModelPost.fetchNextPagePosts()
    }
}