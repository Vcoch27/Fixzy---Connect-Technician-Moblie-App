package com.example.fixzy_ketnoikythuatvien

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.fixzy_ketnoikythuatvien.ui.components.ImageAdapter
import kotlin.math.abs
// Lớp MainActivity kế thừa từ Activity, chịu trách nhiệm quản lý giao diện chính
class MainActivity : Activity() {

    // Các biến lateinit được khai báo cho ViewPager2, Handler, danh sách ảnh và adapter
    private lateinit var viewPager2: ViewPager2
    private lateinit var handler: Handler
    private lateinit var imageList: ArrayList<Int>
    private lateinit var adapter: ImageAdapter

    // Phương thức onCreate được gọi khi Activity được tạo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Cấu hình cho EditText tìm kiếm với icon tìm kiếm
        val editText = findViewById<EditText>(R.id.editTextSearch)
        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_search)
        drawable?.setBounds(0, 0, 60, 60) // Đặt kích thước icon tìm kiếm
        editText.setCompoundDrawables(null, null, drawable, null) // Gắn icon vào bên phải EditText

        // Khởi tạo các thành phần và thiết lập ViewPager2
        init()
        setUpTranformer()

        // Lắng nghe sự kiện thay đổi trang của ViewPager2
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Khi chuyển sang một trang mới, xóa các runnable hiện tại và đặt lại sau 2 giây
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 5000)
            }
        })
    }

    // Khi Activity tạm dừng, xóa các runnable để ngừng việc tự động chuyển trang
    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    // Khi Activity tiếp tục, đặt lại runnable để tự động chuyển trang sau 2 giây
    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, 5000)
    }

    // Runnable để tự động chuyển trang
    private val runnable = Runnable {
        viewPager2.currentItem = viewPager2.currentItem + 1 // Chuyển sang trang tiếp theo
    }

    // Thiết lập hiệu ứng chuyển đổi trang cho ViewPager2
    private fun setUpTranformer() {
        val transformer = CompositePageTransformer()

        // Thêm hiệu ứng khoảng cách giữa các trang
        transformer.addTransformer(MarginPageTransformer(40))

        // Thêm hiệu ứng thu nhỏ trang dựa trên vị trí
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position) // Giá trị `r` giảm dần khi trang xa khỏi vị trí trung tâm
            page.scaleY = 0.85f + r * 0.14f // Tăng kích thước trang gần trung tâm
        }

        // Gắn transformer vào ViewPager2
        viewPager2.setPageTransformer(transformer)
    }

    // Khởi tạo ViewPager2 và các thành phần liên quan
    private fun init() {
        // Gắn ViewPager2 từ layout
        viewPager2 = findViewById(R.id.viewPage2)

        // Tạo handler cho việc xử lý runnable
        handler = Handler(Looper.myLooper()!!)

        // Tạo danh sách ảnh và thêm dữ liệu
        imageList = ArrayList()
        imageList.add(R.drawable.coc)
        imageList.add(R.drawable.coc)
        imageList.add(R.drawable.coc)
        imageList.add(R.drawable.coc)
        imageList.add(R.drawable.coc)
        imageList.add(R.drawable.coc)
        imageList.add(R.drawable.coc)

        // Tạo adapter và gắn nó vào ViewPager2
        adapter = ImageAdapter(imageList, viewPager2)
        viewPager2.adapter = adapter

        // Thiết lập ViewPager2 để hiển thị các trang một cách mượt mà
        viewPager2.offscreenPageLimit = 3 // Giữ tối đa 3 trang trong bộ nhớ
        viewPager2.clipToPadding = false // Cho phép hiển thị một phần của trang liền kề
        viewPager2.clipChildren = false // Tương tự như trên
        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER // Tắt hiệu ứng cuộn quá
    }
}
