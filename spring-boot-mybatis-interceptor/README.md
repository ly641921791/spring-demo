Mybatis拦截器使用场景
- 慢查询监控
- 数据脱敏
- 权限控制
- SQL审计

可拦截组件和方法

| 组件               | 方法                                       | 作用      |
|------------------|------------------------------------------|---------|
| StatementHandler | prepare,parameterize,batch,update,query  | 执行SQL前  |
| ParameterHandler | getParameterObject,setParameters         | 处理参数    |
| Executor         | update,query,select,insert,delete        | 执行SQL   |
| ResultSetHandler | handleResultSets,handleOutputParameters  | 处理结果集   |

创建自定义拦截器，通过Intercepts注解指定拦截的组件、方法、参数类型，参数类型用于确定拦截的重载方法
实现Interceptor接口，重写intercept方法，实现拦截逻辑，具体见项目中[SlowSqlInterceptor.java](src/main/java/org/example/sql/interceptor/SlowSqlInterceptor.java)
将自定义拦截器注册到Mybatis中，具体见项目中[MybatisConfig.java](src/main/java/org/example/config/MybatisConfig.java)