package com.capstone.momomeal.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class ChatRoom extends BaseTimeEntity{
    @Id @GeneratedValue
    @Column(name = "chatroom_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private Category category;
    private String title;
    private Long hostId;
    private int maxCapacity;
    private String storeName;
    private String pickupPlaceName;
    private double pickupPlaceXCoord;
    private double pickupPlaceYCoord;

    public ChatRoom() {
    }


    public ChatRoom(Category category, String title, Long hostId, int maxCapacity,
                String storeName, String pickupPlaceName, double pickupPlaceXCoord, double pickupPlaceYCoord) {
        this.category = category;
        this.title = title;
        this.hostId = hostId;
        this.maxCapacity = maxCapacity;
        this.storeName = storeName;
        this.pickupPlaceName = pickupPlaceName;
        this.pickupPlaceXCoord = pickupPlaceXCoord;
        this.pickupPlaceYCoord = pickupPlaceYCoord;
    }

}
