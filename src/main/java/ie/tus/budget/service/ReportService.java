package ie.tus.budget.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import ie.tus.budget.exception.ExportException;
import ie.tus.budget.model.CardPayment;
import ie.tus.budget.model.CashPayment;
import ie.tus.budget.model.Expense;
import ie.tus.budget.model.PaymentMode;
import ie.tus.budget.model.enums.Category;

public class ReportService {

	/**
	 * map category to icon
	 * @param category
	 * @return
	 */
	public String categoryIcon(Category category) {
		return switch (category) {
        case FOOD          -> "🍔";
        case TRANSPORT     -> "🚌";
        case PERSONAL_CARE -> "🧼";
        case RENT          -> "🏠";
        case UTILITIES     -> "💡";
        case ENTERTAINMENT -> "🎮";
        case GROCERIES     -> "🛒";
        case HEALTH        -> "🏥";
        case COMMUNICATION -> "📱";
        case PASSPORT      -> "🛂";
        case OTHER         -> "🧾";
		};
    }
	
	/**
	 * build month budget report
	 * @param monthBudget
	 * @return
	 */
	public String buildMonthBudgetReport(MonthBudget monthBudget) {
		Objects.requireNonNull(monthBudget, "monthBudget must not be null");
		StringBuilder builder = new StringBuilder();
		builder.append("----- Month Budget (").append(monthBudget.month())
			   .append(") -----").append(System.lineSeparator())
			   .append("The budget of this month: ").append(monthBudget)
			   .append(System.lineSeparator())
			   .append("Total expenses of the month: ").append(monthBudget.total())
			   .append(System.lineSeparator())
			   .append("Remaining budget: ").append(monthBudget.remain())
			   .append(System.lineSeparator())
			   .append("Is over budget? ").append(monthBudget.isOverBudget())
			   .append(System.lineSeparator())
			   .append("-----------------------------------")
			   .append(System.lineSeparator());
		
		return builder.toString();
	}
	
	/**
	 * build month report
	 * list the expenses of the month
	 * @param month
	 * @param expenses
	 * @return
	 */
	public String buildMonthReport(YearMonth month, List<Expense> expenses) {
		Objects.requireNonNull(month, "month must not be null");
        Objects.requireNonNull(expenses, "allExpenses must not be null");
        StringBuilder builder = new StringBuilder();
        builder.append("----- Month Report (").append(month).append(") -----")
        	   .append(System.lineSeparator());

        expenses.forEach(e -> builder.append(categoryIcon(e.category()))
                .append(" ")
                .append(e.title())
                .append(" - ")
                .append(e.money())
                .append(" (")
                .append(e.paymentMode().description())
                .append(")")
                .append(System.lineSeparator()));
        builder.append("-----------------------------------")
        	   .append(System.lineSeparator());
        return builder.toString();
    }
	
	/**
	 * build month category summary
	 * @param month
	 * @param category
	 * @param expenses
	 * @return
	 */
	public String buildMonthCategorySummary(YearMonth month, Category category, List<Expense> expenses) {
		Objects.requireNonNull(month, "month must not be null");
        Objects.requireNonNull(category, "category must not be null");
        Objects.requireNonNull(expenses, "expenses must not be null");
        
        var thisMonthCategoryExpenses = expenses.stream()
                .filter(e -> YearMonth.from(e.date()).equals(month))
                .filter(e -> e.category() == category)
                .toList();
        
        StringBuilder builder = new StringBuilder();
        builder.append("----- Category Summary (")
          	   .append(categoryIcon(category))
	           .append(" ")
	           .append(category.name())
	           .append("_")
	           .append(month)
	           .append(") -----\n");
        
        if (thisMonthCategoryExpenses.isEmpty()) {
        	builder.append("No expenses found for this category in this month.\n");
        	builder.append("----------------------------------------\n");
        	return builder.toString();
        }
        
        double total = thisMonthCategoryExpenses.stream()
                	   .mapToDouble(e -> e.money().amount().doubleValue()).sum();
        String currency = thisMonthCategoryExpenses.get(0).money().currency();
        builder.append("Total spent: ")
        	   .append(String.format("%.2f", total))
        	   .append(" ")
        	   .append(currency)
        	   .append("\n");

        builder.append("Transactions:\n");
        thisMonthCategoryExpenses.forEach(e -> builder.append(
                categoryIcon(e.category()))
                .append(" ")
                .append(e.title())
                .append(" - ")
                .append(String.format("%.2f", e.money().amount()))
                .append(" ")
                .append(e.money().currency())
                .append("\n")
        );
        builder.append("----------------------------------------\n");
        return builder.toString();
	}
	
	/**
	 * build month insights
	 * include highest cost category, payment mode count, 
	 * average daily cost, total of transactions, largest cost
	 * @param month
	 * @param expenses
	 * @return
	 */
	public String buildMonthInsights(YearMonth month, List<Expense> expenses) {
		Objects.requireNonNull(month, "month must not be null");
        Objects.requireNonNull(expenses, "expenses must not be null");
        
        var thisMonthExpenses = expenses.stream()
                .filter(e -> YearMonth.from(e.date()).equals(month))
                .toList();
        
        StringBuilder builder = new StringBuilder();
        builder.append("----- Month Insights (").append(month)
          	   .append(") -----\n");

        if (thisMonthExpenses.isEmpty()) {
        	builder.append("No expense records for this month.\n");
        	builder.append("--------------------------------------\n");
            return builder.toString();
        }
        
        DoubleSummaryStatistics stats = thisMonthExpenses.stream()
                .mapToDouble(e -> e.money().amount().doubleValue()).summaryStatistics();
        
        String currency = thisMonthExpenses.get(0).money().currency();
        
        Map<Category, Double> totalByCategory = 
        		thisMonthExpenses.stream()
				                 .collect(Collectors.groupingBy(
				                          Expense::category,
				                          Collectors.summingDouble(e -> e.money().amount().doubleValue())
				                 ));
        
        var maxCategoryEntry = totalByCategory.entrySet().stream()
                .max(Map.Entry.comparingByValue());
        
        maxCategoryEntry.ifPresent(entry -> builder.append("📌 Highest spending category: ")
                .append(entry.getKey().name())
                .append(" (")
                .append(String.format("%.2f", entry.getValue()))
                .append(" ")
                .append(currency)
                .append(")\n")
        );
        
        Map<String, Long> countByPaymentType = 
        		thisMonthExpenses.stream()
				                 .collect(Collectors.groupingBy(
				                          e -> paymentLabel(e.paymentMode()),
				                          Collectors.counting()
				                  ));
        
        builder.append("💳 Payment methods: ");
        String paymentSummary = countByPaymentType.entrySet().stream()
                .map(en -> en.getKey() + " " + en.getValue() + " time(s)")
                .collect(Collectors.joining(", "));
        builder.append(paymentSummary).append("\n");
        
        int daysInMonth = month.lengthOfMonth();
        double averagePerDay = stats.getSum() / daysInMonth;
        
        builder.append("📅 Average daily cost: ")
		       .append(String.format("%.2f", averagePerDay))
		       .append(" ")
		       .append(currency)
		       .append("\n");
        
        builder.append("🧾 Total transactions: ")
        	   .append(thisMonthExpenses.size())
        	   .append("\n");

        var maxExpense = thisMonthExpenses.stream()
                .max(Comparator.comparingDouble(e -> e.money().amount().doubleValue()));

        maxExpense.ifPresent(e -> builder.append("🔍 Largest single expense: ")
                .append(String.format("%.2f", e.money().amount()))
                .append(" ")
                .append(e.money().currency())
                .append(" (")
                .append(e.title())
                .append(")\n")
        );
        
        builder.append("--------------------------------------\n");
        return builder.toString();
	}
	
	private String paymentLabel(PaymentMode mode) {
        return switch (mode) {
            case CashPayment _ -> "Cash";
            case CardPayment _ -> "Card";
            //TODO 如果以后扩展新支付方式，可以在这里继续加
            default -> "Other";
        };
    }
	
	/**
	 * export report
	 * @param content
	 * @param file
	 */
	public void exportReportToFile(String content, Path file) {
        Objects.requireNonNull(content, "content must not be null");
        Objects.requireNonNull(file, "targetFile must not be null");

        try {
            Path parent = file.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            Files.writeString(file, content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ExportException(
                    "Failed to export report to file: " + file.toAbsolutePath(), e);
        }
    }
	
	/**
	 * build month report
	 * @param month
	 * @param monthBudget
	 * @param monthExpenses
	 * @param targetFile
	 * @return
	 */
	public String buildMonthReport(YearMonth month, MonthBudget monthBudget, List<Expense> monthExpenses,
	          Path targetFile) {
		StringBuilder builder = new StringBuilder();
		//budget report
		builder.append(buildMonthBudgetReport(monthBudget)).append(System.lineSeparator());
		//month report
		builder.append(buildMonthReport(month, monthExpenses)).append(System.lineSeparator());
        //category summary
		builder.append(buildMonthCategorySummary(month, Category.FOOD, monthExpenses)).append(System.lineSeparator());
        //month insights
		builder.append(buildMonthInsights(month, monthExpenses)).append(System.lineSeparator());
		return builder.toString();
	}
	
	/**
	 * export month report
	 * @param month
	 * @param monthBudget
	 * @param monthExpenses
	 * @param file
	 */
	public void exportMonthReport(YearMonth month, MonthBudget monthBudget, List<Expense> monthExpenses,
						          Path file) {
		
		String content = buildMonthReport(month, monthBudget, monthExpenses, file);
		System.out.print(content);
		exportReportToFile(content, file);
	}
}
