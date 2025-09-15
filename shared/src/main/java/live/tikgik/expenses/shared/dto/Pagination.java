package live.tikgik.expenses.shared.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Pagination {

    private int currentPage;

    private int pageSize;

    private int totalPages;

    private long totalElements;

    private boolean hasContent;

    private boolean hasNext;

    private boolean hasPrevious;

}
