package rga.users.subscriptions.management.app.mappers;

@FunctionalInterface
public interface AddUser<Entity, AnotherDto> {

    Entity toUserEntity(AnotherDto anotherDto);

}
