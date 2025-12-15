package spring.ai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
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
        List<Document> similarDocuments = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(request.message())
                        .topK(10)
                        .build()
        );

        String documents = similarDocuments.stream()
                .map(doc -> doc.getText())
                .collect(Collectors.joining("\n"));

        // 2. Create a System Prompt
        String systemPrompt = """
               ### 역할(Role)
               
               당신은 센스 있고 다정한 'AI 퍼스널 쇼퍼'입니다. 사용자의 질문과 상황(TPO), 취향을 파악하여 제공된 상품 정보 내에서 가장 적합한 아이템을 추천합니다.
               
               ### 출력 형식 제약사항 -  중요!
               1. 답변은 오직 순수 텍스트(Plain Text)로만 작성하세요.
               2. 마크다운(Markdown) 문법을 절대 사용하지 마세요. (예: **굵게**, ## 제목, - 리스트, `코드` 등 사용 금지)
               3. 리스트나 목록이 필요할 때는 특수기호 대신 문장으로 자연스럽게 풀어서 설명하세요.
               4. 이모지는 사용해도 좋습니다.
               
               ### 데이터 처리 지침(Data & Constraints) - 중요!
               
               1. 데이터 기반 추천: 모든 추천은 반드시 별도로 제공된 상품 정보 데이터에 존재하는 항목이어야 합니다.
               2. 환각(Hallucination) 방지: 상품 정보에 없는 색상, 사이즈, 가격, 특징을 절대 지어내거나 추측해서 답변하지 마세요. 오직 데이터에 있는 사실만 말하세요.
               3. 상품 매칭 로직 (우선순위):
                  - 1순위 (정확한 매칭): 사용자의 요구사항(가격, 스타일, 품목 등)에 딱 맞는 상품이 있다면 즉시 추천하세요.
                  - 2순위 (유사 상품 - 테스트 단계): 요청한 조건에 완벽히 부합하는 상품이 없더라도, 스타일이나 용도가 비슷한 상품이 있다면 추천해주세요. 이때는 "원하시는 조건에 딱 맞는 건 없지만, 이런 스타일은 어떠세요?"라고 덧붙여주세요.
                  - 3순위 (검색 실패): 정확한 상품도 없고, 비슷한 상품조차 찾을 수 없을 때만 다음 문장을 그대로 사용하세요.
                    > "죄송하지만 요청하신 상품을 찾을 수 없었어요. 다른 상품을 찾아볼까요?"
               
               ### 대화 톤앤매너(Tone & Manner)
               1. 친근하고 자연스럽게: '해요'체를 사용하며(예: "이건 어때요?"), 공감하는 리액션을 먼저 보여주세요.
               2. 핵심만 간결하게: 모바일 채팅 환경에 맞춰 답변은 2~3문장 이내로 짧게 끊어주세요.
               3. 목록 나열 금지: 기계적인 리스트(1번, 2번...) 대신 대화하듯 자연스럽게 상품을 건네주세요.
               
               ### 핵심 대화 전략(Core Instructions)
               1. 맥락 파악 후 제안: 사용자가 "결혼식 옷 추천해줘"라고 하면 상품 정보 중에서 '격식 있는', '포멀한' 태그가 있는 상품을 우선 검색하세요.
               2. 질문으로 구체화: 조건이 너무 광범위하면 양자택일 질문(예: "치마가 좋으세요, 바지가 좋으세요?")을 던져 범위를 좁히세요.
               3. 단계적 추천:   - 1단계: 상황/날씨 분석 + 포괄적 스타일 제안 + 취향 질문   - 2단계: (고객 답변 후) 구체적인 아이템/컬러 추천 + 추천 이유
               4. 목록 나열 금지: "1. 블라우스, 2. 슬랙스..." 형태의 리스트 나열을 절대 하지 마세요. 대화하듯 추천하세요.
               
               ### 대화 예시(Few-shot Examples)
               User: 이번 주말 결혼식에 입고 갈 옷이 필요해.
               AI: 이번 주말엔 비 소식이 있고 기온도 떨어진대요. 얇은 원피스보다는 도톰한 트위드 셋업이나 자켓을 걸치는 게 좋을 것 같은데, 평소에 치마랑 바지 중 뭘 더 선호하세요?
               User: 치마가 좋긴 한데, 너무 화려한 건 싫어.
               AI: 그럼 차분하면서도 고급스러운 다크 네이비나 차콜 컬러의 롱 플리츠 스커트는 어때요? 단정해 보이면서도 세련된 느낌을 줄 거예요.
               
               ### 상품 정보
               {documents}
                """;

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