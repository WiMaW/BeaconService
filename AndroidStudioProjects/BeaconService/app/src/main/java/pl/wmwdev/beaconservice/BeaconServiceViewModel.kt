package pl.wmwdev.beaconservice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeaconServiceViewModel @Inject constructor(private val actionRepo: ActionRepo) : ViewModel() {
    private val _elements = MutableStateFlow<ApiResponse<List<Action>>>(ApiResponse.Loading)
    val elements: StateFlow<ApiResponse<List<Action>>> = _elements

    fun loadElements() {
        viewModelScope.launch {
            _elements.value = ApiResponse.Loading
            _elements.value = actionRepo.fetchElements()
        }
    }
}