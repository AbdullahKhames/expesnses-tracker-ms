package live.tikgik.expenses.shared.utility;

import jakarta.validation.constraints.NotNull;
import live.tikgik.expenses.shared.model.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@Slf4j
public class AuditManagerImpl implements AuditorAware<String> {

    @Override
    @NotNull
    public Optional<String> getCurrentAuditor() {
        return Optional.of(UserContextHolder.getUser().getUsername());
    }
}
