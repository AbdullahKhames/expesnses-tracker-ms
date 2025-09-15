package live.tikgik.expenses.account.config;

public class UserContextHolder {

    private static final ThreadLocal<String> USERNAME = new ThreadLocal<>();

    public static void setUsername(String username) {
        USERNAME.set(username);
    }

    public static String getUsername() {
        return USERNAME.get() != null ? USERNAME.get() : "Anonymous";
    }

    public static void clear() {
        USERNAME.remove();
    }
}