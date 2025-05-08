package rga.users.subscriptions.management.app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class AddUserDto {

    @JsonProperty(value = "e-mail")
    private String email;

    @JsonIgnore
    private String password;

    @JsonProperty(value = "user role")
    private Role role;

}
