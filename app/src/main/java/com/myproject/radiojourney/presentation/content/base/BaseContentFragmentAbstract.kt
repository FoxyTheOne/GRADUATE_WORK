package com.myproject.radiojourney.presentation.content.base

import androidx.fragment.app.Fragment
import com.myproject.radiojourney.presentation.content.dialogLogOut.ILogOutListener
import com.myproject.radiojourney.presentation.content.dialogLogOut.LogOutDialogFragment
import com.myproject.radiojourney.presentation.content.dialogRadioStationList.IRadioStationListener
import com.myproject.radiojourney.presentation.content.dialogRadioStationList.RadioStationListDialogFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Базовый фрагмент для расширения content фрагментами.
 * Содержит логику открытия всплывающего окна при нажатии на кнопку выхода на тулбаре. Непосредственно логика метода onLogOut() описывается в фрагменте, который содержит тулбар
 */
@AndroidEntryPoint
abstract class BaseContentFragmentAbstract: Fragment(), ILogOutListener, IRadioStationListener {
//    // TODO
//    companion object {
//        var SELECTED_CITY = ""
//        var SELECTED_STATION_LIST = listOf<RadioStation>()
//    }

    fun showLogoutDialog() {
        val supportFragment = requireActivity().supportFragmentManager
        // Создаём LogOutDialogFragment для диалогового окна
        val logOutDialogFragment = LogOutDialogFragment()
        logOutDialogFragment.setLogOutListener(this)
        logOutDialogFragment.show(supportFragment, "LogOutDialogFragment")
    }

//    fun showRadioStationListDialog(countryName: String, radioStationList: List<RadioStation>) {
//        val supportFragment = requireActivity().supportFragmentManager
//        // Создаём RadioStationListDialogFragment для диалогового окна
//        SELECTED_CITY = countryName
//        SELECTED_STATION_LIST = radioStationList
//        val radioStationListDialogFragment = RadioStationListDialogFragment()
//        radioStationListDialogFragment.setRadioStationListener(this)
//        radioStationListDialogFragment.show(supportFragment, "RadioStationListDialogFragment")
//    }
}