package live.tikgik.expenses.account.manager;

import live.tikgik.expenses.account.dto.request.BudgetReqDto;
import live.tikgik.expenses.account.dto.request.BudgetUpdateDto;
import live.tikgik.expenses.account.dto.response.BudgetRespDto;
import live.tikgik.expenses.account.entity.Budget;
import live.tikgik.expenses.account.mapper.BudgetMapper;
import live.tikgik.expenses.account.service.BudgetService;
import live.tikgik.expenses.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static live.tikgik.expenses.shared.enums.Models.BUDGET;

@Service
@Slf4j
@RequiredArgsConstructor
public class BudgetManager {
    private final BudgetService service;
    private final BudgetMapper mapper;

    public ApiResponse create(BudgetReqDto budgetReqDto) {
        Budget entity = service.create(budgetReqDto);
        return ApiResponse.getCreateResponse(BUDGET.name(), entity.getRefNo(), mapper.entityToRespDto(entity));

    }

    public void associateAccount(String accountRefNo, Budget sentBudget) {
        service.associateAccount(accountRefNo, sentBudget);
    }

    public ApiResponse get(String refNo) {
        return ApiResponse.getFetchResponse(BUDGET, refNo, mapper.entityToRespDto(service.get(refNo)));
    }

    public ApiResponse update(String refNo, BudgetUpdateDto budgetUpdateDto) {
        return ApiResponse.getUpdateResponse(BUDGET, refNo, mapper.entityToRespDto(service.update(refNo, budgetUpdateDto)));

    }

    public ApiResponse delete(String refNo) {
        service.delete(refNo);
        return ApiResponse.getDeleteResponse(BUDGET, refNo);
    }

    public ApiResponse getAllEntities(Pageable pageable) {
        Page<Budget> budgetPage = service.getAllEntities(pageable);
        Page<BudgetRespDto> budgetDtos = budgetPage.map(mapper::entityToRespDto);
        return ApiResponse.getFetchAllResponse(BUDGET, budgetDtos);
    }

    public ApiResponse getAllEntitiesWithoutAccount(Pageable pageable) {
        Page<Budget> budgetPage = service.getAllEntitiesWithoutAccount(pageable);
        Page<BudgetRespDto> budgetDtos = budgetPage.map(mapper::entityToRespDto);
        return ApiResponse.getFetchAllResponse(BUDGET, budgetDtos);
    }

    public ApiResponse getBudgetByName(String name) {
        if (name == null || name.isBlank()) {
            return ApiResponse.getErrorResponse(804, "name cannot be null");
        }
        List<Budget> budgets = service.getBudgetByName(name);
        if (!budgets.isEmpty()) {
            return ApiResponse.getFetchAllResponse(BUDGET, mapper.entityToRespDto(budgets));
        } else {
            return ApiResponse.getErrorResponse(804, "not found");
        }
    }

    public Budget update(Budget oldBudget) {
        return service.update(oldBudget);
    }


}
