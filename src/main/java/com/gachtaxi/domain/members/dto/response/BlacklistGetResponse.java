package com.gachtaxi.domain.members.dto.response;

import com.gachtaxi.domain.members.entity.Blacklists;
import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public record BlacklistGetResponse(
    List<BlacklistInfo> blacklists,
    Integer pageNumber,
    Integer pageSize,
    Integer numberOfElements,
    Boolean last
) {
  public static BlacklistGetResponse of(Page<Blacklists> blacklistsPage) {
    List<BlacklistInfo> responseList = blacklistsPage.stream()
        .map(BlacklistInfo::of)
        .toList();

    return BlacklistGetResponse.builder()
        .blacklists(responseList)
        .pageNumber(blacklistsPage.getNumber())
        .pageSize(blacklistsPage.getSize())
        .numberOfElements(blacklistsPage.getNumberOfElements())
        .last(blacklistsPage.isLast())
        .build();
  }

  record BlacklistInfo(
      Long receiverId,
      String receiverNickname,
      String gender
  ) {
    public static BlacklistInfo of(Blacklists blacklists) {
      return new BlacklistInfo(
          blacklists.getReceiver().getId(),
          blacklists.getReceiver().getNickname(),
          blacklists.getReceiver().getGender().name()
      );
    }
  }
}
