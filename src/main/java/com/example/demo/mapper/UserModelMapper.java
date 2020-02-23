package com.example.demo.mapper;

import com.example.demo.model.User;
import com.example.demo.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserModelMapper {
    UserModelMapper INSTANCE = Mappers.getMapper(UserModelMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "uid", target = "email")
    User userModelToUser(UserModel userModel);
}
