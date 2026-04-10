package com.hyrs.demo_app;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Expense> expenses = expenseService.getAllExpenses();
        populateModel(model, expenses);
        return "index";
    }

    @GetMapping("/filter")
    public String filterExpenses(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        List<Expense> expenses;
        if (startDate != null && endDate != null) {
            expenses = expenseService.getExpensesBetweenDates(startDate, endDate);
        } else {
            expenses = expenseService.getAllExpenses();
        }

        populateModel(model, expenses);
        return "index";
    }

    @PostMapping("/add")
    public String addExpense(
            @RequestParam String description,
            @RequestParam Double amount,
            @RequestParam String category,
            @RequestParam(required = false, name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        expenseService.addExpense(description, amount, category, date);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return "redirect:/";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Optional<Expense> opt = expenseService.getExpenseById(id);
        if (opt.isEmpty()) {
    return "redirect:/"; // veya hata sayfası
}
     Expense expense = opt.get();
        model.addAttribute("expense", expense);
        model.addAttribute("categories", List.of("Yemek","Ulaşım","Eğitim","Eğlence","Sağlık","Diğer"));
        return "update";
    }

 @PostMapping("/update/{id}")
public String updateExpense(@PathVariable Long id,
                            @ModelAttribute Expense expense,
                            BindingResult result,
                            Model model) {
    if (result.hasErrors()) {
        // Eğer formda hata varsa tekrar update sayfasına dön
        model.addAttribute("expense", expense);
        model.addAttribute("categories", List.of("Yemek","Ulaşım","Eğitim","Eğlence","Sağlık","Diğer"));
        return "update";
    }

    // Güncellenecek kaydı bul
    Optional<Expense> optExisting= expenseService.getExpenseById(id);
    if(optExisting.isEmpty()){
        return "redirect:/";
    }

    // Alanları güncelle
    Expense existingExpense = optExisting.get();
    existingExpense.setDescription(expense.getDescription());
    existingExpense.setAmount(expense.getAmount());
    existingExpense.setCategory(expense.getCategory());
    existingExpense.setExpenseDate(expense.getExpenseDate());

    // Servis üzerinden kaydet
    expenseService.updateExpense(existingExpense);

    // Ana sayfaya yönlendir
    return "redirect:/";
}

  private void populateModel(Model model, List<Expense> expenses) {
    double total = expenses.stream()
            .mapToDouble(Expense::getAmount)
            .sum();

    Map<String, Double> categoryTotals = expenses.stream()
            .collect(Collectors.groupingBy(
                    Expense::getCategory,
                    Collectors.summingDouble(Expense::getAmount)
            ));

    LinkedHashMap<String, Double> sortedCategoryTotals = categoryTotals.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue(Comparator.reverseOrder()))
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (e1, e2) -> e1,
                    LinkedHashMap::new
            ));

    List<String> palette = List.of("#6c63ff","#ff6384","#36a2eb","#ffcd56","#4caf50","#9c27b0");

    // Burada sadece expenses parametresini kullanıyoruz, tekrar override etmiyoruz!
    model.addAttribute("expenses", expenses);
    model.addAttribute("total", total);
    model.addAttribute("categoryTotals", sortedCategoryTotals);
    model.addAttribute("labels", new ArrayList<>(sortedCategoryTotals.keySet()));
    model.addAttribute("values", new ArrayList<>(sortedCategoryTotals.values()));
    model.addAttribute("palette", palette);
}

  
   
}
