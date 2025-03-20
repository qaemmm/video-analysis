package com.videoanalysis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.videoanalysis.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final UserService userService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${coze.api.url}")
    private String cozeApiUrl;

    @Value("${coze.api.token}")
    private String cozeApiToken;

    @Value("${coze.api.workflow-id}")
    private String workflowId;

    public String parseVideo(String videoUrl, User user) {
        if (!userService.deductPoints(user)) {
            throw new RuntimeException("Insufficient points");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(cozeApiToken);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("input", videoUrl);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("parameters", parameters);
        requestBody.put("workflow_id", workflowId);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            Map<String, Object> response = restTemplate.postForObject(
                    cozeApiUrl,
                    request,
                    Map.class
            );

            if (response != null && response.containsKey("data")) {
                String data = (String) response.get("data");
                // 解析返回的JSON字符串
                Map<String, Object> parsedData = objectMapper.readValue(data, Map.class);
                if (parsedData.containsKey("output")) {
                    Map<String, Object> output = (Map<String, Object>) parsedData.get("output");
                    if (output.containsKey("video")) {
                        return (String) output.get("video");
                    }
                }
            }
            throw new RuntimeException("Failed to parse video");
        } catch (Exception e) {
            userService.addPoints(user, 1); // Refund points on failure
            throw new RuntimeException("Error parsing video: " + e.getMessage());
        }
    }
} 