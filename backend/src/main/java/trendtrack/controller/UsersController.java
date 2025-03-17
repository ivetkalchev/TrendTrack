package trendtrack.controller;

import lombok.*;
import jakarta.validation.*;
import trendtrack.business.*;
import trendtrack.domain.user.*;
import jakarta.annotation.security.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;
    private final AuthService authService;

    @GetMapping("{id}")
    @RolesAllowed({"ADMIN", "CLIENT"})
    public ResponseEntity<User> getUser(@PathVariable final Long id,
                                        @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        authService.validateUser(id, token);

        User user = userService.getUser(id);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<GetAllUsersResponse> getAllUsers(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "9") int size) {

        GetAllUsersRequest request = GetAllUsersRequest.builder()
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .page(page)
                .size(size)
                .build();

        GetAllUsersResponse response = userService.getUsers(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(request));
    }

    @DeleteMapping("{id}")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @RolesAllowed({"ADMIN", "CLIENT"})
    public ResponseEntity<Void> updateUser(@PathVariable Long id,
                                           @RequestBody @Valid UpdateUserRequest request,
                                           @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        authService.validateUser(id, token);

        request.setId(id);
        userService.updateUser(request);

        return ResponseEntity.noContent().build();
    }
}