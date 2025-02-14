package com.gachtaxi.domain.matching.common.dto.response;

import com.gachtaxi.domain.matching.common.entity.MatchingRoom;
import java.util.List;

public record MatchingRoomResponse(
        Long roomId,
        Long chattingRoomId,
        String nickname,
        String profilePicture,
        String description,
        String departure,
        String destination,
        String departureTime,
        String departureDate,
        int maxCapacity,
        int currentMembers,
        List<String> tags
) {
    public static MatchingRoomResponse from(MatchingRoom matchingRoom) {
        return new MatchingRoomResponse(
                matchingRoom.getId(),
                matchingRoom.getChattingRoomId(),
                matchingRoom.getRoomMaster().getNickname(),
                matchingRoom.getRoomMaster().getProfilePicture(),
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
