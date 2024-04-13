package com.mahammadjafarzade.common.base

import androidx.lifecycle.ViewModel
import com.mahammadjafarzade.common.flowState.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow

open class BaseViewModel : ViewModel(){
    val state : MutableStateFlow<State?> = MutableStateFlow(null)
}