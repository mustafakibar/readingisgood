package kibar.readingisgood.statistics.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.time.ZoneId;
import java.util.Comparator;

import kibar.readingisgood.order.data.model.Order;
import kibar.readingisgood.order.data.payload.ListOrderByCustomerIdRequest;
import kibar.readingisgood.order.service.OrderService;
import kibar.readingisgood.statistics.data.model.CustomerOrderSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;

@Log4j2
@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final OrderService orderService;

    public Flux<Pair<Month, CustomerOrderSummary>> getCustomerOrderSummary(String customerId) {
        return orderService.getAllByCustomerId(new ListOrderByCustomerIdRequest(customerId, PageRequest.ofSize(Integer.MAX_VALUE)))
                .flatMap(orders -> Flux.fromStream(orders.stream()))
                .groupBy(order -> order.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).getMonth())
                .sort(Comparator.comparing(GroupedFlux::key))
                .flatMap(monthOrderGroupedFlux -> monthOrderGroupedFlux.map(Order::getAmount)
                        .reduce(Long::sum)
                        .map(totalBookAmount -> Pair.of(monthOrderGroupedFlux.key(), CustomerOrderSummary.builder()
                                .totalBookCount(totalBookAmount)
                                .totalOrderCount(monthOrderGroupedFlux.count().block())
                                .build())));
    }

}
