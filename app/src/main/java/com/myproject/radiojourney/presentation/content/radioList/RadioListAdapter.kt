package com.myproject.radiojourney.presentation.content.radioList

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.myproject.radiojourney.R
import com.myproject.radiojourney.model.presentation.RadioStationPresentation

class RadioListAdapter(private val radioStationList: List<RadioStationPresentation>) :
    RecyclerView.Adapter<RadioListAdapter.RadioListViewHolder>() {
    companion object {
        private const val TAG = "RadioListAdapter"
    }

    // Создаём элемент списка. Initialize itemView for each item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioListViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_radio_station_list_item, parent, false)
        return RadioListViewHolder(itemView)
    }

    // Сюда залетает элемент списка, к-рый был создан в onCreateViewHolder() и здесь мы его наполняем
    // Однако, лучше просто вызвать метод из вложенного класа, где и осуществить непосредственно наполнение, описание clickListener и проч.
    override fun onBindViewHolder(holder: RadioListViewHolder, position: Int) {
        Log.d(TAG, "Проверка привязки (связь item и holder) - onBindViewHolder вызван")
        holder.setRadioStation(radioStationList[position])
    }

    // Возвращает количество элементов списка
    override fun getItemCount(): Int =
        radioStationList.size

    inner class RadioListViewHolder(private val itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val textRadioStationName: AppCompatTextView =
            itemView.findViewById(R.id.text_radioStationName)
        private val textradioStationClickCount: AppCompatTextView =
            itemView.findViewById(R.id.text_radioStationClickCount2)

        fun setRadioStation(radioStation: RadioStationPresentation) {
            textRadioStationName.text = radioStation.stationName
            textradioStationClickCount.text = radioStation.clickCount.toString()
        }
    }
}