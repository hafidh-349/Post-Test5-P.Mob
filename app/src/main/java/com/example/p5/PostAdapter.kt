package com.example.p5

class PostAdapter(
    private val posts: MutableList<Post>,
    private val onEdit: (Int) -> Unit,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.binding.tvUsername.text = post.username
        holder.binding.tvCaption.text = post.caption

        Glide.with(holder.itemView.context)
            .load(post.imageUri)
            .into(holder.binding.imgPost)

        holder.itemView.setOnLongClickListener {
            val options = arrayOf("Edit", "Hapus")
            AlertDialog.Builder(holder.itemView.context)
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> onEdit(position)
                        1 -> onDelete(position)
                    }
                }
                .show()
            true
        }
    }

    override fun getItemCount(): Int = posts.size
}
