package rga.users.subscriptions.management.app.mappers;

import org.springframework.data.domain.Page;

@FunctionalInterface
public interface PageMapper<Entity, Dto> {

    Page<Dto> toDtosPage(Page<Entity> entities);

}
