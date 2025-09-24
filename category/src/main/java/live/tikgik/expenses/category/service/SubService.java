package live.tikgik.expenses.category.service;


import live.tikgik.expenses.category.dtos.request.CategoryUpdateDto;
import live.tikgik.expenses.category.dtos.request.SubCategoryReqDto;
import live.tikgik.expenses.category.dtos.request.SubCategoryUpdateDto;
import live.tikgik.expenses.category.entity.Category;
import live.tikgik.expenses.category.entity.SubCategory;
import live.tikgik.expenses.shared.service.CollectionAdder;
import live.tikgik.expenses.shared.service.CollectionRemover;
import live.tikgik.expenses.shared.service.CrudService;
import live.tikgik.expenses.shared.service.UpdateAssociation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubService extends
        CollectionRemover<Category>,
        CollectionAdder<Category>,
        UpdateAssociation<Category, CategoryUpdateDto>,
        CrudService<SubCategoryReqDto, SubCategoryUpdateDto, String, SubCategory> {

    Page<SubCategory> getAllEntitiesWithoutCategory(Pageable pageable);

    Page<SubCategory> getSubcategories(Pageable pageable);

//    ApiResponse getSubCategoryExpenses(String refNo);

    List<SubCategory> getSubCategoryByName(String name);

    Page<SubCategory> getCategorySubcategories(String refNo, Pageable pageable);
}