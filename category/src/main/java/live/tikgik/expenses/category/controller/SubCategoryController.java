package live.tikgik.expenses.category.controller;


import live.tikgik.expenses.category.dtos.request.SubCategoryReqDto;
import live.tikgik.expenses.category.dtos.request.SubCategoryUpdateDto;
import live.tikgik.expenses.category.manager.SubCategoryManager;
import live.tikgik.expenses.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/sub-categories")
@Slf4j
@RequiredArgsConstructor()
@RestController
public class SubCategoryController {
    private final SubCategoryManager manager;
    @PostMapping
    public ResponseEntity<ApiResponse> createSubCategory(@RequestBody SubCategoryReqDto expense){
        ApiResponse responseDto = manager.create(expense);
        if (responseDto != null) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/refNo/{refNo}")
    public ResponseEntity<ApiResponse> getSubCategory(@PathVariable("refNo") String refNo) {
        ApiResponse responseDto = manager.get(refNo);
        if (responseDto != null) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse> getSubCategoryByName(@PathVariable("name") String name) {
        ApiResponse responseDto = manager.getSubCategoryByName(name);
        if (responseDto != null) {
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

//    @GetMapping("/{refNo}/expenses")
//    public ResponseEntity<ApiResponse> getSubCategoryExpenses(@PathVariable("refNo") String refNo) {
//        ApiResponse responseDto = manager.getSubCategoryExpenses(refNo);
//        if (responseDto != null) {
//            return ResponseEntity.ok(responseDto);
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    }

    @PutMapping("/{refNo}")
    public ResponseEntity<ApiResponse> updateSubCategory(@PathVariable("refNo") String refNo, @RequestBody SubCategoryUpdateDto expenseUpdateDto) {
        ApiResponse responseDto = manager.update(refNo, expenseUpdateDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{refNo}")
    public ResponseEntity<ApiResponse> deleteSubCategory(@PathVariable("refNo") String refNo) {
        ApiResponse responseDto = manager.delete(refNo);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllEntities(
            Pageable pageable) {
        ApiResponse responseDto = manager.getAllEntities(pageable);
        return ResponseEntity.ok(responseDto);

    }
    @GetMapping("/noCategory")
    public ResponseEntity<ApiResponse> getAllEntitiesWithoutCategory(
            Pageable pageable) {
        ApiResponse responseDto = manager.getAllEntitiesWithoutCategory(pageable);
        return ResponseEntity.ok(responseDto);

    }

    @GetMapping("/categoryName/{refNo}")
    public ResponseEntity<ApiResponse> getSubcategories(@PathVariable("refNo") String refNo,
                                                        Pageable pageable) {

        ApiResponse responseDto = manager
                .getCategorySubcategories(refNo,
                        pageable);
        return ResponseEntity.ok(responseDto);
    }

}
