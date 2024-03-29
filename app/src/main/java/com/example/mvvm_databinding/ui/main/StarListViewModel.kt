package com.example.mvvm_databinding.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvm_databinding.Repository.RepoRepository
import com.example.mvvm_databinding.model.Repo
import com.example.mvvm_databinding.ui.base.BaseViewModel
import com.example.mvvm_databinding.utils.Schedulers.BaseScheduler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class StarListViewModel @Inject constructor(private val repoRepository: RepoRepository, private val baseScheduler: BaseScheduler) : BaseViewModel<List<Repo>>() {
    val compositeDisposable = CompositeDisposable();
    val repoLifeData = MutableLiveData<ArrayList<Repo>>()

    public fun getMyStarRepos(userName: String) {
        val repoDisposable = repoRepository.fetchRepos(userName)
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
                .doOnSubscribe { loadingStatus.setValue(true) }
                .doAfterTerminate { loadingStatus.setValue(false) }
                .subscribe {
                    repoLifeData.value = it as ArrayList<Repo>?
                }
        compositeDisposable.addAll(repoDisposable)
    }

      fun getLiveData():LiveData<ArrayList<Repo>> = repoLifeData

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}