package com.example.interviewtestproject.home

import android.util.Log
import com.example.interviewtestproject.R
import com.example.interviewtestproject.model.RecipeClass
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_list.view.*
import kotlinx.android.synthetic.main.item_list_step.view.*

class StepItemListView(val stepData: String): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.stepItemListNumber.text = "Step ${position + 1}:"
        viewHolder.itemView.stepItemListDescription.text = stepData




//        val f: NumberFormat = DecimalFormat("00")
//
//        val monthString = DateFormatSymbols().months[ts.monthts.toInt() - 1]
//        viewHolder.itemView.timeslot_date.text = "$monthString, ${ts.dayts}"
////        viewHolder.itemView.displyTimeSlot_time.text = "${f.format(ts.hourts.toInt())}:${f.format(ts.minutets.toInt())} - ${f.format(
////            ts.endHour.toInt()
////        )}:${f.format(ts.endMinute.toInt())}"
//
//        val _24HourTimeStart = "${f.format(ts.hourts.toInt())}:${f.format(ts.minutets.toInt())}"
//        val _24HourSDFStart = SimpleDateFormat("HH:mm")
//        val _12HourSDFStart = SimpleDateFormat("hh:mm a")
//        val _24HourDtStart = _24HourSDFStart.parse(_24HourTimeStart)
//
//        val _24HourTimeEnd = "${f.format(ts.endHour.toInt())}:${f.format(ts.endMinute.toInt())}"
//        val _24HourSDFEnd = SimpleDateFormat("HH:mm")
//        val _12HourSDFEnd = SimpleDateFormat("hh:mm a")
//        val _24HourDtEnd = _24HourSDFEnd.parse(_24HourTimeEnd)
////        System.out.println(_24HourDt)
////        println(_12HourSDFEnd.format(_24HourDtEnd))
//
//        viewHolder.itemView.displyTimeSlot_time.text = "${_12HourSDFStart.format(_24HourDtStart)} - ${_12HourSDFEnd.format(_24HourDtEnd)}"
//
//        val c = Calendar.getInstance()
//        val cyear = c.get(Calendar.YEAR)
//        val month = c.get(Calendar.MONTH)
//        val cmonth = month+1
//        val cday = c.get(Calendar.DAY_OF_MONTH)
//        val chour = c.get(Calendar.HOUR_OF_DAY)
//        val cminute = c.get(Calendar.MINUTE)
//        val currenttime = "$cyear-${f.format(cmonth)}-${f.format(cday)} ${f.format(chour)}:${f.format(
//            cminute
//        )}:00"
//        val selectedtime = "${ts.yearts}-${f.format(ts.monthts.toInt())}-${f.format(ts.dayts.toInt())} ${f.format(
//            ts.hourts.toInt()
//        )}:${f.format(
//            ts.minutets.toInt()
//        )}:00"
//        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//        val DBDate = dateFormat.parse(selectedtime)
//        val currentDate = dateFormat.parse(currenttime)
//        if(selectedtime.compareTo(currenttime) > 0) {
//            val diff: Long = currentDate.time - DBDate.time
//            val rseconds = diff / 1000
//            val rminutes = rseconds / 60
//            val rhours = rminutes / 60
//            val rdays = rhours / 24
//            viewHolder.itemView.timeslot_leftday.text = " left $rdays days"
//        }else{
//            Log.d("ViewTimeSlot", " the current database date is out dated >>>> pls perform delete")
//            viewHolder.itemView.timeslot_leftday.text = " left 1 days"
//        }
//
//        var countTS = 0
//        val currentMerchantid = currentdata1!!.merchantuid
//        val currentTimeSlot = ts.timeslotID
//        val ref = FirebaseDatabase.getInstance().getReference("/HostelBooking")
//        ref.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(p0: DataSnapshot) {
//                p0.children.forEach {
//                    val user = it.getValue(CurrentAgentTs::class.java)
//                    if (user != null && user.merchantuid == currentMerchantid && user.timeSlotID == currentTimeSlot) {
//                        countTS++
//                    }
//                }
//                viewHolder.itemView.Aviable_timeSlot.text = countTS.toString()
//            }
//
//            override fun onCancelled(p0: DatabaseError) {
//
//            }
//        })
//
//
//        //color
//        if(monthString.contains("january", ignoreCase = true)){
//            viewHolder.itemView.the_category_color.setBackgroundColor(Color.parseColor("#FF0000"))
//        }else if (monthString.contains("feb", ignoreCase = true)){
//            viewHolder.itemView.the_category_color.setBackgroundColor(Color.parseColor("#FFa500"))
//        }else if (monthString.contains("march", ignoreCase = true)){
//            viewHolder.itemView.the_category_color.setBackgroundColor(Color.parseColor("#FFff00"))
//        }else if (monthString.contains("april", ignoreCase = true)){
//            viewHolder.itemView.the_category_color.setBackgroundColor(Color.parseColor("#00ff00"))
//        }else if (monthString.contains("may", ignoreCase = true)){
//            viewHolder.itemView.the_category_color.setBackgroundColor(Color.parseColor("#FF00ff"))
//        }else if (monthString.contains("june", ignoreCase = true)){
//            viewHolder.itemView.the_category_color.setBackgroundColor(Color.parseColor("#0000ff"))
//        }else if (monthString.contains("july", ignoreCase = true)){
//            viewHolder.itemView.the_category_color.setBackgroundColor(Color.parseColor("#00FFff"))
//        }else if (monthString.contains("aug", ignoreCase = true)){
//            viewHolder.itemView.the_category_color.setBackgroundColor(Color.parseColor("#808080"))
//        }else if (monthString.contains("sep", ignoreCase = true)){
//            viewHolder.itemView.the_category_color.setBackgroundColor(Color.parseColor("#000000"))
//        }else if (monthString.contains("oct", ignoreCase = true)){
//            viewHolder.itemView.the_category_color.setBackgroundColor(Color.parseColor("#808000"))
//        }else if (monthString.contains("nov", ignoreCase = true)){
//            viewHolder.itemView.the_category_color.setBackgroundColor(Color.parseColor("#008000"))
//        }else if (monthString.contains("dec", ignoreCase = true)){
//            viewHolder.itemView.the_category_color.setBackgroundColor(Color.parseColor("#c0c0c0"))
//        }else {
//            viewHolder.itemView.the_category_color.setBackgroundColor(Color.parseColor("#ffffff"))
//        }
//
////        Picasso.get().load(user.merchantPic).into(viewHolder.itemView.imageview_new_message)
    }

    override fun getLayout(): Int {
        return R.layout.item_list_step
    }
}