package kibar.readingisgood.statistics.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kibar.readingisgood.statistics.data.model.CustomerOrderSummary;
import kibar.readingisgood.statistics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@CrossOrigin(origins = {"*"})
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping(path = "/order-summary", params = "customerId")
    public ResponseEntity<Flux<CustomerOrderSummary>> getAllByCustomerId(@RequestParam("customerId") String customerId) {
        return ResponseEntity.ok(statisticsService.getCustomerOrderSummary(customerId));
    }

}
