package ru.tlgrmbot.myhelperbot.botapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tlgrmbot.myhelperbot.cache.UserDataCache;

@Component
@Slf4j
public class TelegramFacade {
    private BotStateContext botStateContext;
    private UserDataCache userDataCache;

    public TelegramFacade(BotStateContext botStateContext, UserDataCache userDataCache) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage replyMessage = null;

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("Этот лог по нажатию кнопки CallQuery, получен от: {}, userId: {}, with data: {}", callbackQuery.getFrom().getUserName(),
                    callbackQuery.getFrom().getId(), callbackQuery.getData());
            return processCallbackQuery(callbackQuery);
        }

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("Этот лог по случаю пришедшего Message, получен от: {}, chatId: {}, with text: {}",
                    message.getFrom().getUserName(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }

        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) {
        String inputMsg = message.getText();
        int userId = message.getFrom().getId();
        BotState botState;
        SendMessage replyMessage;

        //проверка ВВОДА от пользователя - боту
        switch (inputMsg) {
            case "/start":
                botState = BotState.START_HELPER;
                break;
            default:
                //эта часть кода ПРОВЕРИТ в своей Map<Integer, BotState> usersBotStates и если не найдёт, то вернёт botState = BotState.ASK_HELPER;
                botState = userDataCache.getUsersCurrentBotState(userId);
                break;
        }
        //эта часть кода ЗАКРЕПИТ botState за конкретным userId
        userDataCache.setUsersCurrentBotState(userId, botState);
        //processInputMessage -> перекинет на вызов .handle() - соответствующего слушателя, например AskHandler.java
        replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;
    }

    //Задаём реакцию на нажатие кнопки
    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        final long chatId = buttonQuery.getMessage().getChatId();
        String userName = buttonQuery.getFrom().getUserName();
        BotApiMethod<?> callBackAnswer = new SendMessage();

        if (buttonQuery.getData().equals("buttonOne")) {
            callBackAnswer = new SendMessage(chatId, "Ваш chatId: " + chatId);
        } else if (buttonQuery.getData().equals("buttonTwo")) {
            callBackAnswer = sendAnswerCallbackQuery("Ваш userName: " + userName, false, buttonQuery);
        }

        return callBackAnswer;
    }

    // Всплывающее (АЛЕРТ) окно, которое появится в случае нажатия кнопки "buttonTwo"
    private AnswerCallbackQuery sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackQuery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
        answerCallbackQuery.setShowAlert(alert);
        answerCallbackQuery.setText(text);
        return answerCallbackQuery;
    }
}