package com.neotica.repositoryinjection.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.neotica.repositoryinjection.databinding.FragmentNewsBinding

class NewsFragment : Fragment() {

    private var tabName: String? = null

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabName = arguments?.getString(ARG_TAB)

        //Step 11: Initialize ViewModel.
        //Step 11.1: Delegate viewModels factory by viewModels.
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        val viewModel: NewsViewModel by viewModels { factory }

        //Step 11.2: Define NewsAdapter class
        val newsAdapter = NewsAdapter()

        //Step 11.3: Initialize recyclerView
        binding?.rvNews?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = newsAdapter
        }

        //Step 12: Create condition for the tab
        if (tabName == TAB_NEWS) {
            //Step 12.1: Observe geHeadLineNews via viewModel observer with viewLifecycleOwner as context
            viewModel.getHeadlineNews().observe(viewLifecycleOwner) { result ->
                //Step 12.2: Create condition for when result isnt null
                if (result != null) {
                    when (result) {
                        is com.neotica.repositoryinjection.data.Result.Loading -> {
                            binding?.progressBar?.visibility = View.VISIBLE
                        }
                        is com.neotica.repositoryinjection.data.Result.Success -> {
                            binding?.progressBar?.visibility = View.GONE
                            val newsData = result.data
                            newsAdapter.submitList(newsData)
                        }
                        is com.neotica.repositoryinjection.data.Result.Error -> {
                            binding?.progressBar?.visibility = View.GONE
                            Toast.makeText(
                                context,
                                "Terjadi Kesalahan" + result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
        binding?.rvNews?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = newsAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ARG_TAB = "tab_name"
        const val TAB_NEWS = "news"
        const val TAB_BOOKMARK = "bookmark"
    }
}