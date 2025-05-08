package rga.users.subscriptions.management.app.services.common;

import rga.users.subscriptions.management.app.dtos.AddUserDto;
import rga.users.subscriptions.management.app.dtos.UserAuthRequestDto;
import rga.users.subscriptions.management.app.dtos.UserDto;
import rga.users.subscriptions.management.app.entities.User;
import rga.users.subscriptions.management.app.enums.Role;

public interface UserService {

    UserDto createUser(AddUserDto dto);

    UserDto getUserById(Long id);

    void updateUserRole(Long id, Role role);

    void updateUserPassword(Long id, String password);

    void deleteUser(Long id);

    User findById(Long id);

    User getByEmail(String email);

    User create(UserAuthRequestDto dto);

    boolean validateEmail(String email);

    boolean validatePassword(String password);

    boolean existsByEmail(String email);

}
