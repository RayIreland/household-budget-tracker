package ie.tus.budget.model;

import java.time.YearMonth;

public class MonthBudget implements Billable {

	private YearMonth month;
	private Money plan;
	private Expense[] expenses;
    private int count = 0;
    private Money total;
    private Money remain;
    
    public YearMonth month() {
		return this.month;
	}
	
	public MonthBudget(YearMonth month, Money money) {
        this.month = month;          
        this.plan = money;
        this.expenses = new Expense[100]; 
    }
	
	public MonthBudget(YearMonth month) {
        this(month, Money.MoneyFromDouble(0.0, "EUR"));
    }
	
	public void addExpense(Expense expense) {
        if (count >= expenses.length) {
            throw new IllegalStateException("Too many expenses for month " + month + " (max allowed: 100)");
        }
        expenses[count++] = expense;
    }
	
	@Override
    public Money total() {
		if(this.total != null) {
			return this.total;
		}
        Money total = Money.MoneyFromDouble(0.0,"EUR");
        for (int i = 0; i < count; i++) {
            total = total.add(expenses[i].money());
        }
        return this.total = total;
    }
	
	public Money remain() {
		if(this.remain != null) {
			return this.remain;
		}
		if(this.total == null) {
			total();
		}
		Money remain = new Money(plan.amount().subtract(this.total.amount()) ,"EUR");
		return this.remain = remain;
	}
	
	public boolean isOverBudget() {
        return total().amount().compareTo(plan.amount()) > 0;
    }
	
	@Override
    public String toString() {
        return plan.toString();
    }
}
