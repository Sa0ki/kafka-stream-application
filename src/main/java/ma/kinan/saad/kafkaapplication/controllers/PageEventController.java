package ma.kinan.saad.kafkaapplication.controllers;

import lombok.AllArgsConstructor;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.state.*;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ma.kinan.saad.kafkaapplication.models.PageEvent;
import reactor.core.publisher.Flux;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Eren
 **/
@RestController
@AllArgsConstructor
public class PageEventController {
    private StreamBridge streamBridge;
    private InteractiveQueryService interactiveQueryService;
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
    @GetMapping(value = "/analytics",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String,Long>> analytics(){
        return Flux.interval(Duration.ofSeconds(1))
                .map(seq->{
                    Map<String,Long> map=new HashMap<>();
                    ReadOnlyKeyValueStore<String, Long> stats = interactiveQueryService.getQueryableStore("count-store", QueryableStoreTypes.keyValueStore());
                    Instant now=Instant.now();
                    Instant from=now.minusSeconds(5);
                    KeyValueIterator<String, Long> keyValueIterator = stats.all();
                    while (keyValueIterator.hasNext()){
                        KeyValue<String, Long> next = keyValueIterator.next();
                        map.put(next.key,next.value);
                    }
                    return map;
                });
    }
}
