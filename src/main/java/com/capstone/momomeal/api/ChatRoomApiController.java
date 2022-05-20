package com.capstone.momomeal.api;

import com.capstone.momomeal.domain.*;
import com.capstone.momomeal.service.ChatRoomService;
import com.capstone.momomeal.service.JoinedChatRoomService;
import com.capstone.momomeal.service.MemberService;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ChatRoomApiController {
    private final ChatRoomService chatRoomService;
    private final MemberService memberService;
    private final JoinedChatRoomService joinedChatRoomService;

    /**
     * 채팅방 생성 응답 api
     * @param requestDTO 안드로이드에서 받은 채팅방 데이터
     * @return 생성한 채팅방(ChatRoom) id값
     */
    @PostMapping("/chat")
    public ResponseEntity saveChatRoom(@RequestBody @Valid ChatRoomRequestDTO requestDTO) {
        ChatRoom chatRoom = null;
        // 현재 회원 데이터 가져오기
        Optional<Members> getMember = memberService.findById(requestDTO.getHostId());

        if (getMember.isPresent()){
            Members member = getMember.get();

            // 채팅방 생성
            chatRoom = chatRoomService.createChatRoom(member, requestDTO);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(chatRoom);
    }


    /**
     * 채팅방 생성 요청 처리 후 응답
     */
    @Data
    @NoArgsConstructor
    static class CreateChatRoomResponse {
        private Long id;

        public CreateChatRoomResponse(Long id) {
            this.id = id;
        }
    }

//    /**
//     * 사용자가 클릭한 채팅방 데이터(dto) 전송 api
//     * @param chatroomId 클릭한 채팅방 id
//     * @return 클릭한 채팅방 데이터(dto)
//     */
//    @GetMapping("/clicked-chat/{chatroomId}")
//    public ResponseEntity returnClickedChatRoomData(@PathVariable Long chatroomId){
//        // chatRoomId를 통해 해당 채팅방 데이터 조회
//        ChatRoom clickedChatRoom = chatRoomService.findById(chatroomId);
//
//        ClickedChatRoomDto result;
//
//
//        if (clickedChatRoom == null){   // 없는 채팅방 요청 -> 빈 값
//            result = new ClickedChatRoomDto();
//        } else{
//            result = new ClickedChatRoomDto(clickedChatRoom);   // 해당 chatRoom dto로 변환
//        }
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(result);
//
//    }
//
//    @Data
//    @NoArgsConstructor
//    static class ClickedChatRoomDto{
//        private Long chatRoomId;
//        private String title;
//        private String category;
//        private int maxCapacity;
//        private String storeName;
//        private String pickupPlaceName;
//        private int distance;
//
//        public ClickedChatRoomDto(ChatRoom chatRoom) {
//            this.chatRoomId = chatRoom.getId();
//            this.title = chatRoom.getTitle();
//            this.category = chatRoom.getCategory().getName();
//            this.maxCapacity = chatRoom.getMaxCapacity();
//            this.storeName = chatRoom.getStoreName();
//            this.pickupPlaceName = chatRoom.getPickupPlaceName();
//            this.distance = chatRoom.getDistance();
//        }
//
//    }

    /**
     * 호스트가 아닌 사용자의 채팅방 참여 응답 api
     * 해당 채팅방 멤버에 해당 사용자 추가함
     */
    @GetMapping("/chat/{memberId}/{chatroomId}")
    public CreateJoinedChatRoomResponse enterChatRoom(@PathVariable Long memberId,
                                                      @PathVariable Long chatroomId){

        CreateJoinedChatRoomResponse result;
        // id값으로 회원 객체 가져오기
        Optional<Members> getMember = memberService.findById(memberId);
        // id값으로 채팅방 객체 가져오기
        ChatRoom findChatRoom = chatRoomService.findById(chatroomId);

        // joinedChatRoom 생성
        if (getMember.isPresent()){
            Members findMember = getMember.get();
            Long joinedChatRoomId = joinedChatRoomService.createJoinedChatRoom(findMember, findChatRoom);
            result = new CreateJoinedChatRoomResponse(joinedChatRoomId);
        } else {
            result = new CreateJoinedChatRoomResponse();
        }

        return result;
    }

    /**
     * 채팅방 참여 요청 처리 후 응답
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class CreateJoinedChatRoomResponse {
        private Long id;
    }

    /**
     * 채팅방 삭제 응답 api - 참여한 채팅방(JoinedChatRoom) 삭제 - 연관관계 모두 삭제해야 함
     * @param chatroomId 삭제하려는 joinedChatRoom와 연관된 chatRoom id
     * @return deleteCountDto: 삭제한 joinedChatRoom 레코드 수, 삭제한 chatRoom 레코드 수
     */
    @DeleteMapping("/deleted-chat/{memberId}/{chatroomId}")
    public deleteCountDto deleteJoinedChatRoom(@PathVariable Long memberId,
                                               @PathVariable Long chatroomId){
        deleteCountDto result;

        Optional<Members> getMember = memberService.findById(memberId);
        ChatRoom chatRoom = chatRoomService.findById(chatroomId);

        if (getMember.isPresent()){
            Members member = getMember.get();
            JoinedChatRoom toDeleteJoinedChatRoom = joinedChatRoomService
                    .findByMemberIdAndChatRoomId(member, chatRoom);

            member.deleteJoinChatRoomFromMember(toDeleteJoinedChatRoom);

            int cntDeletedJCRecord = joinedChatRoomService.delete(toDeleteJoinedChatRoom.getId());

            // joinedChatRoom과 연관된 chatRoom없으면 chatRoom도 DB에서 삭제함
            int cntDeleteCRecord = 0;
            int cnt = joinedChatRoomService.countByChatRoom(chatRoom);
            if (cnt == 0) {
                cntDeleteCRecord = chatRoomService.delete(chatroomId);
            }

            result = new deleteCountDto(cntDeletedJCRecord, cntDeleteCRecord);

        } else{
            result = new deleteCountDto();
        }

        return result;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class deleteCountDto{
        private int cntDeletedJoinedChatRoomRecord;
        private int cntDeletedChatRoomRecord;

    }

    /**
     * 채팅방을 클릭하면, 해당 채팅방에 참여하고 있는 멤버의 정보 리턴하는 함수
     * @param chatroomId  해당 채팅방의 id값
     * @return  해당 채팅방에 참여하고 있는 멤버의 정보
     */

    @GetMapping("/entered-chat-info/{chatroomId}")
    public ResponseEntity returnChatRoomInfo(@PathVariable Long chatroomId){
        ChatRoom chatRoom = chatRoomService.findById(chatroomId);

        // 참여중인 채팅방과 연관된 joinedChatRooms
        List<JoinedChatRoom> joinedChatRooms = joinedChatRoomService.findByChatRoom(chatRoom);

        // 채팅방에 참여 중인 멤버의 데이터 리스트
        List <chatRoomInfoDto> memberInfoList = new ArrayList<>();

        // joinedChatRooms에서 멤버 뽑아낸다.
        for (JoinedChatRoom joinedChatRoom : joinedChatRooms) {
            Members member = joinedChatRoom.getMember();
            memberInfoList.add(new chatRoomInfoDto(member.getUser_id(), member.getRealName(),
                    member.getImg()));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(memberInfoList);

    }

    @Data
    @AllArgsConstructor
    static class chatRoomInfoDto{
        private Long userId;
        private String name;
        private String img;

    }
}
