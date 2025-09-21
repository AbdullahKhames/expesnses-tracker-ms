package live.tikgik.expenses.account.contrtoller;

import live.tikgik.expenses.account.dto.request.BudgetReqDto;
import live.tikgik.expenses.account.dto.request.BudgetUpdateDto;
import live.tikgik.expenses.account.service.BudgetService;
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
    private final BudgetService budgetService;
    private final ResponseExceptionBuilder responseExceptionBuilder;

    @PostMapping
    public ResponseEntity<ApiResponse> createBudget(@RequestBody BudgetReqDto request){
        try {
            return ResponseEntity.ok(budgetService.create(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(responseExceptionBuilder.buildResponse(e));
        }
    }
    @GetMapping("/refNo/{refNo}")
    public ResponseEntity<ApiResponse> getBudget(@PathVariable("refNo") String refNo) {
        ApiResponse responseDto = budgetService.get(refNo);
        if (responseDto != null) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse> getBudgetByName(@PathVariable("name") String name) {
        ApiResponse responseDto = budgetService.getBudgetByName(name);
        if (responseDto != null) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{refNo}")
    public ResponseEntity<ApiResponse> updateBudget(@PathVariable("refNo") String refNo, @RequestBody  BudgetUpdateDto request) {
        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            BudgetUpdateDto request = objectMapper.readValue(rawRequestBody, BudgetUpdateDto.class);
            return ResponseEntity.ok(budgetService.update(refNo, request));
        } catch (Exception e) {
            // Handle the deserialization error here...
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(responseExceptionBuilder.buildResponse(e));
        }
    }

    @DeleteMapping("/{refNo}")
    public ResponseEntity<ApiResponse> deleteBudget(@PathVariable("refNo") String refNo) {
        ApiResponse responseDto = budgetService.delete(refNo);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllEntities(
            /*@DefaultValue("1") @QueryParam("page") Long pageNumber,
            @DefaultValue("10") @QueryParam("per_page") Long pageSize,
            @DefaultValue("id") @QueryParam("sortBy") String sortBy,
            @QueryParam("sortDirection") String direction,*/
            Pageable pageable) {
//        SortDirection sortDirection = PageUtil.getSortDirection(direction);
        ApiResponse responseDto = budgetService.getAllEntities(pageable);
        return ResponseEntity.ok(responseDto);

    }
    @GetMapping("/noAccount")
    public ResponseEntity<ApiResponse> getAllEntitiesWithoutAccount(
/*            @DefaultValue("1") @QueryParam("page") Long pageNumber,
            @DefaultValue("10") @QueryParam("per_page") Long pageSize,
            @DefaultValue("id") @QueryParam("sortBy") String sortBy,
            @QueryParam("sortDirection") String direction,*/
            Pageable pageable) {
//        SortDirection sortDirection = PageUtil.getSortDirection(direction);
        ApiResponse responseDto = budgetService.getAllEntitiesWithoutAccount(pageable);
        return ResponseEntity.ok(responseDto);

    }
}
