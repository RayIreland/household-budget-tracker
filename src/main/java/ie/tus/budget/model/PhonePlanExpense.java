package ie.tus.budget.model;

import ie.tus.budget.model.enums.Category;

public class PhonePlanExpense extends FixedExpense {

	private final String nameOfOperator;
	
	public PhonePlanExpense(String name, Money expenseOfMonth, int dayOfMonth, String nameOfOperator) {
        super(name, expenseOfMonth, dayOfMonth);
        this.nameOfOperator = nameOfOperator;
    }
	
	@Override
    public Category category() {
        return Category.COMMUNICATION;
    }

    @Override
    public String description() {
        return nameOfOperator + " plan costs: " + expenseOfMonth();
    }
    
    public String nameOfOperator() {
        return nameOfOperator;
    }
}
