package org.example.batch.batch;

import lombok.RequiredArgsConstructor;
import org.example.batch.processor.deleteSubscriptionsBilledProccessor.DeleteSubscriptionsBilledProccessor;
import org.example.common.subscriptions.entity.Subscriptions;
import org.example.common.subscriptions.repository.BillingRepository;
import org.example.common.subscriptions.repository.SubscriptionsRepository;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class DeleteBilledSubscriptionBatch {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final DeleteSubscriptionsBilledProccessor deleteSubscriptionsBilledProccessor;
    private final SubscriptionsRepository subscriptionsRepository;
    private final BillingRepository billingRepository;

    // 구독 데이터를 읽고 처리 후 Billing에 저장하고 Subscriptions에서 삭제하는 단계를 정의, 청크 크기는 10으로 설정
    @Bean
    public Step firstDeleteStep() {
        return new StepBuilder("firstDeleteStep", jobRepository)
                .<Subscriptions, Subscriptions>chunk(10, platformTransactionManager)
                .reader(beforeDeleteReader()) // 구독 데이터를 읽어옴
                .processor(deleteSubscriptionsBilledProccessor) // 구독 데이터를 처리
                .writer(afterDeleteWriter()) // 처리된 구독 데이터를 Billing에 저장하고 삭제
                .build();
    }

    // 구독 데이터를 읽기 위한 설정을 정의
    @Bean
    public RepositoryItemReader<Subscriptions> beforeDeleteReader() {
        return new RepositoryItemReaderBuilder<Subscriptions>()
                .name("beforeDeleteReader") // 리더의 이름 설정
                .pageSize(10) // 한 번에 10개의 구독 데이터를 읽어옴
                .methodName("findAll") // subscriptionsRepository의 메서드 이름
                .repository(subscriptionsRepository)
                .sorts(Map.of("yield", Sort.Direction.DESC)) // 구독 데이터를 'yield' 기준으로 내림차순 정렬
                .build();
    }

    // 처리된 구독 데이터를 Billing에 저장하고 Subscriptions에서 삭제하기 위한 설정을 정의
    @Bean
    public ItemWriter<Subscriptions> afterDeleteWriter() {
        return items -> {
            for (Subscriptions subscription : items) {
                // BillingRepository에 구독 데이터 저장
                billingRepository.save(subscription);
                // SubscriptionsRepository에서 구독 데이터 삭제
                subscriptionsRepository.delete(subscription);
            }
        };
    }
}
