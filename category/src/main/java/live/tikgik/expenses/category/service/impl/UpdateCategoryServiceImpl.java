package live.tikgik.expenses.category.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import live.tikgik.expenses.category.dtos.request.CategoryUpdateDto;
import live.tikgik.expenses.category.entity.Category;
import live.tikgik.expenses.category.service.SubService;
import live.tikgik.expenses.shared.service.UpdateAssociation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import java.util.ArrayList;
import java.util.List;

@Singleton
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
@Transactional
public class UpdateCategoryServiceImpl {
    private final SubService subCategoryService;


    private final List<UpdateAssociation<Category, CategoryUpdateDto>> updateProductAssociations = new ArrayList<>(4);
    @PostConstruct
    private void init(){
        updateProductAssociations.add(subCategoryService);

    }
    public void updateCategoryAssociations(Category category, CategoryUpdateDto categoryUpdateDto){
        updateProductAssociations
                .forEach(updateProductAssociation ->
                        updateProductAssociation.updateAssociation(category, categoryUpdateDto));
    }

}