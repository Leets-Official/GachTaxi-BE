package com.gachtaxi.domain.members.controller;

import com.gachtaxi.global.common.mail.dto.request.NewTemplateRequestDto;
import com.gachtaxi.global.common.mail.service.SesClientTemplateService;
import com.gachtaxi.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gachtaxi.global.common.mail.message.ResponseMessage.EMAIL_TEMPLATE_CREATE_SUCCESS;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminMemberController {

    private final SesClientTemplateService sesClientTemplateService;

    @PostMapping("/email/template")
    @Operation(summary = "이메일 템플릿을 생성하는 API 입니다. 생성한 템플릿 명을 환경변수에 적용해주세요.")
    public ApiResponse createTemplate(@RequestBody @Valid NewTemplateRequestDto dto){
        sesClientTemplateService.createTemplate(dto);
        return ApiResponse.response(HttpStatus.OK, EMAIL_TEMPLATE_CREATE_SUCCESS.getMessage());
    }
}
