package com.geektechkb.geekmessenger.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.geektechkb.core.utils.CheckInternet
import com.geektechkb.feature_auth.databinding.FragmentAuthorizationFlowBinding.inflate
import com.geektechkb.feature_main.databinding.FragmentMainFlowBinding.inflate
import com.geektechkb.geekmessenger.databinding.FragmentConnectionCheckBinding


class ConnectionCheckFragment : Fragment() {
    private lateinit var cld : CheckInternet
    private lateinit var binding: FragmentConnectionCheckBinding
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
                binding.internet.visibility = View.GONE
                binding.internet2.visibility = View.VISIBLE
            } else {
                binding.internet.visibility = View.VISIBLE
                binding.internet2.visibility = View.GONE
            }
        }
    }
}