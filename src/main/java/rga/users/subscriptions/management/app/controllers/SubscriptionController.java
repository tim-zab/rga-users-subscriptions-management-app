package rga.users.subscriptions.management.app.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rga.users.subscriptions.management.app.dtos.AddSubscriptionDto;
import rga.users.subscriptions.management.app.dtos.ResponseMessageDto;
import rga.users.subscriptions.management.app.dtos.SubscriptionDto;
import rga.users.subscriptions.management.app.services.common.SubscriptionService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/rest/v1/users")
public class SubscriptionController extends PageableResponseHandler {

    private final SubscriptionService service;

    @Operation(summary = "Create subscription", description = "Subscription adding")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Subscription has been added successfully",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SubscriptionDto.class)) }),
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
    @PostMapping("/{id}")
    public ResponseEntity<SubscriptionDto> addSubscription(@PathVariable ("id") Long userId,
                                                           @Valid @RequestBody AddSubscriptionDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.add(userId,dto));
    }


    @Operation(summary = "Get all subscriptions by user id", description = "Getting all subscriptions for particular user",
            parameters = {
                    @Parameter(description = "Page number (0..N)",
                            schema = @Schema(type = "integer", defaultValue = "0"),
                            in = ParameterIn.QUERY,
                            name = "page"),
                    @Parameter(description = "Elements quantity on page",
                            schema = @Schema(type = "integer", maximum = "50", defaultValue = "10"),
                            in = ParameterIn.QUERY,
                            name = "size"),
                    @Parameter(description = "Sorting conditions: property(,asc|desc). " +
                            "<br>By default, data is sorted by timestamp:ASC",
                            schema = @Schema(type = "string"),
                            in = ParameterIn.QUERY,
                            name = "sort")
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscriptions have been got successfully",
                    headers = {
                            @Header(name = "X-Total-Elements-Count",
                                    description = "Total elements",
                                    schema = @Schema(type = "integer", format = "int32", example = "1")),
                            @Header(name = "X-Total-Pages",
                                    description = "Total pages",
                                    schema = @Schema(type = "integer", format = "int32", example = "1")),
                            @Header(name = "X-Current-Page",
                                    description = "Current page",
                                    schema = @Schema(type = "integer", format = "int32", example = "1"))},
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = SubscriptionDto.class)))),
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
    @GetMapping("/{id}/subscriptions")
    public ResponseEntity<List<SubscriptionDto>> getAllSubscriptionsByUserId(@PathVariable ("id") Long userId,
                                                               @Parameter(hidden = true) @PageableDefault Pageable pageable) {
        return pageableSuccessResponse(service.getByUserId(userId, convertPageable(pageable)));
    }

    @Operation(summary = "Delete subscription", description = "Deleting subscription by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Subscription has just been deleted successfully"),
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
    @DeleteMapping("/{id}/subscriptions/{sub_id}")
    public ResponseEntity<Void> deleteSubscriptionById(@PathVariable ("id") Long userId, @PathVariable ("sub_id") Long subId) {
        service.deleteById(userId, subId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
