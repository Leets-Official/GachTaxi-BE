package com.gachtaxi.domain.notice.entity;


import com.gachtaxi.domain.notice.dto.request.NoticeCreateRequest;
import com.gachtaxi.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Notice extends BaseEntity {

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    public static Notice of(NoticeCreateRequest dto) {
        return Notice.builder()
                .title(dto.title())
                .content(dto.content())
                .build();
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
