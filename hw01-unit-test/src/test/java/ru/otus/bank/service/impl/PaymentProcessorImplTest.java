package ru.otus.bank.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.bank.entity.Account;
import ru.otus.bank.entity.Agreement;
import ru.otus.bank.service.AccountService;
import ru.otus.bank.service.exception.AccountException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentProcessorImplTest {

    @Mock
    AccountService accountService;

    @InjectMocks
    PaymentProcessorImpl paymentProcessor;

    @Test
    public void testTransfer() {
        Agreement sourceAgreement = new Agreement();
        sourceAgreement.setId(1L);

        Agreement destinationAgreement = new Agreement();
        destinationAgreement.setId(2L);

        Account sourceAccount = new Account();
        sourceAccount.setAmount(BigDecimal.TEN);
        sourceAccount.setType(0);

        Account destinationAccount = new Account();
        destinationAccount.setAmount(BigDecimal.ZERO);
        destinationAccount.setType(0);

        when(accountService.getAccounts(argThat(new ArgumentMatcher<Agreement>() {
            @Override
            public boolean matches(Agreement argument) {
                return argument != null && argument.getId() == 1L;
            }
        }))).thenReturn(List.of(sourceAccount));

        when(accountService.getAccounts(argThat(new ArgumentMatcher<Agreement>() {
            @Override
            public boolean matches(Agreement argument) {
                return argument != null && argument.getId() == 2L;
            }
        }))).thenReturn(List.of(destinationAccount));

        paymentProcessor.makeTransfer(sourceAgreement, destinationAgreement,
                0, 0, BigDecimal.ONE);

    }

    @Test
    public void testMakeTransferWithComission() {
        Agreement sourceAgreement = new Agreement();
        sourceAgreement.setId(1L);

        Agreement destinationAgreement = new Agreement();
        destinationAgreement.setId(2L);

        Account sourceAccount = new Account();
        sourceAccount.setAmount(BigDecimal.TEN);
        sourceAccount.setType(0);

        Account destinationAccount = new Account();
        destinationAccount.setAmount(BigDecimal.ZERO);
        destinationAccount.setType(0);

        when(accountService.getAccounts(argThat(argument -> argument != null && argument.getId() == 1L))).thenReturn(List.of(sourceAccount));
        when(accountService.getAccounts(argThat(argument -> argument != null && argument.getId() == 2L))).thenReturn(List.of(destinationAccount));
        when(accountService.charge(eq(sourceAccount.getId()), any(BigDecimal.class))).thenReturn(true);

        paymentProcessor.makeTransferWithComission(sourceAgreement, destinationAgreement, 0, 0, BigDecimal.ONE, BigDecimal.ONE);

        verify(accountService).makeTransfer(eq(sourceAccount.getId()), eq(destinationAccount.getId()), any(BigDecimal.class));
    }

    @Test
    public void testMakeTransferWithComission_exception() {
        Agreement sourceAgreement = new Agreement();
        sourceAgreement.setId(1L);

        Agreement destinationAgreement = new Agreement();
        destinationAgreement.setId(2L);

        Account sourceAccount = new Account();
        sourceAccount.setAmount(BigDecimal.TEN);
        sourceAccount.setType(0);

        when(accountService.getAccounts(argThat(argument -> argument != null && argument.getId() == 1L))).thenReturn(Collections.emptyList());
        AccountException exception = assertThrows(AccountException.class, () -> paymentProcessor.makeTransferWithComission(sourceAgreement, destinationAgreement, 0, 0, BigDecimal.ONE, BigDecimal.ONE));
        assertEquals("Account not found", exception.getMessage());

        when(accountService.getAccounts(argThat(argument -> argument != null && argument.getId() == 1L))).thenReturn(List.of(sourceAccount));
        when(accountService.getAccounts(argThat(argument -> argument != null && argument.getId() == 2L))).thenReturn(Collections.emptyList());
        exception = assertThrows(AccountException.class, () -> paymentProcessor.makeTransferWithComission(sourceAgreement, destinationAgreement, 0, 0, BigDecimal.ONE, BigDecimal.ONE));
        assertEquals("Account not found", exception.getMessage());
    }
}
