package net.musecom.community.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Controller
public class KakaoLoginController {

    private final String clientId = "8a6f7c20d5408fbc4fdfe933b6297b05"; // īī�� REST API Ű
    private final String redirectUri = "http://localhost:8080/kakaoLogin"; // ������ Redirect URI
    private final String clientSecret = "YOUR_CLIENT_SECRET"; // ���û���

    // īī�� �α��� ��û URL�� ����Ʈ
    @GetMapping("/kakao/login")
    public String kakaoLogin() {
        String kakaoLoginUrl = "https://kauth.kakao.com/oauth/authorize" +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code";
        return "redirect:" + kakaoLoginUrl;
    }

    // īī�� �α��� �ݹ� �޼���
    @GetMapping("/kakaoLogin")
    public String kakaoCallback(@RequestParam String code, Model model, HttpSession session) {
        String accessToken = getAccessToken(code);
        if (accessToken != null) {
            Map<String, Object> userInfo = getUserInfo(accessToken);
            session.setAttribute("userInfo", userInfo); // ����� ���� ���ǿ� ����
            return "redirect:/home"; // �α��� ���� �� Ȩ���� �̵�
        } else {
            model.addAttribute("error", "�α��ο� �����߽��ϴ�.");
            return "login"; // �α��� ���� �� �α��� �������� �̵�
        }
    }

    // Access Token�� ��� ���� �޼���
    private String getAccessToken(String code) {
        String accessToken = null;
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        try {
            RestTemplate restTemplate = new RestTemplate();
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", clientId);
            params.add("redirect_uri", redirectUri);
            params.add("code", code);
            if (!clientSecret.isEmpty()) {
                params.add("client_secret", clientSecret);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
            accessToken = (String) response.getBody().get("access_token");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return accessToken;
    }

    // Access Token�� ����� ����� ������ �������� �޼���
    private Map<String, Object> getUserInfo(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
        Map<String, Object> userInfo = new HashMap<>();

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);

            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, Map.class);

            userInfo = response.getBody();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userInfo;
    }
}
