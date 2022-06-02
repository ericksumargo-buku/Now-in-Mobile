package com.nowinmobile.base.udf.api.state

import kotlinx.coroutines.flow.Flow

interface StateManager<S> {
    /** Source to emit new state(s) produced by [produce]. **/
    val stateFlow: Flow<S>

    /** Generate new state with current state as parameter provided by [reduce] factory. **/
    fun produce(reduce: (currentState: S) -> S)
}
