package rga.users.subscriptions.management.app.mappers.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import rga.users.subscriptions.management.app.dtos.AddSubscriptionDto;
import rga.users.subscriptions.management.app.dtos.SubscriptionDto;
import rga.users.subscriptions.management.app.entities.Subscription;
import rga.users.subscriptions.management.app.entities.User;
import rga.users.subscriptions.management.app.mappers.Mapper;
import rga.users.subscriptions.management.app.mappers.PageMapper;
import rga.users.subscriptions.management.app.services.common.UserService;

import java.sql.Timestamp;

@Component
@AllArgsConstructor
public class SubscriptionMapper implements Mapper<Subscription, SubscriptionDto, AddSubscriptionDto>,
        PageMapper<Subscription, SubscriptionDto> {

    private UserMapper userMapper;
    private UserService userService;

    @Override
    public SubscriptionDto toDto(Subscription subscription) {
        var dto = new SubscriptionDto();
        dto.setId(subscription.getId());
        dto.setTitle(subscription.getTitle());
        dto.setDescription(subscription.getDescription());
        dto.setUser(userMapper.toDto(userService.findById(subscription.getUser().getId())));
        dto.setTimestamp(new Timestamp(System.currentTimeMillis()));
        return dto;
    }

    @Override
    public Subscription toEntity(AddSubscriptionDto dto) {
        var subscription = new Subscription();
        subscription.setTitle(dto.title());
        subscription.setDescription(dto.description());
        subscription.setUser(new User(
                dto.userEmail()
        ));
        return subscription;
    }

    @Override
    public Page<SubscriptionDto> toDtosPage(Page<Subscription> subscriptions) {
        return new PageImpl<>(subscriptions.stream().map(this::toDto).toList());
    }

}
