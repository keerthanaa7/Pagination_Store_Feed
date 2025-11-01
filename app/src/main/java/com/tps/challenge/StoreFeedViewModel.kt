package com.tps.challenge

import android.R
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tps.challenge.network.TPSCoroutineService
import com.tps.challenge.network.model.StoreResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class StoreFeedViewModel @Inject constructor(private val repository: StoreRepository) :
    ViewModel() {

    fun getStoreFeed(latitude: Double, longitude: Double): Flow<PagingData<Store>> {
     return repository.getStoreFeed(latitude, longitude).cachedIn(viewModelScope)
    }
}
