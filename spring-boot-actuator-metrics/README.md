引入actuator依赖，加入下面配置可以开启服务指标端点

```yaml
management:
  endpoints:
    web:
      exposure:
        include:
          - metrics
```

访问http://127.0.0.1:8080/actuator/metrics地址可以看到有哪些指标可以查看

访问http://127.0.0.1:8080/actuator/metrics/{metricsName}可以看到具体指标信息

自定义指标，只需要将目标指标定义加入Spring容器就可以使用

```java
    @Bean
    public Timer apiTimer(MeterRegistry meterRegistry) {
        return Timer.builder("api_timer")
                .description("API耗时")
                .register(meterRegistry);
    }
```

使用方法

```java
    @Autowired
    private Timer timer;

    @RequestMapping
    public String hello() {
        return timer.record(() -> "hello");
    }
```

访问http://127.0.0.1:8080/actuator/metrics/api_timer可以查看上面自定义指标信息

# 集成Prometheus

加入依赖

```xml
        <dependency>
            <groupId>io.micrometer</groupId>
            <artifactId>micrometer-registry-prometheus</artifactId>
        </dependency>
```

配置Prometheus收集指标信息后，可以通过"{api_timer}"查看自定义指标信息

# 集成Grafana

在Grafana中添加Prometheus数据源，创建新看板，添加"api_timer"指标面板