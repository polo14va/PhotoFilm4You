package edu.uoc.epcsd.user.controllers.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public final class CreateUserRequest {

    @NonNull
    private final String password;

    @NonNull
    private final String email;

    @NonNull
    private final String fullName;

    @NonNull
    private final String phoneNumber;

    private final String role;
}
