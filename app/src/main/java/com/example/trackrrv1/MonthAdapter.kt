package com.example.trackrrv1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment

class MonthAdapter(contextFrag : Fragment, monthList : List<String>) : BaseAdapter() {
    var frag : Fragment = contextFrag
    var months : List<String> = monthList


    override fun getCount(): Int {
        return 12
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var rootView : View = LayoutInflater.from(frag.context).inflate(R.layout.item_month, parent, false)
        var monthName = rootView.findViewById<TextView>(R.id.monthTV)
        monthName.setText(months.get(position))
        return rootView
    }
}