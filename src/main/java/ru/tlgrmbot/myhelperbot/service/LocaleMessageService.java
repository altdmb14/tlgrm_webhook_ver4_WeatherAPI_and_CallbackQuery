package ru.tlgrmbot.myhelperbot.service;

/*
Работает с файлом шаблоном "ответных сообщений" messages.properties
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.tlgrmbot.myhelperbot.api.Weather;

import java.util.Locale;

@Service
public class LocaleMessageService {
    private final Locale locale;
    private final MessageSource messageSource;

    public LocaleMessageService(@Value("${localeTag}") String localeTag, MessageSource messageSource) {
        this.messageSource = messageSource;
        this.locale = Locale.forLanguageTag(localeTag);
    }

    public String getMessage(String message) {
        return messageSource.getMessage(message, null, locale);
    }

    public String getWeather(String cityForWeather) {
        Weather weather = new Weather();
        String response = "";
        try {
            response = weather.getReadyForecast(cityForWeather);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return response;
    }

}
