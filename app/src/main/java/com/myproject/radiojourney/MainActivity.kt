package com.myproject.radiojourney

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import com.myproject.radiojourney.utils.service.ProgressForegroundService
import dagger.hilt.android.AndroidEntryPoint

/**
 * Приложение использует API.radio-browser.info (https://www.radio-browser.info/, распространяется по лицензии GPL),
 * которое позволяет получить доступ к интернет-радиостанциям со всего мира.
 *
 * На главной странице есть карта с маркерами стран, нажав на которые можно увидеть количество радиостанций в этой стране, доступных по используемому api.
 * Нажав на это всплывающее сообщение, можно перейти непосредственно на список радиостанций конкретной страны и выбрать интересующее радио.
 * После нажатия, пользователь переходит обратно на главную страницу и может прослушать выбранную радиостанцию.
 *
 * В этом проекте используется шаблон проектирования архитектуры приложения MVVM. Используется подход Clean Architecture.
 * При создании проекта я старалась опираться на принципы проектирования SOLID (Single responsibility, open-closed, liskov substitution, interface segregation, dependency inversion)
 * В проекте используется dependency injection Hilt, т.к. это рекомендация google.
 * Так же в проекте я использую navigation, что я считаю очень удобным.
 * В фрагментах с аунтентификацией я использую View binding.
 *
 * Так же в проекте я научилась использовать свой custom font, color style, button color (градиент) и др.
 * Пригодились мне так же MediaPlayer, Locale и Geocoder.
 * Названия всех интерфейсов я начинаю с буквы I, чтобы было порще ориентироваться в коде проекта.
 *
 * Для хранения небольших пар ключ-значение (логин и пароль, токен) я использую Shared preferences.
 * Shared preferences д.б. быть Singleton, поэтому для верности в Module я пометила аннотацией @Singleton.
 * Так же в проекте используется реляционная база данных Room.
 * Для кеширования с уведомлением используется foreground service.
 *
 * Для запросов в ViewModel я пользуюсь Coroutines.
 * В основном использую Dispatchers.IO (создаёт необходмое количество потоков, но минимум 64), т.к. он предназначен
 * для выполнения операций ввода-вывода (н-р, операции с файлами, сетевыми запросами, локальной базой данных).
 * А так же launch, когда нам нужно просто сделать вызов функции.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), IAppSettings {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // COUNTRY LIST MARKERS ON MAP -> 1. Получаем список кодов стран, преобразуем в локальные модели, сохраняем в Room.
        // Делается 1 раз, при запуске приложения и по окончанию stopSelf()
        this.startService(
            Intent(
                this,
                ProgressForegroundService::class.java
            )
        )
    }

//    override fun onBackPressed() {
//        val fragmentCount = supportFragmentManager.backStackEntryCount
//
//        if (fragmentCount >= 1) {
//            super.onBackPressed()
//        } else {
//            finish()
//        }
//    }

    private fun clearBackStack() =
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

    override fun setToolbar(toolbar: Toolbar?) {
        setSupportActionBar(toolbar)
    }

}