package com.liviolopez.contentplayer.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.liviolopez.contentplayer.R
import com.liviolopez.contentplayer.data.local.model.Item
import com.liviolopez.contentplayer.databinding.FragmentHomeBinding
import com.liviolopez.contentplayer.ui._components.*
import com.liviolopez.contentplayer.utils.DeviceInfo
import com.liviolopez.contentplayer.utils.Resource
import com.liviolopez.contentplayer.utils.extensions.setGone
import com.liviolopez.contentplayer.utils.extensions.setOptions
import com.liviolopez.contentplayer.utils.extensions.setVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), AppPlayer.OnProgressListener {
    private val TAG = "HomeFragment"

    private val viewModel: HomeViewModel by activityViewModels()

    @Inject
    lateinit var deviceInfo: DeviceInfo

    private lateinit var binding: FragmentHomeBinding

    private lateinit var mediaItemAdapter: MediaItemAdapter
    lateinit var appPlayer: AppPlayer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        appPlayer = AppPlayer(requireContext())

        setupFilters()
        mediaItemAdapter = MediaItemAdapter { item: Item -> onItemClicked(item) }
        binding.rvMediaItems.adapter = mediaItemAdapter
        setupMediaItemsAdapter()
    }

    private fun setupFilters(){
        val streamTypes = AppPlayer.StreamType.values().toList()
        binding.dmStreamType.apply {
            setOptions(streamTypes,
                topValue = "All",
                show = { it.detailsName },
                onClick = { // return null when clicked "topValue"
                    viewModel.filterFormat.value = it?.formats ?: emptyList()
                }
            )

            deviceInfo.ifItsTv { setOnClickListener {
                binding.dmStreamTypeValue.apply { requestFocus(); showDropDown() }
            }}
        }

        val drmTypes = listOf("Anyone", "Without") + AppPlayer.DrmType.values().map { it.name }
        binding.dmContentProtection.apply {
            setOptions(drmTypes,
                show = { it },
                onClick = { viewModel.filterProtection.value = it!! }
            )

            deviceInfo.ifItsTv { setOnClickListener {
                binding.dmContentProtectionValue.apply { requestFocus(); showDropDown() }
            }}
        }
    }

    private fun setupMediaItemsAdapter() {
        viewModel.itemsFiltered.onEach { result ->

            when (result.status) {
                Resource.Status.LOADING -> binding.standbyView.loading
                Resource.Status.SUCCESS -> {
                    binding.standbyView.success

                    if (result.data.isNullOrEmpty()) {
                        binding.rvMediaItems.setGone()
                        binding.standbyView.showEmptyMsg
                    } else {
                        binding.rvMediaItems.setVisible()
                        mediaItemAdapter.submitList(result.data){
                            binding.rvMediaItems.scrollToPosition(0)
                        }
                    }
                }
                Resource.Status.ERROR -> {
                    Log.e(TAG, "Error: ${result.throwable?.localizedMessage}")
                    mediaItemAdapter.submitList(emptyList())

                    binding.standbyView.showError = getString(
                        R.string.error_msg_param,
                        result.throwable?.localizedMessage
                    )
                }
            }

        }.launchIn(lifecycleScope)
    }

    private fun onItemClicked(item : Item) {
        playContent(item)
    }

    private fun playContent(item: Item){
        appPlayer.cleanView()

        appPlayer.onProgressListener = null

        appPlayer.apply {
            setTrackAndPlay(binding.vvItemVideo, item, 0)
            onProgressListener = this@HomeFragment
            loadingBar = binding.loadingVideo
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        appPlayer.release()
    }

    override fun onPause() {
        super.onPause()
        appPlayer.pause()
    }

    override fun onResume() {
        super.onResume()
        appPlayer.resume()
    }

    override fun onUpdatePosition(currentPosition: Long) {
        /** TODO() */
    }
}