package live.tikgik.expenses.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AssociationResponse {
    @Builder.Default
    private Map<String, String> success = new HashMap<>();
    @Builder.Default
    private Map<String, String> error = new HashMap<>();
}