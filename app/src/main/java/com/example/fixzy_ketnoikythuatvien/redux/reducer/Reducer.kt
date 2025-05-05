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
                is Action.SelectCategory -> {
                    Log.i(TAG, "Đã chọn danh mục: ${action.category?.name}")
                    state.copy(selectedCategory = action.category, services = emptyList())
                }
                is Action.FetchServicesRequest -> {
                    Log.i(TAG, "Đặt isLoadingServices=true")
                    state.copy(isLoadingServices = true, servicesError = null)
                }
                is Action.FetchServicesSuccess -> {
                    Log.i(TAG, "Dịch vụ đã tải: số lượng=${action.services.size}, chi tiết=${action.services.map { it.name }}")
                    state.copy(
                        isLoadingServices = false,
                        services = action.services,
                        servicesError = null
                    )
                }
                is Action.FetchServicesFailure -> {
                    Log.e(TAG, "Lỗi tải dịch vụ: ${action.error}")
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
                is Action.FetchAvailabilityRequest -> state.copy(isLoadingAvailability = true, availabilityError = null)
                is Action.FetchAvailabilitySuccess -> state.copy(isLoadingAvailability = false, availability = action.availability)
                is Action.FetchAvailabilityFailure -> state.copy(isLoadingAvailability = false, availabilityError = action.error)
                is Action.SelectService -> state.copy(selectedService = action.service)

                is Action.UpdateBooking -> {
                    Log.d(TAG, "UpdateBooking serviceId=${action.serviceId}")

                    val newBooking = state.booking?.copy(
                        userId = action.userId ?: state.booking?.userId,
                        serviceId = action.serviceId ?: state.booking?.serviceId,
                        availabilityId = action.availabilityId ?: state.booking?.availabilityId,
                        date = action.date ?: state.booking?.date,
                        startTime = action.startTime ?: state.booking?.startTime,
                        address = action.address ?: state.booking?.address,
                        phone = action.phone ?: state.booking?.phone,
                        notes = action.notes ?: state.booking?.notes,
                        totalPrice = action.totalPrice ?: state.booking?.totalPrice,
                    ) ?: Booking(
                        userId = action.userId,
                        serviceId = action.serviceId,
                        availabilityId = action.availabilityId,
                        date = action.date,
                        startTime = action.startTime,
                        address = action.address,
                        phone = action.phone,
                        notes = action.notes,
                        totalPrice = action.totalPrice,
                        status = action.status ?: "Pending"
                    )
                    state.copy(
                        booking = newBooking
                    )
                }
                is Action.CreateBooking -> state.copy(
                    isCreatingBooking = true,
                    createBookingError = null
                )
                is Action.CreateBookingSuccess -> state.copy(
                    isCreatingBooking = false,
                    referenceCode = action.referenceCode,
                    createBookingError = null
                )
                is Action.CreateBookingFailure -> state.copy(
                    isCreatingBooking = false,
                    createBookingError = action.error
                )

                else -> {
                    Log.w(TAG, "Action không được xử lý: $action")
                    state
                }
            }
        }
    }

}
