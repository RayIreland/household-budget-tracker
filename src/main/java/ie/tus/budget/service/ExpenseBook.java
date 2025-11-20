package ie.tus.budget.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import ie.tus.budget.model.CashPayment;
import ie.tus.budget.model.Expense;
import ie.tus.budget.model.Money;
import ie.tus.budget.model.enums.Category;

public class ExpenseBook {

	private final List<Expense> expenses = new ArrayList<>();

    public void addExpense(Expense e) {
        expenses.add(e);
    }

    public void addExpense(String title, double amount, Category category, LocalDate date) {
        var money = Money.MoneyFromDouble(amount, "EUR");
        addExpense(new Expense(title, money, category, date, new CashPayment(money)));
    }
    
    public void addExpenses(List<Expense> expenses) {
    	expenses.addAll(expenses);
    }
    
    public List<Expense> find(Predicate<Expense> filter) {
        return expenses.stream().filter(filter).toList();
    }

    public List<Expense> findByCategory(Category category) {
        return find(e -> e.category() == category);
    }

    public List<Expense> getAll() {
        return List.copyOf(expenses); // defensive copy
    }
}
