package live.tikgik.expenses.category;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditManagerImpl")
@EnableFeignClients
@EnableAspectJAutoProxy
@EntityScan({"live.tikgik.expenses.shared.model", "live.tikgik.expenses.category.entity"})
@ComponentScan(basePackages = {"live.tikgik.expenses.category", "live.tikgik.expenses.shared.utility", "live.tikgik.expenses.shared.error.exception_handler"})

public class CategoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(CategoryApplication.class, args);
	}

}
