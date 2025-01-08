package com.gachtaxi.global.common.mail.service;

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

    public void createTemplate(String templateName, String subject, String htmlBody, String textBody) {
        Template template = Template.builder()
                .templateName(templateName)
                .subjectPart(subject)
                .htmlPart(htmlBody)
                .textPart(textBody)
                .build();

        CreateTemplateRequest request = CreateTemplateRequest.builder()
                .template(template)
                .build();

        sesClient.createTemplate(request);
    }
}
