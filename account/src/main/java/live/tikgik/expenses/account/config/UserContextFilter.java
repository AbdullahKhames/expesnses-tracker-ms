package live.tikgik.expenses.account.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import live.tikgik.expenses.shared.model.User;
import live.tikgik.expenses.shared.model.UserContextHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static live.tikgik.expenses.shared.constant.AppConstants.*;

@Component
public class UserContextFilter extends OncePerRequestFilter {
    private static final List<String> SWAGGER_PATHS = Arrays.asList(
            "/swagger-ui.html",
            "/swagger-ui/",
            "/v3/api-docs",
            "/v3/api-docs/",
            "/v3/api-docs/swagger-config",
            "/webjars/",
            "/swagger-resources"
    );
    @Value("${swagger.mock.enabled:false}")
    private boolean swaggerEnabled;

    @Value("${swagger.mock.user.id:123}")
    private String mockUserId;

    @Value("${swagger.mock.user.name:khames}")
    private String mockUserName;

    @Value("${swagger.mock.user.email:khames@example.com}")
    private String mockEmail;

    @Value("${swagger.mock.user.roles:USER,ADMIN}")
    private String mockRoles;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (swaggerEnabled && isSwaggerRequest(request)) {
            // Process with mock user for Swagger
            processWithMockUser(request, response, filterChain);
        } else {
            // Process with actual headers
            processWithRealHeaders(request, response, filterChain);
        }

    }

    public String getHeaderFromRequest(String headerKey) {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (sra != null) {
            HttpServletRequest request = sra.getRequest();
            return request.getHeader(headerKey);
        }
        return "Anonymous";
    }
//    private boolean isSwaggerRequest(HttpServletRequest request) {
//        String path = request.getRequestURI();
//        String userAgent = request.getHeader("User-Agent");
//
//        return path.contains("/swagger-ui") ||
//                path.contains("/v3/api-docs") ||
//                path.contains("/webjars/") ||
//                path.contains("/swagger-resources") ||
//                (userAgent != null && userAgent.contains("Swagger"));
//    }
    private boolean isSwaggerRequest(HttpServletRequest request) {
        String path = request.getRequestURI();
        return SWAGGER_PATHS.stream().anyMatch(path::contains) ||
                request.getHeader("User-Agent") != null &&
                        request.getHeader("User-Agent").contains("Swagger");
    }
    private void processWithRealHeaders(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userId = getHeaderFromRequest(X_USER_ID.getKey());
        if (ObjectUtils.isEmpty(userId)) {
            throw new ServletException("User not found");
        }
        String userName = getHeaderFromRequest(X_USER_NAME.getKey());
        String email = getHeaderFromRequest(X_EMAIL.getKey());
        String rolesHeader = getHeaderFromRequest(X_ROLES.getKey());
        List<String> roles = new ArrayList<>();

        if (!ObjectUtils.isEmpty(rolesHeader)) {
            roles = Arrays.stream(rolesHeader.split(","))
                    .map(String::trim)
                    .filter(role -> !role.isEmpty())
                    .toList();
        }

        User user = User.builder()
                .id(userId)
                .username(userName)
                .email(email)
                .roles(roles)
                .build();
        UserContextHolder.setUser(user);
        filterChain.doFilter(request, response);
    }

    private void processWithMockUser(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        List<String> roles = Arrays.stream(mockRoles.split(","))
                .map(String::trim)
                .filter(role -> !role.isEmpty())
                .toList();

        User user = User.builder()
                .id(mockUserId)
                .username(mockUserName)
                .email(mockEmail)
                .roles(roles)
                .build();
        UserContextHolder.setUser(user);
        filterChain.doFilter(request, response);
    }
}
