package com.tps.challenge

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.tps.challenge.database.StoreDao
import com.tps.challenge.database.entities.PlaceholderEntity
import com.tps.challenge.network.TPSCoroutineService
import com.tps.challenge.network.model.toEntity

@OptIn(ExperimentalPagingApi::class)
class StoreRemoteMediator(private val tpsCoroutineService: TPSCoroutineService,
                          private val storeDao: StoreDao,
    private val latitude: Double,
    private val longitude: Double): RemoteMediator<Int, PlaceholderEntity>(){
    @OptIn(ExperimentalPagingApi::class)
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PlaceholderEntity>
    ): MediatorResult {
        if(loadType != LoadType.REFRESH){
            return MediatorResult.Success(endOfPaginationReached = true)
        }
        try{
            val storeResponse = tpsCoroutineService.getStoreFeed(latitude,
                longitude)
            val placeholderEntitylist = storeResponse.toEntity()
            storeDao.clearAll()
            storeDao.insertAll(placeholderEntitylist)
            return MediatorResult.Success(endOfPaginationReached = true)
        }catch (e: Exception){
            return MediatorResult.Error(Throwable(e))
        }

    }
}