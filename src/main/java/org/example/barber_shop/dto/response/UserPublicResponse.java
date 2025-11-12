package org.example.barber_shop.dto.response;

import lombok.Data;

@Data
public class UserPublicResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
//    String token; uncomment for later security (jwt)
}
