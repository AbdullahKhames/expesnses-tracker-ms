package live.tikgik.expenses.category.manager;

import live.tikgik.expenses.category.dtos.request.SubCategoryReqDto;
import live.tikgik.expenses.category.dtos.request.SubCategoryUpdateDto;
import live.tikgik.expenses.category.dtos.response.SubCategoryRespDto;
import live.tikgik.expenses.category.mappers.SubCategoryMapper;
import live.tikgik.expenses.category.entity.SubCategory;
import live.tikgik.expenses.category.service.SubService;
import live.tikgik.expenses.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static live.tikgik.expenses.shared.enums.Models.SUB_CATEGORY;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubCategoryManager {
    private final SubService service;
    private final SubCategoryMapper mapper;


    public ApiResponse create(SubCategoryReqDto subCategoryReqDto) {
        SubCategory subCategory = service.create(subCategoryReqDto);
        return ApiResponse.getCreateResponse(SUB_CATEGORY.name(), subCategory.getRefNo(), mapper.entityToRespDto(subCategory));
    }

    public ApiResponse get(String refNo) {
        return ApiResponse.getFetchResponse(SUB_CATEGORY.name(), refNo, mapper.entityToRespDto(service.get(refNo)));
    }

    public ApiResponse update(String refNo, SubCategoryUpdateDto subCategoryUpdateDto) {
        return ApiResponse.getUpdateResponse(SUB_CATEGORY.name(), refNo, mapper.entityToRespDto(service.update(refNo, subCategoryUpdateDto)));
    }

    public ApiResponse delete(String refNo) {
        service.delete(refNo);
        return ApiResponse.getDeleteResponse(SUB_CATEGORY.name(), "subCategory deleted successfully");
    }

    public ApiResponse getAllEntities(Pageable pageable) {
        Page<SubCategory> subCategoryPage = service.getAllEntities(pageable);
        Page<SubCategoryRespDto> subCategoryDtos = subCategoryPage.map(mapper::entityToRespDto);
        return ApiResponse.getFetchAllResponse(SUB_CATEGORY.name(), subCategoryDtos);
    }

    public ApiResponse getAllEntitiesWithoutCategory(Pageable pageable) {
        Page<SubCategory> subCategoryPage = service.getAllEntitiesWithoutCategory(pageable);
        Page<SubCategoryRespDto> subCategoryDtos = subCategoryPage.map(mapper::entityToRespDto);
        return ApiResponse.getFetchAllResponse(SUB_CATEGORY.name(), subCategoryDtos);
    }

    public ApiResponse getSubCategoryByName(String name) {
        if (name == null || name.isBlank()) {
            return ApiResponse.getErrorResponse(804, "name cannot be null");
        }
        List<SubCategory> subCategories = service.getSubCategoryByName( "%" + name + "%");
        if (!subCategories.isEmpty()) {
            return ApiResponse.getFetchAllResponse(SUB_CATEGORY.name(),
                    subCategories.stream().map(mapper::entityToRespDto).collect(Collectors.toList())
            );
        } else {
            return ApiResponse.getErrorResponse(804, "not found");
        }
    }

    public ApiResponse getCategorySubcategories(String refNo, Pageable pageable) {
        Page<SubCategory> subCategoryPage = service.getCategorySubcategories(refNo, pageable);
        Page<SubCategoryRespDto> subCategoryDtos = subCategoryPage.map(mapper::entityToRespDto);
        return ApiResponse.getFetchAllResponse(SUB_CATEGORY.name(), subCategoryDtos);
    }
}
