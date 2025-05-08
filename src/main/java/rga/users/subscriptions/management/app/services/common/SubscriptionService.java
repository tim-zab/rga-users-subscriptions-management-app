package rga.users.subscriptions.management.app.services.common;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rga.users.subscriptions.management.app.dtos.AddSubscriptionDto;
import rga.users.subscriptions.management.app.dtos.SubscriptionDto;

public interface SubscriptionService {

    SubscriptionDto add(Long userId, AddSubscriptionDto dto);

    Page<SubscriptionDto> getByUserId(Long userId, Pageable pageable);

    void deleteById(Long userId, Long subId);

}
