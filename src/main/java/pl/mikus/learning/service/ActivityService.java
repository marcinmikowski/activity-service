package pl.mikus.learning.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.mikus.learning.model.Activity;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;
import java.util.random.RandomGenerator;

@Service
@RequiredArgsConstructor
@Log4j2
public class ActivityService {

    private final RestTemplate restTemplate;

    @Cacheable(value = "activity", key = "#id")
    public CompletableFuture<Activity> getActivitySlowFromCache(long id) {
        return CompletableFuture.supplyAsync(() -> {
            waitRandomly();
            Activity activity = restTemplate.getForObject("https://www.boredapi.com/api/activity", Activity.class);
            log.info("Received async ACTIVITY id {}: {}", id, activity);
            return activity;
        });
    }

    @Cacheable(value = "ehcacheExample", key = "#id")
    public CompletableFuture<Activity> getActivityEh(long id) {
        return CompletableFuture.supplyAsync(() -> {
            waitRandomly();
            Activity activity = restTemplate.getForObject("https://www.boredapi.com/api/activity", Activity.class);
            log.info("Received async ACTIVITY for EH id {}: {}", id, activity);
            return activity;
        });
    }

    @CacheEvict(value = "activity", allEntries = true)
    public void clearCache() {
    }

    public Activity getActivitySlow() {
        waitRandomly();
        Activity activity = restTemplate.getForObject("https://www.boredapi.com/api/activity", Activity.class);
        log.info("Received activity: {}", activity);
        return activity;
    }

    private void waitRandomly() {
        if (RandomGenerator.getDefault().nextBoolean()) {
            try {
                Thread.sleep(Duration.of(10, ChronoUnit.SECONDS));
            } catch (InterruptedException e) {
                log.error("!!!! ----- Error -- {}", e.getMessage(), e);
                throw new RuntimeException("!!!! ----- Error -- " + e.getMessage(), e);
            }
        }
    }
}
