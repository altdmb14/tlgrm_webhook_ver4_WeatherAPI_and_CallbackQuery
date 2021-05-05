package ru.tlgrmbot.myhelperbot.botapi.handlers.start;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.tlgrmbot.myhelperbot.botapi.BotState;
import ru.tlgrmbot.myhelperbot.botapi.InputMessageHandler;
import ru.tlgrmbot.myhelperbot.service.ButtonsAddService;
import ru.tlgrmbot.myhelperbot.service.ReplyMessagesService;

/**
 * При вводе в чат "/start"
 */

@Slf4j
@Component
public class StartHandler implements InputMessageHandler {
    private ReplyMessagesService messagesService;

    public StartHandler(ReplyMessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.START_HELPER;
    }


    @Override
    public SendMessage handle(Message message) {
        long chatId = message.getChatId();
        SendMessage replyMessage = messagesService.getReplyMessage(chatId, "reply.startHelper");
        //помимо ОТВЕТА добавится ещё и панелька с двумя кнопками
        replyMessage.setReplyMarkup(ButtonsAddService.getInlineMessageButtons());
        return replyMessage;
    }

}
