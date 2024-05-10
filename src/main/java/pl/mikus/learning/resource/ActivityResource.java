package pl.mikus.learning.resource;

import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.mikus.learning.model.Activity;
import pl.mikus.learning.service.ActivityService;

import java.util.concurrent.CompletableFuture;
import java.util.random.RandomGenerator;

@RestController
@RequiredArgsConstructor
@Log4j2
public class ActivityResource {

    private final ActivityService activityService;

    @GetMapping("/activity")
    @TimeLimiter(name = "activity", fallbackMethod = "gainFallback")
    public CompletableFuture<Activity> getActivity() {
        return CompletableFuture.supplyAsync(activityService::getActivitySlow);
    }

    @TimeLimiter(name = "activity")
    @GetMapping("/activityCached/{id}")
    public CompletableFuture<Activity> getActivityFromCache(@PathVariable long id) {
        return activityService.getActivitySlowFromCache(id);
    }

    @GetMapping("/activity/clearCache")
    @ResponseStatus(HttpStatus.OK)
    public void clearCache() {
        activityService.clearCache();
    }

    @GetMapping("activity/eh/{id}")
    public CompletableFuture<Activity> getActivityFromLocalCache(@PathVariable long id) {
        return activityService.getActivityEh(id);
    }

    public CompletableFuture<Activity> gainFallback(Throwable ex) throws Throwable {
        if (RandomGenerator.getDefault().nextBoolean()) {
            log.info("Entered for recovery path ...");
            return CompletableFuture.supplyAsync(() -> Activity
                    .builder()
                    .activity("Ooops, some bad death occured...")
                    .build());
        }
        log.info("Nobody can help you!!!");
        throw ex;
    }
}
