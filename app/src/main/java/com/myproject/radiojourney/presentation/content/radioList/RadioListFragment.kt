package com.myproject.radiojourney.presentation.content.radioList

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.myproject.radiojourney.IAppSettings
import com.myproject.radiojourney.R
import com.myproject.radiojourney.data.repository.ContentRepository
import com.myproject.radiojourney.model.presentation.CountryPresentation
import com.myproject.radiojourney.model.presentation.RadioStationPresentation
import com.myproject.radiojourney.presentation.content.base.BaseContentFragmentAbstract
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class RadioListFragment : BaseContentFragmentAbstract() {
    companion object {
        private const val TAG = "RadioListFragment"
    }

    @Inject
    lateinit var appSettings: IAppSettings

    private val viewModel by viewModels<RadioListViewModel>()
    private lateinit var dialogInternetTrouble: Dialog
    private lateinit var frameLayout: FrameLayout
    private lateinit var progressCircular: ProgressBar
    private lateinit var countryCode: String
    private lateinit var countryName: String
    private lateinit var textRadioStationCity: AppCompatTextView
    private lateinit var radioStationListRecyclerView: RecyclerView
    private lateinit var radioStationListAdapter: RadioListAdapter
    private var radioStationList = listOf<RadioStationPresentation>(
        RadioStationPresentation("Test", "test", 2, "test")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // TOOLBAR
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.layout_radio_station_list, container, false)
        // TOOLBAR - где будет находиться в нашем layout
        appSettings.setToolbar(view?.findViewById(R.id.home_toolbar))
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Переменные для отображения прогресса
        frameLayout = view.findViewById(R.id.frameLayout)
        progressCircular = view.findViewById(R.id.progressCircular)

        // Получаем результат с предыдущей страницы
        arguments?.getString("country_code")?.let { country_code_string ->
            val resultArray = country_code_string.split("||")
            countryCode = resultArray[0]
//            viewModel.countryCode = resultArray[0]
            countryName = resultArray[1]
        }
        textRadioStationCity = view.findViewById(R.id.text_radioStationDialogCity)
        textRadioStationCity.text = countryName
        radioStationListRecyclerView = view.findViewById(R.id.recyclerView_radioStationList)
//        radioStationListRecyclerView.adapter = RadioListAdapter(radioStationList)

        // Получаем список радиостанций, преобразуем. Сохранять в Room не будем. Радиостанций очень много, будет занимать много места на телефоне.
        // Кроме того, списки на сервере постоянно обновляются. Возможно какой-то радиостанции в списке уже не будет, а в локальной БД она ещё осталась. Пользователь выберет её и будет ошибка.
        viewModel.getRadioStationList(countryCode)

        // Настройки диалогового окна
        dialogInternetTrouble = Dialog(requireContext())
        // Передайте ссылку на разметку
        dialogInternetTrouble.setContentView(R.layout.layout_internet_trouble_dialog)

//        initListeners()
        subscribeOnLiveData()
    }

    private fun initListeners() {
        TODO("Not yet implemented")
    }

    private fun subscribeOnLiveData() {
        // Показываем или прячем Progress
        viewModel.showProgressLiveData.observe(viewLifecycleOwner, {
            showProgress()
        })
        viewModel.hideProgressLiveData.observe(viewLifecycleOwner, {
            hideProgress()
        })
        viewModel.dialogInternetTroubleLiveData.observe(viewLifecycleOwner, {
            dialogInternetTrouble.show()
        })
        viewModel.radioStationListLiveData.observe(
            viewLifecycleOwner,
            { radioStationPresentationList ->
                radioStationList = radioStationPresentationList
                showProgress()

                // 1.5. ОБРАБОТКА КЛИКА -> Получаем результат клика во фрагменте (описываем нашу анонимную функцию из RecyclerView)
                radioStationListRecyclerView.adapter =
                    RadioListAdapter(radioStationList) { radioStationPresentationOnClick ->
                        Log.d(TAG, "Выбранный элемент списка: $radioStationPresentationOnClick")
                        // Открываем по клику другой фрагмент, передаём туда нашу радиостанцию
                        val direction = RadioListFragmentDirections.actionRadioListFragmentToHomeRadioFragment(radioStationPresentationOnClick)
                        this.findNavController().navigate(direction)
                    }

                radioStationPresentationList.forEach { radioStation ->
                    Log.d(
                        TAG,
                        "результат запроса в локальную БД (радиостанции): ${radioStation.countryCode}, ${radioStation.url}"
                    )
                }
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
        this.findNavController().navigate(R.id.action_homeRadioFragment_to_auth_nav_graph)
    }
}