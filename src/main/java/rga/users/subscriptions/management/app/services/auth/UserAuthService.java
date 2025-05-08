package rga.users.subscriptions.management.app.services.auth;

import rga.users.subscriptions.management.app.dtos.UserAuthRequestDto;
import rga.users.subscriptions.management.app.dtos.UserAuthResponseDto;

public interface UserAuthService {

    UserAuthResponseDto authenticate(UserAuthRequestDto userAuthRequestDto);

    UserAuthResponseDto register(UserAuthRequestDto userAuthRequestDto);

}
