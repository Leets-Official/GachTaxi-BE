package com.gachtaxi.domain.chat.entity;

import com.gachtaxi.domain.chat.entity.enums.ChatStatus;
import com.gachtaxi.domain.chat.exception.UnSubscriptionException;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChattingParticipant extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "chatting_room_id")
    private ChattingRoom chattingRoom;

    @ManyToOne
    @JoinColumn(name = "members_id")
    private Members members;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ChatStatus status = ChatStatus.ACTIVE;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime joinedAt;

    private LocalDateTime disconnectedAt;

    public static ChattingParticipant of(ChattingRoom chattingRoom, Members members) {
        return ChattingParticipant.builder()
                .chattingRoom(chattingRoom)
                .members(members)
                .build();
    }

    public void checkSubscriptionStatus() {
        if (this.status == ChatStatus.INACTIVE) {
            throw new UnSubscriptionException();
        }
    }

    public void subscribe() {
        this.status = ChatStatus.ACTIVE;
    }

    public void unsubscribe() {
        this.status = ChatStatus.INACTIVE;
        this.disconnectedAt = LocalDateTime.now();
    }

    public void disconnect() {
        if (this.status == ChatStatus.ACTIVE) {
            this.status = ChatStatus.INACTIVE;
            this.disconnectedAt = LocalDateTime.now();
        }
    }
}
