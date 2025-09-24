package live.tikgik.expenses.shared.service;

import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

public interface CrudService<T, J, H, E> {

    default E update(E entity, EntityManager entityManager){
        return entityManager.merge(entity);
    }
    E create(T entityReqDto);
    E get(H refNo);
    Optional<E> getEntity(H refNo);

    E update(H refNo, J entityUpdateDto);
    void delete(H refNo);
    Page<E> getAllEntities(Pageable pageable);
    Set<E> getEntities(Set<H> refNos);
}
