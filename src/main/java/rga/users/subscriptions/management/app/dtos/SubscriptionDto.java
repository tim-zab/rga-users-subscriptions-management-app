package rga.users.subscriptions.management.app.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Timestamp;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SubscriptionDto {

    private Long id;

    private String title;

    private String description;

    @JsonProperty("User")
    private UserDto user;

    private Timestamp timestamp;
}
