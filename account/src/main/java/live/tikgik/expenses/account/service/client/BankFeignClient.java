//package live.tikgik.expenses.account.service.client;
//
//import live.tikgik.bank.account.dto.response.BankDto;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestHeader;
//
//@FeignClient(name = "bank", fallback = BanksFallback.class)
//public interface BankFeignClient {
//
//    @GetMapping("/api/v1/banks/{mobileNumber}")
//    ResponseEntity<BankDto> fetchLoanDetails(
//            @RequestHeader("tikGik-correlation-id") String correlationId,
//            @PathVariable("mobileNumber") String mobileNumber);
//
//}
