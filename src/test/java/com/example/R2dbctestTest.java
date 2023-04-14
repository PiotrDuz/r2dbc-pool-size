package com.example;

import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import jakarta.inject.Inject;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.subscriber.TestSubscriberBuilder;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;



@MicronautTest(transactional = false)
class R2dbctestTest {
    Logger log = LoggerFactory.getLogger(R2dbctestTest.class);

    @Inject
    EmbeddedApplication<?> application;

    @Inject
    ItemsRepository itemsRepository;

    @Test
    void testItWorksFLux() throws InterruptedException {
        Long milli = Instant.now().toEpochMilli();
        Flux itemFlux = Flux.just(
                        Flux.from(itemsRepository.findAll()).doOnComplete(() -> log.info("all loaded1")),
                        Flux.from(itemsRepository.findAll()).doOnComplete(() -> log.info("all loaded2")))
                .flatMap(pub -> pub.subscribeOn(reactor.core.scheduler.Schedulers.parallel()))
                .doOnError(err -> log.error("err", err))
                .count()
                .flux()
                .doOnNext(number -> log.info("num:" + number));
       itemFlux.blockLast();

        System.out.println(Instant.now().toEpochMilli() - milli + "");
    }

}
