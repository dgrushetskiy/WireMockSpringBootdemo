package com.wiremock.demo.dto;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UniDTO {
    private String name;
    private String country;
    private String alpha_two_code;
    private List<String> domains;
    private List<String> web_pages;
}
