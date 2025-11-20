package ie.tus.budget.app;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ie.tus.budget.model.CardPayment;
import ie.tus.budget.model.CashPayment;
import ie.tus.budget.model.Expense;
import ie.tus.budget.model.Money;
import ie.tus.budget.model.enums.Category;
import ie.tus.budget.service.ExpenseBook;

public class BudgetApp {

	public static void main(String[] args) {


		var book = new ExpenseBook();
		//add single
		Money cityLinkMoney = Money.MoneyFromDouble(11, "EUR");
		var cityLink = new Expense(
                "transport with cityLink",
                cityLinkMoney,
                Category.TRANSPORT,
                LocalDate.of(2025, 11, 9),
                new CashPayment(cityLinkMoney));
		book.addExpense(cityLink);
		
		//simple add single
		book.addExpense("shopping in Lidl", 32.15, Category.FOOD, LocalDate.of(2025, 10, 12));
		
		//add batch
		List<Expense> expenses = new ArrayList<Expense>(2);
		
		Money haircutMoney = Money.MoneyFromDouble(15, "EUR");
		var haircut = new Expense(
                "Haircut",
                haircutMoney,
                Category.PERSONAL_CARE,
                LocalDate.of(2025, 10, 28),
                new CashPayment(haircutMoney));
		expenses.add(haircut);
		
		Money lunchMoney = Money.MoneyFromDouble(16, "EUR");
		var lunch = new Expense(
                "Lamb doner at Raytoon",
                lunchMoney,
                Category.FOOD,
                LocalDate.of(2025, 11, 9),
                new CardPayment(lunchMoney,"9988"));
		expenses.add(lunch);
		book.addExpenses(expenses);
		
		
		
	}

}
