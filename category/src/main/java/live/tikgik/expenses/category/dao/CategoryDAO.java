package live.tikgik.expenses.category.dao;

import jakarta.ejb.Local;
import name.expenses.features.category.models.Category;
import name.expenses.features.sub_category.models.SubCategory;
import name.expenses.globals.Page;
import name.expenses.globals.SortDirection;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Local
public interface CategoryDAO {
    Category create(Category expense);
    Optional<Category> get(String refNo);
    Page<Category> findAll(Long pageNumber, Long pageSize, String sortBy, SortDirection sortDirection);
    List<Category> get();
    Category update(Category expense);
    String delete(String refNo);
    Set<Category> getEntities(Set<String> refNos);

    Page<SubCategory> getSubcategories(String refNo, Long pageNumber, Long pageSize, String sortBy, SortDirection sortDirection);

    List<Category> getByName(String name);
}