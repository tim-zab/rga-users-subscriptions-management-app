package rga.users.subscriptions.management.app.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rga.users.subscriptions.management.app.dtos.ResponseMessageDto;
import rga.users.subscriptions.management.app.dtos.UserAuthRequestDto;
import rga.users.subscriptions.management.app.dtos.UserAuthResponseDto;
import rga.users.subscriptions.management.app.services.auth.UserAuthService;

@AllArgsConstructor
@RestController
@RequestMapping("/rest/v1/security")
@SecurityRequirements
public class UserAuthController {

    private final UserAuthService service;

    @Operation(summary = "User authentication", description = "For user to get authenticated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User has just authenticated successfully",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserAuthResponseDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid user data",
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
    @PostMapping("/auth")
    public ResponseEntity<UserAuthResponseDto> authenticate(@Valid @RequestBody final UserAuthRequestDto request) {
        return ResponseEntity.status(HttpStatus.OK).body(service.authenticate(request));
    }

    @Operation(summary = "User registration (new user creation)", description = "For user to get registered (create new user)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User has just registered successfully",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserAuthResponseDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid user data",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ResponseMessageDto.class))),
            @ApiResponse(responseCode = "503", description = "Service unavailable",
                    content = @Content(mediaType = "application/json", schema =
                    @Schema(implementation = ResponseMessageDto.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<UserAuthResponseDto> register(@Valid @RequestBody final UserAuthRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(request));
    }

}
