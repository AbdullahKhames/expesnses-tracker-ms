package live.tikgik.expenses.category.service;


import live.tikgik.expenses.category.dtos.request.CategoryReqDto;
import live.tikgik.expenses.category.dtos.request.CategoryUpdateDto;
import live.tikgik.expenses.category.entity.Category;
import live.tikgik.expenses.shared.service.CrudService;

import java.util.List;

public interface CategoryService extends CrudService<CategoryReqDto, CategoryUpdateDto, String, Category> {
    boolean addAssociation(String categoryRefNo, String subCategoryRefNo);
    boolean removeAssociation(String categoryRefNo, String subCategoryRefNo);
    List<Category> getCategoryByName(String name);
}