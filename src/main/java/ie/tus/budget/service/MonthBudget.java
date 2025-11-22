package ie.tus.budget.service;

import java.time.YearMonth;

import ie.tus.budget.model.Expense;
import ie.tus.budget.model.Money;

public class MonthBudget implements Billable {

	private final YearMonth month;
	private Money plan;
	private final Expense[] expenses;   
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
        Money total = Money.MoneyFromDouble(0.0,"EUR");
        for (int i = 0; i < count; i++) {
            total = total.add(expenses[i].money());
        }
        return this.total = total;
    }
	
	public Money remain() {
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
