package trendtrack.domain.user;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllUsersRequest {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private int page;
    private int size;
}