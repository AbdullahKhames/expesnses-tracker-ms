package live.tikgik.expenses.account.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import live.tikgik.expenses.account.dto.request.AccountUpdateDto;
import live.tikgik.expenses.account.entity.Account;
import live.tikgik.expenses.account.service.BudgetService;
import live.tikgik.expenses.shared.service.UpdateAssociation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import java.util.ArrayList;
import java.util.List;

@Singleton
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
@Transactional
public class UpdateAccountServiceImpl {
    private final BudgetService budgetService;


    private final List<UpdateAssociation<Account, AccountUpdateDto>> updateProductAssociations = new ArrayList<>(4);
    @PostConstruct
    private void init(){
        updateProductAssociations.add(budgetService);

    }
    public void updateCategoryAssociations(Account account, AccountUpdateDto accountUpdateDto){
        updateProductAssociations
                .forEach(updateProductAssociation ->
                        updateProductAssociation.updateAssociation(account, accountUpdateDto));
    }

}