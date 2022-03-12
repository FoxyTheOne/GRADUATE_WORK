package com.myproject.radiojourney.presentation.content.dialogRadioStationList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.myproject.radiojourney.R

//class RadioStationListAdapter(private val radioStationList: List<RadioStation>, private val radioStationListener: IRadioStationListener): RecyclerView.Adapter<RadioStationListAdapter.RadioStationListViewHolder>() {
//
//    // Создаём элемент списка. Initialize itemView for each item
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioStationListViewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_radio_station_list_dialog_item, parent,false)
//        return RadioStationListViewHolder(itemView)
//    }
//
//    // Сюда залетает элемент списка, к-рый был создан в onCreateViewHolder() и здесь мы его наполняем
//    // Однако, лучше просто вызвать метод из вложенного класа, где и осуществить непосредственно наполнение, описание clickListener и проч.
//    // Bind the items to their data
//    override fun onBindViewHolder(holder: RadioStationListViewHolder, position: Int) {
////        holder.setRadioStation(radioStation[position])
//        holder.itemPosition = position
//        holder.bind()
//    }
//
//    // Возвращает количество элементов списка
//    override fun getItemCount(): Int = radioStationList.size
//
//    // Viewholder class for handling interactions with corresponding item
//    inner class RadioStationListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
//        var radioStationName: AppCompatTextView = itemView.findViewById(R.id.text_radioStationName)
//        var itemPosition:Int = 0
//
//        // bind data to item views
//        fun bind() {
//            itemView.setOnClickListener(this)
//            radioStationName.text = radioStationList[itemPosition].radioStationName // radioStationName - название станции. Написать правильно после создания класса
//        }
//
//        //report click events to dialog with listener
//        override fun onClick(v: View?) {
//            radioStationListener.onRadioStationClicked(station = radioStationList[itemPosition], position = itemPosition)
//        }
//    }
//}


//class AgeGroupAdapter(private val ageList:ArrayList<AgeGroupModel>, private val listenerAge: AgeItemSelectListener): RecyclerView.Adapter<AgeGroupAdapter.AgeGroupViewHolder>() {
//
//    //initialize itemView for each item
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgeGroupViewHolder {
//        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.layout_age_group_item,parent,false)
//        return AgeGroupViewHolder(itemView)
//    }
//
//    //bind the items to their data
//    override fun onBindViewHolder(holder: AgeGroupViewHolder, position: Int) {
//        holder.itemPosition = position
//        holder.bind()
//    }
//
//    // return total number of items to be displayed
//    override fun getItemCount(): Int {
//        return ageList.size
//    }
//
//    //Viewholder class for handling interactions with corresponding item
//    inner class AgeGroupViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
//        var label:TextView = itemView.findViewById(R.id.label)
//        var group:TextView = itemView.findViewById(R.id.group)
//        var itemPosition:Int = 0
//
//        // bind data to item views
//        fun bind(){
//            itemView.setOnClickListener(this)
//            label.text = ageList.get(itemPosition).label
//            val ag:String = ageList.get(itemPosition).startInterval.toString()+"-"+ageList.get(itemPosition).endInterval+" years"
//            group.text = ag
//        }
//
//        //report click events to dialog with listener
//        override fun onClick(v: View?) {
//            listenerAge.itemClicked(ageList.get(itemPosition),itemPosition)
//        }
//    }
//}