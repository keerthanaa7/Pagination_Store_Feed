package com.tps.challenge.features.storefeed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.LoadType
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tps.challenge.Constants
import com.tps.challenge.R
import com.tps.challenge.StoreFeedViewModel
import com.tps.challenge.TCApplication
import com.tps.challenge.UIState
import com.tps.challenge.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Displays the list of Stores with its title, description and the cover image to the user.
 */
class StoreFeedFragment : Fragment() {
    companion object {
        const val TAG = "StoreFeedFragment"
    }

    private lateinit var storeFeedAdapter: StoreFeedAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var storeFeedViewModel: StoreFeedViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<StoreFeedViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        TCApplication.getAppComponent().inject(this)
        storeFeedViewModel =
            ViewModelProvider(this, viewModelFactory).get(StoreFeedViewModel::class.java)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_store_feed, container, false)
        swipeRefreshLayout = view.findViewById(R.id.swipe_container)
        // Enable if Swipe-To-Refresh functionality will be needed
        swipeRefreshLayout.isEnabled = true

        storeFeedAdapter = StoreFeedAdapter()
        recyclerView = view.findViewById(R.id.stores_view)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            // TODO uncomment the line below whe Adapter is implemented
            adapter = storeFeedAdapter
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ObserveViewModel()
        swipeRefreshLayout.setOnRefreshListener {
            storeFeedAdapter.refresh()
        }
    }

    fun ObserveViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            storeFeedViewModel.getStoreFeed(Constants.DEFAULT_LATITUDE, Constants.DEFAULT_LONGITUDE)
                .collectLatest { pagingData -> storeFeedAdapter.submitData(pagingData) }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            storeFeedAdapter.loadStateFlow.collectLatest { loadStates ->
                swipeRefreshLayout.isRefreshing = loadStates.refresh is LoadState.Loading

                val refreshstate = loadStates.refresh
                if(refreshstate is LoadState.Error){
                    Log.d(TAG, "error")
                }
            }
        }
    }
}
