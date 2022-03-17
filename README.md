# TMS_GRADUATE_WORK
It is a repository for preparing graduate work.

Created by Alina Piatrova.

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

