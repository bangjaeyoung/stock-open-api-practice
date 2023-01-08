package com.example.openapitest.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 요청 데이터에 Parameter와 Body 정보를 담아서 요청하는 로직
 */
@RestController
@RequestMapping("/get-stock-info")
@CrossOrigin(originPatterns = "https://openapi.koreainvestment.com")
public class OpenApiControllerV2 {
    @GetMapping
    public ResponseEntity getStockInfo(
            @RequestParam("FID_COND_MRKT_DIV_CODE") String FID_COND_MRKT_DIV_CODE,
            @RequestParam("FID_INPUT_ISCD") String FID_INPUT_ISCD,
            @RequestBody String authorization,
            @RequestBody String appKey) {

        // 최종 반환 데이터 선언
        Map<String, Object> result = new HashMap<>();

        // 요청 body 설정
        Map<String, String> requestBody = new LinkedHashMap<>();
        requestBody.put("authorization", authorization);
        requestBody.put("appkey", appKey);

        HttpEntity<Object> entity = new HttpEntity<>(requestBody);

        // http 요청 주소 및 query parameter 설정
        String url = "https://openapi.koreainvestment.com:9443/uapi/domestic-stock/v1/quotations/inquire-price";

        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("FID_COND_MRKT_DIV_CODE", FID_COND_MRKT_DIV_CODE)
                .queryParam("FID_INPUT_ISCD", FID_INPUT_ISCD)
                .build(true);

        RestTemplate restTemplate = new RestTemplate();

        // http 요청 실시 / GET QueryString
        ResponseEntity<Object> response =
                restTemplate.exchange(
                        uriBuilder.toString(),
                        HttpMethod.GET,
                        entity,
                        Object.class
                );

        result.put("responseBody", response.getBody());
        HttpStatus httpStatus = response.getStatusCode();

        // 요청 성공 시, 문구 출력
        if (httpStatus.is2xxSuccessful()) System.out.println("Request Successfully!");

        return new ResponseEntity<>(result, httpStatus);
    }
}
