
package com.capstone.momomeal.api;

import com.capstone.momomeal.domain.*;
import com.capstone.momomeal.service.ChatRoomService;
import com.capstone.momomeal.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ChatRoomListApiController {
    private final ChatRoomService chatRoomService;
    private final MemberService memberService;


    /**
     * 사용자가 참여한 채팅방 제외한 해당 카테고리별 채팅방 데이터(DTO) 전송 api
     * @param categoryName 사용자가 선택한 카테고리명
     * @param memberId 현재 접속한 사용자 id
     * @param type 시간 순 or 거리 순
     *             시간 순 - 채팅 생성일이 느린 순으로
     *             거리 순 - 현재 사용자의 위치와 가까운 수령장소를 가진 채팅방이 우선순위
     * @return 해당 카테고리에 해당하는 모든 채팅방 DTO 리스트 Body에 담은 ResponseEntity
     */
    @GetMapping("/chat-list/{categoryName}/{memberId}/{type}")
    public ResponseEntity returnCategoryList(@PathVariable String categoryName,
                                             @PathVariable Long memberId,
                                             @PathVariable String type){

        List<ChatRoomListDto> result = new ArrayList<>();

        // string -> Category enum 타입 변환
        TransStringToEnum te = new TransStringToEnum();
        Category selectedCategory = te.transferStringToEnum(categoryName);

        Optional<Members> getMember = memberService.findById(memberId);
        if (getMember.isPresent()){
            Members member = getMember.get();
            // 참여한 채팅 제외한 모든 채팅방
            List<ChatRoom> chatRooms = new ArrayList<>();

            double memberX = member.getX_value();
            double memberY = member.getY_value();

            // type에 따라 채팅방 우선순위 달라짐
            if (type.equals("time")){
                chatRooms = getChatRoomsExceptParticipatedCharRooms(memberId, "time");

                // 해당 카테고리의 채팅방만 뽑고 DTO로 변경
                result = chatRooms.stream()
                        .filter(c -> c.getCategory().equals(selectedCategory))
                        .map(c -> new ChatRoomListDto(c, memberX, memberY))
                        .collect(Collectors.toList());
            } else{
                // 거리 순
                chatRooms = getChatRoomsExceptParticipatedCharRooms(memberId, "distance");

                // 해당 카테고리의 채팅방만 뽑고, DTO로 변경하고, 거리 순으로 오름차순 정렬함
                result = chatRooms.stream()
                        .filter(c -> c.getCategory().equals(selectedCategory))
                        .map(c -> new ChatRoomListDto(c, memberX, memberY))
                        .sorted(Comparator.comparing(ChatRoomListDto::getDistance))
                        .collect(Collectors.toList());
            }

        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(result);

    }


    /**
     * 사용자가 참여한 채팅방 제외한 모든 채팅방 데이터(DTO) 전송 api
     * @param memberId 현재 접속한 사용자 id
     * @param type 시간 순 or 거리 순
     *             시간 순 - 채팅 생성일이 느린 순으로
     *             거리 순 - 현재 사용자의 위치와 가까운 수령장소를 가진 채팅방이 우선순위
     * @return 모든 채팅방 DTO 리스트 Body에 담은 ResponseEntity
     */
    @GetMapping("/chat-list/{memberId}/{type}")
    public ResponseEntity returnAllList(@PathVariable Long memberId,
                                        @PathVariable String type) {

        // 모든 채팅방 가져옴
        // 참여한 채팅 제외한 모든 채팅방
        List<ChatRoom> chatRooms = new ArrayList<>();
        List<ChatRoomListDto> result = new ArrayList<>();
        Optional<Members> getMember = memberService.findById(memberId);
        if (getMember.isPresent()){
            Members member = getMember.get();

            double memberX = member.getX_value();
            double memberY = member.getY_value();

            if (type.equals("time")){
                chatRooms = getChatRoomsExceptParticipatedCharRooms(memberId, "time");

                // 채팅방 DTO로 변경
                result = chatRooms.stream()
                        .map(c -> new ChatRoomListDto(c, memberX, memberY))
                        .collect(Collectors.toList());
            } else{
                chatRooms = getChatRoomsExceptParticipatedCharRooms(memberId, "distance");

                // 채팅방 DTO로 변경하고, 거리 순으로 오름차순 정렬함
                result = chatRooms.stream()
                        .map(c -> new ChatRoomListDto(c, memberX, memberY))
                        .sorted(Comparator.comparing(ChatRoomListDto::getDistance))
                        .collect(Collectors.toList());
            }


        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    // 참여한 채팅 제외한 모든 채팅방 리턴
    private List<ChatRoom> getChatRoomsExceptParticipatedCharRooms(Long testMemberId,
                                                                   String type) {

        List<ChatRoom> chatRooms = new ArrayList<>();
        // 사용자가 이미 참여한 채팅 거르기 위해 사용자가 참여한 채팅방 id(ChatRoomId)값이 필요
        Optional<Members> getMember = memberService.findById(testMemberId);
        if (getMember.isPresent()){
            Members member = getMember.get();
            List<JoinedChatRoom> joinedChatRooms = member.getJoinedChatRooms(); // 참여한 joinedChatRooms
            List<Long> participatedChatRoomIds = new ArrayList<>();    // 참여한 chatRoomIds

            for (JoinedChatRoom joinedChatRoom : joinedChatRooms) {
                Long participatedChatRoomId = joinedChatRoom.getChatRoom().getId();
                participatedChatRoomIds.add(participatedChatRoomId);
            }   // end


            // 참여하고 있는 채팅방이 없을 때 -> 전체 채팅방 가져옴
            if (participatedChatRoomIds.size() < 1) {
                if (type.equals("time")) {  // 시간순 정렬
                    chatRooms = chatRoomService.findAllOrderByTime();
                } else {
                    // 거리 순 정렬
                    chatRooms = chatRoomService.findAll();
                }

            } else{     // 참여하고 있는 채팅방이 있을 때
                // 참여하고 있는 채팅방 제외한 모든 채팅방 가져옴
                if (type.equals("time")) {  // 시간 순 정렬
                    chatRooms = chatRoomService.findExceptParticipatedChatRoomOrderByTime(participatedChatRoomIds);
                } else{
                    // 거리 순 정렬
                    chatRooms = chatRoomService.findExceptParticipatedChatRoom(participatedChatRoomIds);
                }

            }
        }

        return chatRooms;
    }


    // response body가 []가 아닌 {}로 감싸기 위한 장치
    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }


    /**
     * 사용자가 참여하고 있는 채팅방 데이터(DTO) 리턴 (채팅 아이콘 클릭하면 나오는 화면)
     * @param memberId : 현재 사용자의 id
     * @return ResponseEntity body : 사용자가 참여하고 있는 채팅방 DTO 리스트
     */
    @GetMapping("/entered-chat-list/{memberId}")
    public ResponseEntity returnEnteredChatRoomList(@PathVariable Long memberId){
        List<ChatRoomListDto> result = new ArrayList<>();

        Optional<Members> getMember = memberService.findById(memberId);
        if (getMember.isPresent()){
            Members member = getMember.get();
            List<JoinedChatRoom> joinedChatRooms = member.getJoinedChatRooms();
            List<ChatRoom> chatRooms = new ArrayList<>();

            for (JoinedChatRoom joinedChatRoom : joinedChatRooms) {
                chatRooms.add(joinedChatRoom.getChatRoom());
            }

            if (chatRooms.size() > 0) { // 참여하고 있는 채팅방이 있을 때
                double memberX = member.getX_value();
                double memberY = member.getY_value();

                // 참여하고 있는 채팅방 제외한 모든 채팅방 가져옴
                result = chatRooms.stream()
                        .map(c -> new ChatRoomListDto(c, memberX, memberY))
                        .collect(Collectors.toList());
            }    // 참여하고 있는 채팅방이 없을 때 -> 빈 객체

        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(result);

    }

}
