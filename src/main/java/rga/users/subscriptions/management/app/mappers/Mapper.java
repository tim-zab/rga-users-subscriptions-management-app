package rga.users.subscriptions.management.app.mappers;

public interface Mapper<Entity, Dto, AnotherDto> {

    Dto toDto (Entity entity);

    Entity toEntity (AnotherDto dto);

}
