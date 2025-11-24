package ie.tus.budget.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ie.tus.budget.model.CashPayment;
import ie.tus.budget.model.Expense;
import ie.tus.budget.model.Money;
import ie.tus.budget.model.enums.Category;

class ExpenseBookTest {
	
	private ExpenseBook book;
	
	@BeforeEach
    void setUp() {
        book = new ExpenseBook();
        
        book.addExpense("Burger King", 12, Category.FOOD, LocalDate.of(2025, 10, 5));
        
        Money localLinkMoney = Money.MoneyFromDouble(1, "EUR");
		var localLink = new Expense(
                "transport with A2 localLink",
                localLinkMoney,
                Category.TRANSPORT,
                LocalDate.of(2025, 11, 2),
                new CashPayment(localLinkMoney));
		
		Money drinksMoney = Money.MoneyFromDouble(15, "EUR");
		var drinks = new Expense(
                "drinks at bar",
                drinksMoney,
                Category.ENTERTAINMENT,
                LocalDate.of(2025, 11, 15),
                new CashPayment(drinksMoney));
		
		Money shoppingMoney = Money.MoneyFromDouble(23.4, "EUR");
		var shopping = new Expense(
                "shopping at asian supermarket",
                shoppingMoney,
                Category.GROCERIES,
                LocalDate.of(2025, 11, 20),
                new CashPayment(shoppingMoney));
		
		book.addExpenses(localLink, drinks, shopping);
	}

	@Test
	void testAddExpenseExpense() {
		Money localLinkMoney = Money.MoneyFromDouble(1, "EUR");
		var localLink = new Expense(
                "transport with A2 localLink",
                localLinkMoney,
                Category.TRANSPORT,
                LocalDate.of(2025, 11, 2),
                new CashPayment(localLinkMoney));
		book.addExpense(localLink);
	}

	@Test
	void testAddExpenseStringDoubleCategoryLocalDate() {
		book.addExpense("Burger King", 12, Category.FOOD, LocalDate.of(2025, 11, 5));
	}

	@Test
	void testAddExpenses() {
		fail("Not yet implemented");
	}
	
	@Test
	void testAddExpensesVarargs() {
		Money drinksMoney = Money.MoneyFromDouble(15, "EUR");
		var drinks = new Expense(
                "drinks at bar",
                drinksMoney,
                Category.ENTERTAINMENT,
                LocalDate.of(2025, 11, 15),
                new CashPayment(drinksMoney));
		
		Money shoppingMoney = Money.MoneyFromDouble(23.4, "EUR");
		var shopping = new Expense(
                "shopping at asian supermarket",
                shoppingMoney,
                Category.GROCERIES,
                LocalDate.of(2025, 11, 20),
                new CashPayment(shoppingMoney));
		book.addExpenses(drinks, shopping);
	}

	@Test
	void testFind() {
		
		var monthCondition = YearMonth.of(2025, 10);
        Predicate<Expense> monthPredicate = exp -> YearMonth.from(exp.date()).equals(monthCondition);
		book.find(monthPredicate).forEach(System.out::println);
	}

	@Test
	void testFindByCategory() {
		book.findByCategory(Category.ENTERTAINMENT).forEach(System.out::println);
	}

	@Test
	void testGetAll() {
		book.getAll().forEach(System.out::println);
	}

	@Test
	void testDeleteExpenseByIndex() {
		book.getAll().forEach(System.out::println);
		System.out.println();
		System.out.println("delete: " + book.deleteExpenseByIndex(0));
		System.out.println();
		book.getAll().forEach(System.out::println);
	}

	@Test
	void testDeleteExpense() {
		fail("Not yet implemented");
	}

	@Test
	void testEditExpenseByIndex() {
		book.getAll().forEach(System.out::println);
		System.out.println();
		UnaryOperator<Expense> editor = old -> new Expense(
              "shopping in tesco",
              Money.MoneyFromDouble(old.money().amount().doubleValue() + 20, old.money().currency()),
              old.category(),
              old.date(),
              old.paymentMode()
        );
		var editedByIndex = book.editExpenseByIndex(0, editor);
		System.out.println("edit: " + editedByIndex);
		System.out.println();
		book.getAll().forEach(System.out::println);
	}

}
