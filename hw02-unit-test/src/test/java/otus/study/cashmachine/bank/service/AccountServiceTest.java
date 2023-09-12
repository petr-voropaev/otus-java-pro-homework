package otus.study.cashmachine.bank.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import otus.study.cashmachine.bank.dao.AccountDao;
import otus.study.cashmachine.bank.data.Account;
import otus.study.cashmachine.bank.service.impl.AccountServiceImpl;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class AccountServiceTest {

    AccountDao accountDao;

    AccountServiceImpl accountServiceImpl;

    @BeforeEach
    void init() {
        accountDao = mock(AccountDao.class);
        accountServiceImpl = new AccountServiceImpl(accountDao);
    }

    @Test
    void createAccountMock() {
        BigDecimal amount = BigDecimal.ONE;
        Account account = new Account(0, amount);

        when(accountDao.saveAccount(any())).thenReturn(account);
        Account actual = accountServiceImpl.createAccount(amount);

        assertEquals(account, actual);
    }

    @Test
    void createAccountCaptor() {
        BigDecimal amount = BigDecimal.ONE;
        Account account = new Account(0, amount);

        accountServiceImpl.createAccount(amount);

        verify(accountDao).saveAccount(account);
    }

    @Test
    void addSum() {
        BigDecimal amount = BigDecimal.ONE;
        Account account = mock(Account.class);

        when(accountDao.getAccount(any())).thenReturn(account);
        when(account.getAmount()).thenReturn(BigDecimal.ONE);

        BigDecimal actual = accountServiceImpl.putMoney(1L, amount);

        assertEquals(account.getAmount(), actual);
    }

    @Test
    void getSum() {
        BigDecimal amount = BigDecimal.ONE;
        Account account = mock(Account.class);

        when(accountDao.getAccount(any())).thenReturn(account);
        when(account.getAmount()).thenReturn(BigDecimal.ONE);

        BigDecimal actual = accountServiceImpl.getMoney(1L, amount);

        assertEquals(account.getAmount(), actual);
    }

    @Test
    void getAccount() {
        Account account = mock(Account.class);

        when(accountDao.getAccount(any())).thenReturn(account);

        Account actual = accountServiceImpl.getAccount(1L);

        assertEquals(account, actual);
    }

    @Test
    void checkBalance() {
        Account account = mock(Account.class);

        when(accountDao.getAccount(any())).thenReturn(account);
        when(account.getAmount()).thenReturn(BigDecimal.ONE);

        BigDecimal actual = accountServiceImpl.checkBalance(1L);

        assertEquals(account.getAmount(), actual);
    }
}
