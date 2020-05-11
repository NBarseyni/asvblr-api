package com.pa.asvblrapi.mapper;

import com.pa.asvblrapi.dto.PlayerDto;
import com.pa.asvblrapi.entity.Player;
import com.pa.asvblrapi.repository.JerseyRepository;
import com.pa.asvblrapi.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = { UserRepository.class, JerseyRepository.class })
public interface PlayerMapper extends EntityMapper<PlayerDto, Player> {
    PlayerMapper instance = Mappers.getMapper(PlayerMapper.class);

    @Mapping(source = "idUser", target = "user")
    @Mapping(source = "idJersey", target = "jersey")
    @Mapping(target = "subscriptions", ignore = true)
    Player toEntity(PlayerDto playerDto);

    @Mapping(source = "user.id", target = "idUser")
    @Mapping(source = "jersey.id", target = "idJersey")
    @Mapping(source = "topSize.id", target = "idTopSize")
    @Mapping(source = "pantsSize.id", target = "idPantsSize")
    PlayerDto toDto(Player player);
}
