package com.gachtaxi.domain.notice.dto.response;

import com.gachtaxi.domain.notice.dto.response.NoticeDTO.NoticeResponse;
import java.util.List;
import lombok.Builder;

@Builder
public record NoticeListResponse (
    List<NoticeDTO.NoticeResponse> notices,
    NoticePageableResponse pageable
){
    public static NoticeListResponse of(List<NoticeResponse> notices, NoticePageableResponse pageable) {
        return NoticeListResponse.builder()
                .notices(notices)
                .pageable(pageable)
                .build();
    }
}
