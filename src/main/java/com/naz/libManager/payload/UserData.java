package com.naz.libManager.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
}
