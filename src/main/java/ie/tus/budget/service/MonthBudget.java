package ie.tus.budget.service;

import java.time.YearMonth;

import ie.tus.budget.model.Expense;
import ie.tus.budget.model.Money;

public class MonthBudget implements Billable {

	private final YearMonth month;
	private Money plan;
	private final Expense[] expenses;   
    private int count = 0;
	
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
//            throw new InvalidExpenseRuntimeException("Too many expenses for month " + month);
        }
        expenses[count++] = expense;
    }
	
	@Override
    public Money calculateTotal() {
        Money total = Money.MoneyFromDouble(0.0,"EUR");
        for (int i = 0; i < count; i++) {
            total = total.add(expenses[i].money());
        }
        return total;
    }
	
}
