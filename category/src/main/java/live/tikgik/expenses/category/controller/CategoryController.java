package live.tikgik.expenses.category.controller;

import live.tikgik.expenses.category.dtos.request.CategoryReqDto;
import live.tikgik.expenses.category.dtos.request.CategoryUpdateDto;
import live.tikgik.expenses.category.manager.CategoryManager;
import live.tikgik.expenses.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/v1/categories")
@Slf4j
@RequiredArgsConstructor()
@RestController
public class CategoryController {
    private final CategoryManager categoryManager;

    @PostMapping
    public ResponseEntity<ApiResponse> createCategory(@RequestBody CategoryReqDto expense) {
        ApiResponse responseDto = categoryManager.create(expense);
        if (responseDto != null) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/refNo/{refNo}")
    public ResponseEntity<ApiResponse> getCategory(@PathVariable("refNo") String refNo) {
        ApiResponse responseDto = categoryManager.get(refNo);
        if (responseDto != null) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable("name") String name) {
        ApiResponse responseDto = categoryManager.getCategoryByName(name);
        if (responseDto != null) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{refNo}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable("refNo") String refNo, @RequestBody CategoryUpdateDto expenseUpdateDto) {
        ApiResponse responseDto = categoryManager.update(refNo, expenseUpdateDto);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/addAssociation/{categoryRefNo}/{subCategoryRefNo}")
    public ResponseEntity<ApiResponse> addAssociation(@PathVariable("categoryRefNo") String categoryRefNo, @PathVariable("subCategoryRefNo") String subCategoryRefNo) {
        ApiResponse responseDto = categoryManager.addAssociation(categoryRefNo, subCategoryRefNo);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/removeAssociation/{categoryRefNo}/{subCategoryRefNo}")
    public ResponseEntity<ApiResponse> removeAssociation(@PathVariable("categoryRefNo") String categoryRefNo, @PathVariable("subCategoryRefNo") String subCategoryRefNo) {
        ApiResponse responseDto = categoryManager.removeAssociation(categoryRefNo, subCategoryRefNo);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{refNo}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable("refNo") String refNo) {
        ApiResponse responseDto = categoryManager.delete(refNo);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllEntities(
            Pageable pageable
    ) {
        ApiResponse responseDto = categoryManager.getAllEntities(pageable);
        return ResponseEntity.ok(responseDto);

    }
}
