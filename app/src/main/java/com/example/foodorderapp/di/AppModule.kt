package com.example.foodorderapp.di

import com.example.foodorderapp.data.datasource.FoodsDataSource
import com.example.foodorderapp.data.repo.FoodsRepository
import com.example.foodorderapp.retrofit.ApiUtils
import com.example.foodorderapp.retrofit.FoodsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideFoodsRepository(fds: FoodsDataSource): FoodsRepository {
        return FoodsRepository(fds)
    }

    @Provides
    @Singleton
    fun provideFoodsDataSource(fdao: FoodsDao): FoodsDataSource {
        return FoodsDataSource(fdao)
    }

    @Provides
    @Singleton
    fun provideFoodsDao(): FoodsDao {
        return ApiUtils.getFoodsDao()
    }

}