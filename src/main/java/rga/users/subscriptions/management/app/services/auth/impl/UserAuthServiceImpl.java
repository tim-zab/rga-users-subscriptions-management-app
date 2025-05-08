package rga.users.subscriptions.management.app.services.auth.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rga.users.subscriptions.management.app.dtos.UserAuthRequestDto;
import rga.users.subscriptions.management.app.dtos.UserAuthResponseDto;
import rga.users.subscriptions.management.app.exceptions.InvalidDataException;
import rga.users.subscriptions.management.app.exceptions.UserAlreadyExistentException;
import rga.users.subscriptions.management.app.security.JwtProvider;
import rga.users.subscriptions.management.app.services.auth.UserAuthService;
import rga.users.subscriptions.management.app.services.common.UserService;

@AllArgsConstructor
@Service
public class UserAuthServiceImpl implements UserAuthService {

    private final JwtProvider provider;
    private final UserService service;
    private final PasswordEncoder encoder;

    @Override
    public UserAuthResponseDto authenticate(UserAuthRequestDto dto) {
        if (!service.validateEmail(dto.email())) {
            throw new InvalidDataException(HttpStatus.BAD_REQUEST,
                    "Invalid e-mail " + dto.email() + " has been provided");
        } else {
            var user = service.getByEmail(dto.email());
            if (encoder.matches(dto.password(), user.getPassword())) {
                return new UserAuthResponseDto(provider.buildAccessJwt(user));
            } else {
                throw new InvalidDataException(HttpStatus.BAD_REQUEST,
                        "Invalid password " + dto.password() + " has been provided");
            }
        }
    }

    @Override
    @Transactional
    public UserAuthResponseDto register(UserAuthRequestDto dto) {
        if(service.existsByEmail(dto.email())) {
            throw new UserAlreadyExistentException(HttpStatus.BAD_REQUEST,
                    "User with e-mail " + dto.email() + " is already existent in the system. " +
                            "Try to use another e-mail to register or just try to authenticate with this e-mail.");
        } else {
            if(!service.validateEmail(dto.email())) {
                throw new InvalidDataException(HttpStatus.BAD_REQUEST,
                        "Invalid e-mail " + dto.email() + " has been provided");
            } else if (!service.validatePassword(dto.password())) {
                throw new InvalidDataException(HttpStatus.BAD_REQUEST,
                        "Invalid password " + dto.password() + " has been provided");
            } else {
                var user = service.create(dto);
                return new UserAuthResponseDto(provider.buildAccessJwt(user));
            }
        }
    }

}
