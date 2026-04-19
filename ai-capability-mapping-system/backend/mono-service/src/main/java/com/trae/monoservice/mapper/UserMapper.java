package com.trae.monoservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trae.monoservice.entity.User;

public interface UserMapper extends BaseMapper<User> {
    
    User findByUsername(String username);
    
    User findById(Long id);
}