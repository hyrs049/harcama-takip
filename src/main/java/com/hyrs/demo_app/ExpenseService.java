package com.hyrs.demo_app;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public List<Expense> getExpensesBetweenDates(LocalDate startDate, LocalDate endDate) {
        // Eğer startDate > endDate olursa tersini al veya hata fırlatabilirsin; burada basitçe repository çağrısı
        return expenseRepository.findByExpenseDateBetween(startDate, endDate);
    }

   
public Expense addExpense(String description, Double amount, String category, LocalDate date) {
    Expense expense = new Expense();
    expense.setDescription(description);
    expense.setAmount(amount);
    expense.setCategory(category);
    expense.setExpenseDate(date != null ? date : LocalDate.now());
    return expenseRepository.save(expense);
}


    public Optional<Expense> getExpenseById(Long id) {
    return expenseRepository.findById(id);
}
     public Expense save(Expense expense) {
        return expenseRepository.save(expense);
    }
     public Expense updateExpense(Expense expense) {
        return expenseRepository.save(expense);
    }
    public Expense addExpense(Long id, String description, Double amount, String category, LocalDate date) {
        Optional<Expense> opt = getExpenseById(id);
    if (opt.isEmpty()) {
        throw new RuntimeException("Expense not found with id " + id);
    }
        Expense expense = opt.get();
        expense.setDescription(description);
        expense.setAmount(amount);
        expense.setCategory(category);
        expense.setExpenseDate(date != null ? date : expense.getExpenseDate());
        expenseRepository.save(expense);
        return expense;
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    public Double getTotalAmount() {
        return expenseRepository.findAll()
                .stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    public Map<String, Double> getTotalByCategory() {
        return expenseRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Expense::getAmount)
                ));
    }
}
