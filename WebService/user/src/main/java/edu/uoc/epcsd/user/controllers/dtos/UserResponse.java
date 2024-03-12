package edu.uoc.epcsd.user.controllers.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public final class UserResponse {

    @NonNull
    private Long id;

    @NonNull
    private String fullName;

    @NonNull
    private String email;

    @NonNull
    private String phoneNumber;

    private String role;
}