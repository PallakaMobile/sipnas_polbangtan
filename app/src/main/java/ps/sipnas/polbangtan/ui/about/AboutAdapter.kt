package ps.sipnas.polbangtan.ui.aboutimport

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ps.sipnas.polbangtan.R
import ps.sipnas.polbangtan.databinding.FragmentAboutItemBinding
import ps.sipnas.polbangtan.ui.about.AboutFragment


/**
 **********************************************
 * Created by ukie on 10/24/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class AboutAdapter : RecyclerView.Adapter<AboutAdapter.AboutHolder>() {
    private lateinit var list: List<AboutFragment.About>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutHolder =
            AboutHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.fragment_about_item, parent, false))

    override fun onBindViewHolder(holder: AboutHolder, position: Int) = holder.bind(list[position], position)

    override fun getItemCount() = if (::list.isInitialized) list.size else 0

    fun updateAboutAdapter(list: List<AboutFragment.About>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class AboutHolder(private val binding: FragmentAboutItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: AboutFragment.About, position: Int) = with(itemView) {
            binding.dataAbout = data
            if (position == 2) binding.vDivider.visibility = View.GONE
            setOnClickListener {
                when (position) {
                    0 -> {
                        val gmmIntentUri = Uri.parse("geo:0,0?q=${data.desc}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        itemView.context.startActivity(mapIntent)
                    }
                    1 -> {
                        @Suppress("DEPRECATION")
                        binding.tvContent.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            Html.fromHtml("<a href='https://polbangtan-gowa.ac.id'>polbangtan-gowa.ac.id</a>", Html.FROM_HTML_MODE_LEGACY)
                        else Html.fromHtml("<a href='https://polbangtan-gowa.ac.id'>polbangtan-gowa.ac.id</a>")
                        binding.tvContent.autoLinkMask = Linkify.ALL
                    }
                    2 -> {
                        val mailIntent = Intent(Intent.ACTION_SENDTO)
                        mailIntent.data = Uri.parse("mailto:${data.desc}")
                        itemView.context.startActivity(mailIntent)
                    }
                }
            }
        }
    }
}