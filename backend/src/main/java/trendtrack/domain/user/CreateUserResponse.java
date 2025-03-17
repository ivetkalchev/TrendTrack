package trendtrack.domain.user;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserResponse {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
}