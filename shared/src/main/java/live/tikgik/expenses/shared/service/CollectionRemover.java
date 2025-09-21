package live.tikgik.expenses.shared.service;


import live.tikgik.expenses.shared.dto.ApiResponse;
import live.tikgik.expenses.shared.enums.Models;

import java.util.Set;

public interface CollectionRemover  <T> {
    boolean removeAssociation(T entity, Models entityModel, String refNo);
//    ApiResponse removeAssociation(Object entity, Models entityModel, Set<String> refNo);
//    ApiResponse removeDtoAssociation(Object entity, Models entityModel, Set<?> associationsUpdateDto);

}