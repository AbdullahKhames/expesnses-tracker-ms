package live.tikgik.expenses.category.service.impl;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import live.tikgik.expenses.shared.model.UserContextHolder;
import live.tikgik.expenses.category.dtos.request.CategoryUpdateDto;
import live.tikgik.expenses.category.dtos.request.SubCategoryReqDto;
import live.tikgik.expenses.category.dtos.request.SubCategoryUpdateDto;
import live.tikgik.expenses.category.mappers.SubCategoryMapper;
import live.tikgik.expenses.category.entity.Category;
import live.tikgik.expenses.category.entity.SubCategory;
import live.tikgik.expenses.category.repository.CategoryRepository;
import live.tikgik.expenses.category.repository.SubCategoryRepository;
import live.tikgik.expenses.category.service.SubService;
import live.tikgik.expenses.shared.enums.Models;
import live.tikgik.expenses.shared.error.exception.GeneralFailureException;
import live.tikgik.expenses.shared.error.exception.ResourceNotFoundException;
import live.tikgik.expenses.shared.utility.ValidateInputUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static live.tikgik.expenses.shared.constant.AppConstants.REF_NO;
import static live.tikgik.expenses.shared.enums.Models.SUB_CATEGORY;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Inject})
@Transactional
public class SubServiceImpl implements SubService {
    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryMapper subCategoryMapper;


    @Override
    public SubCategory create(SubCategoryReqDto subCategoryReqDto) {
        Optional<Category> categoryOptional = categoryRepository.findByRefNoAndCustomerId(subCategoryReqDto.getCategoryRefNo(), UserContextHolder.getUser().getId());
        if (categoryOptional.isPresent()) {
            SubCategory sentSubCategory = subCategoryMapper.reqDtoToEntity(subCategoryReqDto);
            Category category = categoryOptional.get();
            if (category.getSubCategories() == null) {
                category.setSubCategories(new HashSet<>());
            }
            category.getSubCategories().add(sentSubCategory);
            sentSubCategory.setCategory(category);
            categoryRepository.save(category);
            return sentSubCategory;
        } else {
            throw new ResourceNotFoundException(Models.CATEGORY.name(), REF_NO.name(), subCategoryReqDto.getCategoryRefNo());
        }
    }

    public Optional<SubCategory> getEntity(String refNo) {
        try {
            Optional<SubCategory> subCategoryOptional = subCategoryRepository.findByRefNoAndCustomerId(refNo, UserContextHolder.getUser().getId());
            log.info("fetched subCategory {}", subCategoryOptional);
            return subCategoryOptional;
        } catch (Exception _) {
            return Optional.empty();
        }
    }

    @Override
    public SubCategory get(String refNo) {
        return getEntity(refNo).orElseThrow(() -> new ResourceNotFoundException(SUB_CATEGORY.name(), REF_NO.name(), refNo));
    }

    @Override
    public SubCategory update(String refNo, SubCategoryUpdateDto subCategoryUpdateDto) {
        SubCategory subCategory = get(refNo);
        subCategoryMapper.update(subCategory, subCategoryUpdateDto);
//            expenseService.updateExpensesAssociation(subCategory, subCategoryUpdateDto);
        log.info("updated subCategory {}", subCategory);
        subCategory.setUpdatedAt(LocalDateTime.now());
        return subCategoryRepository.save(subCategory);
    }

    @Override
    public void delete(String refNo) {
        log.info("deleting subCategory with ref {}", refNo);
        subCategoryRepository.delete(get(refNo));
    }

    @Override
    public Page<SubCategory> getAllEntities(Pageable pageable) {
        return subCategoryRepository.findAllWithCustomerId(UserContextHolder.getUser().getId(), pageable);
    }

    @Override
    public Set<SubCategory> getEntities(Set<String> refNos) {
        return subCategoryRepository.findByRefNoInAndCustomerId(refNos, UserContextHolder.getUser().getId());
    }

    @Override
    public boolean addAssociation(Category category, Models entityModel, String refNo) {
        if (ValidateInputUtils.isValidInput(category, category.getSubCategories())) {
            Optional<SubCategory> subCategoryOptional = getEntity(refNo);
            if (subCategoryOptional.isEmpty()) {
                throw new GeneralFailureException(GeneralFailureException.OBJECT_NOT_FOUND,
                        Map.of("error", String.format("the subcategory with given reference number : %s doesn't exist", refNo)));
            }
            SubCategory subCategory = subCategoryOptional.get();
            Long categoryId = subCategoryRepository.checkCategoryAssociation(subCategory.getId());
            if (categoryId != null) {
                if (Objects.equals(categoryId, category.getId())) {
                    throw new GeneralFailureException(GeneralFailureException.ERROR_UPDATE,
                            Map.of("error", "this subcategory is already present in the given category!!"));
                } else {
                    throw new GeneralFailureException(GeneralFailureException.ERROR_UPDATE,
                            Map.of("error", "this subcategory is already present in another category!!"));
                }
            }

            category.getSubCategories().add(subCategoryOptional.get());
            return true;
        }
        return false;
    }


    @Override
    public boolean removeAssociation(Category category, Models entityModel, String refNo) {
        if (ValidateInputUtils.isValidInput(category, category.getSubCategories())) {
            Optional<SubCategory> subCategoryOptional = getEntity(refNo);
            if (subCategoryOptional.isEmpty()) {
                throw new GeneralFailureException(GeneralFailureException.OBJECT_NOT_FOUND,
                        Map.of("error", String.format("the subcategory with given reference number : %s doesn't exist", refNo)));
            }
            SubCategory subCategory = subCategoryOptional.get();
            Long categoryId = subCategoryRepository.checkCategoryAssociation(subCategory.getId());
            if (categoryId != null) {
                if (Objects.equals(categoryId, category.getId())) {
                    category.getSubCategories().remove(subCategoryOptional.get());
                } else {
                    throw new GeneralFailureException(GeneralFailureException.ERROR_UPDATE,
                            Map.of("error", "this subcategory is already present in another category!!"));
                }
            } else {
                throw new GeneralFailureException(GeneralFailureException.ERROR_UPDATE,
                        Map.of("error", "this subcategory is bot present in any category!!"));
            }
            return true;
        }
        return false;
    }


    @Override
    public void updateAssociation(Category category, CategoryUpdateDto categoryUpdateDto) {
        if (category.getSubCategories() != null && categoryUpdateDto.getSubCategories() != null) {
            Set<SubCategory> existingSubCategories = category.getSubCategories();
            Set<SubCategoryUpdateDto> subCategoryUpdateDtos = categoryUpdateDto.getSubCategories();
            //new subCategory will contain only the new subCategory ie the one not included with refs
            Set<SubCategory> newSubCategories = getSubCategories(subCategoryUpdateDtos, existingSubCategories);
            //remove non existent in the new collection
            Set<SubCategory> removedSubCategories = new HashSet<>(existingSubCategories);
            List<String> newListRefs = subCategoryUpdateDtos.stream().map(SubCategoryUpdateDto::getRefNo).toList();
            removedSubCategories.removeIf(subCategory -> newListRefs.contains(subCategory.getRefNo()));
            category.getSubCategories().removeAll(removedSubCategories);
            category.getSubCategories().addAll(newSubCategories);
        }

    }

    private Set<SubCategory> getSubCategories(Set<SubCategoryUpdateDto> subCategoryUpdateDtos, Set<SubCategory> existingSubCategories) {
        Set<SubCategory> addedSubCategories = new HashSet<>();
        subCategoryUpdateDtos.forEach(newSubCategory -> {
            if (newSubCategory.getRefNo() != null) {
                SubCategory existingSubCategory = findExistingEntity(existingSubCategories, newSubCategory.getRefNo());
                if (existingSubCategory != null) {
                    subCategoryMapper.update(existingSubCategory, newSubCategory);
                    existingSubCategory.setUpdatedAt(LocalDateTime.now());
//                    expenseService.updateExpensesAssociation(existingSubCategory, newSubCategory);
                }
            } else {
                //if id doesn't have id add it
                addedSubCategories.add(subCategoryMapper.reqEntityToEntity(newSubCategory));
            }
        });
        return addedSubCategories;
    }

    private SubCategory findExistingEntity(Set<SubCategory> existingSubCategories, String refNo) {
        return existingSubCategories.stream().filter(subCategory -> subCategory.getRefNo().equals(refNo)).findFirst().orElse(null);
    }

    @Override
    public Page<SubCategory> getAllEntitiesWithoutCategory(Pageable pageable) {
        return subCategoryRepository.findByCategoryNull(pageable);
    }

    //    @Override
//    public ApiResponse getSubCategoryExpenses(String refNo) {
//        Optional<SubCategory> subCategoryOptional = getEntity(refNo);
//        if (subCategoryOptional.isPresent()) {
//            SubCategory subCategory = subCategoryOptional.get();
//            return ApiResponse.getFetchResponse(SUB_CATEGORY, subCategory.getRefNo(), expenseService.entityToRespDto(subCategory.getExpenses()));
//
//        } else {
//            return ApiResponse.getErrorResponse(804, "no category found with given ref");
//        }
//    }
    @Override
    public Page<SubCategory> getSubcategories(Pageable pageable) {
        return subCategoryRepository.findAllWithCustomerId(UserContextHolder.getUser().getId(), pageable);
    }

    @Override
    public List<SubCategory> getSubCategoryByName(String name) {
        return subCategoryRepository.findByNameLikeIgnoreCaseAndCustomerId(name, UserContextHolder.getUser().getId());
    }

    @Override
    public Page<SubCategory> getCategorySubcategories(String refNo, Pageable pageable) {
        return subCategoryRepository.findByCategory_RefNo(refNo, pageable);
    }
}
