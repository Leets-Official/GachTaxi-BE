package com.gachtaxi.global.common.mail.dto.request;

import jakarta.validation.constraints.NotBlank;

public record NewTemplateRequestDto(

        @NotBlank String templateName, // 템플릿 제목
        @NotBlank String subject, // 메일 제목
        @NotBlank String htmlBody, // html 바디
        @NotBlank String textBody // html 전환 오류시 사용

) { }
