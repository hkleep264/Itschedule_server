package com.itschedule.itschedule_server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StartupManager implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("itschedule Server Start");
    }
}
