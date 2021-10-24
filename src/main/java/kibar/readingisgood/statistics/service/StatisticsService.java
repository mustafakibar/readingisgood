package kibar.readingisgood.statistics.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

import kibar.readingisgood.order.data.model.Order;
import kibar.readingisgood.order.data.payload.ListOrdersByCustomerIdRequest;
import kibar.readingisgood.order.service.OrderService;
import kibar.readingisgood.statistics.data.model.CustomerOrderSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final OrderService orderService;

    public Flux<CustomerOrderSummary> getCustomerOrderSummary(String customerId) {
        return orderService.getAllByCustomerId(new ListOrdersByCustomerIdRequest(customerId, PageRequest.ofSize(Integer.MAX_VALUE)))
                .groupBy(order -> order.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).getMonth())
                .flatMap(group -> Mono.just(group.key()).zipWith(group.collectList()))
                .map(o -> CustomerOrderSummary.builder()
                        .month(o.getT1().toString())
                        .totalOrderCount(((long) o.getT2().size()))
                        .totalBookCount(o.getT2().stream().map(Order::getAmount).reduce(Long::sum).orElse(0L))
                        .build());
    }

}
