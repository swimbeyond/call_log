package org.bogucki.calllog.presentation.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.bogucki.calllog.R
import org.bogucki.calllog.databinding.FragmentMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModel<MainViewModel>()

    private val adapter = CallsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.callLog.adapter = adapter
        viewModel.getIpAddress()
        getCallLog()
        observeState()
    }

    fun getCallLog() = viewModel.observeCallLog()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.state.collect {
                with(binding) {
                    serverAddressTextView.text =
                        getString(R.string.fragment_main_ip_address, it.ipAddress)
                    adapter.submitList(it.callLog)
                }
            }
        }
    }
}