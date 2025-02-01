package com.example.fixzy_ketnoikythuatvien.ui.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.fixzy_ketnoikythuatvien.R
// Adapter để gắn dữ liệu cho RecyclerView, dùng để hiển thị danh sách ảnh trong ViewPager2
class ImageAdapter (private val imageList: ArrayList<Int>, private val viewPager2: ViewPager2)
    : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    // Lớp ViewHolder lưu trữ các thành phần của item trong RecyclerView
    class ImageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        // Lấy tham chiếu đến ImageView trong layout item để hiển thị ảnh
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    // Phương thức này được gọi khi RecyclerView tạo ra một ViewHolder mới
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        // Inflate layout image_container để tạo view cho mỗi item trong RecyclerView
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_container, parent, false)
        // Trả về ViewHolder chứa ImageView của item
        return ImageViewHolder(view)
    }

    // Phương thức này trả về số lượng phần tử trong danh sách (số lượng ảnh)
    override fun getItemCount(): Int {
        return imageList.size
    }

    // Phương thức này sẽ gắn dữ liệu (ảnh) cho ImageView trong ViewHolder
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        // Đặt ảnh từ imageList vào ImageView của ViewHolder tại vị trí tương ứng
        holder.imageView.setImageResource(imageList[position])

        // Kiểm tra nếu đang ở vị trí cuối cùng trong danh sách
        if(position == imageList.size - 1) {
            // Nếu là ảnh cuối cùng, gọi runnable để nhân bản danh sách ảnh và cập nhật RecyclerView
            viewPager2.post(runnable)
        }
    }

    // Runnable để thực hiện thêm tất cả ảnh vào imageList một lần nữa, nhằm tạo hiệu ứng lặp vô hạn
    private val runnable = Runnable {
        // Nhân bản nội dung của imageList và thêm vào cuối danh sách
        imageList.addAll(imageList)
        // Cập nhật lại RecyclerView sau khi thay đổi dữ liệu
        notifyDataSetChanged()
    }
}
