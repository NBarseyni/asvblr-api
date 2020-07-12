package com.pa.asvblrapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UrlSourceImageDto {
    private String url;

    public UrlSourceImageDto(String url) {
        this.url = url;
    }
}
