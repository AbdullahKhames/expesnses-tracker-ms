package live.tikgik.expenses.shared.dto;

import lombok.Builder;

import java.util.Collections;
import java.util.List;

@Builder
public record PagedData<T>(Pagination pagination, List<T> data) {

    public static <T> PagedData<T> empty() {
        return PagedData.<T>builder().pagination(Pagination.builder().build()).data(Collections.emptyList()).build();
    }
}