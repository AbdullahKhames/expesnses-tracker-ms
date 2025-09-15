package live.tikgik.expenses.shared.service;

import jakarta.persistence.EntityManager;
import live.tikgik.expenses.shared.dto.ApiResponse;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

public interface CrudService<T, J, H, E> {

    default E update(E entity, EntityManager entityManager){
        return entityManager.merge(entity);
    }
    ApiResponse create(T entityReqDto);
    ApiResponse get(H refNo);
    Optional<E> getEntity(H refNo);

    ApiResponse update(H refNo, J entityUpdateDto);
    ApiResponse delete(H refNo);
    ApiResponse getAllEntities(Pageable pageable);
    Set<E> getEntities(Set<H> refNos);
}
