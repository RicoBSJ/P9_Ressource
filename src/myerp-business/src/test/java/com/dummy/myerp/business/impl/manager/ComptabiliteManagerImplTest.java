package com.dummy.myerp.business.impl.manager;

import com.dummy.myerp.business.contrat.BusinessProxy;
import com.dummy.myerp.business.impl.TransactionManager;
import com.dummy.myerp.business.impl.manager.ComptabiliteManagerImpl;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.consumer.dao.impl.DaoProxyImpl;
import com.dummy.myerp.consumer.dao.impl.db.dao.ComptabiliteDaoImpl;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ComptabiliteManagerImplTest {

    public ComptabiliteManagerImplTest() {
        super();
    }

    class ComptabiliterManagerTestClass extends ComptabiliteManagerImpl {
        @Override
        protected BusinessProxy getBusinessProxy() {
            return super.getBusinessProxy();
        }

        @Override
        protected DaoProxy getDaoProxy() {
            return super.getDaoProxy();
        }

        @Override
        protected TransactionManager getTransactionManager() {
            return super.getTransactionManager();
        }

        @Override
        protected Validator getConstraintValidator() {
            return super.getConstraintValidator();
        }
    }

    private EcritureComptable vEcritureComptable;

    private ComptabiliteManagerImpl vComptabiliteManager = new ComptabiliteManagerImpl();

    private ComptabiliterManagerTestClass vComptabiliteManagerMock;
    private ComptabiliterManagerTestClass vComptabiliteManagerSpy;

    private ComptabiliteDaoImpl vComptabiliteDaoMock;
    private ComptabiliteDaoImpl vComptabiliteDaoSpy;

    private DaoProxyImpl vDaoProxyMock;
    private DaoProxyImpl vDaoProxySpy;

    @BeforeEach
    public void setUpBeforeEach() {
        vComptabiliteManagerMock = mock(ComptabiliterManagerTestClass.class);
        vComptabiliteManagerSpy = spy(vComptabiliteManagerMock);

        vComptabiliteDaoMock = mock(ComptabiliteDaoImpl.class);
        vComptabiliteDaoSpy = spy(vComptabiliteDaoMock);

        vDaoProxyMock = mock(DaoProxyImpl.class);
        vDaoProxySpy = spy(vDaoProxyMock);

        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(1);
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.setReference("AC-2019/00001");
    }

    @Test
    public void checkEcritureComptable() throws NotFoundException {
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(123)));

        doReturn(vDaoProxyMock).when(vComptabiliteManagerSpy).getDaoProxy();
        doReturn(vComptabiliteDaoSpy).when(vDaoProxySpy).getComptabiliteDao();
        doReturn(null).when(vComptabiliteDaoSpy).getEcritureComptableByRef(anyString());

        assertDoesNotThrow(() -> vComptabiliteManagerSpy.checkEcritureComptable(vEcritureComptable));
    }

    @Test
    public void checkEcritureComptableWithExistingRefWithoutId() throws FunctionalException, NotFoundException {
        EcritureComptable vEcritureComptableSample = new EcritureComptable();
        vEcritureComptableSample.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptableSample.setDate(new Date());
        vEcritureComptableSample.setLibelle("Libelle");
        vEcritureComptableSample.setReference("AC-2019/00001");

        assertThrows(FunctionalException.class, () -> vComptabiliteManager.checkEcritureComptable(vEcritureComptableSample));
    }

    @Test
    public void checkEcritureComptableContextWithoutSameId() throws FunctionalException, NotFoundException {
        EcritureComptable vEcritureComptableSample = new EcritureComptable();
        vEcritureComptableSample.setId(2);
        vEcritureComptableSample.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptableSample.setDate(new Date());
        vEcritureComptableSample.setLibelle("Libelle");
        vEcritureComptableSample.setReference("AC-2019/00001");

        assertThrows(FunctionalException.class, () -> vComptabiliteManager.checkEcritureComptable(vEcritureComptableSample));
    }

    @Test
    public void checkEcritureComptableUnitViolation() throws Exception {
        assertThrows(FunctionalException.class, () -> vComptabiliteManager.checkEcritureComptableUnit(vEcritureComptable));
    }

    @Test
    public void checkEcritureComptableUnitRG2() throws Exception {
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                null, null,
                new BigDecimal(1234)));

        assertThrows(FunctionalException.class, () -> vComptabiliteManager.checkEcritureComptableUnit(vEcritureComptable));
    }

    @Test
    public void checkEcritureComptableUnitRG3() throws Exception {
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));

        assertThrows(FunctionalException.class, () -> vComptabiliteManager.checkEcritureComptableUnit(vEcritureComptable));
    }

    @Test
    public void checkEcritureComptableUnitRG3_NombreLignes() throws Exception {
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                null, new BigDecimal(123),
                null));

        assertThrows(FunctionalException.class, () -> vComptabiliteManager.checkEcritureComptableUnit(vEcritureComptable));
    }

    @Test
    public void checkEcritureComptableUnitRG5WithWrongYear() throws Exception {
        vEcritureComptable.setReference("AAC-2018/00001");

        vComptabiliteManager.addReference(vEcritureComptable);

        assertThrows(FunctionalException.class, () -> vComptabiliteManager.checkEcritureComptableUnit(vEcritureComptable));
    }

    @Test
    public void checkEcritureComptableUnitRG5WithWrongCode() throws Exception {
        vEcritureComptable.setReference("AAC-2019/00001");

        vComptabiliteManager.addReference(vEcritureComptable);

        assertThrows(FunctionalException.class, () -> vComptabiliteManager.checkEcritureComptableUnit(vEcritureComptable));
    }

    @Test
    public void checkAddReferenceAsFirst() throws Exception {
        vEcritureComptable.setReference(null);

        vComptabiliteManager.addReference(vEcritureComptable);

        assertEquals("AC-2019/00001", vEcritureComptable.getReference());
    }

    @Test
    public void checkAddReferenceAsNew() throws Exception {
        EcritureComptable vNewEcritureComptable = new EcritureComptable();
        vNewEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vNewEcritureComptable.setDate(new Date());
        vNewEcritureComptable.setLibelle("Libelle");

        vComptabiliteManager.addReference(vNewEcritureComptable);

        assertEquals("AC-2019/00001", vNewEcritureComptable.getReference());
    }
}
