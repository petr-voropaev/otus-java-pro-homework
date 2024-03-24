package ru.otus.bank.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.otus.bank.dao.AgreementDao;
import ru.otus.bank.entity.Agreement;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AgreementServiceImplTest {

    private AgreementDao dao = Mockito.mock(AgreementDao.class);

    AgreementServiceImpl agreementServiceImpl;

    @BeforeEach
    public void init() {
        agreementServiceImpl = new AgreementServiceImpl(dao);
    }

    @Test
    public void testFindByName() {
        String name = "test";
        Agreement agreement = new Agreement();
        agreement.setId(10L);
        agreement.setName(name);

        Mockito.when(dao.findByName(name)).thenReturn(
                Optional.of(agreement));

        Optional<Agreement> result = agreementServiceImpl.findByName(name);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(10, agreement.getId());
    }

    @Test
    public void testFindByNameWithCaptor() {
        String name = "test";
        Agreement agreement = new Agreement();
        agreement.setId(10L);
        agreement.setName(name);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        Mockito.when(dao.findByName(captor.capture())).thenReturn(
                Optional.of(agreement));

        Optional<Agreement> result = agreementServiceImpl.findByName(name);

        Assertions.assertEquals("test", captor.getValue());
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(10, agreement.getId());
    }

    @Test
    public void testAddAgreement() {
        Agreement expected = newAgreement();
        when(dao.save(any(Agreement.class))).thenReturn(expected);

        Agreement actual = agreementServiceImpl.addAgreement("agreement");

        verify(dao).save(any(Agreement.class));
        assertEquals(expected, actual);
    }

    private static Agreement newAgreement() {
        Agreement agreement = new Agreement();
        agreement.setId(1L);
        return agreement;
    }
}
