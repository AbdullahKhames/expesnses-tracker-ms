package live.tikgik.expenses.shared.error.exception_handler.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import live.tikgik.expenses.shared.error.exception_handler.models.APIError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class APIErrorLoader {
    @PersistenceContext(name = "expenses-unit")
    EntityManager entityManager;

    public APIError loadApiError(String errorCode) {
        APIError apiError = null;

        try {
            apiError = entityManager.find(APIError.class, errorCode);
        } catch (Exception e) {
            log.error("##########  Exception APIErrorLoader loadApiError for errorCode: '" + errorCode + "'");
            e.printStackTrace();
        }

        return apiError;
    }
}