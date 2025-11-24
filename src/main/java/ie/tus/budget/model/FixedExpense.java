package ie.tus.budget.model;

import java.time.LocalDate;
import java.time.YearMonth;

import ie.tus.budget.model.enums.Category;

public abstract class FixedExpense {

	protected final String name;
	protected final Money expenseOfMonth;
    protected final int dayOfMonth; 
    
    protected FixedExpense(String name, Money expenseOfMonth, int dayOfMonth) {
        this.name = name;
        this.expenseOfMonth = expenseOfMonth;
        //which day cost
        this.dayOfMonth = dayOfMonth;
    }
    
    public abstract Category category();
    
    public Expense addExpenseForMonth(YearMonth month, PaymentMode mode) {
        LocalDate date = LocalDate.of(month.getYear(), month.getMonth(), dayOfMonth);
        return Expense.build(name, expenseOfMonth, category(), date, mode);
    }

    public String name() {
        return name;
    }

    public Money expenseOfMonth() {
        return expenseOfMonth;
    }

    public String description() {
        return name + " costs " + expenseOfMonth + " per month.";
    }
}
