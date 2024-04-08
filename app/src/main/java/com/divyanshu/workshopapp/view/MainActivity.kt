package com.divyanshu.workshopapp.view

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.divyanshu.workshopapp.R
import com.divyanshu.workshopapp.adapter.MovieAdapter
import com.divyanshu.workshopapp.databinding.ActivityMainBinding
import com.divyanshu.workshopapp.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var shouldSearch: Boolean = false
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MovieViewModel by viewModels()
    private lateinit var adapter: MovieAdapter
    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        setupRecyclerView()
        loadData()
    }

    private fun initUI() {
        setSupportActionBar(binding.toolbar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(true)
        } else {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        binding.toolbar.setNavigationOnClickListener {
            this@MainActivity.onBackPressedDispatcher.onBackPressed()
        }
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_arrow)
        this@MainActivity.adapter = MovieAdapter(lifecycleScope)
    }

    private fun setupRecyclerView() {
        binding.content.rlvMovies.apply {
            layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            adapter = this@MainActivity.adapter
            setHasFixedSize(true)
        }
    }

    private fun loadData(){
        lifecycleScope.launch {
            viewModel.movieListData.collect {
                this@MainActivity.adapter.submitData(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchView = menu.findItem(R.id.action_search)?.actionView as SearchView
        searchView.setOnQueryTextFocusChangeListener { _: View?, hasFocus: Boolean ->
            shouldSearch = hasFocus
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (shouldSearch && newText.length > 2) {
                    adapter.filter.filter(newText)
                } else if (shouldSearch && newText.length < 2) {
                    loadData()
                }
                return true
            }
        })
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}