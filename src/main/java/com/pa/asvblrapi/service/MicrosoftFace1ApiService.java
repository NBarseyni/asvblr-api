package com.pa.asvblrapi.service;

import com.pa.asvblrapi.dto.UrlSourceImageDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.security.*;

@Service
public class MicrosoftFace1ApiService {

    @Value("${asvblrapi.app.documentFolder}")
    private String UPLOADED_FOLDER;

    @Value("${asvblrapi.app.linkWebSite}")
    private String linkWebSite;

    @Value("${asvblrapi.app.secretKey}")
    private String secretKey;

    private final String uriMicrosoftApi = "https://microsoft-face1.p.rapidapi.com/detect";
    private final String rapidapiHost = "microsoft-face1.p.rapidapi.com";

    @Value("${asvblrapi.app.apiKeyFace1}")
    private String apiKey;

    public boolean checkIfPhotoHasFace(String srcImage) throws NoSuchAlgorithmException {
        // Headers
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("x-rapidapi-host", rapidapiHost);
        headers.add("x-rapidapi-key", apiKey);
        // Body
        String token = srcImage + secretKey;
        String hashToken = convertStringToMD5(token);
        String uri = linkWebSite + "p/" + hashToken + "/" + srcImage;

        //UrlSourceImageDto body = new UrlSourceImageDto(UPLOADED_FOLDER + srcImage);
        UrlSourceImageDto body = new UrlSourceImageDto(uri);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpEntity<UrlSourceImageDto> request = new HttpEntity<>(body, headers);

        String result = restTemplate.postForObject(uriMicrosoftApi, request, String.class);

        assert result != null;
        return (!result.equals("[]"));
    }

    private String convertStringToMD5(String url) throws NoSuchAlgorithmException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(url.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmException(e.getMessage());
        }
    }
}
