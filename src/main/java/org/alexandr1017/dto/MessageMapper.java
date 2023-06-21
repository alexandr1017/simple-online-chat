package org.alexandr1017.dto;

import org.alexandr1017.model.Message;

public class MessageMapper {
    public static DtoMessage map (Message message) {
        DtoMessage dtoMessage = new DtoMessage();
        dtoMessage.setDateTime(message.getDateTime().toString());
        dtoMessage.setUsername(message.getUser().getName());
        dtoMessage.setText(message.getMessage());
        return dtoMessage;
    }
}
