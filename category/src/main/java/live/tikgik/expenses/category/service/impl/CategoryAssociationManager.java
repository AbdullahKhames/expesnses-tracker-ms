package live.tikgik.expenses.category.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import live.tikgik.expenses.category.entity.Category;
import live.tikgik.expenses.category.service.SubService;
import live.tikgik.expenses.shared.enums.Models;
import live.tikgik.expenses.shared.service.CollectionAdder;
import live.tikgik.expenses.shared.service.CollectionRemover;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;

@Singleton
@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryAssociationManager {
    private final SubService subCategoryService;


    private final Map<Models, CollectionAdder<Category>> adderHandler = HashMap.newHashMap(5);
    private final Map<Models, CollectionRemover<Category>> removerHandler = HashMap.newHashMap(5);


    @PostConstruct
    private void init(){
        adderHandler.put(Models.SUB_CATEGORY, subCategoryService);


        removerHandler.put(Models.SUB_CATEGORY, subCategoryService);


    }
    public boolean addAssociation(Category category,
                                  Models categoryModels,
                                  String refNo) {

        CollectionAdder<Category> categoryCollectionAdder = adderHandler.get(categoryModels);
        if (categoryCollectionAdder == null) {
            return false;
        }
        return categoryCollectionAdder.addAssociation(category, Models.CATEGORY, refNo);
    }


    public boolean removeAssociation(Category category,
                                     Models categoryModels,
                                     String refNo) {

        CollectionRemover<Category> categoryCollectionRemover = removerHandler.get(categoryModels);
        if (categoryCollectionRemover == null) {
            return false;
        }
        return categoryCollectionRemover.removeAssociation(category, Models.CATEGORY, refNo);
    }
}