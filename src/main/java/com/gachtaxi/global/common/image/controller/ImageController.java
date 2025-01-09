package com.gachtaxi.global.common.image.controller;

import com.gachtaxi.global.common.image.ImageUtil;
import com.gachtaxi.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gachtaxi.global.common.image.controller.ResponseMessage.PRESIGNED_URL_GENERATE_SUCCESS;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "IMAGE")
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageUtil imageUtil;

    @GetMapping()
    @Operation(summary = "Presigned Url 반환을 위한 요청 API 입니다.")
    public ApiResponse<String> getPutUrl() {
        String putUrl = imageUtil.generateUrl();

        return ApiResponse.response(OK, PRESIGNED_URL_GENERATE_SUCCESS.getMessage(), putUrl);
    }
}
