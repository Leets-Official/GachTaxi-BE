package com.gachtaxi.domain.matching.common.dto.request;

import com.gachtaxi.domain.matching.common.entity.enums.Tags;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public record ManualMatchingRequest(

        String description,

        @NotBlank
        String startName,

        @NotBlank
        String destinationName,

        @NotNull
        String departureTime,

        @Schema(description = "예상 요금")
        @Min(value = 4000)
        int expectedTotalCharge,

        @Schema(description = "매칭 태그")
        List<String> criteria,

        @Schema(description = "초대할 친구 아이디 리스트")
        List<Long> members
) {
    public List<Tags> getCriteria() {
        return this.criteria.stream()
                .map(Tags::valueOf)
                .toList();
    }

    public List<Long> getFriendsId() {
        return members;
    }

    public int getTotalCharge() {
        return expectedTotalCharge;
    }

    public String getDeparture() {
        return startName;
    }

    public String getDestination() {
        return destinationName;
    }
}
