package com.capstone.momomeal.api;


import com.capstone.momomeal.domain.*;
import com.capstone.momomeal.service.ChatRoomService;
import com.capstone.momomeal.service.MemberService;
import com.capstone.momomeal.service.RecommendCategoryService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RecommendationApiController {
    private final MemberService memberService;
    private final RecommendCategoryService recommendCategoryService;
    private final ChatRoomService chatRoomService;


    /**
     * 추천 카테고리 객체 생성하고, 설문조사에서 사용자가 선택한 카테고리의 가중치 값 더하는 메서드
     * @param memberId 현재 사용자의 id값
     * @param categories 설문조사에서 사용자가 선택한 카테고리 리스트
     */
    @GetMapping("/preferred-category/{memberId}/{categories}")
    public void getPreferredCategory(@PathVariable Long memberId, @PathVariable List<String> categories){
        List <String> nullRemovedCategories = categories.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 현재 회원 데이터 가져오기
        Optional<Members> getMember = memberService.findById(memberId);

        if (getMember.isPresent()){
            Members member = getMember.get();
            RecommendCategory recommendCategory = member.getRecommendCategory();

            if (recommendCategory != null) { // 추천 카테고리 객체가 존재할 때(이전에 설문조사를 한 경우)
                // 새로 설문조사 한 데이터로 업데이트해야 하기 때문에 모든 카테고리 점수 0으로 리셋함
                recommendCategory.initScore();

            } else {    // 처음 설문조사를 한 경우
                // 추천 카테고리 객체 생성
                recommendCategory =
                        recommendCategoryService.createRecommendCategory(member);
            }

            // 설문조사에서 사용자가 선택한 카테고리의 가중치 더해서 저장
            for (String category : nullRemovedCategories){
                recommendCategoryService.addValue(recommendCategory, category, 10);
            }

        }
    }

    /**
     * 임계값을 기준으로 사용자에게 추천할 카테고리 뽑아서 해당 카테고리 && 참여하지 않은
     * 채팅방 DTO 목록 반환하는 메서드
     * @param memberId 현재 사용자의 id값
     * @return 추천 카테고리 채팅방 데이터 목록
     */
    @GetMapping("/recommend-chat-list/{memberId}")
    public ResponseEntity returnRecommendChatRoomList(@PathVariable Long memberId){
        Optional<Members> getMember = memberService.findById(memberId);
        List<ChatRoomListDto> result = new ArrayList<>();
        List<ChatRoom> chatRooms = new ArrayList<>();

        if (getMember.isPresent()){
            Members member = getMember.get();
            // 사용자가 이미 참여한 채팅 거르기 위해 사용자가 참여한 채팅방 id(ChatRoomId)값이 필요
            List<JoinedChatRoom> joinedChatRooms = member.getJoinedChatRooms(); // 참여한 joinedChatRooms
            List<Long> participatedChatRoomIds = new ArrayList<>();    // 참여한 chatRoomIds

            for (JoinedChatRoom joinedChatRoom : joinedChatRooms) {
                Long participatedChatRoomId = joinedChatRoom.getChatRoom().getId();
                participatedChatRoomIds.add(participatedChatRoomId);
                System.out.println("id: " + participatedChatRoomId);
            }   // end

            RecommendCategory recommendCategory = member.getRecommendCategory();

            if (recommendCategory != null){ // 추천 카테고리 객체가 존재할 때
                // 추천 카테고리 리스트
                List<Category> top3ScoreCategory = recommendCategory.getTop3ScoreCategory(3);

                // 추천할 카테고리 리스트가 존재할 때
                if (top3ScoreCategory != null){
                    // 추천할 카테고리의 채팅방 리스트
                    chatRooms = chatRoomService.
                            findByCategoryIn(top3ScoreCategory, participatedChatRoomIds);

                    double memberX = member.getX_value();
                    double memberY = member.getY_value();

                    // DTO로 변환함
                    result = chatRooms.stream()
                            .map(c -> new ChatRoomListDto(c, memberX, memberY))
                            .collect(Collectors.toList());

                }
            }

        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);

    }
}
