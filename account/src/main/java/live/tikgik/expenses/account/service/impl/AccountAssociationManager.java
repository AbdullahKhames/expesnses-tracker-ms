package live.tikgik.expenses.account.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import live.tikgik.expenses.account.entity.Account;
import live.tikgik.expenses.account.service.BudgetService;
import live.tikgik.expenses.shared.enums.Models;
import live.tikgik.expenses.shared.service.CollectionAdder;
import live.tikgik.expenses.shared.service.CollectionRemover;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class AccountAssociationManager {
    private final BudgetService budgetService;


    private final Map<Models, CollectionAdder<Account>> adderHandler = new HashMap<>(5);
    private final Map<Models, CollectionRemover<Account>> removerHandler = new HashMap<>(5);
//    private final Map<Models, CollectionAdder<Account, budgetUpdateDto>> adderHandler = new HashMap<>(5);
//    private final Map<Models, CollectionRemover<Account, budgetUpdateDto>> removerHandler = new HashMap<>(5);

    @PostConstruct
    private void init(){
        adderHandler.put(Models.BUDGET, budgetService);


        removerHandler.put(Models.BUDGET, budgetService);


    }
    public <T> boolean addAssociation(Account account,
                                      Models accountModels,
                                      T refNo) {

        CollectionAdder<Account> accountCollectionAdder = adderHandler.get(accountModels);
        if (accountCollectionAdder == null) {
            return false;
        }
        if (refNo instanceof String s){
            return accountCollectionAdder.addAssociation(account, Models.ACCOUNT, s);
        }
        return false;
    }


    public boolean removeAssociation(Account account,
                                     Models accountModels,
                                     Object refNo) {

        CollectionRemover<Account> accountCollectionRemover = removerHandler.get(accountModels);
        if (accountCollectionRemover == null) {
            return false;
        }
        if (refNo instanceof String s){
            return accountCollectionRemover.removeAssociation(account, Models.ACCOUNT, s);
        }
        return false;

    }
}