package live.tikgik.expenses.category.service;


import live.tikgik.expenses.category.entity.Category;
import live.tikgik.expenses.shared.service.GetRefNo;

import java.util.Set;

public interface CategoryGetter  extends GetRefNo {
    Set<Category> getCategories();
    void setCategories(Set<Category> categories);
}