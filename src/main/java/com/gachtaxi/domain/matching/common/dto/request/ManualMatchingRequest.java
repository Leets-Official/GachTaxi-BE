package com.gachtaxi.domain.matching.common.dto.request;

import com.gachtaxi.domain.matching.common.entity.enums.Tags;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public record ManualMatchingRequest(
        @NotBlank
        String title,

        String description,

        @NotBlank
        String departure,

        @NotBlank
        String destination,

        @NotNull
        LocalDateTime departureTime,

        @Schema(description = "예상 요금")
        @Min(value = 0)
        int totalCharge,

        @Schema(description = "매칭 태그")
        List<String> criteria
) {
    public List<Tags> getCriteria() {
        return this.criteria.stream()
                .map(Tags::valueOf)
                .toList();
    }
}
