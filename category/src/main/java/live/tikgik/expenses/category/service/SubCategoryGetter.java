package live.tikgik.expenses.category.service;


import live.tikgik.expenses.category.entity.SubCategory;
import live.tikgik.expenses.shared.service.GetRefNo;

import java.util.Set;

public interface SubCategoryGetter extends GetRefNo {
    Set<SubCategory> getSubCategories();
    void setSubCategories(Set<SubCategory> subCategories);
}