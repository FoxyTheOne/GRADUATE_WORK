package com.myproject.radiojourney.presentation.content.favouriteList

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.myproject.radiojourney.IAppSettings
import com.myproject.radiojourney.R
import com.myproject.radiojourney.presentation.content.base.BaseContentFragmentAbstract
import com.myproject.radiojourney.presentation.content.radioList.RadioListAdapter
import com.myproject.radiojourney.presentation.content.radioList.RadioListFragment
import com.myproject.radiojourney.presentation.content.radioList.RadioListFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavouriteListFragment : BaseContentFragmentAbstract() {
    companion object {
        private const val TAG = "FavouriteListFragment"
    }

    @Inject
    lateinit var appSettings: IAppSettings

    private val viewModel by viewModels<FavouriteListViewModel>()
    private lateinit var recyclerViewRadioStationList: RecyclerView
    private lateinit var frameLayout: FrameLayout
    private lateinit var progressCircular: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // TOOLBAR
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.layout_radio_station_list_favourite, container, false)
        // TOOLBAR - где будет находиться в нашем layout
        appSettings.setToolbar(view?.findViewById(R.id.home_toolbar))
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewRadioStationList = view.findViewById(R.id.recyclerView_radioStationList)

        // Переменные для отображения прогресса
        frameLayout = view.findViewById(R.id.frameLayout)
        progressCircular = view.findViewById(R.id.progressCircular)

        // Получаем список избранного для отображения
        viewModel.getRadioStationFavouriteListAndShow()

        subscribeOnLiveData()
    }

    private fun subscribeOnLiveData() {
        // Показываем или прячем Progress
        viewModel.showProgressLiveData.observe(viewLifecycleOwner, {
            showProgress()
        })
        viewModel.hideProgressLiveData.observe(viewLifecycleOwner, {
            hideProgress()
        })
        viewModel.favouritesFailedLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(
                context,
                "Interacting with favourites failed. Smth wrong with your token. Try re-login.",
                Toast.LENGTH_LONG
            ).show()
        })
        viewModel.failedLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, "Failure. Something went wrong", Toast.LENGTH_LONG).show()
        })
        viewModel.radioStationFavouriteListLiveData.observe(
            viewLifecycleOwner,
            { radioStationFavouritePresentationList ->
                showProgress()

                // 1.5. ОБРАБОТКА КЛИКА -> Получаем результат клика во фрагменте (описываем нашу анонимную функцию из RecyclerView)
                recyclerViewRadioStationList.adapter =
                    FavouiteListAdapter(radioStationFavouritePresentationList) { radioStationFavouriteOnClick ->
                        Log.d(TAG, "Выбранный элемент списка: $radioStationFavouriteOnClick")
                        // Открываем по клику другой фрагмент, передаём туда нашу радиостанцию
                        val direction =
                            FavouriteListFragmentDirections.actionFavouriteListFragmentToHomeRadioFragment(
                                radioStationFavouriteOnClick
                            )
                        this.findNavController().navigate(direction)
                    }

                Log.d(
                    TAG,
                    "Успешный запрос в локальную БД (радиостанции). Получен результат: массив size = ${radioStationFavouritePresentationList.size}, элемент[0] = ${radioStationFavouritePresentationList[0].countryCode}, ${radioStationFavouritePresentationList[0].url}"
                )

                hideProgress()
            })
    }

    private fun showProgress() {
        frameLayout.isVisible = true
        progressCircular.isVisible = true
    }

    private fun hideProgress() {
        frameLayout.isVisible = false
        progressCircular.isVisible = false
    }

    // TOOLBAR
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_toolbar_menu, menu)
    }

    // TOOLBAR - обработка клика
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.log_out -> {
            showLogoutDialog()
            Log.d(TAG, "showLogoutDialog() was called")
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            Log.d(TAG, "else result")
            super.onOptionsItemSelected(item)
        }
    }

    // TOOLBAR - Описываем метод из интерфейса ILogOutListener для выхода из аккаунта приложения
    override fun onLogOut() {
        viewModel.logout()
        this.findNavController().navigate(R.id.action_favouriteListFragment_to_auth_nav_graph)
    }
}