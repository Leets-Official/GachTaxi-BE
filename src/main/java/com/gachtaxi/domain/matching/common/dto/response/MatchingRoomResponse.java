package com.gachtaxi.domain.matching.common.dto.response;

import com.gachtaxi.domain.matching.common.entity.MatchingRoom;
import java.time.LocalDateTime;
import java.util.List;

public record MatchingRoomResponse(
        Long roomId,
        String title,
        String departure,
        String destination,
        LocalDateTime departureTime,
        int maxCapacity,
        int currentMembers,
        List<String> tags
) {
    public static MatchingRoomResponse from(MatchingRoom matchingRoom) {
        return new MatchingRoomResponse(
                matchingRoom.getId(),
                matchingRoom.getTitle(),
                matchingRoom.getDeparture(),
                matchingRoom.getDestination(),
                matchingRoom.getDepartureTime(),
                matchingRoom.getCapacity(),
                matchingRoom.getCurrentMemberCount(),
                matchingRoom.getTags()
        );
    }
}
