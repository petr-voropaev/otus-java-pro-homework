package ru.otus.bank.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.bank.dao.AccountDao;
import ru.otus.bank.entity.Account;
import ru.otus.bank.entity.Agreement;
import ru.otus.bank.service.exception.AccountException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {
    @Mock
    AccountDao accountDao;

    @InjectMocks
    AccountServiceImpl accountServiceImpl;

    @Test
    public void testTransfer() {
        Account sourceAccount = new Account();
        sourceAccount.setAmount(new BigDecimal(100));

        Account destinationAccount = new Account();
        destinationAccount.setAmount(new BigDecimal(10));

        when(accountDao.findById(eq(1L))).thenReturn(Optional.of(sourceAccount));
        when(accountDao.findById(eq(2L))).thenReturn(Optional.of(destinationAccount));

        accountServiceImpl.makeTransfer(1L, 2L, new BigDecimal(10));

        assertEquals(new BigDecimal(90), sourceAccount.getAmount());
        assertEquals(new BigDecimal(20), destinationAccount.getAmount());
    }

    @Test
    public void testSourceNotFound() {
        when(accountDao.findById(any())).thenReturn(Optional.empty());

        AccountException result = assertThrows(AccountException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                accountServiceImpl.makeTransfer(1L, 2L, new BigDecimal(10));
            }
        });
        assertEquals("No source account", result.getLocalizedMessage());
    }


    @Test
    public void testTransferWithVerify() {
        Account sourceAccount = new Account();
        sourceAccount.setAmount(new BigDecimal(100));
        sourceAccount.setId(1L);

        Account destinationAccount = new Account();
        destinationAccount.setAmount(new BigDecimal(10));
        destinationAccount.setId(2L);

        when(accountDao.findById(eq(1L))).thenReturn(Optional.of(sourceAccount));
        when(accountDao.findById(eq(2L))).thenReturn(Optional.of(destinationAccount));

        ArgumentMatcher<Account> sourceMatcher =
                argument -> argument.getId().equals(1L) && argument.getAmount().equals(new BigDecimal(90));

        ArgumentMatcher<Account> destinationMatcher =
                argument -> argument.getId().equals(2L) && argument.getAmount().equals(new BigDecimal(20));

        accountServiceImpl.makeTransfer(1L, 2L, new BigDecimal(10));

        verify(accountDao).save(argThat(sourceMatcher));
        verify(accountDao).save(argThat(destinationMatcher));
        }

    @Test
    public void testAddAccount() {
        Agreement agreement = newAgreement();
        Account expected = newAccount();
        when(accountDao.save(any(Account.class))).thenReturn(expected);

        Account actual = accountServiceImpl.addAccount(agreement, "number", 1, BigDecimal.ONE);

        verify(accountDao).save(any(Account.class));
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAccounts() {
        Account account = newAccount();
        when(accountDao.findAll()).thenReturn(Collections.singletonList(account));

        List<Account> actual = accountServiceImpl.getAccounts();

        List<Account> expected = Collections.singletonList(account);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAccounts_agreement() {
        Agreement agreement = newAgreement();
        Account account = newAccount();
        when(accountDao.findByAgreementId(agreement.getId())).thenReturn(Collections.singletonList(account));

        List<Account> actual = accountServiceImpl.getAccounts(agreement);

        List<Account> expected = Collections.singletonList(account);
        assertEquals(expected, actual);
    }

    @Test
    public void testCharge() {
        Account account = newAccount();
        when(accountDao.findById(eq(1L))).thenReturn(Optional.of(account));
        when(accountDao.save(any(Account.class))).thenReturn(account);

        boolean actual = accountServiceImpl.charge(1L, BigDecimal.ONE);

        verify(accountDao).save(any(Account.class));
        assertTrue(actual);
    }

    @Test
    public void testCharge_exception() {
        when(accountDao.findById(eq(1L))).thenReturn(Optional.empty());

        AccountException exception = assertThrows(AccountException.class, () -> accountServiceImpl.charge(1L, BigDecimal.ONE));
        assertEquals("No source account", exception.getMessage());
        verify(accountDao, never()).save(any(Account.class));
    }

    private static Account newAccount() {
        Account account = new Account();
        account.setAmount(BigDecimal.ONE);
        account.setId(1L);
        account.setAgreementId(1L);
        account.setType(1);
        account.setNumber("number");
        return account;
    }

    private static Agreement newAgreement() {
        Agreement agreement = new Agreement();
        agreement.setId(1L);
        return agreement;
    }
}
