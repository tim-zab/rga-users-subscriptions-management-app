package rga.users.subscriptions.management.app.services.common.impl;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rga.users.subscriptions.management.app.consts.Consts;
import rga.users.subscriptions.management.app.dtos.AddUserDto;
import rga.users.subscriptions.management.app.dtos.UserAuthRequestDto;
import rga.users.subscriptions.management.app.dtos.UserDto;
import rga.users.subscriptions.management.app.entities.User;
import rga.users.subscriptions.management.app.enums.Role;
import rga.users.subscriptions.management.app.exceptions.AccessForbiddenException;
import rga.users.subscriptions.management.app.exceptions.InvalidDataException;
import rga.users.subscriptions.management.app.exceptions.NotFoundException;
import rga.users.subscriptions.management.app.exceptions.UserAlreadyExistentException;
import rga.users.subscriptions.management.app.mappers.impl.UserMapper;
import rga.users.subscriptions.management.app.repositories.UserRepository;
import rga.users.subscriptions.management.app.services.access.UserAccessService;
import rga.users.subscriptions.management.app.services.common.UserService;

@Slf4j
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final UserAccessService accessService;

    @Override
    @Transactional
    public UserDto createUser(AddUserDto dto) throws HttpMessageNotReadableException {
        if (accessService.isAdmin()) {
            if(existsByEmail(dto.getEmail())) {
                throw new UserAlreadyExistentException(HttpStatus.BAD_REQUEST,
                        "User with e-mail " + dto.getEmail() + " is already existent in the system.");
            } else {
                if(!validateEmail(dto.getEmail())) {
                    throw new InvalidDataException(HttpStatus.BAD_REQUEST,
                            "Invalid e-mail " + dto.getEmail() + " has been provided");
                } else {
                    var user = repository.save(mapper.toUserEntity(dto));
                    return mapper.toDto(user);
                }
            }
        } else {
            throw new AccessForbiddenException(HttpStatus.FORBIDDEN,
                    "Access denied. User does not have the required permissions to create new user");
        }
    }

    @Override
    public User findById(@NotNull Long id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException(HttpStatus.NOT_FOUND, "User with id " + id + " is not found")
        );
    }

    @Override
    public UserDto getUserById(Long id) {
        if (accessService.isAdmin()) {
            var user = findById(id);
            return mapper.toDto(user);
        } else {
            throw new AccessForbiddenException(HttpStatus.FORBIDDEN,
                    "Access denied. User does not have the required permissions to get user by id");
        }
    }

    @Override
    @Transactional
    public void updateUserRole(Long id, Role role) throws HttpMessageNotReadableException {
        if (accessService.isAdmin()) {
            var user = findById(id);
            user.setRole(role);
            repository.save(user);
            log.info("Role has been updated successfully.");
        } else {
            throw new AccessForbiddenException(HttpStatus.FORBIDDEN,
                    "Access denied. Current user does not have the required permissions to update user role.");
        }
    }

    @Override
    @Transactional
    public void updateUserPassword(Long id, String password) {
        if (accessService.getCurrentEmail().equals(findById(id).getEmail())){
            var user = findById(id);
            if(validatePassword(password)){
                user.setPassword(password);
                repository.save(user);
                log.info("Password has been updated successfully.");
            } else {
                log.info("Invalid data to update password. Please try to use another password.");
                throw new InvalidDataException(HttpStatus.BAD_REQUEST,
                        "Invalid password " + password + " has been provided");
            }
        } else {
            throw new AccessForbiddenException(HttpStatus.FORBIDDEN,
                    "Access denied. Current user does not have the required permissions to update user password.");
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if(!accessService.isAdmin()){
            throw new AccessForbiddenException(HttpStatus.FORBIDDEN,
                    "Access denied. User does not have the required permissions to delete user");
        }
        repository.delete(findById(id));
    }

    @Override
    public User getByEmail(@NotNull String email) {
        return repository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(HttpStatus.NOT_FOUND, "User with e-mail " + email + " is not found")
        );
    }

    @Override
    @Transactional
    public User create(UserAuthRequestDto dto) {
        return repository.save(
                mapper.toEntity(dto)
        );
    }

    @Override
    public boolean validateEmail(@NotNull String email) {
        return email.matches(Consts.EMAIL_PATTERN);
    }

    @Override
    public boolean validatePassword(@NotNull String password) {
        return password.length() >= Consts.PASSWORD_MIN_LENGTH;
    }

    @Override
    public boolean existsByEmail(@NotNull String email) {
            return repository.existsByEmail(email);
    }

}
