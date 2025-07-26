package com.service.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    public String firstName;
    public String lastName;
    public String email;
    public String phoneCode;
    public String phone;
}
