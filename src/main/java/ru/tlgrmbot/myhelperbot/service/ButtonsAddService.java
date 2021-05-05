package ru.tlgrmbot.myhelperbot.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class ButtonsAddService {

    //Задаём название "коллбэков", который вернутся при нажатии определённых кнопок, например "buttonOne"
    public static InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonOne = new InlineKeyboardButton().setText("Узнать мой chatId");
        InlineKeyboardButton buttonTwo = new InlineKeyboardButton().setText("Узнать мой userName");

        buttonOne.setCallbackData("buttonOne");
        buttonTwo.setCallbackData("buttonTwo");

        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();
        keyboardButtons.add(buttonOne);
        keyboardButtons.add(buttonTwo);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtons);

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
