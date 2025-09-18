package live.tikgik.expenses.account.contrtoller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import live.tikgik.expenses.account.aop.ToLog;
import live.tikgik.expenses.account.dto.AccountsContactInfoDto;
import live.tikgik.expenses.account.dto.request.AccountReqDto;
import live.tikgik.expenses.account.dto.request.AccountUpdateDto;
import live.tikgik.expenses.account.service.AccountService;
import live.tikgik.expenses.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/account")
@RequiredArgsConstructor
@Slf4j
@RefreshScope  // Add this
public class AccountController {
    private final AccountService accountService;
    private final AccountsContactInfoDto accountsContactInfoDto;
    @Value("${build.version}")
    private String buildVersion;

    @PostMapping
    @ToLog
    public ResponseEntity<ApiResponse> createAccount(@Valid @RequestBody AccountReqDto account) {
        ApiResponse responseDto = accountService.create(account);
        if (responseDto != null) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/refNo/{refNo}")
    public ResponseEntity<ApiResponse> getAccount(@PathVariable("refNo") String refNo) {
        ApiResponse responseDto = accountService.get(refNo);
        if (responseDto != null) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse> getAccountByName(@PathVariable("name") String name) {
        ApiResponse responseDto = accountService.getAccountByName(name);
        if (responseDto != null) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{refNo}/budgets")
    public ResponseEntity<ApiResponse> getAccountBudgets(@PathVariable("refNo") String refNo) {
        ApiResponse responseDto = accountService.getAccountBudgets(refNo);
        if (responseDto != null) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{refNo}")
    public ResponseEntity<ApiResponse> updateAccount(@PathVariable("refNo") String refNo, @Valid @RequestBody AccountUpdateDto accountUpdateDto) {
        ApiResponse responseDto = accountService.update(refNo, accountUpdateDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{refNo}")
    public ResponseEntity<ApiResponse> deleteAccount(@PathVariable("refNo") String refNo) {
        ApiResponse responseDto = accountService.delete(refNo);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllEntities(
            /*@DefaultValue("1") @QueryParam("page") Long pageNumber,
            @DefaultValue("10") @QueryParam("per_page") Long pageSize,
            @DefaultValue("id") @QueryParam("sortBy") String sortBy,
            @QueryParam("sortDirection") String direction*/
            Pageable pageable) {
        ApiResponse responseDto = accountService.getAllEntities(pageable);
        return ResponseEntity.ok(responseDto);

    }

    @PutMapping("/addAssociation/{accountRefNo}/{budgetRefNo}")
    public ResponseEntity<ApiResponse> addAssociation(@PathVariable("accountRefNo") String accountRefNo, @PathVariable("budgetRefNo") String budgetRefNo) {
        ApiResponse responseDto = accountService.addAssociation(accountRefNo, budgetRefNo);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/removeAssociation/{accountRefNo}/{budgetRefNo}")
    public ResponseEntity<ApiResponse> removeAssociation(@PathVariable("accountRefNo") String accountRefNo, @PathVariable("budgetRefNo") String budgetRefNo) {
        ApiResponse responseDto = accountService.removeAssociation(accountRefNo, budgetRefNo);
        return ResponseEntity.ok(responseDto);
    }

    @Retry(name = "getBuildVersion",
            fallbackMethod = "getBuildVersionFallback")
    @GetMapping("/build-version")
    @ToLog
    public String test(

    ) {
        return buildVersion;
    }

    // must be of same signature except of name and add throwable parameter
    public String getBuildVersionFallback(
            Throwable throwable
    ) {
        log.error("getBuildVersionFallback", throwable);
        return "0.9";
    }

    @RateLimiter(name = "getContactInfo", fallbackMethod = "getContactInfoFallBack")
    @GetMapping("/contact")
    @ToLog
    public AccountsContactInfoDto contact(

    ) {
        return accountsContactInfoDto;
    }

    public AccountsContactInfoDto getContactInfoFallBack(
            Throwable throwable
    ) {
        log.error("getContactInfoFallBack", throwable);
        return new AccountsContactInfoDto(
                "default Contacts",
                Map.of("name", "hamada"),
                List.of("123")
        );
    }

}
