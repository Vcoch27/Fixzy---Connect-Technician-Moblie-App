package com.example.fixzy_ketnoikythuatvien.redux.reducer

import android.util.Log
import com.example.fixzy_ketnoikythuatvien.redux.action.Action
import com.example.fixzy_ketnoikythuatvien.redux.data_class.AppState
import com.example.fixzy_ketnoikythuatvien.service.model.Booking

private const val TAG = "Reducer"

//xử lí các hoạt động và cập nhật trạng thái
//Lớp xử l logic chuyển đổi trạng thái
class Reducer {
    companion object {
        //        nhận vào một trạng thái hiện tại và một action
        val appReducer: (AppState, Any) -> AppState = { state, action ->
            when (action) {
                is Action.FetchUserDataStart -> {
                    Log.d(TAG, "FetchUserDataStart: Starting fetch for UID ${action.firebaseUid}")
                    state.copy(
                        isLoading = true,
                        error = null
                    )
                }

                is Action.FetchUserDataSuccess -> {
                    Log.d(TAG, "FetchUserDataSuccess: User data received: ${action.userData}")
                    state.copy(
                        isLoading = false,
                        user = action.userData,
                        error = null
                    )
                }

                is Action.FetchUserDataFailure -> {
                    Log.e(TAG, "FetchUserDataFailure: Error: ${action.error}")
                    state.copy(
                        isLoading = false,
                        user = null,
                        error = action.error
                    )
                }

                is Action.getTest -> {
                    state.copy(test = action.test)
                }

                is Action.setUser -> {
                    state.copy(user = action.user)
                }

                is Action.FetchCategoriesRequest -> { //Bắt đầu tải danh mục
                    Log.i("Reducer", "Fetching categories...")
                    state.copy(isLoadingCategories = true, categoriesError = null)
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

                is Action.FetchBookings -> {
                    state.copy(isLoading = true, error = null)
                }

                is Action.FetchBookingsSuccess -> {
                    state.copy(
                        isLoading = false,
                        bookings = action.bookings,
                        error = null
                    )
                }

                is Action.FetchBookingsFailure -> {
                    state.copy(
                        isLoading = false,
                        error = action.error
                    )
                }

                is Action.FetchTopTechnicians -> {
                    Log.i(
                        TAG,
                        "Đặt isLoading=true cho kỹ thuật viên, categoryId=${action.categoryId}"
                    )
                    state.copy(isLoading = true, error = null)
                }

                is Action.TopTechniciansLoaded -> {
                    Log.i(
                        TAG,
                        "Kỹ thuật viên đã tải: số lượng=${action.technicians.size}, chi tiết=${action.technicians.map { it.name }}"
                    )
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

                is Action.SelectCategory -> {
                    state.copy(selectedCategory = action.category, services = emptyList())
                }

                is Action.FetchServicesRequest -> {
                    Log.i(TAG, "Đặt isLoadingServices=true")
                    state.copy(isLoadingServices = true, servicesError = null)
                }

                is Action.FetchServicesSuccess -> {
                    state.copy(
                        isLoadingServices = false,
                        services = action.services,
                        servicesError = if(action.services.isEmpty()) "Không có dịch vụ nào" else null
                    )
                }

                is Action.FetchServicesFailure -> {
                    state.copy(
                        isLoadingServices = false,
                        servicesError = action.error
                    )
                }

                is Action.FetchProviderRequest -> {
                    Log.i(TAG, "Fetching provider details...")
                    state.copy(isLoadingProvider = true, providerError = null)
                }

                is Action.FetchProviderSuccess -> {
                    Log.i(TAG, "Provider fetched: ${action.provider.name}")
                    state.copy(
                        isLoadingProvider = false,
                        provider = action.provider,
                        providerError = null
                    )
                }

                is Action.FetchProviderFailure -> {
                    Log.e(TAG, "Failed to fetch provider: ${action.error}")
                    state.copy(
                        isLoadingProvider = false,
                        providerError = action.error
                    )
                }

                is Action.FetchAvailabilityRequest -> state.copy(
                    isLoadingAvailability = true,
                    availabilityError = null
                )

                is Action.FetchAvailabilitySuccess -> state.copy(
                    isLoadingAvailability = false,
                    availability = action.availability
                )

                is Action.FetchAvailabilityFailure -> state.copy(
                    isLoadingAvailability = false,
                    availabilityError = action.error
                )

                is Action.SelectService -> state.copy(selectedService = action.service)

                is Action.UpdateBooking -> {
                    Log.d(TAG, "UpdateBooking serviceId=${action.serviceId}")

                    val currentBooking = state.booking

                    val newBooking = currentBooking?.copy(
                        userId = action.userId ?: currentBooking.userId,
                        serviceId = action.serviceId ?: currentBooking.serviceId,
                        serviceName = action.serviceName ?: currentBooking.serviceName,
                        availabilityId = action.availabilityId ?: currentBooking.availabilityId,
                        date = action.date ?: currentBooking.date,
                        startTime = action.startTime ?: currentBooking.startTime,
                        duration = action.duration ?: currentBooking.duration,
                        address = action.address ?: currentBooking.address,
                        phone = action.phone ?: currentBooking.phone,
                        notes = action.notes ?: currentBooking.notes,
                        totalPrice = action.totalPrice ?: currentBooking.totalPrice,
                        status = action.status ?: currentBooking.status
                    ) ?: Booking(
                        userId = action.userId ?: 0,
                        serviceId = action.serviceId ?: 0,
                        serviceName = action.serviceName ?: "",
                        availabilityId = action.availabilityId ?: 0,
                        date = action.date ?: "",
                        startTime = action.startTime ?: "",
                        duration = action.duration ?: 0,
                        address = action.address ?: "",
                        phone = action.phone ?: "",
                        notes = action.notes,
                        totalPrice = action.totalPrice ?: 0.0,
                        status = action.status ?: "Pending"
                    )

                    state.copy(booking = newBooking)
                }


                is Action.CreateBooking -> state.copy(
                    isCreatingBooking = true,
                    createBookingError = null
                )

                is Action.StartCreatingBooking -> state.copy(
                    isCreatingBooking = true,
                    createBookingError = null
                )

                is Action.CreateBookingSuccess -> state.copy(
                    referenceCode = action.referenceCode,
                    isCreatingBooking = false, // Reset sau khi thành công
                    createBookingError = null
                )

                is Action.CreateBookingFailure -> state.copy(
                    createBookingError = action.error,
                    isCreatingBooking = false
                )

                is Action.ResetBookingState -> state.copy(
                    referenceCode = null,
                    isCreatingBooking = false,
                    createBookingError = null
                )

                is Action.UpdateBookingStatus -> state.copy(
                    isUpdatingBookingStatus = true,
                    updateBookingStatusError = null,
                    updateBookingStatusMessage = null
                )

                is Action.UpdateBookingAction -> {
                    val updatedBookings = state.bookings.map {
                        if (it.booking_id == action.booking.booking_id) action.booking else it
                    }
                    state.copy(bookings = updatedBookings)
                }

                is Action.UpdateBookingStatusSuccess -> state.copy(
                    isUpdatingBookingStatus = false,
                    updateBookingStatusError = null,
                    updateBookingStatusMessage = action.message
                )

                is Action.UpdateBookingStatusFailure -> state.copy(
                    isUpdatingBookingStatus = false,
                    updateBookingStatusError = action.error,
                    updateBookingStatusMessage = null
                )

                is Action.UpdateProfileLoading -> {
                    state.copy(isLoading = true)
                }

                is Action.UpdateProfileSuccess -> {
                    state.copy(
                        isLoading = false,
                        user = action.user
                    )
                }

                is Action.UpdateProfileFailure -> {
                    state.copy(
                        isLoading = false,
                        error = action.error
                    )
                }

                is Action.RegisterProviderSuccess -> {
                    state.copy(
                        isLoadingProvider = false,
                        providerError = null
                    )
                }

                is Action.RegisterProviderFailure -> {
                    state.copy(
                        isLoadingProvider = false,
                        providerError = action.error
                    )
                }

                is Action.RegisterProviderLoading -> {
                    state.copy(
                        isLoadingProvider = true,
                        providerError = null
                    )
                }

                is Action.GetRegistrationSuccess -> {
                    state.copy(
                        isGetRegistration = false,
                        getRegistrationError = null,
                        registration = action.registration
                    )
                }

                is Action.GetRegistrationFailure -> {
                    state.copy(
                        isGetRegistration = false,
                        getRegistrationError = action.error,
                        registration = null
                    )
                }

                is Action.GetRegistrationStatusLoading -> {
                    state.copy(
                        isGetRegistration = true,
                        getRegistrationError = null,
                        registration = null
                    )
                }

                is Action.CreatePaymentSuccess -> {
                    state.copy(
                        isGetPayment = false,
                        getPaymentError = null,
                        orderUrl = action.orderUrl,
                        appTransId = action.appTransId
                    )
                }

                is Action.CreatePaymentFailure -> {
                    state.copy(
                        isGetPayment = false,
                        getPaymentError = action.error,
                        orderUrl = null,
                    )
                }

                is Action.CreatePaymentLoading -> {
                    state.copy(
                        isGetPayment = true,
                        getPaymentError = null,
                        orderUrl = null,
                    )
                }

                is Action.CreateServiceLoading -> state.copy(
                    isCreatingService = true,
                    createServiceError = null,
                    createServiceMessage = null
                )

                is Action.CreateServiceSuccess -> state.copy(
                    isCreatingService = false,
                    createServiceError = null,
                    createServiceMessage = action.message
                )

                is Action.CreateServiceFailure -> state.copy(
                    isCreatingService = false,
                    createServiceError = action.error,
                    createServiceMessage = null
                )

                is Action.GetModeServiceSuccess -> state.copy(
                    modeService = action.data,
                    isLoading = false,
                    error = null
                )

                is Action.GetModeServiceFailure -> state.copy(
                    isLoading = false,
                    error = action.error
                )

                is Action.GetSummaryStatusSuccess -> {
                    state.copy(
                        isLoading = false,
                        success = true,
                        todayBookings = action.todayBookings,
                        needAction = action.needAction,
                        error = null,
                        hasShownBookingNotification = true
                    )
                }

                is Action.GetSummaryStatusFailure -> {
                    state.copy(
                        isLoading = false,
                        success = false,
                        error = action.error,
                        hasShownBookingNotification = true
                    )
                }

                is Action.AddScheduleSuccess -> {
                    state.copy(
                        isLoading = false,
                        success = true,
                        newScheduleId = action.message.toIntOrNull(),
                        error = null
                    )
                }

                is Action.AddScheduleFailure -> {
                    state.copy(
                        isLoading = false,
                        success = false,
                        error = action.error,
                    )
                }

                is Action.AddScheduleLoading -> {
                    state.copy(isLoading = true, newScheduleId = null)
                }

                else -> {
                    Log.w(TAG, "Action không được xử lý: $action")
                    state
                }
            }
        }
    }

}
