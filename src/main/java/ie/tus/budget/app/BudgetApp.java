package ie.tus.budget.app;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import ie.tus.budget.exception.ExportException;
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
		var cityLink = Expense.build(
                "transport with cityLink",
                cityLinkMoney,
                Category.TRANSPORT,
                LocalDate.of(2025, 10, 9),
                new CashPayment(cityLinkMoney));
		book.addExpense(cityLink);
		
		//simple add single
		book.addExpense("shopping in Lidl", 32.15, Category.FOOD, LocalDate.of(2025, 11, 12));
		
		//add varargs
		Money haircutMoney = Money.MoneyFromDouble(15, "EUR");
		var haircut = Expense.build(
                "Haircut",
                haircutMoney,
                Category.PERSONAL_CARE,
                LocalDate.of(2025, 11, 28),
                new CashPayment(haircutMoney));
		
		Money lunchMoney = Money.MoneyFromDouble(16, "EUR");
		var lunch = Expense.build(
                "Lamb doner at Raytoon",
                lunchMoney,
                Category.FOOD,
                LocalDate.of(2025, 11, 9),
                new CardPayment(lunchMoney,"9988"));
		book.addExpenses(haircut, lunch);
		
		YearMonth month = YearMonth.of(2025, 11);
		
		//add fixed expense
		var rentExpense = new RentExpense("Single-room Rent", Money.MoneyFromDouble(500, "EUR"), 30, 
				"willow park Athlone Co.Westmeath Ireland");
		var phonePlanExpense = new PhonePlanExpense("Phone Plan", Money.MoneyFromDouble(10.99, "EUR"), 1, "My48");
		List<FixedExpense> fixedExpenses = List.of(rentExpense, phonePlanExpense);
		fixedExpenses.forEach(fe -> {
            Expense e = fe.addExpenseForMonth(month, new CardPayment(fe.expenseOfMonth(), "8899"));
            book.addExpense(e);
        });
		
		//delete
		//see unitTest
		
		//edit
		//see unitTest
		
		//query all
		System.out.println("----- get all -----");
		book.getAll(true).forEach(System.out::println);
		System.out.println();
		
		//query by category
		System.out.println("----- query by category -----");
        var transportExp = book.findByCategory(Category.TRANSPORT, true);
        transportExp.forEach(System.out::println);
        System.out.println();
        
        //query by month
        System.out.println("----- query by month -----");
        var monthCondition = YearMonth.of(2025, 11);
        Predicate<Expense> monthPredicate = exp -> YearMonth.from(exp.date()).equals(monthCondition);
        var thisMonthExp = book.find(monthPredicate, false);
        thisMonthExp.forEach(System.out::println);
        System.out.println();
        
        //query by period
        System.out.println("----- query by period -----");
        LocalDate start = LocalDate.of(2025, 11, 1);
        LocalDate end   = LocalDate.of(2025, 11, 15);
        List<Expense> thisPeriodExp = book.findByDateRange(start, end, false);
        thisPeriodExp.forEach(System.out::println);
        System.out.println();
		
		//set budget
		var monthBudget = new MonthBudget(month, Money.MoneyFromDouble(900, "EUR"));
		
		List<Expense> thisMonthExpenses = book.getAll(true).stream()
                .filter(e -> YearMonth.from(e.date()).equals(month))
                .toList();
		thisMonthExpenses.forEach(monthBudget::addExpense);
		
		//exception
        try {
        	int i = 0;
        	while(i < 100) {
        		monthBudget.addExpense(Expense.build("Changes", Money.MoneyFromDouble(1, "EUR"),
                    Category.OTHER, LocalDate.now(), new CashPayment(Money.MoneyFromDouble(1,"EUR"))));
        	}
        } catch (IllegalStateException e) {
            System.out.println("exception: " + e.getMessage());
            System.out.println();
        }
		
        //report
		var reportService = new ReportService();
		Path path = Path.of("reports", "report_" + month + ".txt");
        
        try {
        	reportService.exportMonthReport(month, monthBudget, thisMonthExpenses, path);
            System.out.println("Report exported to: " + path.toAbsolutePath());
        } catch (ExportException e) {
            System.err.println("Export failed: " + e.getMessage());
        }

	}

}
