package com.insite.userhistory.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Accessors(chain = true)
@Data
public class MessageDto {
    @NotBlank(message = "name is mandatory")
    String userName;

    @NotBlank(message = "message is mandatory")
    String text;
}
