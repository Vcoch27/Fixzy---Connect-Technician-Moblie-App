package com.example.fixzy_ketnoikythuatvien.redux.reducer

import android.util.Log
import com.example.fixzy_ketnoikythuatvien.redux.action.Action
import com.example.fixzy_ketnoikythuatvien.redux.data_class.AppState

//xử lí các hoạt động và cập nhật trạng thái
//Lớp xử l logic chuyển đổi trạng thái
class Reducer {
    companion object {
//        nhận vào một trạng thái hiện tại và một action
        val appReducer: (AppState, Any) -> AppState = { state, action ->
            when (action) {
                is Action.getTest -> {
                    Log.i("Reducer", "Reducer received: ${action.test}") // 👈 thêm log
                    state.copy(test = action.test)
                }
                is Action.setUser -> {
                    Log.i("Reducer", "Reducer received user: ${action.user}")
                    state.copy(user = action.user)
                }
                is Action.FetchCategoriesRequest -> { //Bắt đầu tải danh mục
                    Log.i("Reducer", "Fetching categories...")
                    state.copy(isLoadingCategories = true, categoriesError = null )
                }
                is Action.FetchCategoriesSuccess -> { //khi api trả về danh mục thành công
                    Log.i("Reducer", "Categories fetched: ${action.categories}")
                    state.copy(
                        isLoadingCategories = false,
                        categories = action.categories, // Cập nhật danh sách danh mục
                        categoriesError = null
                    )
                }
                is Action.FetchCategoriesFailure -> {//khi có lỗi
                    Log.i("Reducer", "Failed to fetch categories: ${action.error}")
                    state.copy(
                        isLoadingCategories = false,
                        categoriesError = action.error
                    )
                }
                else -> state //khoong khớp, giữ trạng thái hiện tại
            }
        }
    }

}
