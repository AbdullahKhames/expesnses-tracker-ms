package live.tikgik.expenses.category.service.service_impl;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.expenses.error.exception.ErrorCode;
import name.expenses.error.exception.GeneralFailureException;
import name.expenses.error.exception_handler.models.ErrorCategory;
import name.expenses.error.exception_handler.models.ResponseError;
import name.expenses.features.association.AssociationResponse;
import name.expenses.features.association.Models;
import name.expenses.features.category.dao.CategoryDAO;
import name.expenses.features.category.dtos.request.CategoryReqDto;
import name.expenses.features.category.dtos.request.CategoryUpdateDto;
import name.expenses.features.category.dtos.response.CategoryRespDto;
import name.expenses.features.category.mappers.CategoryMapper;
import name.expenses.features.category.models.Category;
import name.expenses.features.category.service.CategoryService;
import name.expenses.features.customer.models.Customer;
import name.expenses.features.sub_category.dtos.response.SubCategoryRespDto;
import name.expenses.features.sub_category.mappers.SubCategoryMapper;
import name.expenses.features.sub_category.models.SubCategory;
import name.expenses.globals.Page;
import name.expenses.globals.PageReq;
import name.expenses.globals.SortDirection;
import name.expenses.globals.responses.ResponseDto;
import name.expenses.utils.PageUtil;
import name.expenses.utils.ResponseDtoBuilder;
import name.expenses.utils.ValidateInputUtils;
import name.expenses.utils.collection_getter.CategoryGetter;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Stateless
@RequiredArgsConstructor(onConstructor_ = {@Inject})
@Transactional
public class CategoryServiceImpl implements CategoryService {
    public static final String CATEGORY = "Category";
    private final CategoryDAO categoryDAO;
    private final CategoryMapper categoryMapper;
    private final SubCategoryMapper subCategoryMapper;
    private final CategoryAssociationManager categoryAssociationManager;



    @Override
    public ResponseDto create(CategoryReqDto category) {
        Category sentCategory = categoryMapper.reqDtoToEntity(category);
        Category savedCategory = categoryDAO.create(sentCategory);
        log.info("created category {}", savedCategory);
        return ResponseDtoBuilder.getCreateResponse(CATEGORY, savedCategory.getRefNo(), categoryMapper.entityToRespDto(savedCategory));
    }

    public Optional<Category> getEntity(String refNo){
        try {
            Optional<Category> categoryOptional = categoryDAO.get(refNo);
            log.info("fetched category {}", categoryOptional);
            return categoryOptional;

        }catch (Exception ex){
            return Optional.empty();
        }
    }
    @Override
    public ResponseDto get(String refNo) {
        try {
            Optional<Category> categoryOptional = getEntity(refNo);
            log.info("fetched category {}", categoryOptional);
            if (categoryOptional.isPresent()){
                Category category = categoryOptional.get();
                return ResponseDtoBuilder.getFetchResponse(CATEGORY, category.getRefNo(), categoryMapper.entityToRespDto(category));
            }else {
                throw new GeneralFailureException(GeneralFailureException.OBJECT_NOT_FOUND,
                        Map.of("error", String.format("category with the ref number %s was not found", refNo)));
            }

        }catch (Exception ex){
            ResponseError responseError = new ResponseError();
            responseError.setErrorCategory(ErrorCategory.DATABASE_Error);
            responseError.setErrorCode(ErrorCode.OBJECT_NOT_FOUND.getErrorCode());
            responseError.setErrorMessage(String.format("category with the ref number %s was not found", refNo));
            return ResponseDtoBuilder.getErrorResponse(804, responseError);
        }

    }

    @Override
    public ResponseDto update(String refNo, CategoryUpdateDto categoryUpdateDto) {
        Optional<Category> categoryOptional = getEntity(refNo);
        if (categoryOptional.isPresent()){
            Category category = categoryOptional.get();
            log.info("fetched category {}", category);
            categoryMapper.update(category, categoryUpdateDto);
//            updateCategoryService.updateCategoryAssociations(category, categoryUpdateDto);
            log.info("updated category {}", category);
            category.setUpdatedAt(LocalDateTime.now());
            return ResponseDtoBuilder.getUpdateResponse(CATEGORY, category.getRefNo(), categoryMapper.entityToRespDto(categoryDAO.update(category)));
        }
        ResponseError responseError = new ResponseError();
        responseError.setErrorCategory(ErrorCategory.DATABASE_Error);
        responseError.setErrorCode(ErrorCode.OBJECT_NOT_FOUND.getErrorCode());
        responseError.setErrorMessage(String.format("category with the ref number %s was not found", refNo));
        return ResponseDtoBuilder.getErrorResponse(804, responseError);

    }

    @Override
    public ResponseDto delete(String refNo) {
        return ResponseDtoBuilder.getDeleteResponse(CATEGORY,categoryDAO.delete(refNo));
    }

    @Override
    public ResponseDto addAssociation(String categoryRefNo, String subCategoryRefNo) {
        Optional<Category> categoryOptional = getEntity(categoryRefNo);
        if (categoryOptional.isPresent()){
            Category category = categoryOptional.get();
            if (categoryAssociationManager.addAssociation(category, Models.SUB_CATEGORY, subCategoryRefNo)){
                return ResponseDtoBuilder.getUpdateResponse(CATEGORY, categoryRefNo, categoryMapper.entityToRespDto(category));
            }
            return ResponseDtoBuilder.getErrorResponse(804, "something went wrong couldn't add");

        }
        ResponseError responseError = new ResponseError();
        responseError.setErrorCategory(ErrorCategory.DATABASE_Error);
        responseError.setErrorCode(ErrorCode.OBJECT_NOT_FOUND.getErrorCode());
        responseError.setErrorMessage(String.format("category with the ref number %s was not found", categoryRefNo));
        return ResponseDtoBuilder.getErrorResponse(804, responseError);
    }

    @Override
    public ResponseDto removeAssociation(String categoryRefNo, String subCategoryRefNo) {
        Optional<Category> categoryOptional = getEntity(categoryRefNo);
        if (categoryOptional.isPresent()){
            Category category = categoryOptional.get();
            if (categoryAssociationManager.removeAssociation(category, Models.SUB_CATEGORY, subCategoryRefNo)){
                return ResponseDtoBuilder.getUpdateResponse(CATEGORY, categoryRefNo, categoryMapper.entityToRespDto(category));
            }
            return ResponseDtoBuilder.getErrorResponse(804, "something went wrong couldn't remove");
        }
        ResponseError responseError = new ResponseError();
        responseError.setErrorCategory(ErrorCategory.DATABASE_Error);
        responseError.setErrorCode(ErrorCode.OBJECT_NOT_FOUND.getErrorCode());
        responseError.setErrorMessage(String.format("category with the ref number %s was not found", categoryRefNo));
        return ResponseDtoBuilder.getErrorResponse(804, responseError);
    }

    @Override
    public ResponseDto getSubcategories(String refNo, Long pageNumber, Long pageSize, String sortBy, SortDirection sortDirection) {
        PageReq pageReq = ValidateInputUtils.validatePageData(pageNumber, pageSize);
        Page<SubCategory> subCategoryPage = categoryDAO.getSubcategories(refNo, pageReq.pageNumber(), pageReq.pageSize(), sortBy, sortDirection);
        Page<SubCategoryRespDto> subCategoryDtos = subCategoryMapper.entityToRespDto(subCategoryPage);
        return ResponseDtoBuilder.getFetchAllResponse(CATEGORY, subCategoryDtos);
    }

    @Override
    public ResponseDto getCategoryByName(String name) {
        if (name == null || name.isBlank()) {
            return ResponseDtoBuilder.getErrorResponse(804, "name cannot be null");
        }
        List<Category> categories = categoryDAO.getByName(name);
        if (!categories.isEmpty()){
            return ResponseDtoBuilder.getFetchAllResponse(CATEGORY, categoryMapper.entityToRespDto(
                    PageUtil.createPage(1L, (long) categories.size(), categories, categories.size())
            ));
        }else {
            return ResponseDtoBuilder.getErrorResponse(804, "not found");
        }
    }

    @Override
    public ResponseDto getAllEntities(Long pageNumber, Long pageSize, String sortBy, SortDirection sortDirection) {
        PageReq pageReq = ValidateInputUtils.validatePageData(pageNumber, pageSize);
        Page<Category> categoryPage = categoryDAO.findAll(pageReq.pageNumber(), pageReq.pageSize(), sortBy, sortDirection);
        Page<CategoryRespDto> categoryDtos = categoryMapper.entityToRespDto(categoryPage);
        return ResponseDtoBuilder.getFetchAllResponse(CATEGORY, categoryDtos);
    }

    @Override
    public Set<Category> getEntities(Set<String> refNos) {
        return categoryDAO.getEntities(refNos);
    }

    @Override
    public boolean addAssociation(Customer entity, Models entityModel, String refNo) {
        return false;
    }



    @Override
    public ResponseDto addDtoAssociation(Object entity, Models entityModel, Set<?> associationUpdateDto) {
        return null;
    }

    @Override
    public boolean removeAssociation(Customer entity, Models entityModel, String refNo) {
        return false;
    }

    @Override
    public ResponseDto removeDtoAssociation(Object entity, Models entityModel, Set<?> associationsUpdateDto) {
        return null;
    }
    @Override
    public ResponseDto addAssociation(Object entity, Models entityModel, Set<String> refNos) {
        ResponseDto entityResponse = ValidateInputUtils.validateEntity(entity, CategoryGetter.class);
        if (entityResponse != null) {
            return entityResponse;
        }
        CategoryGetter categoryGetter = (CategoryGetter) entity;
        AssociationResponse associationResponse = new AssociationResponse();
        for (String refNo: refNos){
            Optional<Category> categoryOptional = getEntity(refNo);
            if (categoryOptional.isPresent()){
                Category category = categoryOptional.get();
                if (categoryGetter.getCategories() == null){
                    categoryGetter.setCategories(new HashSet<>());
                }
                if (categoryGetter.getCategories().contains(category)){
                    associationResponse.getError().put(refNo, "this categoryGetter already contain this category");
                }else {
                    categoryGetter.getCategories().add(category);
                    associationResponse.getSuccess().put(refNo, "was added successfully");
                }
            }else {
                associationResponse.getError().put(refNo, "no category corresponds to this ref no");
            }
        }
        return ResponseDtoBuilder.getUpdateResponse(entityModel.name(), categoryGetter.getRefNo(), associationResponse);
    }

    @Override
    public ResponseDto removeAssociation(Object entity, Models entityModel, Set<String> refNos) {
        ResponseDto entityResponse = ValidateInputUtils.validateEntity(entity, CategoryGetter.class);
        if (entityResponse != null) {
            return entityResponse;
        }
        CategoryGetter categoryGetter = (CategoryGetter) entity;
        AssociationResponse associationResponse = new AssociationResponse();
        for (String refNo: refNos){
            Optional<Category> categoryOptional = getEntity(refNo);
            if (categoryOptional.isPresent()){
                Category category = categoryOptional.get();
                if (categoryGetter.getCategories() == null){
                    categoryGetter.setCategories(new HashSet<>());
                }
                if (categoryGetter.getCategories().contains(category)){
                    categoryGetter.getCategories().remove(categoryOptional.get());
                    associationResponse.getSuccess().put(refNo, "was removed successfully");
                }else {
                    associationResponse.getError().put(refNo, "this categoryGetter doesn't contain this category");
                }
            }else {
                associationResponse.getError().put(refNo, "no category corresponds to this ref no");
            }
        }
        return ResponseDtoBuilder.getUpdateResponse(entityModel.name(), categoryGetter.getRefNo(), associationResponse);
    }
}
