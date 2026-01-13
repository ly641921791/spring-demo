package org.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

@Slf4j
@Service
public class WeatherService {

    @Tool(description = "获取指定城市的当前天气情况，返回格式化后的天气报告")
    public String getWeather(@ToolParam(description = "城市名称必须是英文，例如：London 或者 Beijing") String city) throws UnsupportedEncodingException {
        log.info("====调用了查询天气=====");
        String weatherDescription = "无描述";
        Random random = new Random();
        double temperature = random.nextDouble();
        double feelsLike = random.nextDouble();
        double tempMin = random.nextDouble();
        double tempMax = random.nextDouble();
        int pressure = random.nextInt();
        int humidity = random.nextInt();
        double windSpeed = random.nextDouble();
        return String.format("""
                            城市: %s 
                            天气描述: %s 
                            当前温度: %.1f°C 
                            体感温度: %.1f°C 
                            最低温度: %.1f°C 
                            最高温度: %.1f°C 
                            气压: %d hPa 
                            湿度: %d%% 
                            风速: %.1f m/s 
                            """, city,
                weatherDescription,
                temperature,
                feelsLike,
                tempMin,
                tempMax,
                pressure,
                humidity,
                windSpeed);
    }

}
