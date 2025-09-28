package dev.sportsbetmini.betservice.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sportsbetmini.betservice.events.OddsUpdated;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/odds")
public class DevOddsPublisherController {

    private final KafkaTemplate<String,String> kafka;
    private final ObjectMapper mapper;

    public DevOddsPublisherController(KafkaTemplate<String,String> kafka, ObjectMapper mapper) {
        this.kafka = kafka; this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<String> publish(@RequestBody OddsUpdated body) throws Exception {
        var json = mapper.writeValueAsString(body);
        kafka.send("odds.updates", body.eventId().toString(), json);
        return ResponseEntity.ok("published");
    }
}