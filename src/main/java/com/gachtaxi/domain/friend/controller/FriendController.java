package com.gachtaxi.domain.friend.controller;

import com.gachtaxi.domain.friend.dto.request.FriendRequestDto;
import com.gachtaxi.domain.friend.dto.request.FriendUpdateDto;
import com.gachtaxi.domain.friend.dto.response.FriendsSliceResponse;
import com.gachtaxi.domain.friend.service.FriendService;
import com.gachtaxi.global.auth.jwt.annotation.CurrentMemberId;
import com.gachtaxi.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.gachtaxi.domain.friend.controller.ResponseMessage.*;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PostMapping
    public ApiResponse<Void> sendFriendRequest(
            @CurrentMemberId Long senderId,
            @RequestBody FriendRequestDto dto
    ){
        friendService.sendFriendRequest(senderId, dto);
        return ApiResponse.response(OK, FRIEND_REQUEST_SUCCESS.getMessage());
    }

    // 나의 친구를 반환하는 API
    @GetMapping
    public ApiResponse<FriendsSliceResponse> getFriendsList(
            @CurrentMemberId Long memberId,
            @RequestParam int pageNum,
            @RequestParam int pageSize
    ){
        FriendsSliceResponse response = friendService.findFriendsListByMemberId(memberId, pageNum, pageSize);
        return ApiResponse.response(OK, FRIEND_LIST_SUCCESS.getMessage(), response);
    }

    @PatchMapping
    public ApiResponse<Void> acceptFriendRequest(
            @CurrentMemberId Long currentId,
            @RequestBody FriendUpdateDto dto
    ){
        friendService.updateFriendStatus(dto.memberId(), currentId); // 친구 요청 보낸 사람(dto), 받은 사람(토큰 추출)
        return ApiResponse.response(OK, FRIEND_STATUS_ACCEPTED.getMessage());
    }

    @DeleteMapping("/{memberId}")
    public ApiResponse<Void> deleteFriend(
            @CurrentMemberId Long currentId,
            @PathVariable Long memberId
    ) {
        friendService.deleteFriend(currentId, memberId);

        return ApiResponse.response(OK, FRIEND_DELETE.getMessage());
    }

}
