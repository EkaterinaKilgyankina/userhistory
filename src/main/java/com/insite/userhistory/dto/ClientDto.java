package com.insite.userhistory.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;

@Accessors(chain = true)
@Data
public class ClientDto {
    @NotBlank(message = "name is mandatory")
    private String name;

    @NotBlank(message = "password is mandatory")
    @Size(min = 5, message = "password length must be more than five")
    @Pattern(regexp = "^(?=.*?[#?!@$%^&*-])(?=.*?[0-9])(?=.*?[a-z])(?=.*?[A-Z]).{5,}",
            message = "password must contain letters (upper and lower case), digits and special symbols")
    private String password;
}