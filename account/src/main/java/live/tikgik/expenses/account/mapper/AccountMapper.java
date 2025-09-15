package live.tikgik.expenses.account.mapper;


import live.tikgik.expenses.account.dto.request.AccountReqDto;
import live.tikgik.expenses.account.dto.request.AccountUpdateDto;
import live.tikgik.expenses.account.dto.response.AccountRespDto;
import live.tikgik.expenses.account.entity.Account;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                BudgetMapper.class
        },
        imports = {LocalDateTime.class})
public abstract class AccountMapper {


    @Mappings(

            {
                    @Mapping(target = "id", ignore = true),
                    @Mapping(target = "deleted", ignore = true),
                    @Mapping(target = "refNo", ignore = true),
                    @Mapping(target = "createdAt", ignore = true),
                    @Mapping(target = "updatedAt", ignore = true),
                    @Mapping(target = "budgets", ignore = true),
            }

    )
    public abstract Account reqDtoToEntity(AccountReqDto entityReqDto);
    public abstract AccountRespDto entityToRespDto(Account entity);
    public abstract Set<AccountRespDto> entityToRespDto(Set<Account> entities);
    public abstract List<AccountRespDto> entityToRespDto(List<Account> entities);

    @Mappings(

            {
                    @Mapping(target = "id", ignore = true),
                    @Mapping(target = "deleted", ignore = true),
                    @Mapping(target = "refNo", ignore = true),
                    @Mapping(target = "createdAt", ignore = true),
                    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())"),

            }

    )
    public abstract void update(@MappingTarget Account entity, AccountUpdateDto entityUpdateDto);

}
