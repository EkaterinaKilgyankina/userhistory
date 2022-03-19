package com.insite.userhistory.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class TokenDto {
    private String token;
}
