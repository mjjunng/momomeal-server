package com.capstone.momomeal.service;

import com.capstone.momomeal.domain.*;
import com.capstone.momomeal.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final JoinedChatRoomService joinedChatRoomService;
    private final RecommendCategoryService recommendCategoryService;

    /**
     * 채팅방 저장 메서드
     * @param chatRoom 저장할 채팅방 object
     * @return 저장한 채팅방의 id값
     */
    @Transactional
    public Long save(ChatRoom chatRoom){
        chatRoomRepository.save(chatRoom);
        return chatRoom.getId();
    }

    /**
     * 채팅방 생성 메서드
     * @param member    해당 채팅방 생성자
     * @param requestDTO   채팅방의 정보(타이틀, 최대인원수...)
     * @return 생성한 채팅방(ChatRoom)
     */
    @Transactional
    public ChatRoom createChatRoom(Members member, ChatRoomRequestDTO requestDTO){
        // string -> Category enum 타입 변환
        TransStringToEnum te = new TransStringToEnum();
        Category category = te.transferStringToEnum(requestDTO.getCategoryName());

        // 채팅방 생성
        ChatRoom chatRoom = new ChatRoom(category, requestDTO.getTitle(), requestDTO.getHostId(),
                requestDTO.getMaxCapacity(), requestDTO.getStoreName(), requestDTO.getPickupPlaceName(),
                requestDTO.getPickupPlaceXCoord(), requestDTO.getPickupPlaceYCoord());

        save(chatRoom);

        // 참여 채팅방 생성하고 현재 회원과 연관관계 설정
        JoinedChatRoom joinedChatRoom = new JoinedChatRoom(chatRoom, MemberStatus.HOST);
        joinedChatRoom.setMember(member);
        joinedChatRoomService.save(joinedChatRoom);

        // 해당 사용자가 참여한 채팅방의 카테고리 가중치 증가
        RecommendCategory recommendCategory = member.getRecommendCategory();
        if (recommendCategory != null){
            recommendCategoryService.addValue(recommendCategory, requestDTO.getCategoryName(), 1);
        }
        return chatRoom;

    }

    public ChatRoom findById(Long id){
        return chatRoomRepository.findById(id);
    }

    public List<ChatRoom> findAll(){
        return chatRoomRepository.findAll();
    }

    public List<ChatRoom> findAllOrderByTime(){
        return chatRoomRepository.findAllOrderByTime();
    }

    public List<ChatRoom> findAllOrderByDistance() {
        return chatRoomRepository.findAllOrderByDistance();
    }

    public List<ChatRoom> findExceptParticipatedChatRoom(List<Long> participatedChatRoomIds){
        return chatRoomRepository.findExceptParticipatedChatRoom(participatedChatRoomIds);
    }

    public List<ChatRoom> findExceptParticipatedChatRoomOrderByTime(List<Long> participatedChatRoomIds){
        return chatRoomRepository.findExceptParticipatedChatRoomOrderByTime(participatedChatRoomIds);
    }

    public List<ChatRoom> findExceptParticipatedChatRoomOrderByDistance(List<Long> participatedChatRoomIds){
        return chatRoomRepository.findExceptParticipatedChatRoomOrderByDistance(participatedChatRoomIds);
    }

    public List<ChatRoom> findByCategoryIn(List<Category> categories, List<Long> participatedChatRoomIds){
        return chatRoomRepository.findByCategoryIn(categories, participatedChatRoomIds);
    }


    @Transactional
    public int delete(Long chatRoomId){
        return chatRoomRepository.deleteById(chatRoomId);
    }

    @Transactional
    public List<ChatRoom> getSearchedChatRooms(String keyword){
        return chatRoomRepository.findByKeyword(keyword);
    }

    @Transactional
    public List<ChatRoom> getSearchedAndExceptParticipatedChatRooms(String keyword, List<Long> participatedChatRoomIds){
        return chatRoomRepository.findExceptParticipatedByKeyword(keyword, participatedChatRoomIds);
    }

}
