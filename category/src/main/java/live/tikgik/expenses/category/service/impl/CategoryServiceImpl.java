package live.tikgik.expenses.category.service.impl;

import live.tikgik.expenses.shared.model.UserContextHolder;
import live.tikgik.expenses.category.dtos.request.CategoryReqDto;
import live.tikgik.expenses.category.dtos.request.CategoryUpdateDto;
import live.tikgik.expenses.category.mappers.CategoryMapper;
import live.tikgik.expenses.category.entity.Category;
import live.tikgik.expenses.category.repository.CategoryRepository;
import live.tikgik.expenses.category.service.CategoryService;
import live.tikgik.expenses.shared.enums.Models;
import live.tikgik.expenses.shared.error.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static live.tikgik.expenses.shared.constant.AppConstants.REF_NO;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CategoryAssociationManager categoryAssociationManager;


    @Override
    public Category create(CategoryReqDto category) {
        log.info("creating category named {}", category.getName());
        Category sentCategory = categoryMapper.reqDtoToEntity(category);
        sentCategory.getCustomerIds().add(UserContextHolder.getUser().getId());
        return categoryRepository.save(sentCategory);
    }

    public Optional<Category> getEntity(String refNo) {
        try {
            Optional<Category> categoryOptional = categoryRepository.findByRefNoAndCustomerId(refNo, UserContextHolder.getUser().getId());
            log.info("fetched category {}", categoryOptional);
            return categoryOptional;

        } catch (Exception _) {
            return Optional.empty();
        }
    }

    @Override
    public Category get(String refNo) {
        return getEntity(refNo).orElseThrow(() -> new ResourceNotFoundException(Models.CATEGORY.name(), REF_NO.name(), refNo));

    }

    @Override
    public Category update(String refNo, CategoryUpdateDto categoryUpdateDto) {
        Category category = get(refNo);
        categoryMapper.update(category, categoryUpdateDto);
        return categoryRepository.save(category);
    }

    @Override
    public void delete(String refNo) {
        Category category = get(refNo);
        categoryRepository.delete(category);
    }

    @Override
    public boolean addAssociation(String categoryRefNo, String subCategoryRefNo) {
        Category category = get(categoryRefNo);
        return categoryAssociationManager.addAssociation(category, Models.SUB_CATEGORY, subCategoryRefNo);
    }

    @Override
    public boolean removeAssociation(String categoryRefNo, String subCategoryRefNo) {
        return categoryAssociationManager.removeAssociation(get(categoryRefNo), Models.SUB_CATEGORY, subCategoryRefNo);
    }

    @Override
    public List<Category> getCategoryByName(String name) {
        return categoryRepository.findByNameLikeIgnoreCaseAndCustomerId(name, UserContextHolder.getUser().getId());
    }

    @Override
    public Page<Category> getAllEntities(Pageable pageable) {
        return categoryRepository.findAllWithCustomerIdOptimized(UserContextHolder.getUser().getId(), pageable);
    }

    @Override
    public Set<Category> getEntities(Set<String> refNos) {
        return categoryRepository.findByRefNoInAndCustomerId(refNos, UserContextHolder.getUser().getId());
    }

}
