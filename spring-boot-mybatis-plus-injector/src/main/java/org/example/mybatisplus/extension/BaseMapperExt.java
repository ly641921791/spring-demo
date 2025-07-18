package org.example.mybatisplus.extension;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface BaseMapperExt<T> extends BaseMapper<T> {

    void truncateTable();

}
