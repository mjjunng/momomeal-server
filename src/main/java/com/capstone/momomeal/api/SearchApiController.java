package com.capstone.momomeal.api;

import com.capstone.momomeal.domain.ChatRoom;
import com.capstone.momomeal.domain.JoinedChatRoom;
import com.capstone.momomeal.domain.Members;
import com.capstone.momomeal.service.ChatRoomService;

import com.capstone.momomeal.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class SearchApiController {
    private final ChatRoomService chatRoomService;
    private final MemberService memberService;

    @GetMapping("/searched-chat-list/{keyword}/{memberId}")
    public ResponseEntity getSearchChatRoomList(@PathVariable String keyword,
                                                @PathVariable Long memberId){

        List<ChatRoom> result = getChatRoomsExceptParticipatedChatRoomsByKeyword(keyword, memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    // 참여한 채팅 제외하고, 해당 키워드 가지고 있는채팅방 리턴
    private List<ChatRoom> getChatRoomsExceptParticipatedChatRoomsByKeyword(String keyword,
                                                                   Long memberId) {

        List<ChatRoom> chatRooms = new ArrayList<>();
        // 사용자가 이미 참여한 채팅 거르기 위해 사용자가 참여한 채팅방 id(ChatRoomId)값이 필요
        Optional<Members> getMember = memberService.findById(memberId);
        if (getMember.isPresent()){
            Members member = getMember.get();
            List<JoinedChatRoom> joinedChatRooms = member.getJoinedChatRooms(); // 참여한 joinedChatRooms
            List<Long> participatedChatRoomIds = new ArrayList<>();    // 참여한 chatRoomIds

            for (JoinedChatRoom joinedChatRoom : joinedChatRooms) {
                Long participatedChatRoomId = joinedChatRoom.getChatRoom().getId();
                participatedChatRoomIds.add(participatedChatRoomId);
            }   // end


            // 참여하고 있는 채팅방이 없을 때 -> 해당 키워드를 포함한 전체 채팅방 가져옴
            if (participatedChatRoomIds.size() < 1) {
                chatRooms = chatRoomService.getSearchedChatRooms(keyword);


            } else{     // 참여하고 있는 채팅방이 있을 때
                // 참여하고 있는 채팅방 제외하고 해당 키워드를 포함한 채팅방 가져옴
                chatRooms = chatRoomService.getSearchedAndExceptParticipatedChatRooms(keyword,
                                                                            participatedChatRoomIds);

            }
        }

        return chatRooms;
    }

}
