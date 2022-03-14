package com.myproject.radiojourney.presentation.content.radioList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.myproject.radiojourney.R
import com.myproject.radiojourney.model.presentation.RadioStationPresentation

// 1.1. ОБРАБОТКА КЛИКА -> передадим в конструктор анонимную функцию (как класса Adapter, так и вложенного класса). Затем отдаём эту лямбду каждому ViewHolder
class RadioListAdapter(
    private val radioStationList: List<RadioStationPresentation>,
    private val onItemClicked: (RadioStationPresentation) -> Unit
) :
    RecyclerView.Adapter<RadioListAdapter.RadioListViewHolder>() {
    companion object {
        private const val TAG = "RadioListAdapter"
    }

    // Создаём элемент списка. Initialize itemView for each item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioListViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_radio_station_list_item, parent, false)
        return RadioListViewHolder(itemView, onItemClicked)
    }

    // Сюда залетает элемент списка, к-рый был создан в onCreateViewHolder() и здесь мы его наполняем
    // Однако, лучше просто вызвать метод из вложенного класа, где и осуществить непосредственно наполнение, описание clickListener и проч.
    override fun onBindViewHolder(holder: RadioListViewHolder, position: Int) {
        holder.setRadioStation(radioStationList[position])
    }

    // Возвращает количество элементов списка
    override fun getItemCount(): Int =
        radioStationList.size

    inner class RadioListViewHolder(
        private val itemView: View,
        private val onItemClicked: (RadioStationPresentation) -> Unit
    ) :
        RecyclerView.ViewHolder(itemView) {
        private val textRadioStationName: AppCompatTextView =
            itemView.findViewById(R.id.text_radioStationName)
        private val textradioStationClickCount: AppCompatTextView =
            itemView.findViewById(R.id.text_radioStationClickCount2)

        // 1.2. ОБРАБОТКА КЛИКА -> Будем просто возвращать элемент, на который кликнули. Создадим переменную
        private var radioStation: RadioStationPresentation? = null

        // 1.4. ОБРАБОТКА КЛИКА -> В функции init{} д.б. view.setOnClickListener{}, который передаст информацию в фрагмент, а уже из фрагмента мы будем передавать информацию во view model
        init {
            itemView.setOnClickListener {
                // ОБРАБОТКА КЛИКА -> Передадим по клику нашу переменную, если она не null (передаём в нашу анонимную функцию)
                radioStation?.let { nonNullRadioStationPresentation ->
                    onItemClicked(nonNullRadioStationPresentation)
                }
            }
        }

        fun setRadioStation(radioStation: RadioStationPresentation) {
            // 1.3. ОБРАБОТКА КЛИКА -> В методе обработки элемента списка, инициализируем нашу переменную
            this.radioStation = radioStation

            textRadioStationName.text = radioStation.stationName
            textradioStationClickCount.text = radioStation.clickCount.toString()
        }
    }
}