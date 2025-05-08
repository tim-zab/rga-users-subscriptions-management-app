package rga.users.subscriptions.management.app.mappers.impl;

import org.springframework.stereotype.Component;
import rga.users.subscriptions.management.app.consts.Consts;
import rga.users.subscriptions.management.app.dtos.AddUserDto;
import rga.users.subscriptions.management.app.dtos.UserAuthRequestDto;
import rga.users.subscriptions.management.app.dtos.UserDto;
import rga.users.subscriptions.management.app.entities.User;
import rga.users.subscriptions.management.app.enums.Role;
import rga.users.subscriptions.management.app.mappers.AddUser;
import rga.users.subscriptions.management.app.mappers.Mapper;
import rga.users.subscriptions.management.app.utils.PasswordGenerator;

@Component
public class UserMapper implements Mapper<User, UserDto, UserAuthRequestDto>, AddUser<User, AddUserDto> {

    @Override
    public UserDto toDto(User user) {
        var dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setRole(user.getRole());
        return dto;
    }

    @Override
    public User toEntity(UserAuthRequestDto dto){
        var user = new User();
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setRole(Role.ROLE_USER);
        return user;
    }

    @Override
    public User toUserEntity(AddUserDto addUserDto) {
        var user = new User();
        user.setEmail(addUserDto.getEmail());
        user.setPassword(PasswordGenerator.generate(Consts.PASSWORD_MIN_LENGTH));
        user.setRole(addUserDto.getRole());
        return user;
    }
}
