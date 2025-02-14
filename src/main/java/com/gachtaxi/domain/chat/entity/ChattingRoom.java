package com.gachtaxi.domain.chat.entity;

import com.gachtaxi.domain.chat.entity.enums.ChatStatus;
import com.gachtaxi.global.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChattingRoom extends BaseEntity {

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ChatStatus status = ChatStatus.ACTIVE;

    public void delete() {
        status = ChatStatus.INACTIVE;
    }
}
