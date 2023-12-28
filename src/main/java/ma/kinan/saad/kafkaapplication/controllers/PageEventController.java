package ma.kinan.saad.kafkaapplication.controllers;

import lombok.AllArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ma.kinan.saad.kafkaapplication.models.PageEvent;

import java.util.Date;
import java.util.Random;

/**
 * @author Eren
 **/
@RestController
@AllArgsConstructor
public class PageEventController {
    private StreamBridge streamBridge;
    @GetMapping("/publish/{topic}/{pageName}")
    public PageEvent publish(@PathVariable String topic, @PathVariable String pageName){
        PageEvent pageEvent = new PageEvent(
                pageName,
                Math.random() > 0.5 ? "U1": "U2",
                new Date(),
                new Random().nextInt(9000));
        this.streamBridge.send(topic, pageEvent);
        return pageEvent;
    }
}
