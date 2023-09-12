package otus.study.cashmachine.machine.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import otus.study.cashmachine.TestUtil;
import otus.study.cashmachine.bank.dao.CardsDao;
import otus.study.cashmachine.bank.data.Card;
import otus.study.cashmachine.bank.service.AccountService;
import otus.study.cashmachine.bank.service.impl.CardServiceImpl;
import otus.study.cashmachine.machine.data.CashMachine;
import otus.study.cashmachine.machine.data.MoneyBox;
import otus.study.cashmachine.machine.service.impl.CashMachineServiceImpl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CashMachineServiceTest {

    @Captor
    ArgumentCaptor<Card> argumentCaptorCard;
    @Spy
    @InjectMocks
    private CardServiceImpl cardService;
    @Mock
    private CardsDao cardsDao;
    @Mock
    private AccountService accountService;
    @Mock
    private MoneyBoxService moneyBoxService;
    private CashMachineServiceImpl cashMachineService;
    private CashMachine cashMachine = new CashMachine(new MoneyBox());

    @BeforeEach
    void init() {
        cashMachineService = new CashMachineServiceImpl(cardService, accountService, moneyBoxService);
    }


    @Test
    void getMoney() {
        String cardNum = "1";
        String pin = "0";
        BigDecimal sum = BigDecimal.ONE;

        doReturn(sum).when(cardService).getMoney(cardNum, pin, sum);
        when(moneyBoxService.getMoney(cashMachine.getMoneyBox(), sum.intValue())).thenReturn(List.of(1, 2));

        List<Integer> actual = cashMachineService.getMoney(cashMachine, cardNum, pin, sum);

        assertEquals(List.of(1, 2), actual);

        when(moneyBoxService.getMoney(cashMachine.getMoneyBox(), sum.intValue())).thenThrow(new IllegalStateException());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> cashMachineService.getMoney(cashMachine, cardNum, pin, sum));
        verify(cardService).putMoney(any(), any(), any());
        assertEquals("No card found", exception.getMessage());
    }

    @Test
    void putMoney() {
        String cardNum = "1";
        String pin = "0";
        BigDecimal sum = new BigDecimal(6000);

        Card card = new Card(1L, cardNum, 10L, TestUtil.getHash(pin));

        when(cardsDao.getCardByNumber(any())).thenReturn(card);
        verify(moneyBoxService, never()).putMoney(any(MoneyBox.class), anyInt(), anyInt(), anyInt(), anyInt());
        when(cardService.putMoney(cardNum, pin, sum)).thenReturn(sum);

        BigDecimal actual = cashMachineService.putMoney(cashMachine, cardNum, pin, Arrays.asList(1, 1, 0, 0));
        assertEquals(sum, actual);
    }

    @Test
    void checkBalance() {
        String cardNum = "1";
        String pin = "0";
        BigDecimal sum = BigDecimal.ONE;

        Card card = new Card(1L, cardNum, 10L, TestUtil.getHash(pin));
        when(accountService.checkBalance(any())).thenReturn(sum);

        when(cardsDao.getCardByNumber(any())).thenReturn(card);
        BigDecimal actual = cashMachineService.checkBalance(cashMachine, cardNum, pin);
        assertEquals(sum, actual);

        card.setPinCode("0");
        when(cardsDao.getCardByNumber(any())).thenReturn(card);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cashMachineService.checkBalance(cashMachine, cardNum, pin));
        assertEquals("Pincode is incorrect", exception.getMessage());

        when(cardsDao.getCardByNumber(any())).thenReturn(null);
        exception = assertThrows(IllegalArgumentException.class, () -> cashMachineService.checkBalance(cashMachine, cardNum, pin));
        assertEquals("No card found", exception.getMessage());
    }

    @Test
    void changePin() {
        String cardNum = "1";
        String oldPin = "0";
        String newPin = "1";

        Card card = new Card(1L, cardNum, 10L, TestUtil.getHash(oldPin));
        when(cardsDao.getCardByNumber(any())).thenReturn(card);

        boolean actual = cashMachineService.changePin(cardNum, oldPin, newPin);
        verify(cardsDao).saveCard(argumentCaptorCard.capture());
        assertEquals(TestUtil.getHash(newPin), argumentCaptorCard.getValue().getPinCode());
        assertTrue(actual);

        card.setPinCode(oldPin);
        when(cardsDao.getCardByNumber(any())).thenReturn(card);
        actual = cashMachineService.changePin(cardNum, oldPin, newPin);
        assertFalse(actual);
    }

    @Test
    void changePinWithAnswer() {
        String cardNum = "1";
        String oldPin = "0";
        String newPin = "1";

        when(cardsDao.getCardByNumber(any())).thenAnswer(invocation -> null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> cashMachineService.changePin(cardNum, oldPin, newPin));
        assertEquals("No card found", exception.getMessage());
    }
}