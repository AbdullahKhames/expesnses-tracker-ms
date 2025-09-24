package live.tikgik.expenses.account.contrtoller;

import live.tikgik.expenses.account.dto.request.BudgetReqDto;
import live.tikgik.expenses.account.dto.request.BudgetUpdateDto;
import live.tikgik.expenses.account.manager.BudgetManager;
import live.tikgik.expenses.shared.dto.ApiResponse;
import live.tikgik.expenses.shared.error.exception_handler.ResponseExceptionBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/budgets")
@Slf4j
@RequiredArgsConstructor
@RestController
public class BudgetController {
    private final BudgetManager manager;
    private final ResponseExceptionBuilder responseExceptionBuilder;

    @PostMapping
    public ResponseEntity<ApiResponse> createBudget(@RequestBody BudgetReqDto request) {
        try {
            return ResponseEntity.ok(manager.create(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseExceptionBuilder.buildResponse(e));
        }
    }

    @GetMapping("/refNo/{refNo}")
    public ResponseEntity<ApiResponse> getBudget(@PathVariable("refNo") String refNo) {
        ApiResponse responseDto = manager.get(refNo);
        if (responseDto != null) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse> getBudgetByName(@PathVariable("name") String name) {
        ApiResponse responseDto = manager.getBudgetByName(name);
        if (responseDto != null) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{refNo}")
    public ResponseEntity<ApiResponse> updateBudget(@PathVariable("refNo") String refNo, @RequestBody BudgetUpdateDto request) {
        try {
            return ResponseEntity.ok(manager.update(refNo, request));
        } catch (Exception e) {
            // Handle the deserialization error here...
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseExceptionBuilder.buildResponse(e));
        }
    }

    @DeleteMapping("/{refNo}")
    public ResponseEntity<ApiResponse> deleteBudget(@PathVariable("refNo") String refNo) {
        ApiResponse responseDto = manager.delete(refNo);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllEntities(Pageable pageable) {
        ApiResponse responseDto = manager.getAllEntities(pageable);
        return ResponseEntity.ok(responseDto);

    }

    @GetMapping("/noAccount")
    public ResponseEntity<ApiResponse> getAllEntitiesWithoutAccount(Pageable pageable) {
        ApiResponse responseDto = manager.getAllEntitiesWithoutAccount(pageable);
        return ResponseEntity.ok(responseDto);

    }
}
