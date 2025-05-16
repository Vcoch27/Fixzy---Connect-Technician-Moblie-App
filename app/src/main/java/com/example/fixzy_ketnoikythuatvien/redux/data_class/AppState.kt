package com.example.fixzy_ketnoikythuatvien.redux.data_class

import androidx.compose.ui.graphics.Color
import com.example.fixzy_ketnoikythuatvien.data.model.CategoryData
import com.example.fixzy_ketnoikythuatvien.data.model.UserData
import com.example.fixzy_ketnoikythuatvien.service.TestItem
import com.example.fixzy_ketnoikythuatvien.service.model.Availability
import com.example.fixzy_ketnoikythuatvien.service.model.Booking
import com.example.fixzy_ketnoikythuatvien.service.model.DetailBooking
import com.example.fixzy_ketnoikythuatvien.service.model.GetModeServiceResponse
import com.example.fixzy_ketnoikythuatvien.service.model.Notification
import com.example.fixzy_ketnoikythuatvien.service.model.ProviderBooking
import com.example.fixzy_ketnoikythuatvien.service.model.ProviderData
import com.example.fixzy_ketnoikythuatvien.service.model.Registration
import com.example.fixzy_ketnoikythuatvien.service.model.Service
import com.example.fixzy_ketnoikythuatvien.service.model.ServiceDetail
import com.example.fixzy_ketnoikythuatvien.service.model.SummaryBooking
import com.example.fixzy_ketnoikythuatvien.service.model.TopTechnician

//lớp chứa các model dữ liệu
data class AppState(

    val user: UserData? = null,
    val test: List<TestItem> = arrayListOf(),
    val categories: List<CategoryData> = emptyList(), // Danh sách các danh mục (mặc định rỗng)
    val isLoadingCategories: Boolean = false,        // Cờ hiển thị trạng thái đang tải danh mục
    val categoriesError: String? = null,              // Lỗi (nếu có) khi tải danh mục (null nếu không lỗi)

    val topTechnicians: List<TopTechnician> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,

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

    val bookings: List<DetailBooking> = emptyList(),

    val isUpdatingBookingStatus: Boolean = false,
    val updateBookingStatusError: String? = null,
    val updateBookingStatusMessage: String? = null,
//____________
    val isGetRegistration: Boolean = false,
    val getRegistrationError: String? = null,
    val registration: Registration? = null,
    //_________


    val isGetPayment: Boolean = false,
    val getPaymentError: String? = null,
    val orderUrl: String? = null,
    val appTransId: String? = null,

    val createServiceError: String? = null,
    val createServiceMessage: String? = null,
    val isCreatingService: Boolean = false,

    val modeService: GetModeServiceResponse? = null,

    val todayBookings: List<SummaryBooking> = emptyList(),
    val needAction: List<SummaryBooking> = emptyList(),
    var hasShownBookingNotification: Boolean = false,

    val newScheduleId: Int? = null,
    val tempPrice:String? = null,

    //notification
    val notifications: List<Notification> = emptyList(),
    val providerBookings: List<ProviderBooking> = emptyList(),

    )
