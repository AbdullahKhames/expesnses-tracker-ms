package live.tikgik.expenses.category.service.service_impl;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.expenses.features.association.CollectionAdder;
import name.expenses.features.association.CollectionRemover;
import name.expenses.features.association.Models;
import name.expenses.features.category.models.Category;
import name.expenses.features.sub_category.service.SubService;

import java.util.HashMap;
import java.util.Map;

@Singleton
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class CategoryAssociationManager {
    private final SubService subCategoryService;


    private final Map<Models, CollectionAdder<Category>> adderHandler = new HashMap<>(5);
    private final Map<Models, CollectionRemover<Category>> removerHandler = new HashMap<>(5);
//    private final Map<Models, CollectionAdder<Category, SubCategoryUpdateDto>> adderHandler = new HashMap<>(5);
//    private final Map<Models, CollectionRemover<Category, SubCategoryUpdateDto>> removerHandler = new HashMap<>(5);

    @PostConstruct
    private void init(){
        adderHandler.put(Models.SUB_CATEGORY, subCategoryService);


        removerHandler.put(Models.SUB_CATEGORY, subCategoryService);


    }
    public boolean addAssociation(Category category,
                                  Models categoryModels,
                                  String refNo) {

        CollectionAdder<Category> CategoryCollectionAdder = adderHandler.get(categoryModels);
//        CollectionAdder<Category, SubCategoryUpdateDto> CategoryCollectionAdder = adderHandler.get(categoryModels);
        if (CategoryCollectionAdder == null) {
            return false;
        }
        return CategoryCollectionAdder.addAssociation(category, Models.CATEGORY, refNo);
    }


    public boolean removeAssociation(Category category,
                                     Models categoryModels,
                                     String refNo) {

//        CollectionRemover<Category, SubCategoryUpdateDto> CategoryCollectionRemover = removerHandler.get(categoryModels);
        CollectionRemover<Category> CategoryCollectionRemover = removerHandler.get(categoryModels);
        if (CategoryCollectionRemover == null) {
            return false;
        }
        return CategoryCollectionRemover.removeAssociation(category, Models.CATEGORY, refNo);
    }
}