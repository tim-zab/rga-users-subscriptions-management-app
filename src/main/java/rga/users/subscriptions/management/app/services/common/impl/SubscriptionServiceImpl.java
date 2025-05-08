package rga.users.subscriptions.management.app.services.common.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rga.users.subscriptions.management.app.dtos.AddSubscriptionDto;
import rga.users.subscriptions.management.app.dtos.SubscriptionDto;
import rga.users.subscriptions.management.app.entities.Subscription;
import rga.users.subscriptions.management.app.exceptions.AccessForbiddenException;
import rga.users.subscriptions.management.app.exceptions.NotFoundException;
import rga.users.subscriptions.management.app.mappers.impl.SubscriptionMapper;
import rga.users.subscriptions.management.app.repositories.SubscriptionRepository;
import rga.users.subscriptions.management.app.services.access.UserAccessService;
import rga.users.subscriptions.management.app.services.common.SubscriptionService;
import rga.users.subscriptions.management.app.services.common.UserService;

@RequiredArgsConstructor
@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository repository;
    private final SubscriptionMapper mapper;
    private final UserService userService;
    private final UserAccessService userAccessService;

    @Override
    @Transactional
    public SubscriptionDto add(Long userId, AddSubscriptionDto dto) {
        if (userAccessService.getCurrentEmail().equals(dto.userEmail())) {
            var subscription = mapper.toEntity(dto);
            subscription.setUser(userService.findById(userId));
            return mapper.toDto(repository.save(subscription));
        }
        throw new AccessForbiddenException(HttpStatus.FORBIDDEN,
                "Access is denied. Current user does not have permission to create subscription.");
    }

    @Override
    public Page<SubscriptionDto> getByUserId(Long userId, Pageable pageable) {
        if (userAccessService.isAdmin() ||
                userAccessService.getCurrentEmail().equals(userService.findById(userId).getEmail())) {
            var user = userService.findById(userId);
            if (pageable.getSort().isUnsorted()){
                return mapper.toDtosPage(repository.findAllByUserOrderByTimestampAsc(user, pageable));
            } else {
                return mapper.toDtosPage(repository.findAllByUser(user, pageable));
            }
        } else {
            throw new AccessForbiddenException(HttpStatus.FORBIDDEN,
                    "Access denied. Current user does not have the required permissions to get subscriptions page by user id");
        }
    }

    @Override
    @Transactional
    public void deleteById(Long userId, Long subId) {
        if(
                userAccessService.getCurrentEmail().equals(userService.findById(userId).getEmail()) &&
                        userAccessService.isSubscriber(findSubscriptionById(subId))
        ){
            repository.delete(findSubscriptionById(subId));
        } else {
            throw new AccessForbiddenException(HttpStatus.FORBIDDEN,
                    "Access denied. Current user does not have the required permissions to delete subscription");
        }
    }

    private Subscription findSubscriptionById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException(HttpStatus.NOT_FOUND,
                        "Subscription with id " + id + " is not found")
        );
    }

}
