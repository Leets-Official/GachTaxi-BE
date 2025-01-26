package com.gachtaxi.domain.chat.entity;

import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.global.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
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

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime joinedAt;

    @CreatedDate
    private LocalDateTime lastReadAt;

    public static ChattingParticipant of(ChattingRoom chattingRoom, Members members) {
        return ChattingParticipant.builder()
                .chattingRoom(chattingRoom)
                .members(members)
                .build();
    }

    public void reSubscribe() {
        this.lastReadAt = LocalDateTime.now();
    }

    public void unsubscribe() {
        this.lastReadAt = LocalDateTime.now();
    }

    public void disconnect() {
        this.lastReadAt = LocalDateTime.now();
    }
}
