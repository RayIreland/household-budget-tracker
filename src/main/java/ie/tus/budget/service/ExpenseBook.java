package ie.tus.budget.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import ie.tus.budget.exception.NotFoundException;
import ie.tus.budget.model.CashPayment;
import ie.tus.budget.model.Expense;
import ie.tus.budget.model.Money;
import ie.tus.budget.model.enums.Category;

public class ExpenseBook {

	private List<Expense> expenses = new ArrayList<>();

    public void addExpense(Expense newExpense) {
        expenses.add(newExpense);
    }

    public void addExpense(String title, double amount, Category category, LocalDate date) {
        var money = Money.MoneyFromDouble(amount, "EUR");
        addExpense(new Expense(UUID.randomUUID(), title, money, category, date, new CashPayment(money)));
    }
    
    public void addExpenses(List<Expense> newExpenses) {
    	Objects.requireNonNull(newExpenses, "expenses must not be null");
    	expenses.addAll(newExpenses);
    }
    
    public void addExpenses(Expense... newExpenses) {
        Objects.requireNonNull(newExpenses, "newExpenses must not be null");
        for (var e : newExpenses) {
            if (e == null) continue;
            expenses.add(e);
        }
    }
    
    public List<Expense> find(Predicate<Expense> filter, Boolean asc) {
    	Objects.requireNonNull(filter);
    	Objects.requireNonNull(asc);
        return orderExpensesByDate(expenses.stream().filter(filter).toList(), asc);
    }

    public List<Expense> findByCategory(Category category, Boolean asc) {
    	Objects.requireNonNull(category);
    	Objects.requireNonNull(asc);
        return orderExpensesByDate(find(e -> e.category() == category, asc), asc);
    }

    public List<Expense> getAll(Boolean asc) {
    	Objects.requireNonNull(asc);
        return orderExpensesByDate(List.copyOf(expenses.stream().toList()), asc); 
    }
    
    public List<Expense> findByDateRange(LocalDate start, LocalDate end, Boolean asc) {
    	
        return orderExpensesByDate(expenses.stream()
                .filter(e -> !e.date().isBefore(start) && !e.date().isAfter(end))
                .toList(), asc);
    }
    
    public List<Expense> orderExpensesByDate(List<Expense> expenses, Boolean asc) {
    	List<Expense> results;
    	if(asc) {
    		results = expenses.stream()
            .sorted(Comparator.comparing(Expense::date))
            .toList();
    	}else {
    		results = expenses.stream()
    	            .sorted(Comparator.comparing(Expense::date).reversed())
    	            .toList();
    	}
        return results;
    }
    
    public Expense deleteById(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        int idx = indexOfId(id);
        if (idx < 0) {
            throw new NotFoundException("Expense with id " + id + " not found.");
        }
        return expenses.remove(idx);
    }
    
    public Expense editById(UUID id, UnaryOperator<Expense> editor) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(editor, "editor must not be null");

        int idx = indexOfId(id);
        if (idx < 0) {
            throw new NotFoundException("Expense with id " + id + " not found.");
        }

        Expense origin = expenses.get(idx);
        Expense update = Objects.requireNonNull(editor.apply(origin), "editor returned null");
        if (!origin.id().equals(update.id())) {
        	update = new Expense(origin.id(),
        			update.title(),
        			update.money(),
        			update.category(),
        			update.date(),
        			update.paymentMode());
        }
        expenses.set(idx, update);
        return update;
    }
    
    public boolean deleteExpense(Expense expense) {
        Objects.requireNonNull(expense, "expense must not be null");
        return expenses.remove(expense);
    }
    
    protected int indexOfId(UUID id) {
        for (int i = 0; i < expenses.size(); i++) {
            if (expenses.get(i).id().equals(id)) return i;
        }
        return -1;
    }
    
}
