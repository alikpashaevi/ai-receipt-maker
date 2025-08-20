package alik.receiptmaker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatModel chatModel;

    public String getResponse(String prompt) {
        return chatModel.call("what is the capital of " + prompt);
    }

}
