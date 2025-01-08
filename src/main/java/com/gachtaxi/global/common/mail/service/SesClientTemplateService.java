package com.gachtaxi.global.common.mail.service;

import com.gachtaxi.global.common.mail.dto.request.NewTemplateRequestDto;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.CreateTemplateRequest;
import software.amazon.awssdk.services.ses.model.Template;

@Service
public class SesClientTemplateService {

    private final SesClient sesClient;

    public SesClientTemplateService(SesClient sesClient) {
        this.sesClient = sesClient;
    }

    public void createTemplate(NewTemplateRequestDto dto) {
        Template template = Template.builder()
                .templateName(dto.templateName())
                .subjectPart(dto.subject())
                .htmlPart(dto.htmlBody())
                .textPart(dto.textBody())
                .build();

        CreateTemplateRequest request = CreateTemplateRequest.builder()
                .template(template)
                .build();

        sesClient.createTemplate(request);
    }
}
