package com.paddez.wordformed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HiScoreViewModel(private val hiScoreDao: HiScoreDao) : ViewModel() {
    private val _hiScores = MutableStateFlow<List<HiScore>>(emptyList())
    val hiScores: StateFlow<List<HiScore>> = _hiScores

    init {
        loadHiScores()
    }

    private fun loadHiScores() {
        viewModelScope.launch {
            _hiScores.value = hiScoreDao.getAllHiScores()
        }
    }
}

class HiScoreViewModelFactory(private val hiScoreDao: HiScoreDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HiScoreViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HiScoreViewModel(hiScoreDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
