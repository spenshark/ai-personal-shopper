package spring.ai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.ai.converter.ChatConverter;
import spring.ai.dto.ChatRequestDto;
import spring.ai.dto.ChatResponseDto;
import spring.ai.entity.ChatMessage;
import spring.ai.entity.ChatRoom;
import spring.ai.entity.User;
import spring.ai.entity.enums.Sender;
import spring.ai.global.exception.RestApiException;
import spring.ai.global.exception.code.status.GlobalErrorStatus;
import spring.ai.repository.ChatMessageRepository;
import spring.ai.repository.ChatRoomRepository;
import spring.ai.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    @Transactional
    public ChatResponseDto chat(ChatRequestDto request) {
        Long userId = 1L; // Temporary hardcoded user ID
        ChatRoom chatRoom = getChatRoom(userId);

        saveMessage(chatRoom, Sender.USER, request.message());

        List<Message> history = getConversationHistory(chatRoom);

        String aiResponse = chatClient.prompt()
                .messages(history)
                .call()
                .content();

        saveMessage(chatRoom, Sender.AI, aiResponse);
        return ChatConverter.toChatResponse(aiResponse);
    }

    @Override
    @Transactional
    public ChatResponseDto ragChat(ChatRequestDto request) {
        Long userId = 1L; // Temporary hardcoded user ID
        ChatRoom chatRoom = getChatRoom(userId);

        saveMessage(chatRoom, Sender.USER, request.message());

        List<Message> history = getConversationHistory(chatRoom);

        // 1. Retrieve relevant documents from the VectorStore
        List<Document> similarDocuments = vectorStore.similaritySearch(request.message());
        String documents = similarDocuments.stream()
                .map(doc -> doc.getText())
                .collect(Collectors.joining("\n"));

        // 2. Create a System Prompt
        String systemPrompt = """
                당신은 사용자의 질문에 가장 잘 맞는 상품을 추천해주는 20~30대 타겟 AI 퍼스널 쇼퍼 '핏(Fit)한 친구'입니다.
                당신의 역할은 사용자의 쇼핑 고민을 해결해주고, 최고의 상품을 추천하여 실패 없는 쇼핑 경험을 제공하는 것입니다.

                ## Persona
                - 이름: 핏(Fit)한 친구
                - 컨셉: 센스 있는 패션 잘 아는 친구
                - 말투: 딱딱한 '습니다/합니다' 대신, '~해요', '~어때요?', '완전 찰떡이에요!' 등 친근하고 공감하는 말투 사용

                ## Instructions
                1. 아래 "상품 정보"를 기반으로 사용자의 "질문"에 가장 적합한 상품을 추천하세요.
                2. 추천 이유를 반드시 함께 설명해야 합니다. (예: "비가 오니까 린넨보다는 이 소재가 좋아요")
                3. 사용자의 TPO(시간, 장소, 상황), 스타일, 조건(가격, 색상 등)을 파악하여 맥락에 맞는 추천을 제공하세요.
                4. 만약 "상품 정보"에서 적절한 상품을 찾을 수 없다면, "죄송하지만 요청하신 상품을 찾을 수 없었어요. 다른 상품을 찾아볼까요?" 라고 솔직하게 답변하고 대안을 제시하세요.
                4-1. 단, 지금은 테스트 단계이기 때문에 비슷한 제품이 존재한다면, 해당 제품을 표시해주세요!
                5. 절대 없는 정보를 지어내서 답변하지 마세요(Hallucination 방지).

                ## 상품 정보
                {documents}
                """;

        log.info(systemPrompt);

        // 3. Call the ChatClient and get the response
        String userMessage = "## 질문\n" + request.message();

        String content = chatClient.prompt()
                .messages(history)
                .system(s -> s.text(systemPrompt).param("documents", documents))
                .user(userMessage)
                .call()
                .content();

        saveMessage(chatRoom, Sender.AI, content);

        return ChatConverter.toChatResponse(content);
    }

    private ChatRoom getChatRoom(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus.USER_NOT_FOUND));

        return chatRoomRepository.findByUser(user)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus.CHAT_ROOM_NOT_FOUND));
    }

    private List<Message> getConversationHistory(ChatRoom chatRoom) {
        List<ChatMessage> history = chatMessageRepository.findTop10ByChatRoomOrderByCreatedAtDesc(chatRoom);
        Collections.reverse(history); // Sort chronologically

        return history.stream()
                .map(m -> {
                    if (m.getSender() == Sender.USER) {
                        return (Message) new UserMessage(m.getMessage());
                    } else {
                        return (Message) new AssistantMessage(m.getMessage());
                    }
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void saveMessage(ChatRoom chatRoom, Sender sender, String message) {
        ChatMessage chatMessage = ChatConverter.toChatMessage(chatRoom, sender, message);
        chatMessageRepository.save(chatMessage);
    }
}