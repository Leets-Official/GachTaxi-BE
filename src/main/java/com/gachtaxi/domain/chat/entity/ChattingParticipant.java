package com.gachtaxi.domain.chat.entity;

import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.global.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
}
