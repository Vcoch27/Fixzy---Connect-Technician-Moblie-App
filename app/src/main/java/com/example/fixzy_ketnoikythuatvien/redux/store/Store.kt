package com.example.fixzy_ketnoikythuatvien.redux.store

import com.example.fixzy_ketnoikythuatvien.redux.data_class.AppState
import com.example.fixzy_ketnoikythuatvien.redux.reducer.Reducer.Companion.appReducer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.reduxkotlin.Store as ReduxStore
import org.reduxkotlin.createStore

class Store {
    companion object {
        private val reduxStore = createStore(appReducer, AppState())

        // Tạo StateFlow để phát ra trạng thái
        private val _stateFlow = MutableStateFlow(reduxStore.getState())
        val stateFlow: StateFlow<AppState> get() = _stateFlow

        // Store gốc để dispatch action
        val store: ReduxStore<AppState> = reduxStore.apply {
            // Cập nhật StateFlow mỗi khi trạng thái thay đổi
            subscribe { _stateFlow.value = getState() }
        }
    }
}