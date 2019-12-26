package com.thevarungupta.selfieapp.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thevarungupta.selfieapp.R
import com.thevarungupta.selfieapp.models.Selfie
import kotlinx.android.synthetic.main.row_selfie_adapter.view.*


class AdapterSelfie(var mContext: Context, var mList: ArrayList<Selfie>) :
    RecyclerView.Adapter<AdapterSelfie.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.row_selfie_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var selfie = mList.get(position)
        holder.bindView(selfie)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(selfie: Selfie) {
            itemView.text_view.text = selfie.name

            var bitmap = BitmapFactory.decodeFile(selfie.path)
            itemView.image_view.setImageBitmap(bitmap)

        }
    }
}