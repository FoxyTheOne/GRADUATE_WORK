# TMS_GRADUATE_WORK
It is a repository for preparing graduate work.

This source code is free for studying purposes but you are not allowed to copy and use it in other applications (projects).

Created by Alina Piatrova.

------------------

Приложение использует API.radio-browser.info, которое позволяет получить доступ к интернет-радиостанциям со всего мира (https://www.radio-browser.info/). 
Этот API доступен бесплатно. Автор разрешает его использовать в бесплатном и платном программном обеспечении без ограничений.
 
На главной странице есть карта с маркерами стран, нажав на которые можно увидеть количество радиостанций в этой стране, доступных по используемому api.

Нажав на это всплывающее сообщение, можно перейти непосредственно на список радиостанций конкретной страны и выбрать интересующее радио.

После нажатия, пользователь переходит обратно на главную страницу и может прослушать выбранную радиостанцию.

В этом проекте используется шаблон проектирования архитектуры приложения MVVM. Используется подход Clean Architecture. При создании проекта я старалась опираться на принципы проектирования SOLID (Single responsibility, open-closed, liskov substitution, interface segregation, dependency inversion). В проекте используется dependency injection Hilt, т.к. это рекомендация google. Так же в проекте я использую navigation, что я считаю очень удобным. В фрагментах с аунтентификацией я использую View binding.

Так же в проекте я научилась использовать свой custom font, color style, button color (градиент) и др. Пригодились мне так же MediaPlayer, Locale и Geocoder. Названия всех интерфейсов я начинаю с буквы I, чтобы было порще ориентироваться в коде проекта.

Для хранения небольших пар ключ-значение (логин и пароль, токен) я использую Shared preferences. Так же в проекте используется реляционная база данных Room. Для кеширования с уведомлением используется foreground service. Для запросов в ViewModel я пользуюсь Coroutines.

------------------

При использовании функции родного android геокодера для получения местоположения по адресу - geocoder.getFromLocationName(), довольно часто приходит исключение «grpc failed». 

Процитирую один из комментариев по этому поводу с сайта stackoverflow.com:

It looks like this is ongoing issue that was reported in the Google issue tracker both for real devices and emulators. You can refer to the following bugs:

https://issuetracker.google.com/issues/64418751

https://issuetracker.google.com/issues/64247769

Unfortunately, Google haven't solved these issues yet.

As a workaround you can consider using the Geocoding API web service. Please note that there is a Java client library for web services that you can find on Github:

https://github.com/googlemaps/google-maps-services-java

Using Java client library for web services you can implement reverse geocoding lookup that shouldn't give you the error that you experience with native Android geocoder.

The Javadoc for client library is located at

https://googlemaps.github.io/google-maps-services-java/v0.2.5/javadoc/

I hope this helps!

В своём проекте я не использовала Geocoding API на данный момент, т.к. он платный. Если при первом запуске программы во время кэширования возникнет ошибка, следует закрыть программу и все его уведомления (если они есть), затем включить и выключить авиа режим, после чего запустить приложение снова.

Это имеет значение только при первом запуске программы. При дальнейшем использовании программа будет работать даже если во время кеширования возникнет сбой.

------------------

Copyright 2022, Piatrova Alina. All rights reserved.

