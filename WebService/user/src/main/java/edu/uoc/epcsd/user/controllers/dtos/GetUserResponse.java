package edu.uoc.epcsd.user.controllers.dtos;

import edu.uoc.epcsd.user.entities.User;
import lombok.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public final class GetUserResponse {

    private final Long id;

    private final String fullName;

    private final String email;

    private final String phoneNumber;

    private final String role;

    public static GetUserResponse fromDomain(User user) {
        return GetUserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .build();
    }
}
