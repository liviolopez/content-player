package com.liviolopez.contentplayer.ui.home

import androidx.lifecycle.*
import com.liviolopez.contentplayer.data.local.model.Item
import com.liviolopez.contentplayer.repository.Repository
import com.liviolopez.contentplayer.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (
    private val repository: Repository
) : ViewModel() {

}