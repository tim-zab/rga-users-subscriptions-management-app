package rga.users.subscriptions.management.app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rga.users.subscriptions.management.app.enums.Role;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "e-mail")
    private String email;

    @JsonProperty(value = "initial password")
    private String password;

    @JsonProperty(value = "user role")
    private Role role;

}
