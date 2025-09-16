package live.tikgik.expenses.shared.model;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    private String id;
    private String username;
    private String email;
    private List<String> roles;
}
