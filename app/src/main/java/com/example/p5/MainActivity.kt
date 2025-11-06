package com.example.p5

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.postapp.databinding.ActivityMainBinding
import com.example.postapp.databinding.DialogAddPostBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PostAdapter
    private val posts = mutableListOf<Post>()
    private var imageUri: Uri? = null

    // Launcher untuk memilih gambar dari galeri
    private val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri = it
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi adapter RecyclerView
        adapter = PostAdapter(posts,
            onEdit = { index -> showAddPostDialog(isEdit = true, index = index) },
            onDelete = { index ->
                Toast.makeText(this, "Hapus ${posts[index].username}", Toast.LENGTH_SHORT).show()
                posts.removeAt(index)
                adapter.notifyDataSetChanged()
            })

        binding.rvPosts.layoutManager = LinearLayoutManager(this)
        binding.rvPosts.adapter = adapter

        // Tombol tambah post
        binding.btnAdd.setOnClickListener {
            showAddPostDialog(isEdit = false)
        }
    }

    // Fungsi untuk menampilkan dialog tambah/edit postingan
    private fun showAddPostDialog(isEdit: Boolean, index: Int = -1) {
        val dialogBinding = DialogAddPostBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this).setView(dialogBinding.root).create()

        // Jika mode edit, isi field dengan data lama
        if (isEdit) {
            val post = posts[index]
            dialogBinding.etUsername.setText(post.username)
            dialogBinding.etCaption.setText(post.caption)
            Glide.with(this).load(post.imageUri).into(dialogBinding.imgPreview)
            imageUri = Uri.parse(post.imageUri)
        }

        // Pilih gambar dari galeri
        dialogBinding.btnAddImage.setOnClickListener {
            imagePicker.launch("image/*")
        }

        // Simpan post baru atau update post lama
        dialogBinding.btnSave.setOnClickListener {
            val username = dialogBinding.etUsername.text.toString()
            val caption = dialogBinding.etCaption.text.toString()
            val image = imageUri?.toString()

            if (username.isBlank() || caption.isBlank() || image == null) {
                Toast.makeText(this, "Isi semua kolom dulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isEdit) {
                posts[index] = Post(username, caption, image)
                Toast.makeText(this, "Post diperbarui", Toast.LENGTH_SHORT).show()
            } else {
                posts.add(Post(username, caption, image))
                Toast.makeText(this, "Post ditambahkan", Toast.LENGTH_SHORT).show()
            }

            adapter.notifyDataSetChanged()
            dialog.dismiss()
        }

        dialog.show()
    }
}

