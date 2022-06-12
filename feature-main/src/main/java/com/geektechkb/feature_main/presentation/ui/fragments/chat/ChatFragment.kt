package com.geektechkb.feature_main.presentation.ui.fragments.chat

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>(R.layout.fragment_chat) {
    override val binding by viewBinding(FragmentChatBinding::bind)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun assembleViews() {
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override val viewModel: ChatViewModel by viewModels()
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.chat_toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


}