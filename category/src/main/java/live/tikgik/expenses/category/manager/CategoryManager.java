package live.tikgik.expenses.category.manager;

import live.tikgik.expenses.category.dtos.request.CategoryReqDto;
import live.tikgik.expenses.category.dtos.request.CategoryUpdateDto;
import live.tikgik.expenses.category.dtos.response.CategoryRespDto;
import live.tikgik.expenses.category.mappers.CategoryMapper;
import live.tikgik.expenses.category.entity.Category;
import live.tikgik.expenses.category.service.CategoryService;
import live.tikgik.expenses.shared.dto.ApiResponse;
import live.tikgik.expenses.shared.enums.Models;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryManager {
    private final CategoryService service;
    private final CategoryMapper categoryMapper;

    public ApiResponse create(CategoryReqDto category) {
        Category savedCategory = service.create(category);
        log.info("created category {}", savedCategory);
        return ApiResponse.getCreateResponse(Models.CATEGORY.name(), savedCategory.getRefNo(), categoryMapper.entityToRespDto(savedCategory));
    }

    public ApiResponse get(String refNo) {
        return ApiResponse.getFetchResponse(Models.CATEGORY, refNo, categoryMapper.entityToRespDto(service.get(refNo)));
    }

    public ApiResponse update(String refNo, CategoryUpdateDto categoryUpdateDto) {
        return ApiResponse.getUpdateResponse(Models.CATEGORY, refNo, categoryMapper.entityToRespDto(service.update(refNo, categoryUpdateDto)));
    }

    public ApiResponse delete(String refNo) {
        service.delete(refNo);
        return ApiResponse.getDeleteResponse(Models.CATEGORY, "success");
    }

    public ApiResponse addAssociation(String categoryRefNo, String subCategoryRefNo) {
        if (service.addAssociation(categoryRefNo, subCategoryRefNo)) {
            return ApiResponse.success("association added successfully");
        }
        return ApiResponse.failed(List.of("association couldn't be added"));
    }

    public ApiResponse removeAssociation(String categoryRefNo, String subCategoryRefNo) {
        if (service.removeAssociation(categoryRefNo, subCategoryRefNo)) {
            return ApiResponse.success("association removed successfully");
        }
        return ApiResponse.failed(List.of("association couldn't be removed"));
    }

    public ApiResponse getCategoryByName(String name) {
        if (name == null || name.isBlank()) {
            return ApiResponse.getErrorResponse(804, "name cannot be null");
        }
        List<Category> categories = service.getCategoryByName(name);
        if (!categories.isEmpty()) {
            return ApiResponse.getFetchAllResponse(Models.CATEGORY, categories.stream().map(categoryMapper::entityToRespDto).toList());
        } else {
            return ApiResponse.getErrorResponse(804, "not found");
        }
    }

    public ApiResponse getAllEntities(Pageable pageable) {
        Page<Category> categoryPage = service.getAllEntities(pageable);
        Page<CategoryRespDto> categoryDtos = categoryPage.map(categoryMapper::entityToRespDto);
        return ApiResponse.getFetchAllResponse(Models.CATEGORY, categoryDtos);
    }


}
