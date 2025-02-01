package com.gachtaxi.domain.notification.controller;

import com.gachtaxi.domain.notification.dto.response.NotificationInfoResponse;
import com.gachtaxi.domain.notification.dto.response.NotificationListResponse;
import com.gachtaxi.domain.notification.service.NotificationService;
import com.gachtaxi.global.auth.jwt.annotation.CurrentMemberId;
import com.gachtaxi.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.gachtaxi.domain.notification.controller.ResponseMessage.NOTIFICATION_DELETE_SUCCESS;
import static com.gachtaxi.domain.notification.controller.ResponseMessage.NOTIFICATION_GET_SUCCESS;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "NOTIFICATION")
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "전체 알림을 조회하는 API입니다.")
    public ApiResponse<NotificationListResponse> getNotifications(@CurrentMemberId Long memberId,
                                                                  @RequestParam int pageNum,
                                                                  @RequestParam int pageSize) {
        NotificationListResponse response = notificationService.getNotifications(memberId, pageNum, pageSize);

        return ApiResponse.response(OK, NOTIFICATION_GET_SUCCESS.getMessage(), response);
    }

    @GetMapping("/unread")
    @Operation(summary = "홈에서 알림 개수와 여부를 확인하는 API입니다.")
    public ApiResponse<NotificationInfoResponse> getInfo(@CurrentMemberId Long memberId) {
        NotificationInfoResponse response = notificationService.getInfo(memberId);

        return ApiResponse.response(OK, NOTIFICATION_GET_SUCCESS.getMessage(), response);
    }

    @DeleteMapping("/{notificationId}")
    @Operation(summary = "알림을 개별 삭제하는 API입니다.")
    public ApiResponse<Void> delete(@CurrentMemberId Long memberId,
                                    @PathVariable Long notificationId) {
        notificationService.delete(memberId, notificationId);

        return ApiResponse.response(OK, NOTIFICATION_DELETE_SUCCESS.getMessage());

    }

}
