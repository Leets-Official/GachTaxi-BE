package com.gachtaxi.domain.members.entity;

import com.gachtaxi.global.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@SuperBuilder
@Table(name="blacklists",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id", "blacklist_member_id"})
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Blacklists extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  private Members requester;

  @ManyToOne(fetch = FetchType.LAZY)
  private Members receiver;

  public static Blacklists create(Members requester, Members receiver) {
    return Blacklists.builder()
        .requester(requester)
        .receiver(receiver)
        .build();
  }
}
