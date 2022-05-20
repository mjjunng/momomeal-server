package com.capstone.momomeal.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChatRoomListDto {
    private Long id;
    private Category category;
    private String title;
    private Long hostId;
    private int maxCapacity;
    private String storeName;
    private String pickupPlaceName;
    private int distance;
    private LocalDateTime createdDate;
    private Double pickupPlaceXCoord;
    private Double pickupPlaceYCoord;


    public ChatRoomListDto(ChatRoom chatRoom, double x, double y) {
        this.id = chatRoom.getId();
        this.category = chatRoom.getCategory();
        this.title = chatRoom.getTitle();
        this.hostId = chatRoom.getHostId();
        this.maxCapacity = chatRoom.getMaxCapacity();
        this.storeName = chatRoom.getStoreName();
        this.pickupPlaceName = chatRoom.getPickupPlaceName();
        this.createdDate = chatRoom.getCreatedDate();
        // 사용자의 현재 위치와 수령 장소까지의 거리 계산
        this.distance = calculateDistance(x, y,
                chatRoom.getPickupPlaceXCoord(), chatRoom.getPickupPlaceYCoord());

        this.pickupPlaceXCoord = chatRoom.getPickupPlaceXCoord();
        this.pickupPlaceYCoord = chatRoom.getPickupPlaceYCoord();
    }

    // 사용자의 현재 위치와 수령 장소까지의 거리 계산 메서드
    public int calculateDistance(double memberX, double memberY, double placeX, double placeY){
        double x = Math.cos(Math.toRadians(memberX) * 6400 * 2 * 3.14 / 360) * Math.abs(memberY - placeY);
        double y = 111 * Math.abs(memberX - placeX);

        double theta = memberY - placeY;
        double dist = Math.sin(deg2rad(memberX)) * Math.sin(deg2rad(placeX)) + Math.cos(deg2rad(memberX))
                * Math.cos(deg2rad(placeX)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist *= 60 * 1.1515 * 1609.344;

        return (int) dist;

    }

    // converts decimal degrees to radians
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // converts radians to decimal degrees
    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
