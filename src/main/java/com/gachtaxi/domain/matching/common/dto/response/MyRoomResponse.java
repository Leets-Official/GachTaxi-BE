package com.gachtaxi.domain.matching.common.dto.response;

import com.gachtaxi.domain.matching.common.entity.MatchingRoom;
import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalDate;

public record MyRoomResponse(
        Long roomId,
        Long chattingRoomId,
        String description,
        String departure,
        String destination,
        LocalDateTime departureTime,
        LocalDate departureDate,
        int maxCapacity,
        int currentMembers,
        List<String> tags
) {
    public static MyRoomResponse from(MatchingRoom matchingRoom) {
        return new MyRoomResponse(
                matchingRoom.getId(),
                matchingRoom.getChattingRoomId(),
                matchingRoom.getDescription(),
                matchingRoom.getDeparture(),
                matchingRoom.getDestination(),
                matchingRoom.getDepartureTime(),
                matchingRoom.getDepartureDate(),
                matchingRoom.getCapacity(),
                matchingRoom.getCurrentMemberCount(),
                matchingRoom.getTags()
        );
    }
}
