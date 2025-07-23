引入依赖后默认开启health端点

访问http://127.0.0.1:8080/actuator/health响应下面内容
```json
{
    "status": "UP"
}
```

加入下面配置可以显示更详细内容
```yaml
management:
  endpoint:
    health:
      # 只显示组件状态信息
      show-components: ALWAYS
      # 除了显示组件状态信息，还会显示更详细的组件信息
      show-details: ALWAYS
```

自定义健康信息：继承NacosHealthIndicator实现doHealthCheck方法即可，如下
```java
// NacosHealthIndicator类名中的nacos将作为健康指标名
@Component
public class NacosHealthIndicator extends AbstractHealthIndicator {

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        // 这里模拟Nacos服务状态
        if (new Random().nextInt(100) > 1) {
            builder.up().withDetail("ext1", "extInfo");
        } else {
            builder.down().withDetail("ext2","extInfo");
        }
    }

}
```

访问健康端点返回
```json
{
    "status": "DOWN",
    "components": {
        "diskSpace": {
            "status": "UP",
            "details": {
                "total": 213792059392,
                "free": 95601954816,
                "threshold": 10485760,
                "path": "C:\\Users\\Administrator\\IdeaProjects\\spring-demo\\.",
                "exists": true
            }
        },
        "nacos": {
            "status": "DOWN"
        },
        "ping": {
            "status": "UP"
        },
        "ssl": {
            "status": "UP",
            "details": {
                "validChains": [],
                "invalidChains": []
            }
        }
    }
}
```

# 配置k8s健康探针

修改application.yml，加入下面配置，可实现健康端点返回DOWN时，k8s自动重启

```yaml
management:
  endpoint:
    health:
      probes:
        enabled: true
  health:
    livenessstate:
      enabled: true
    readinessstate:
      enabled: true
```