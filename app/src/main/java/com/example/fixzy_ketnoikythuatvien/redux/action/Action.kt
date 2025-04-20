package com.example.fixzy_ketnoikythuatvien.redux.action

import com.example.fixzy_ketnoikythuatvien.data.model.CategoryData
import com.example.fixzy_ketnoikythuatvien.data.model.UserData
import com.example.fixzy_ketnoikythuatvien.service.TestItem
//định nghĩa các hành động có thể thực hiện
sealed class Action{
    data class getTest(val test: List<TestItem>) : Action()//chứa dữ liệu từ API ---> action này sẽ được gửi đi khi API trả về dữ liệu thành công
    data class setUser(val user: UserData) : Action()

    // Add actions for categories
    object FetchCategoriesRequest : Action() //dispath để yêu cầu tải danh mục
    data class FetchCategoriesSuccess(val categories: List<CategoryData>) : Action() //dispath khi api trả về danh mục thành công
    data class FetchCategoriesFailure(val error: String) : Action()
}
