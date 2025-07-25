package org.example.mybatisplus.extension;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.session.Configuration;
import org.example.mybatisplus.extension.injector.methods.TruncateTable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SqlInjectorExt extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Configuration configuration, Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(configuration, mapperClass, tableInfo);
        methodList.add(new TruncateTable());
        return methodList;
    }

}
