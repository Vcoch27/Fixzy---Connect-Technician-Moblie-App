package com.example.fixzy_ketnoikythuatvien.redux.data_class

import androidx.compose.ui.graphics.Color
import com.example.fixzy_ketnoikythuatvien.data.model.CategoryData
import com.example.fixzy_ketnoikythuatvien.data.model.UserData
import com.example.fixzy_ketnoikythuatvien.service.TestItem
//lớp chứa các model dữ liệu
data class AppState(
    val test: List<TestItem> = arrayListOf(),//định nghĩa trạng thái ứng dụng chứa một danh sách TestItem // mặc định rỗng
    val user: UserData? = null,

    val categories: List<CategoryData> = emptyList(), // Danh sách các danh mục (mặc định rỗng)
    val isLoadingCategories: Boolean = false,        // Cờ hiển thị trạng thái đang tải danh mục
    val categoriesError: String? = null              // Lỗi (nếu có) khi tải danh mục (null nếu không lỗi)
)
