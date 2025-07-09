package com.example.qbankapi.dummy;

import com.example.qbankapi.service.AppInitService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

@Configuration
@RequiredArgsConstructor
public class DummyTestRunner implements ApplicationListener<ContextRefreshedEvent> {

    private final AppInitService appInitService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        appInitService.run();
    }

}
