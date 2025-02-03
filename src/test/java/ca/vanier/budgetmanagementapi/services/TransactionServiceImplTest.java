package ca.vanier.budgetmanagementapi.services;

import ca.vanier.budgetmanagementapi.Exceptions.CategoryNotFoundException;
import ca.vanier.budgetmanagementapi.Exceptions.TransactionNotFoundException;
import ca.vanier.budgetmanagementapi.Exceptions.UserNotFoundException;
import ca.vanier.budgetmanagementapi.entity.Category;
import ca.vanier.budgetmanagementapi.entity.Transaction;
import ca.vanier.budgetmanagementapi.entity.UserCategory;
import ca.vanier.budgetmanagementapi.entity.Users;
import ca.vanier.budgetmanagementapi.repository.CategoryRepository;
import ca.vanier.budgetmanagementapi.repository.TransactionRepository;
import ca.vanier.budgetmanagementapi.repository.UserCategoryRepository;
import ca.vanier.budgetmanagementapi.repository.UserRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.times;

public class TransactionServiceImplTest {

    private TransactionRepository transactionRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private UserCategoryRepository userCategoryRepository;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        userRepository = mock(UserRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        userCategoryRepository = mock(UserCategoryRepository.class);

        transactionService = new TransactionServiceImpl(transactionRepository, userRepository,
                categoryRepository, userCategoryRepository);
    }

    @Test
    public void testSave(){

        Users user = mock(Users.class);
        when(userRepository.findById(3L)).thenReturn(Optional.of(user));
        when(user.getId()).thenReturn(3L);

        Category category = mock(Category.class);
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(category.getId()).thenReturn(2L);
        when(category.getDescription()).thenReturn("Rent");

        Transaction transaction = mock(Transaction.class);
        when(transaction.getUser()).thenReturn(user);
        when(transaction.getCategory()).thenReturn(category);
        when(transaction.isIncome()).thenReturn(false);
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        Transaction actual = transactionService.save(transaction);


        assertThat(actual, is(transaction));
    }

    @Test
    public void testSaveWithUserNonExist(){

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Users user = mock(Users.class);
        when(user.getId()).thenReturn(3L);

        Category category = mock(Category.class);
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(category.getId()).thenReturn(2L);

        Transaction transaction = mock(Transaction.class);
        when(transaction.getUser()).thenReturn(user);
        when(transaction.getCategory()).thenReturn(category);
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        assertThrows(UserNotFoundException.class, () -> transactionService.save(transaction));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    public void testSaveWithCategoryNonExist(){

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        Category category = mock(Category.class);
        when(category.getId()).thenReturn(3L);

        Users user = mock(Users.class);
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(user.getId()).thenReturn(2L);

        Transaction transaction = mock(Transaction.class);
        when(transaction.getUser()).thenReturn(user);
        when(transaction.getCategory()).thenReturn(category);
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        assertThrows(CategoryNotFoundException.class, () -> transactionService.save(transaction));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    public void testSaveWithInsertionToUserCategory(){

        Users user = mock(Users.class);
        when(userRepository.findById(3L)).thenReturn(Optional.of(user));
        when(user.getId()).thenReturn(3L);

        Category category = mock(Category.class);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(category.getId()).thenReturn(1L);
        when(category.getDescription()).thenReturn("income");

        Transaction transaction = mock(Transaction.class);
        when(transaction.getCategory()).thenReturn(category);
        when(transaction.getUser()).thenReturn(user);
        when(transaction.isIncome()).thenReturn(true);

        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(transactionRepository.existsById(transaction.getId())).thenReturn(true);

        UserCategory userCategory = mock(UserCategory.class);
        when(userCategory.getCategory()).thenReturn(category);
        when(userCategory.getUser()).thenReturn(user);


        when(userCategoryRepository.save(userCategory)).thenReturn(userCategory);

        transactionService.save(transaction);
        UserCategory actualUserCategory = userCategoryRepository.save(userCategory);

        verify(userCategoryRepository, timeout(1)).save(userCategory);
        assertThat(actualUserCategory, is(userCategory));
    }

    @Test
    public void testUpdateTransactionNotFound() {

        Transaction existingTransaction = mock(Transaction.class);
        when(existingTransaction.getId()).thenReturn(2L);

        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.update(1L, existingTransaction));
    }


    @Test
    public void testUpdate() {
        Transaction existingTransaction = new Transaction();
        when(transactionRepository.findById(3L)).thenReturn(Optional.of(existingTransaction));
        existingTransaction.setAmount(350.0);

        Transaction transactionDetails = mock(Transaction.class);
        Category category = mock(Category.class);

        when(category.getId()).thenReturn(1L);
        when(transactionDetails.getAmount()).thenReturn(200.0);
        when(transactionDetails.isIncome()).thenReturn(false);
        when(transactionDetails.getCategory()).thenReturn(category);

        Transaction expectedResult = mock(Transaction.class);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(expectedResult);

        Transaction actualResult = transactionService.update(3L, transactionDetails);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());

        Transaction savedTransaction = transactionCaptor.getValue();
        assertThat(savedTransaction.isIncome(), is(true));
        assertThat(savedTransaction.getAmount(), is(equalTo(200.0)));
        assertThat(savedTransaction.getCategory(), is(equalTo(category)));

        assertThat(actualResult, is(equalTo(expectedResult)));


    }

    @Test
    public void testUpdateWithNullCategory() {
        Transaction existingTransaction = new Transaction();
        when(transactionRepository.findById(3L)).thenReturn(Optional.of(existingTransaction));

        Transaction transactionDetails = mock(Transaction.class);
        when(transactionDetails.getCategory()).thenReturn(null);

        transactionService.update(3L, transactionDetails);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction savedTransaction = transactionCaptor.getValue();

        assertThat(savedTransaction.isIncome(), is(false));
    }

    @Test
    public void testUpdateWithDifferentCategory() {
        Transaction existingTransaction = new Transaction();
        when(transactionRepository.findById(3L)).thenReturn(Optional.of(existingTransaction));

        Transaction transactionDetails = mock(Transaction.class);
        Category category = mock(Category.class);
        when(category.getId()).thenReturn(10L);
        when(transactionDetails.getCategory()).thenReturn(category);

        transactionService.update(3L, transactionDetails);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction savedTransaction = transactionCaptor.getValue();

        assertThat(savedTransaction.isIncome(), is(false));
    }

    @Test
    public void testUpdateWithSameCategory() {
        Category category = mock(Category.class);

        Transaction existingTransaction = new Transaction();
        when(transactionRepository.findById(3L)).thenReturn(Optional.of(existingTransaction));
        existingTransaction.setCategory(category);

        Transaction transactionDetails = mock(Transaction.class);

        when(category.getId()).thenReturn(10L);
        when(transactionDetails.getCategory()).thenReturn(category);
        transactionService.update(3L, transactionDetails);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction savedTransaction = transactionCaptor.getValue();

        assertThat(savedTransaction.getCategory(), is(equalTo(category)));
    }

    @Test
    public void testFindById() {
        Transaction transaction = mock(Transaction.class);
        when(transactionRepository.findById(3L)).thenReturn(Optional.of(transaction));

        Optional<Transaction> actual = transactionService.findById(3L);

        assertThat(actual.isPresent(), is(true));
        assertThat(actual.get(), is(equalTo(transaction)));
    }

    @Test
    public void testFindByIdWithNull() {
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Transaction> actual = transactionService.findById(3L);

        assertThat(actual.isPresent(), is(false));
    }

    @Test
    public void testFindExistingByTransactionId() {
        Transaction transaction = mock(Transaction.class);
        when(transactionRepository.findById(3L)).thenReturn(Optional.of(transaction));

        Transaction actual = transactionService.findExistingByTransactionId(3L);

        assertNotNull(actual);
        assertThat(actual, is(equalTo(transaction)));
        verify(transactionRepository, timeout(1)).findById(3L);
    }

    @Test
    public void testFindExistingByTransactionIdWithNonExisting() {
        when(transactionRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.findExistingByTransactionId(3L));
        verify(transactionRepository, timeout(1)).findById(3L);
    }

    @Test
    public void testFindAll(){
        Transaction transaction = mock(Transaction.class);
        Transaction transaction2 = mock(Transaction.class);
        Transaction transaction3 = mock(Transaction.class);

        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction,transaction2,transaction3));

        List<Transaction> transactionList = transactionService.findAll();
        assertThat(transactionList.size(), is(equalTo(3)));
    }

    @Test
    public void testFindAllWithEmpty(){
        when(transactionRepository.findAll()).thenReturn(Collections.emptyList());

        List<Transaction> transactionList = transactionService.findAll();
        assertThat(transactionList.size(), is(equalTo(0)));

    }

    @Test
    public void testDelete() {
        Transaction transaction = mock(Transaction.class);
        when(transactionRepository.findById(3L)).thenReturn(Optional.of(transaction));

        transactionService.delete(3L);

        verify(transactionRepository).delete(transaction);
    }

    @Test
    public void testDeleteWithNonExisting() {
        when(transactionRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.delete(3L));
        verify(transactionRepository, never()).delete(any(Transaction.class));
    }

    @Test
    public void testGetAllTransactionsByUserId() {
        Transaction transaction = mock(Transaction.class);
        Transaction transaction2 = mock(Transaction.class);
        Transaction transaction3 = mock(Transaction.class);

        List<Transaction> transactionList = Arrays.asList(transaction,transaction2,transaction3);

        when(transactionRepository.findAllByUserId(3L)).thenReturn(transactionList);

        List<Transaction> actualTransactionList = transactionService.getAllTransactionsByUserId(3L);

        assertThat(actualTransactionList,is(equalTo(transactionList)));
    }

    @Test
    public void testGetAllTransactionsByUserIdWithEmpty() {
        when(transactionRepository.findAllByUserId(3L)).thenReturn(emptyList());

        List<Transaction> actualTransactionList = transactionService.getAllTransactionsByUserId(3L);

        assertThat(actualTransactionList,is(equalTo(emptyList())));
    }

    @Test
    public void testGetExpensesByUserId() {

        Transaction transaction = mock(Transaction.class);
        Transaction transaction2 = mock(Transaction.class);
        Transaction transaction3 = mock(Transaction.class);
        List<Transaction> transactionList = Arrays.asList(transaction,transaction2,transaction3);

        when(transactionRepository.findAllByUserIdAndCategoryIdNot(3L, 1L)).thenReturn(transactionList);

        List<Transaction> actualTransactionList = transactionService.getExpensesByUserId(3L);

        verify(transactionRepository).findAllByUserIdAndCategoryIdNot(3L, 1L);
        assertThat(actualTransactionList,is(equalTo(transactionList)));
    }

    @Test
    public void testGetIncomesByUserId() {

        Transaction transaction = mock(Transaction.class);
        Transaction transaction2 = mock(Transaction.class);
        Transaction transaction3 = mock(Transaction.class);
        List<Transaction> transactionList = Arrays.asList(transaction,transaction2,transaction3);

        when(transactionRepository.findAllByUserIdAndCategoryId(3L, 1L)).thenReturn(transactionList);

        List<Transaction> actualTransactionList = transactionService.getIncomesByUserId(3L);

        verify(transactionRepository).findAllByUserIdAndCategoryId(3L, 1L);
        assertThat(actualTransactionList,is(equalTo(transactionList)));
    }

    @Test
    public void testGetTotalIncomeByUser() {

        Transaction transaction = mock(Transaction.class);
        when(transaction.getAmount()).thenReturn(12.0);

        Transaction transaction2 = mock(Transaction.class);
        when(transaction2.getAmount()).thenReturn(56.0);

        Transaction transaction3 = mock(Transaction.class);
        when(transaction3.getAmount()).thenReturn(78.5);

        when(transactionRepository.findAllByUserIdAndCategoryId(3L, 1L)).thenReturn(Arrays.asList(transaction, transaction2, transaction3));

        double sum = transactionService.getTotalIncomeByUser(3L);

        assertThat(sum, is(equalTo(12.0 + 56.0 + 78.5)));

    }

    @Test
    public void testGetTotalIncomeByUserWithEmpty() {
        when(transactionRepository.findAllByUserIdAndCategoryId(3L,1L)).thenReturn(Collections.emptyList());

        double actual = transactionService.getTotalIncomeByUser(3L);

        assertThat(actual, is(equalTo(0.0)));
    }

    @Test
    public void testGetTotalExpensesByUser(){
        Transaction transaction = mock(Transaction.class);
        when(transaction.getAmount()).thenReturn(12.0);

        Transaction transaction2 = mock(Transaction.class);
        when(transaction2.getAmount()).thenReturn(36.0);

        Transaction transaction3 = mock(Transaction.class);
        when(transaction3.getAmount()).thenReturn(125.75);

        when(transactionRepository.findAllByUserIdAndCategoryIdNot(3L,1L)).thenReturn(Arrays.asList(transaction,transaction2,transaction3));

        double actual = transactionService.getTotalExpensesByUser(3L);

        assertThat(actual,is(equalTo(12.0 + 36.0 + 125.75)));

    }

    @Test
    public void testGetTotalExpensesByUserWithEmpty(){

        when(transactionRepository.findAllByUserIdAndCategoryIdNot(3L,1L)).thenReturn(Collections.emptyList());

        double actual = transactionService.getTotalExpensesByUser(3L);

        assertThat(actual,is(equalTo(0.0)));

    }

    @Test
    public void testGetUserBalance(){
        Transaction income = mock(Transaction.class);
        when(income.getAmount()).thenReturn(36.0);

        Transaction income2 = mock(Transaction.class);
        when(income2.getAmount()).thenReturn(845.0);

        Transaction income3 = mock(Transaction.class);
        when(income3.getAmount()).thenReturn(7500.0);


        when(transactionRepository.findAllByUserIdAndCategoryId(3L,1L)).thenReturn(Arrays.asList(income,income2,income3));

        Transaction expense = mock(Transaction.class);
        when(expense.getAmount()).thenReturn(20.0);

        Transaction expense2 = mock(Transaction.class);
        when(expense2.getAmount()).thenReturn(800.0);

        Transaction expense3 = mock(Transaction.class);
        when(expense3.getAmount()).thenReturn(8500.0);


        when(transactionRepository.findAllByUserIdAndCategoryIdNot(3L,1L)).thenReturn(Arrays.asList(expense,expense2,expense3));

        double actualExpenses = transactionService.getTotalExpensesByUser(3L);
        double actualIncomes = transactionService.getTotalIncomeByUser(3L);

        double actual = transactionService.getUserBalance(3L);

        assertThat(actual,is(equalTo(actualIncomes-actualExpenses)));

    }

    @Test
    public void testGetUserBalanceWithEmptyTransactions(){
        when(transactionRepository.findAllByUserIdAndCategoryId(3L,1L)).thenReturn(Collections.emptyList());

        when(transactionRepository.findAllByUserIdAndCategoryIdNot(3L,1L)).thenReturn(Collections.emptyList());

        double actualExpenses = transactionService.getTotalExpensesByUser(3L);
        double actualIncomes = transactionService.getTotalIncomeByUser(3L);
        double actual = transactionService.getUserBalance(3L);

        assertThat(actual,is(equalTo(0.0)));

    }

    @Test
    public void testGetUserTransactionCategorySummary(){
        Users user = mock(Users.class);
        when(user.getId()).thenReturn(2L);

        Transaction transaction = mock(Transaction.class);
        Transaction transaction2 = mock(Transaction.class);
        Transaction transaction3 = mock(Transaction.class);
        Transaction transaction4 = mock(Transaction.class);
        Transaction transaction5 = mock(Transaction.class);
        Transaction transaction6 = mock(Transaction.class);

        Category category = mock(Category.class);
        Category category1 = mock(Category.class);
        Category category2 = mock(Category.class);
        Category category3 = mock(Category.class);

        when(category.getDescription()).thenReturn("Income");
        when(category1.getDescription()).thenReturn("Entertaiment");
        when(category2.getDescription()).thenReturn("Grocery");
        when(category3.getDescription()).thenReturn("Rent");

        when(transaction.getCategory()).thenReturn(category);
        when(transaction2.getCategory()).thenReturn(category1);
        when(transaction3.getCategory()).thenReturn(category2);
        when(transaction4.getCategory()).thenReturn(category1);
        when(transaction5.getCategory()).thenReturn(category3);
        when(transaction6.getCategory()).thenReturn(category);

        when(transaction.getAmount()).thenReturn(5000.00);
        when(transaction2.getAmount()).thenReturn(300.00);
        when(transaction3.getAmount()).thenReturn(1000.00);
        when(transaction4.getAmount()).thenReturn(75.00);
        when(transaction5.getAmount()).thenReturn(850.00);
        when(transaction6.getAmount()).thenReturn(365.00);

        List<Transaction> transactionList = Arrays.asList(transaction,transaction2,transaction3,transaction4,transaction5,transaction6);

        when(transactionRepository.findAllByUserId(2L)).thenReturn(transactionList);

        Map<String, Double> result = transactionService.getUserTransactionCategorySummary(2L);
        assertNotNull(result);
        assertThat(result, Matchers.hasEntry("Income", 5365.00));
        assertThat(result, Matchers.hasEntry("Rent", 850.00));
        assertThat(result.values(), Matchers.containsInAnyOrder(1000.00,375.00,5365.00,850.00));

    }

    @Test
    public void testGetTransactionsByDateRange() {
        Users user = mock(Users.class);
        when(user.getId()).thenReturn(2L);

        Transaction transaction = mock(Transaction.class);
        Transaction transaction2 = mock(Transaction.class);
        Transaction transaction3 = mock(Transaction.class);
        Transaction transaction4 = mock(Transaction.class);
        Transaction transaction5 = mock(Transaction.class);
        Transaction transaction6 = mock(Transaction.class);

        when(transaction.getCreatedAt()).thenReturn(LocalDateTime.parse("2024-01-10T10:00:00"));
        when(transaction2.getCreatedAt()).thenReturn(LocalDateTime.parse("2024-01-15T15:00:00"));
        when(transaction3.getCreatedAt()).thenReturn(LocalDateTime.parse("2024-01-10T10:00:00"));
        when(transaction4.getCreatedAt()).thenReturn(LocalDateTime.parse("2024-01-15T15:00:00"));
        when(transaction5.getCreatedAt()).thenReturn(LocalDateTime.parse("2024-01-10T10:00:00"));
        when(transaction6.getCreatedAt()).thenReturn(LocalDateTime.parse("2024-01-15T15:00:00"));

        List<Transaction> transactionList = Arrays.asList(transaction,transaction2,transaction3,transaction4,transaction5,transaction6);

        when(transactionRepository.findAllByUserIdAndCreatedAtBetween(2L, LocalDateTime.parse("2024-01-01T00:00:00"),LocalDateTime.parse("2024-01-31T23:59:59"))).thenReturn(transactionList);

        List<Transaction> result = transactionService.getTransactionsByDateRange(2L, LocalDateTime.parse("2024-01-01T00:00:00"),LocalDateTime.parse("2024-01-31T23:59:59"));

        assertThat(result,is(equalTo(transactionList)));
    }

    @Test
    public void testGetTransactionsByDateRangeWithSomeNotInRange() {
        Users user = mock(Users.class);
        when(user.getId()).thenReturn(2L);

        Transaction transaction = mock(Transaction.class);
        Transaction transaction2 = mock(Transaction.class);
        Transaction transaction3 = mock(Transaction.class);
        Transaction transaction4 = mock(Transaction.class);
        Transaction transaction5 = mock(Transaction.class);
        Transaction transaction6 = mock(Transaction.class);

        when(transaction.getCreatedAt()).thenReturn(LocalDateTime.parse("2024-01-10T10:00:00"));
        when(transaction2.getCreatedAt()).thenReturn(LocalDateTime.parse("2024-01-15T15:00:00"));
        when(transaction3.getCreatedAt()).thenReturn(LocalDateTime.parse("2024-01-10T10:00:00"));
        when(transaction4.getCreatedAt()).thenReturn(LocalDateTime.parse("2024-02-15T15:00:00"));
        when(transaction5.getCreatedAt()).thenReturn(LocalDateTime.parse("2024-01-10T10:00:00"));
        when(transaction6.getCreatedAt()).thenReturn(LocalDateTime.parse("2025-01-15T15:00:00"));

        List<Transaction> transactionList = Arrays.asList(transaction,transaction2,transaction3,transaction5);

        when(transactionRepository.findAllByUserIdAndCreatedAtBetween(2L, LocalDateTime.parse("2024-01-01T00:00:00"),LocalDateTime.parse("2024-01-31T23:59:59"))).thenReturn(transactionList);

        List<Transaction> result = transactionService.getTransactionsByDateRange(2L, LocalDateTime.parse("2024-01-01T00:00:00"),LocalDateTime.parse("2024-01-31T23:59:59"));

        assertThat(result,is(equalTo(transactionList)));
    }

    @Test
    public void testGetTransactionsByDateRangeWithNotInRange() {
        Users user = mock(Users.class);
        when(user.getId()).thenReturn(2L);

        Transaction transaction = mock(Transaction.class);
        Transaction transaction2 = mock(Transaction.class);
        Transaction transaction3 = mock(Transaction.class);
        Transaction transaction4 = mock(Transaction.class);
        Transaction transaction5 = mock(Transaction.class);
        Transaction transaction6 = mock(Transaction.class);

        when(transaction.getCreatedAt()).thenReturn(LocalDateTime.parse("2024-01-10T10:00:00"));
        when(transaction2.getCreatedAt()).thenReturn(LocalDateTime.parse("2024-01-15T15:00:00"));
        when(transaction3.getCreatedAt()).thenReturn(LocalDateTime.parse("2024-01-10T10:00:00"));
        when(transaction4.getCreatedAt()).thenReturn(LocalDateTime.parse("2024-02-15T15:00:00"));
        when(transaction5.getCreatedAt()).thenReturn(LocalDateTime.parse("2024-01-10T10:00:00"));
        when(transaction6.getCreatedAt()).thenReturn(LocalDateTime.parse("2024-01-15T15:00:00"));

        List<Transaction> transactionList = Arrays.asList();

        when(transactionRepository.findAllByUserIdAndCreatedAtBetween(2L, LocalDateTime.parse("2025-01-01T00:00:00"),LocalDateTime.parse("2025-01-31T23:59:59"))).thenReturn(transactionList);

        List<Transaction> result = transactionService.getTransactionsByDateRange(2L, LocalDateTime.parse("2025-01-01T00:00:00"),LocalDateTime.parse("2025-01-31T23:59:59"));

        assertThat(result,is(equalTo(transactionList)));
    }

}
