package live.tikgik.expenses.shared.service;

import java.util.Optional;

public interface ObjectGetter<T> {
    Optional<T> get(String refNo);
}
