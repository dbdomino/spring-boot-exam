package com.minod.aop.aopsample;

import com.minod.aop.aopsample.aspect.Retry;
import com.minod.aop.aopsample.aspect.Trace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SampleService {
    private final SampleRepository sampleRepository;


    @Trace   // 커스텀 어노테이션, AOP 프록시 만들어 쓰려고
    public void request(String itemId) {
        // 출력이 어떻게 제대로 될지 알 수 없다.
        // AOP로 로그 찍어보자.
        sampleRepository.save(itemId);
    }

    @Retry(value=6)
    public void requestRetry(String itemId) {
        // 출력이 어떻게 제대로 될지 알 수 없다.
        // AOP로 로그 찍어보자.
        sampleRepository.save(itemId);
    }
}
