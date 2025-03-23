package com.ortim.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

//    @NotBlank()
//    @Size(min = 3, max = 30, message = "isminizi muhakkak girin ve en az 3 karakter en fazla 30 karakter olmalı.")
//    private String name;
//
//    @NotBlank(message = "Soyisminizi girmelisiniz!")
//    private String lastName;

    @NotBlank
    @Email(message = "Email formatında olmalı")
    private String email;

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}", message = "Parola en az 8 karakter, bir küçük harf, bir büyük harf ve bir sayı içermelidir.")
    @NotBlank
    private String password;

}
