package com.liviolopez.contentplayer.ui.home

import androidx.lifecycle.*
import com.liviolopez.contentplayer.data.local.model.Item
import com.liviolopez.contentplayer.repository.Repository
import com.liviolopez.contentplayer.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (
    private val repository: Repository
) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.initializeDb()
            filterCrimes()
        }
    }

    private val _itemsFiltered = MutableStateFlow<Resource<List<Item>>>(Resource.loading())
    val itemsFiltered = _itemsFiltered.asStateFlow()

    val filterFormat = MutableStateFlow(emptyList<String>())
    val filterProtection = MutableStateFlow("Anyone")

    private val isValidFilter: Flow<Boolean> = combine(filterFormat, filterProtection) { filterFormat, filterProtection ->
        /** TODO() **/

        return@combine filterFormat !== null && filterProtection !== null
    }

    private suspend fun filterCrimes() {
        isValidFilter
            .collectLatest {
                _itemsFiltered.value = Resource.loading()

                repository.filterItems(filterFormat.value, filterProtection.value)
                    .catch { e -> _itemsFiltered.value = Resource.error(e, emptyList()) }
                    .collectLatest {  _itemsFiltered.value = Resource.success(it) }
            }
    }
}