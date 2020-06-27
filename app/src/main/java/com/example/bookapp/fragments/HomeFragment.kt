import android.net.ConnectivityManager
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
import com.example.bookapp.Adapters.CustomDivider
import com.example.bookapp.Adapters.HomeAdapter
import com.example.bookapp.R
import com.example.bookapp.databinding.LayoutHomeFragmentBinding
import com.example.bookapp.getConnectivityManager
import com.example.bookapp.viewModels.ViewModelPost
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class HomeFragment : Fragment() {

    lateinit var binding: LayoutHomeFragmentBinding
    private val viewModelPost: ViewModelPost by activityViewModels()
    private lateinit var connectivityManager:ConnectivityManager
    private val homeAdapter: HomeAdapter by lazy {
        HomeAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_home_fragment, container, false)
        connectivityManager = requireContext().getConnectivityManager()
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
            if (connectivityManager.activeNetwork != null) {
                val action = HomeFragmentDirections.actionHomeToFragmentAddPost()
                findNavController().navigate(action)
            } else {
                Snackbar.make(binding.root, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).show()
            }
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