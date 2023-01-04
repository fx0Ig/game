package com.example.game.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.game.GameViewModel
import com.example.game.R
import com.example.game.databinding.FragmentGameplayBinding

class GameplayFragment : Fragment() {

    lateinit var binding: FragmentGameplayBinding
    private val viewModel: GameViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameplayBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.score.text = getString(R.string.score_txt, 0)


        viewModel.score.observe(viewLifecycleOwner) {
            binding.score.text = getString(R.string.score_txt, it.score)
            binding.coinIv.setImageResource(it.image)
            if (it.isUserWon) {
                Toast.makeText(binding.root.context, "You got a point!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(binding.root.context, "You lose!", Toast.LENGTH_LONG).show()
            }
        }

        binding.headsBtn.setOnClickListener {
            viewModel.rolling(true)
        }

        binding.tailsBtn.setOnClickListener {
            viewModel.rolling(false)
        }

    }
}