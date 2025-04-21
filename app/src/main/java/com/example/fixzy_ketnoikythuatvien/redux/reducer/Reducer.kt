package com.example.fixzy_ketnoikythuatvien.redux.reducer

import android.util.Log
import com.example.fixzy_ketnoikythuatvien.redux.action.Action
import com.example.fixzy_ketnoikythuatvien.redux.data_class.AppState

private const val TAG = "Reducer"
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
                is Action.FetchTopTechnicians -> {
                    Log.i(TAG, "Đặt isLoading=true cho kỹ thuật viên, categoryId=${action.categoryId}")
                    state.copy(isLoading = true, error = null)
                }
                is Action.TopTechniciansLoaded -> {
                    Log.i(TAG, "Kỹ thuật viên đã tải: số lượng=${action.technicians.size}, chi tiết=${action.technicians.map { it.name }}")
                    state.copy(
                        isLoading = false,
                        topTechnicians = action.technicians,
                        error = null
                    )
                }
                is Action.TopTechniciansLoadFailed -> {
                    Log.e(TAG, "Lỗi tải kỹ thuật viên: ${action.error}")
                    state.copy(
                        isLoading = false,
                        error = action.error
                    )
                }
                else -> {
                    Log.w(TAG, "Action không được xử lý: $action")
                    state
                }
            }
        }
    }

}
