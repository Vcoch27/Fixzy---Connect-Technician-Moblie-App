package com.example.fixzy_ketnoikythuatvien.data.model

data class TestData(
    val id: Int,
    val count: Int
)

data class TestApiResponse(
    val success: Boolean,
    val data: List<TestData>? = null,
    val message: String? = null
)

data class UpdateCountRequest(
    val id: Int
)

data class UpdateCountResponse(
    val success: Boolean,
    val data: TestData? = null,
    val message: String? = null
)