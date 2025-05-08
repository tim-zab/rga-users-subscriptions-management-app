package rga.users.subscriptions.management.app.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rga.users.subscriptions.management.app.dtos.AddUserDto;
import rga.users.subscriptions.management.app.dtos.ResponseMessageDto;
import rga.users.subscriptions.management.app.dtos.UserDto;
import rga.users.subscriptions.management.app.enums.Role;
import rga.users.subscriptions.management.app.services.common.UserService;

@AllArgsConstructor
@RestController
@RequestMapping("/rest/v1/users")
public class UserController {

    private final UserService service;

    @Operation(summary = "User creation", description = "Add new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User has been added successfully",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ResponseMessageDto.class))),
            @ApiResponse(responseCode = "403", description = "Access is forbidden",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ResponseMessageDto.class))),
            @ApiResponse(responseCode = "404", description = "There is nothing found",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ResponseMessageDto.class))),
            @ApiResponse(responseCode = "503", description = "Service unavailable",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ResponseMessageDto.class)))
    })
    @PostMapping
    public ResponseEntity<UserDto> createNewUser(@Valid @RequestBody AddUserDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(dto));
    }

    @Operation(summary = "Get user by id", description = "Getting user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User has been got successfully",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ResponseMessageDto.class))),
            @ApiResponse(responseCode = "403", description = "Access is forbidden",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ResponseMessageDto.class))),
            @ApiResponse(responseCode = "404", description = "There is nothing found",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ResponseMessageDto.class))),
            @ApiResponse(responseCode = "503", description = "Service unavailable",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ResponseMessageDto.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getUserById(id));
    }

    @Operation(summary = "Update user role", description = "Updating user role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User role has been updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ResponseMessageDto.class))),
            @ApiResponse(responseCode = "403", description = "Access is forbidden",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ResponseMessageDto.class))),
            @ApiResponse(responseCode = "404", description = "There is nothing found",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ResponseMessageDto.class))),
            @ApiResponse(responseCode = "503", description = "Service unavailable",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ResponseMessageDto.class)))
    })
    @PutMapping("/{id}/role")
    public ResponseEntity<Void> updateUserRole(@PathVariable Long id, @RequestBody Role role) {
        service.updateUserRole(id, role);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Update user password", description = "Updating user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User password has been updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ResponseMessageDto.class))),
            @ApiResponse(responseCode = "403", description = "Access is forbidden",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ResponseMessageDto.class))),
            @ApiResponse(responseCode = "404", description = "There is nothing found",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ResponseMessageDto.class))),
            @ApiResponse(responseCode = "503", description = "Service unavailable",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ResponseMessageDto.class)))
    })
    @PutMapping("/{id}/pass")
    public ResponseEntity<Void> updateUserPassword(@PathVariable Long id, @RequestBody String password) {
        service.updateUserPassword(id, password);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Delete user", description = "Deleting user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User has just been deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ResponseMessageDto.class))),
            @ApiResponse(responseCode = "403", description = "Access is forbidden",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ResponseMessageDto.class))),
            @ApiResponse(responseCode = "404", description = "There is nothing found",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ResponseMessageDto.class))),
            @ApiResponse(responseCode = "503", description = "Service unavailable",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ResponseMessageDto.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
