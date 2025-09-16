package live.tikgik.expenses.account.config;

import live.tikgik.expenses.shared.model.User;

import java.util.List;

public class UserContextHolder {

    private static final String ANONYMOUS = "anonymous";
    private static final User ANONYMOUS_USER = User.builder()
            .id(ANONYMOUS)
            .username(ANONYMOUS)
            .email(ANONYMOUS)
            .roles(List.of(ANONYMOUS))
            .build();
    private static final ThreadLocal<User> USER = new ThreadLocal<>();

    public static void setUser(User user) {
        USER.set(user);
    }

    public static User getUser() {
        return USER.get() != null ? USER.get() : ANONYMOUS_USER;
    }

    public static void clear() {
        USER.remove();
    }
}