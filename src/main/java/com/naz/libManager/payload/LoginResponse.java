package com.naz.libManager.payload;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String token;
}
