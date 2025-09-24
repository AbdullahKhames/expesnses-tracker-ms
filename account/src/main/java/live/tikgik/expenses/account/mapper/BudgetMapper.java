package live.tikgik.expenses.account.mapper;


import live.tikgik.expenses.shared.model.UserContextHolder;
import live.tikgik.expenses.account.dto.request.BudgetReqDto;
import live.tikgik.expenses.account.dto.request.BudgetUpdateDto;
import live.tikgik.expenses.account.dto.response.BudgetRespDto;
import live.tikgik.expenses.account.entity.Account;
import live.tikgik.expenses.account.entity.Budget;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {
                AccountMapper.class
        },
        imports = {LocalDateTime.class})
public abstract class BudgetMapper {

    @Mappings(

            {
                    @Mapping(target = "id", ignore = true),
                    @Mapping(target = "deleted", ignore = true),
                    @Mapping(target = "refNo", ignore = true),
                    @Mapping(target = "createdAt", ignore = true),
                    @Mapping(target = "updatedAt", ignore = true),
            }

    )
    public abstract Budget reqDtoToEntity(BudgetReqDto entityReqDto);
    @Mappings(

            {
                    @Mapping(target = "accountName", source = "account.name"),
                    @Mapping(target = "accountRefNo", source = "account.refNo"),
            }

    )
    public abstract BudgetRespDto entityToRespDto(Budget entity);

    public abstract Set<BudgetRespDto> entityToRespDto(Set<Budget> entities);
    public abstract List<BudgetRespDto> entityToRespDto(List<Budget> entities);

    @Mappings(

            {
                    @Mapping(target = "id", ignore = true),
                    @Mapping(target = "deleted", ignore = true),
                    @Mapping(target = "refNo", ignore = true),
                    @Mapping(target = "createdAt", ignore = true),

            }

    )
    public abstract void update(@MappingTarget Budget entity, BudgetUpdateDto entityUpdateDto);
    @Mappings(

            {
                    @Mapping(target = "id", ignore = true),
                    @Mapping(target = "deleted", ignore = true),
                    @Mapping(target = "refNo", ignore = true),
                    @Mapping(target = "createdAt", ignore = true),
                    @Mapping(target = "updatedAt", ignore = true),
            }

    )
    public abstract Budget reqEntityToEntity(BudgetUpdateDto newBudget, @Context Account account);
    public abstract List<Budget> reqEntityToEntity(List<BudgetUpdateDto> newBudget, @Context Account account);

    @AfterMapping
    public void afterMapping(@MappingTarget Budget entity, BudgetUpdateDto entityUpdateDto, @Context Account account){
        entity.setAccount(account);
        entity.setCustomerId(UserContextHolder.getUser().getId());
    }

}
