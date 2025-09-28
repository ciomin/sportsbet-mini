package dev.sportsbetmini.betservice.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sportsbetmini.betservice.domain.OddsEntity;
import dev.sportsbetmini.betservice.repo.OddsRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class OddsEventsConsumer {

    private final ObjectMapper mapper;
    private final OddsRepository oddsRepo;

    public OddsEventsConsumer(ObjectMapper mapper, OddsRepository oddsRepo) {
        this.mapper = mapper; this.oddsRepo = oddsRepo;
    }

    @KafkaListener(topics = "odds.updates", groupId = "betservice-local")
    public void handleOddsUpdate(String message) throws Exception {
        var evt = mapper.readValue(message, OddsUpdated.class);
        var ts = (evt.updatedAt() != null) ? evt.updatedAt() : OffsetDateTime.now();

        var existing = oddsRepo.findByEventIdAndMarketAndSelection(evt.eventId(), evt.market(), evt.selection());
        if (existing.isPresent()) {
            var o = existing.get();
            o.setPriceDecimal(evt.price());
            o.setUpdatedAt(ts);
            oddsRepo.save(o); // merge/update
        } else {
            var fresh = new OddsEntity(evt.eventId(), evt.market(), evt.selection(), evt.price(), ts);
            oddsRepo.save(fresh); // insert
        }
    }
}