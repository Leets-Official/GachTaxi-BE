package com.gachtaxi.domain.notice.controller;



import com.gachtaxi.domain.notice.dto.response.NoticeDTO;
import com.gachtaxi.domain.notice.dto.response.NoticeDTO.NoticeResponse;
import com.gachtaxi.domain.notice.dto.response.NoticeListResponse;
import com.gachtaxi.domain.notice.service.NoticeService;
import com.gachtaxi.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.gachtaxi.domain.notice.controller.ResponseMessage.GET_NOTICE_ALL_SUCCESS;
import static com.gachtaxi.domain.notice.controller.ResponseMessage.GET_NOTICE_SUCCESS;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "NOTICE", description = "공지사항")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notices")
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "공지사항 단건 상세 조회")
    @GetMapping("/{noticeId}")
    public ApiResponse<NoticeResponse> getNoticeDetail(@PathVariable Long noticeId) {
        NoticeDTO.NoticeResponse response = noticeService.getNoticeDetail(noticeId);

        return ApiResponse.response(OK, GET_NOTICE_SUCCESS.getMessage(), response);
    }

    @Operation(summary = "공지사항 목록 조회")
    @GetMapping("/list")
    public ApiResponse<NoticeListResponse> getNoticeList(@RequestParam int pageNumber, @RequestParam int pageSize) {
        NoticeListResponse response = noticeService.getNoticeListResponse(pageNumber, pageSize);

        return ApiResponse.response(OK, GET_NOTICE_ALL_SUCCESS.getMessage(), response);
    }

}
