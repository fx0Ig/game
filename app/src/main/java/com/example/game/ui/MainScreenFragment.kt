package com.example.game.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.game.MainScreenViewModel
import com.example.game.R
import com.example.game.databinding.FragmentMainScreenBinding

class MainScreenFragment : Fragment() {

    private val viewModel: MainScreenViewModel by viewModels()
    lateinit var binding: FragmentMainScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainScreenBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getShowGame().observe(viewLifecycleOwner) {
            when (it) {
                0 -> findNavController().navigate(R.id.action_mainScreenFragment_to_gameFragment)
                1 -> binding.webView.visibility = View.VISIBLE
            }
            setupWebView()
//        viewModel.result.observe(viewLifecycleOwner){
//            when(it){
//                0-> findNavController().navigate(R.id.action_mainScreenFragment_to_gameFragment)
//                1-> binding.webView.visibility = View.VISIBLE
//            }
//        }
        }
    }



    private fun setupWebView() {
        binding.webView.loadUrl("https://webqr.com/")
//        binding.webView.visibility = View.GONE
    }



}