package com.gachtaxi.domain.notice.dto.response;

import com.gachtaxi.domain.notice.entity.Notice;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class NoticeDTO {

    public record NoticeResponse(
            @NotNull Long id,
            @NotNull String title,
            @NotNull String content,
            @NotNull LocalDateTime createDate
    ) {

        public static NoticeResponse from(Notice notice) {
            return new NoticeResponse(
                    notice.getId(),
                    notice.getTitle(),
                    notice.getContent(),
                    notice.getCreateDate()
            );
        }
    }

}
