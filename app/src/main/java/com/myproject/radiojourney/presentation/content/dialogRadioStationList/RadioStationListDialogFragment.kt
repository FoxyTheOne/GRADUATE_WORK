package com.myproject.radiojourney.presentation.content.dialogRadioStationList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myproject.radiojourney.R
import com.myproject.radiojourney.presentation.content.base.BaseContentFragmentAbstract
import com.myproject.radiojourney.presentation.content.dialogLogOut.ILogOutListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

/**
 * Фрагмент всплывающего окна для уточнения перед выходом
 */
@AndroidEntryPoint
class RadioStationListDialogFragment : DialogFragment() {
//    // Переменная нашего интерфейса, чтобы вызвать его метод
//    private var listener: IRadioStationListener? = null
//
//    fun setRadioStationListener(listener: IRadioStationListener) {
//        this.listener = listener
//    }
//
//    // RecyclerView for displaying fruit names to the screen
//    private lateinit var radioStationListRecyclerView: RecyclerView;
//
//    // Dialog view is created
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        Objects.requireNonNull(dialog)?.window!!.requestFeature(Window.FEATURE_NO_TITLE)
//        return inflater.inflate(R.layout.layout_radio_station_list_dialog, container, false)
//    }
//
//    // Dialog view is ready
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val radioStationCity: AppCompatTextView = view.findViewById(R.id.text_radioStationDialogCity)
//        radioStationCity.text = BaseContentFragmentAbstract.SELECTED_CITY
//
//        // initialize and setup RecyclerView
//        radioStationListRecyclerView = view.findViewById(R.id.recyclerView_radioStationList)
//        radioStationListRecyclerView.setHasFixedSize(true)
////        radioStationListRecyclerView.setItemViewCacheSize(20)
//        radioStationListRecyclerView.layoutManager  = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
//
//        // ArrayList to be displayed in RecyclerView
//        val radioStationList = BaseContentFragmentAbstract.SELECTED_STATION_LIST
//
//        // create FruitItemListener to listen to click event on items from the RecyclerView Adapter
//        val listener:IRadioStationListener = object : IRadioStationListener {
//            override fun onFruitClicked(fruit: String, position: Int) {
//                //when item in adapter is clicked, show selected fruit in an AlertDialog
//                showSelectedFruit(fruit)
//            }
//
//            override fun onRadioStationClicked(station: RadioStation, position: Int) {
//                // When item in adapter is clicked, show selected radioStation in an AlertDialog
//            }
//        }
//
//        // create Adapter and pass as parameters the fruits and the FruitItemListener
//        val adapter: RadioStationListAdapter = RadioStationListAdapter(radioStationList, listener)
//        radioStationListRecyclerView.adapter = adapter
//
//    }
}