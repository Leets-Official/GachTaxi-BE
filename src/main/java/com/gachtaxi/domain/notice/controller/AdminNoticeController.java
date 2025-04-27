package com.gachtaxi.domain.notice.controller;

import com.gachtaxi.domain.notice.dto.request.NoticeCreateRequest;
import com.gachtaxi.domain.notice.dto.request.NoticeUpdateRequest;
import com.gachtaxi.domain.notice.dto.response.NoticeDTO;
import com.gachtaxi.domain.notice.dto.response.NoticeDTO.NoticeResponse;
import com.gachtaxi.domain.notice.service.NoticeAdminService;
import com.gachtaxi.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ADMIN NOTICE", description = "어드민 공지사항")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminNoticeController {

    private final NoticeAdminService noticeAdminService;

    @PostMapping("/notices")
    public ApiResponse<NoticeResponse> create(@RequestBody @Validated NoticeCreateRequest dto)
    {
        NoticeDTO.NoticeResponse response = noticeAdminService.createNotice(dto);

        return ApiResponse.response(HttpStatus.OK, ResponseMessage.NOTICE_CREATE_SUCCESS.getMessage(), response);
    }

    @PatchMapping("/notices/{id}")
    public ApiResponse<NoticeDTO.NoticeResponse> update(@PathVariable Long id, @RequestBody @Validated NoticeUpdateRequest dto)
    {
        NoticeDTO.NoticeResponse response = noticeAdminService.updateNotice(id, dto);

        return ApiResponse.response(HttpStatus.OK, ResponseMessage.NOTICE_UPDATE_SUCCESS.getMessage(), response);
    }

    @DeleteMapping("/notices/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        noticeAdminService.deleteNotice(id);

        return ApiResponse.response(HttpStatus.OK, ResponseMessage.NOTICE_DELETE_SUCCESS.getMessage(), null);
    }

}
