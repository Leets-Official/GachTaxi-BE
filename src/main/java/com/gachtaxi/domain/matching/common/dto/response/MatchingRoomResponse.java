package com.gachtaxi.domain.matching.common.dto.response;

import com.gachtaxi.domain.matching.common.entity.MatchingRoom;
import com.gachtaxi.domain.members.entity.enums.Gender;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record MatchingRoomResponse(
        Long roomId,
        Long chattingRoomId,
        String nickname,
        Gender gender,
        String profilePicture,
        String description,
        String departure,
        String destination,
        LocalDateTime departureTime,
        LocalDate departureDate,
        int maxCapacity,
        int currentMembers,
        List<String> tags
) {
    public static MatchingRoomResponse from(MatchingRoom matchingRoom) {
        return new MatchingRoomResponse(
                matchingRoom.getId(),
                matchingRoom.getChattingRoomId(),
                matchingRoom.getRoomMaster().getNickname(),
                matchingRoom.getRoomMaster().getGender(),
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
