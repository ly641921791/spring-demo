package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.entity.User;
import org.example.mybatisplus.extension.BaseMapperExt;

@Mapper
public interface UserMapper extends BaseMapperExt<User> {
}
