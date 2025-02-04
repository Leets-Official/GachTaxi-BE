package com.gachtaxi.domain.members.dto.response;

import com.gachtaxi.domain.members.entity.Blacklists;
import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

@Builder
public record BlacklistGetResponse(
    List<BlacklistInfo> blacklists,
    BlacklistPageable pageable
) {
  public static BlacklistGetResponse of(Slice<Blacklists> blacklistsPage) {
    List<BlacklistInfo> responseList = blacklistsPage.stream()
        .map(BlacklistInfo::of)
        .toList();

    return BlacklistGetResponse.builder()
        .blacklists(responseList)
        .pageable(BlacklistPageable.of(blacklistsPage))
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

  record BlacklistPageable(
      Integer pageNumber,
      Integer pageSize,
      Integer numberOfElements,
      Boolean last
  ) {

    public static BlacklistPageable of (Slice<Blacklists> blacklistsPage) {
      return new BlacklistPageable(
          blacklistsPage.getNumber(),
          blacklistsPage.getSize(),
          blacklistsPage.getNumberOfElements(),
          blacklistsPage.isLast()
      );
    }
  }
}
