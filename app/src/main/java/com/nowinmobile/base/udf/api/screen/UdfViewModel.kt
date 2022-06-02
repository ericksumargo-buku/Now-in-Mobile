package com.nowinmobile.base.udf.api.screen

import androidx.lifecycle.ViewModel
import com.nowinmobile.base.udf.api.state.StateManager

abstract class UdfViewModel<S>(stateManager: StateManager<S>) :
    ViewModel(),
    StateManager<S> by stateManager
