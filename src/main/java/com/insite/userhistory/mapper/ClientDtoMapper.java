package com.insite.userhistory.mapper;

import com.insite.userhistory.dto.ClientDto;
import com.insite.userhistory.model.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientDtoMapper {

//    @Mapping(target = "name", source = "name")
//    @Mapping(target = "password", source = "password")
    ClientDto toDto(Client user);

}


