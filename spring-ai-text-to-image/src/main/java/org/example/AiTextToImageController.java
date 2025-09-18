package org.example;

import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/ai")
public class AiTextToImageController {

    @Autowired
    ImageModel imageModel;

    @GetMapping("/tti")
    public void tti(@RequestParam("message") String message) {
        ImageResponse response = imageModel.call(
                new ImagePrompt(message));
        System.out.println(response);
    }

}
