package live.tikgik.expenses.shared.service;

public interface UpdateAssociation <T, J> {
    void updateAssociation(T entity, J entityUpdateDto);
}