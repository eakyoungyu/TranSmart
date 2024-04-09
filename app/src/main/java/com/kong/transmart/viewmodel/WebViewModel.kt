package com.kong.transmart.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WebViewModel @Inject constructor(

): ViewModel() {
    private val _selectedTab = mutableStateOf(0)

    fun getSelectedTab(): Int {
        return _selectedTab.value
    }

    fun setSelectedTab(tab: Int) {
        _selectedTab.value = tab
    }
}