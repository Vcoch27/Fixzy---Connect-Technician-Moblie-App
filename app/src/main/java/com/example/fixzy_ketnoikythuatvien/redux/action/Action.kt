package com.example.fixzy_ketnoikythuatvien.redux.action

import com.example.fixzy_ketnoikythuatvien.data.model.CategoryData
import com.example.fixzy_ketnoikythuatvien.data.model.UserData
import com.example.fixzy_ketnoikythuatvien.service.TestItem
import com.example.fixzy_ketnoikythuatvien.service.model.TopTechnician

//định nghĩa các hành động có thể thực hiện
sealed class Action{
    data class getTest(val test: List<TestItem>) : Action()//chứa dữ liệu từ API ---> action này sẽ được gửi đi khi API trả về dữ liệu thành công
    data class setUser(val user: UserData) : Action()

    // Add actions for categories
    object FetchCategoriesRequest : Action() //dispath để yêu cầu tải danh mục
    data class FetchCategoriesSuccess(val categories: List<CategoryData>) : Action() //dispath khi api trả về danh mục thành công
    data class FetchCategoriesFailure(val error: String) : Action()

    object FetchTopTechniciansRequest : Action()
//    data class FetchTopTechniciansSuccess(val technicians: List<TopTechnician>) : Action()
//    data class FetchTopTechniciansFailure(val error: String) : Action()
    data class FetchTopTechnicians(val categoryId: String? = null) : Action()
    data class TopTechniciansLoaded(val technicians: List<TopTechnician>) : Action()
    data class TopTechniciansLoadFailed(val error: String) : Action()
}
