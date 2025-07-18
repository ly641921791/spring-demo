Mybatis-Plus集成BaseMapper可以得到一些内置的sql实现，如果内置sql实现达不到我们的要求，
Mybatis-Plus提供了拓展方案

首先创建自定义SQL注入实现，继承AbstractMethod，声明SQL生成规则和mapper方法名，参考[TruncateTable.java](src/main/java/org/example/mybatisplus/extension/injector/methods/TruncateTable.java)

然后将自定义SQL方法注册到MybatisPlus配置中，参考[SqlInjectorExt.java](src/main/java/org/example/mybatisplus/extension/SqlInjectorExt.java)
只要将这个类注入Spring容器，就可以替换MybatisPlus配置

在Mapper中添加上面定义的SQL方法名，或创建父类，供所有Mapper集成，参考[BaseMapperExt.java](src/main/java/org/example/mybatisplus/extension/BaseMapperExt.java)

这样就可以在项目中使用自定义的SQL方法了

另外MybatisPlus提供了默认的实现，参考包com.baomidou.mybatisplus.extension.injector.methods内实现