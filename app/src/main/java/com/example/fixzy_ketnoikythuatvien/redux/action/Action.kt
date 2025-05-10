package com.example.fixzy_ketnoikythuatvien.redux.action

import com.example.fixzy_ketnoikythuatvien.data.model.CategoryData
import com.example.fixzy_ketnoikythuatvien.data.model.UserData
import com.example.fixzy_ketnoikythuatvien.service.TestItem
import com.example.fixzy_ketnoikythuatvien.service.model.Availability
import com.example.fixzy_ketnoikythuatvien.service.model.DetailBooking
import com.example.fixzy_ketnoikythuatvien.service.model.ProviderData
import com.example.fixzy_ketnoikythuatvien.service.model.Registration
import com.example.fixzy_ketnoikythuatvien.service.model.Service
import com.example.fixzy_ketnoikythuatvien.service.model.ServiceDetail
import com.example.fixzy_ketnoikythuatvien.service.model.TopTechnician

//định nghĩa các hành động có thể thực hiện
sealed class Action{
    data class getTest(val test: List<TestItem>) : Action()//chứa dữ liệu từ API ---> action này sẽ được gửi đi khi API trả về dữ liệu thành công
    data class setUser(val user: UserData) : Action()

    data class FetchUserDataStart(val firebaseUid: String) : Action()
    data class FetchUserDataSuccess(val userData: UserData) : Action()
    data class FetchUserDataFailure(val error: String) : Action()

    // Add actions for categories
    object FetchCategoriesRequest : Action()
    data class FetchCategoriesSuccess(val categories: List<CategoryData>) : Action()
    data class FetchCategoriesFailure(val error: String) : Action()

    data class FetchTopTechnicians(val categoryId: String? = null) : Action()
    data class TopTechniciansLoaded(val technicians: List<TopTechnician>) : Action()
    data class TopTechniciansLoadFailed(val error: String) : Action()

    // Actions mới cho services
    data class SelectCategory(val category: CategoryData?) : Action()
    object FetchServicesRequest : Action()
    data class FetchServicesSuccess(val services: List<Service>) : Action()
    data class FetchServicesFailure(val error: String) : Action()

    object FetchBookings : Action()
    data class FetchBookingsSuccess(val bookings: List<DetailBooking>) : Action()
    data class FetchBookingsFailure(val error: String) : Action()

    object FetchProviderRequest : Action() // Action mới để yêu cầu tải provider
    data class FetchProviderSuccess(val provider: ProviderData) : Action() // Action khi tải thành công
    data class FetchProviderFailure(val error: String) : Action()

    object FetchAvailabilityRequest : Action()
    data class FetchAvailabilitySuccess(val availability: List<Availability>) : Action()
    data class FetchAvailabilityFailure(val error: String) : Action()
    data class SelectService(val service: ServiceDetail?) : Action()

    data class UpdateBooking(
        val userId: Int? = null,
        val serviceId: Int? = null,
        val availabilityId: Int? = null,
        val date: String? = null,
        val startTime: String? = null,
        val address: String? = null,
        val phone: String? = null,
        val notes: String? = null,
        val totalPrice: Double? = null,
        val status: String? = null
    ) : Action()
    data class DateForBooking(val date: String) : Action()
    object CreateBooking : Action()

    object StartCreatingBooking : Action()
    data class CreateBookingSuccess(val referenceCode: String) : Action()
    data class CreateBookingFailure(val error: String) : Action()
    object ResetBookingState : Action()

    object UpdateBookingStatus : Action()
    data class UpdateBookingAction(val booking: DetailBooking) : Action()
    data class UpdateBookingStatusSuccess(val message: String) : Action()
    data class UpdateBookingStatusFailure(val error: String) : Action()

    data class UpdateUserProfileAction(val user: UserData) : Action()

    data class UpdateProfileSuccess(val user: UserData) : Action()
    data class UpdateProfileFailure(val error: String) : Action()
    object UpdateProfileLoading : Action()

    data class  RegisterProviderSuccess (val message: String) : Action()
    data class  RegisterProviderFailure (val error: String) : Action()
    object  RegisterProviderLoading : Action()

    data class GetRegistrationSuccess(val registration: Registration) : Action()
    data class GetRegistrationFailure(val error: String) : Action()
    object GetRegistrationStatusLoading : Action()

    data class CreatePaymentSuccess(val orderUrl: String, val appTransId: String?) : Action()
    data class CreatePaymentFailure(val error: String) : Action()
    object CreatePaymentLoading : Action()

    data class CheckPaymentStatusSuccess(val registration: Registration) : Action()
    data class CheckPaymentStatusFailure(val error: String) : Action()

    data class CreateServiceSuccess(val message: String) : Action()
    data class CreateServiceFailure(val error: String) : Action()
    object CreateServiceLoading : Action()

}
