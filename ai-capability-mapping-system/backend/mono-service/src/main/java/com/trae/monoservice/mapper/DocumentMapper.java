package com.trae.monoservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trae.monoservice.entity.Document;

public interface DocumentMapper extends BaseMapper<Document> {
    
    Document findById(Long id);
}