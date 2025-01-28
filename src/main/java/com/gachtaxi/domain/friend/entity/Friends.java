package com.gachtaxi.domain.friend.entity;

import com.gachtaxi.domain.friend.entity.enums.FriendStatus;
import com.gachtaxi.domain.members.entity.Members;
import com.gachtaxi.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static com.gachtaxi.domain.friend.entity.enums.FriendStatus.ACCEPTED;
import static com.gachtaxi.domain.friend.entity.enums.FriendStatus.PENDING;


@Entity
@Getter
@Table(
        name = "Friends",
        uniqueConstraints = @UniqueConstraint(columnNames = {"sender_id", "receiver_id"})
)@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friends extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Members sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Members receiver;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private FriendStatus status = PENDING;

    public static Friends of(Members sender, Members receiver) {
        return Friends.builder()
                .sender(sender)
                .receiver(receiver)
                .build();
    }

    public void updateStatus(){
        this.status = ACCEPTED;
    }
}
