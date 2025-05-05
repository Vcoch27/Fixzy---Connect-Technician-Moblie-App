package com.example.fixzy_ketnoikythuatvien.redux.data_class

import androidx.compose.ui.graphics.Color
import com.example.fixzy_ketnoikythuatvien.data.model.CategoryData
import com.example.fixzy_ketnoikythuatvien.data.model.UserData
import com.example.fixzy_ketnoikythuatvien.service.TestItem
import com.example.fixzy_ketnoikythuatvien.service.model.Availability
import com.example.fixzy_ketnoikythuatvien.service.model.Booking
import com.example.fixzy_ketnoikythuatvien.service.model.DetailBooking
import com.example.fixzy_ketnoikythuatvien.service.model.ProviderData
import com.example.fixzy_ketnoikythuatvien.service.model.Service
import com.example.fixzy_ketnoikythuatvien.service.model.ServiceDetail
import com.example.fixzy_ketnoikythuatvien.service.model.TopTechnician

//lớp chứa các model dữ liệu
data class AppState(
    val test: List<TestItem> = arrayListOf(),//định nghĩa trạng thái ứng dụng chứa một danh sách TestItem // mặc định rỗng
    val user: UserData? = null,

    val categories: List<CategoryData> = emptyList(), // Danh sách các danh mục (mặc định rỗng)
    val isLoadingCategories: Boolean = false,        // Cờ hiển thị trạng thái đang tải danh mục
    val categoriesError: String? = null,              // Lỗi (nếu có) khi tải danh mục (null nếu không lỗi)

    val topTechnicians: List<TopTechnician> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,

    val selectedCategory: CategoryData? = null, // Category được chọn
    val services: List<Service> = emptyList(), // Danh sách services
    val isLoadingServices: Boolean = false,    // Trạng thái loading services
    val servicesError: String? = null,          // Lỗi khi lấy services

    val provider: ProviderData? = null, // Thêm trạng thái provider
    val isLoadingProvider: Boolean = false, // Trạng thái loading cho provider
    val providerError: String? = null,

    val isLoadingAvailability: Boolean = false,
    val availabilityError: String? = null,
    val availability: List<Availability> = emptyList(),
    val selectedService: ServiceDetail? = null,

    val booking: Booking? = null,
    val isCreatingBooking: Boolean = false,
    val createBookingError: String? = null,
    val referenceCode: String? = null,

    val bookings: List<DetailBooking> = emptyList()
)
