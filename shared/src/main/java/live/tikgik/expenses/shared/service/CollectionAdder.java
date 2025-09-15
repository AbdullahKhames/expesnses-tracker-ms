package live.tikgik.expenses.shared.service;


import live.tikgik.expenses.shared.dto.ApiResponse;
import live.tikgik.expenses.shared.enums.Models;

import java.util.Set;

public interface CollectionAdder<T> {
    boolean addAssociation(T entity, Models entityModel, String refNo);
    ApiResponse addAssociation(Object entity, Models entityModel, Set<String> refNos);
//    ApiResponse addAssociation(T entity, Models entityModel, Set<J> associationsUpdateDto);

    ApiResponse addDtoAssociation(Object entity, Models entityModel, Set<?> associationUpdateDto);
}
