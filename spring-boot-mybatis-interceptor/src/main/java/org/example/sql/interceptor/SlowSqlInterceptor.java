package org.example.sql.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;

import java.sql.Statement;

/**
 * 慢查询拦截器
 */
@Slf4j
@Intercepts({
        // 拦截查询方法
        @Signature(
                type = StatementHandler.class,
                method = "query",
                args = {Statement.class, ResultHandler.class}
        )
})
public class SlowSqlInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long startTime = System.currentTimeMillis();

        try {
            return invocation.proceed();
        } finally {
            long costTime = System.currentTimeMillis() - startTime;

            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
            // 未绑定参数的SQL
            String sql = statementHandler.getBoundSql().getSql();
            Object parameterObject = statementHandler.getBoundSql().getParameterObject();
            if (costTime > 0) {
                log.warn("【慢查询警告】执行时间：{}ms,SQL:{},参数:{}", costTime, sql, parameterObject);
            } else {
                log.info("【SQL监控】执行时间：{},SQL:{}", costTime, sql);
            }
        }
    }

}
