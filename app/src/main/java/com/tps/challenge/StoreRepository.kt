package com.tps.challenge

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.tps.challenge.database.StoreDao
import com.tps.challenge.database.entities.toDomain
import com.tps.challenge.network.TPSCoroutineService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class StoreRepository @Inject constructor(
    private val coroutineApiService: TPSCoroutineService,
    private val storeDao: StoreDao
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getStoreFeed(latitude: Double, longitude:Double): Flow<PagingData<Store>> {
        // This provides the flow of data from the database to the UI.
        val pagingSourceFactory = { storeDao.getAllPagingStores() }
        return Pager(
            config = PagingConfig(pageSize = 25, enablePlaceholders = false),
            // The RemoteMediator handles the Network -> Database flow.
            remoteMediator = StoreRemoteMediator(
                tpsCoroutineService = coroutineApiService,
                storeDao = storeDao,
                latitude, longitude
            ),
            // The PagingSource handles the Database -> UI flow.
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData -> pagingData.map { placeholderEntity -> placeholderEntity.toDomain() } }
    }
}


