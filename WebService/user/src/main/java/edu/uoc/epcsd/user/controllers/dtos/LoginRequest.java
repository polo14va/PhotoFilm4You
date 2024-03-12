package edu.uoc.epcsd.user.controllers.dtos;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor(force = true)
public class LoginRequest {
    @NotNull
    private String email;

    @NotNull
    private String password;

}
