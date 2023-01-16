package com.wiremock.demo.service;

import com.wiremock.demo.dto.UniDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.List;

@Service
public class UniService {
    @Autowired
    private  RestTemplate restTemplate;

    private String apiURL;

    public UniService(@Value("${connectingAPIURL}") String apiURL) {
        this.apiURL = apiURL;
    }

    public List<UniDTO> getUniInfo(String name) {
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(apiURL + "/search").queryParam("name", name).encode().toUriString();

        System.out.println("Calling urlTemplate " + urlTemplate);;

        ResponseEntity<List<UniDTO>> rateResponse =
                restTemplate.exchange(urlTemplate,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        }, name);
        return rateResponse.getBody();
    }
}
