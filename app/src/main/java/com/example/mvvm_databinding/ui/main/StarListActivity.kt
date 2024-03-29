package com.example.mvvm_databinding.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm_databinding.R
import com.example.mvvm_databinding.databinding.ActivityStarListBinding
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class StarListActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: ActivityStarListBinding
    private lateinit var starListViewModel: StarListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpViews();
         starListViewModel = ViewModelProviders.of(this,viewModelFactory).get(StarListViewModel::class.java)
        getStarredRepos(starListViewModel)
        observeMyStars(starListViewModel)
        observeResponse(starListViewModel)
    }

    private fun setUpViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_star_list)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }


    private fun observeResponse(viewModel:StarListViewModel) {
        viewModel.loadingStatus.observe(
                this,
                androidx.lifecycle.Observer { isLoading->if (isLoading){
                    Toast.makeText(this,"Loading",Toast.LENGTH_LONG).
                            show()
                }else{
                    Toast.makeText(this,"Not Loading",Toast.LENGTH_LONG).
                            show()
                }
                })
    }

    private fun getStarredRepos(viewModel: StarListViewModel) {
        viewModel.getMyStarRepos("mrabelwahed")
    }

    private fun observeMyStars(viewModel: StarListViewModel) {
        viewModel.getLiveData().observe(this, Observer { repos ->
            binding.repos = repos
            binding.executePendingBindings()
        })
    }
}