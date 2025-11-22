package ie.tus.budget.app;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import ie.tus.budget.model.CardPayment;
import ie.tus.budget.model.CashPayment;
import ie.tus.budget.model.Expense;
import ie.tus.budget.model.FixedExpense;
import ie.tus.budget.model.Money;
import ie.tus.budget.model.PhonePlanExpense;
import ie.tus.budget.model.RentExpense;
import ie.tus.budget.model.enums.Category;
import ie.tus.budget.service.ExpenseBook;
import ie.tus.budget.service.MonthBudget;
import ie.tus.budget.service.ReportService;

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
		book.addExpense("shopping in Lidl", 32.15, Category.FOOD, LocalDate.of(2025, 11, 12));
		
		//add batch
		List<Expense> expenses = new ArrayList<Expense>(2);
		
		Money haircutMoney = Money.MoneyFromDouble(15, "EUR");
		var haircut = new Expense(
                "Haircut",
                haircutMoney,
                Category.PERSONAL_CARE,
                LocalDate.of(2025, 11, 28),
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
		
		YearMonth month = YearMonth.of(2025, 11);
		
		//fixed expense
		var rentExpense = new RentExpense("Single-room Rent", Money.MoneyFromDouble(500, "EUR"), 30, 
				"willow park Athlone Co.Westmeath Ireland");
		var phonePlanExpense = new PhonePlanExpense("Phone Plan", Money.MoneyFromDouble(10.99, "EUR"), 1, "48 Company");
		List<FixedExpense> fixedExpenses = List.of(rentExpense, phonePlanExpense);
		fixedExpenses.forEach(rp -> {
            Expense e = rp.addExpenseForMonth(month,
                    new CardPayment(rp.expenseOfMonth(), "8899"));
            book.addExpense(e);
        });
		
		//report
		//set budget and judge if exceed
		var monthBudget = new MonthBudget(month, Money.MoneyFromDouble(900, "EUR"));
		List<Expense> thisMonthExpenses = book.getAll().stream()
                .filter(e -> YearMonth.from(e.date()).equals(month))
                .toList();
		thisMonthExpenses.forEach(monthBudget::addExpense);
		
		var report = new ReportService();
		
		//budget
		System.out.println(report.buildMonthBudgetReport(monthBudget));
		//month report
        System.out.println(report.buildMonthReport(month, thisMonthExpenses));
        //category summary
        System.out.println(report.buildMonthCategorySummary(month, Category.FOOD, thisMonthExpenses));
        //month insights
        System.out.println(report.buildMonthInsights(month, thisMonthExpenses));
        
        //exception
        try {
        	int i = 0;
        	while(i < 100) {
        		monthBudget.addExpense(new Expense("Changes", Money.MoneyFromDouble(1, "EUR"),
                    Category.OTHER, LocalDate.now(), new CashPayment(Money.MoneyFromDouble(1,"EUR"))));
        	}
        } catch (IllegalStateException ex) {
            System.out.println("exception: " + ex.getMessage());
        }

	}

}
