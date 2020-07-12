package com.pa.asvblrapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidityPhotoDto {
    private boolean validity;

    public ValidityPhotoDto(boolean validity) {
        this.validity = validity;
    }
}
