package org.example.mybatisplus.extension.injector.methods;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

public class TruncateTable extends AbstractMethod {

    public TruncateTable() {
        super("truncateTable");
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql = "TRUNCATE TABLE " + tableInfo.getTableName();
        SqlSource sqlSource = super.createSqlSource(configuration, sql, modelClass);
        return addUpdateMappedStatement(mapperClass, modelClass, methodName, sqlSource);
    }

}
