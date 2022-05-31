package com.geektechkb.feature_main.presentation.ui.fragments.checknetworkconnection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.geektechkb.core.utils.CheckInternet
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentConnectionCheckBinding


class ConnectionCheckFragment : Fragment() {

     private lateinit var binding: FragmentConnectionCheckBinding
     private lateinit var cld: CheckInternet

     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentConnectionCheckBinding.inflate(LayoutInflater.from(context), container, false)
        checkInternet()
        return binding.root


    }

    private fun checkInternet() {
        activity?.application?.let {
            cld = CheckInternet(it)
        }
        cld.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigateUp()
                binding.connectionImage.visibility = View.GONE
                binding.connectionText.visibility = View.GONE
            } else {
                binding.connectionImage.visibility = View.VISIBLE
                binding.connectionText.visibility = View.VISIBLE
            }
        }
    }
}

