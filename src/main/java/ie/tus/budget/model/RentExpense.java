package ie.tus.budget.model;

import ie.tus.budget.model.enums.Category;

public final class RentExpense extends FixedExpense {

	private final String address;
	
	public RentExpense(String name, Money expenseOfMonth, int dayOfMonth, String address) {
        super(name, expenseOfMonth, dayOfMonth);
        this.address = address;
    }
	
	@Override
    public Category category() {
        return Category.RENT;
    }
	
	@Override
    public String description() {
        return super.description() + " at " + address;
    }
	
	public String address() {
        return address;
    }
}
