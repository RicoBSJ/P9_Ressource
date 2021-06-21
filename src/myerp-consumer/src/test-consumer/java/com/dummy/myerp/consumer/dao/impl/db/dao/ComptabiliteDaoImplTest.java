package com.dummy.myerp.consumer.dao.impl.db.dao;

import com.dummy.myerp.consumer.dao.impl.DaoProxyImpl;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.NotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ComptabiliteDaoImplTest extends ConsumerHelperTest {

    private static DaoProxyImpl daoProxy;

    private ComptabiliteDaoImpl comptabiliteDao;

    private EcritureComptable ecritureComptableSample;

    @BeforeEach
    public void setUp() {
        daoProxy = (DaoProxyImpl) ConsumerHelperTest.getBean("DaoProxy");
        comptabiliteDao = (ComptabiliteDaoImpl) daoProxy.getComptabiliteDao();

        ecritureComptableSample = new EcritureComptable();
        ecritureComptableSample.setJournal(new JournalComptable("AC", "Achat"));
        ecritureComptableSample.setDate(Date.from(Instant.now()));
        ecritureComptableSample.setLibelle("Libelle");
        ecritureComptableSample.setReference("AC-2019/00001");
    }

    @AfterEach
    public void reInit() {
        ecritureComptableSample = null;
    }


    @Test
    @Order(1)
    void getListCompteComptableTest() {
        List<CompteComptable> compteComptableList;

        compteComptableList = comptabiliteDao.getListCompteComptable();

        assertEquals(7, compteComptableList.size());
    }

    @Test
    @Order(1)
    void getListJournalComptableTest() {
        List<JournalComptable> journalComptableList;

        journalComptableList = comptabiliteDao.getListJournalComptable();

        assertEquals(4, journalComptableList.size());
    }

    @Test
    @Order(2)
    void getListEcritureComptableTest() {
        List<EcritureComptable>  ecritureComptableList = comptabiliteDao.getListEcritureComptable();

        assertEquals(5, ecritureComptableList.size());
    }

    @Test
    @Order(3)
    void getEcritureComptableTest() throws NotFoundException {
        EcritureComptable ecritureComptableSample;

        ecritureComptableSample = comptabiliteDao.getEcritureComptable(-1);

        assertEquals("AC-2016/00001", ecritureComptableSample.getReference());
    }

    @Test
    @Order(4)
    void getEcritureComptableByRefTest() throws NotFoundException {
        EcritureComptable ecritureComptableSample;

        ecritureComptableSample = comptabiliteDao.getEcritureComptableByRef("BQ-2016/00003");

        assertEquals("Paiement Facture F110001", ecritureComptableSample.getLibelle());
    }

    @Test
    @Order(5)
    void loadListLigneEcritureTest() throws NotFoundException {
        EcritureComptable ecritureComptableSample = comptabiliteDao.getEcritureComptable(-1);

        comptabiliteDao.loadListLigneEcriture(ecritureComptableSample);

        assertEquals(3, ecritureComptableSample.getListLigneEcriture().size());
    }

    @Test
    @Order(6)
    void insertEcritureComptableTest() throws NotFoundException {
        comptabiliteDao.insertEcritureComptable(ecritureComptableSample);

        assertEquals(ecritureComptableSample.getReference(), comptabiliteDao.getEcritureComptableByRef(ecritureComptableSample.getReference()).getReference());
    }

    @Test
    @Order(7)
    void insertListLigneEcritureComptableTest() throws NotFoundException {
        ecritureComptableSample = comptabiliteDao.getListEcritureComptable().get(comptabiliteDao.getListEcritureComptable().size() - 1);
        ecritureComptableSample.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(706),
                null, null,
                new BigDecimal(123)));
        ecritureComptableSample.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(706),
                null, null,
                new BigDecimal(123)));

        comptabiliteDao.insertListLigneEcritureComptable(ecritureComptableSample);

        assertEquals(2, ecritureComptableSample.getListLigneEcriture().size());
    }

    @Test
    @Order(8)
    void updateEcritureComptableTest() throws NotFoundException {

        ecritureComptableSample = comptabiliteDao.getEcritureComptableByRef("AC-2016/00001");
        ecritureComptableSample.setLibelle("Nouveau libelle 1234");

        comptabiliteDao.updateEcritureComptable(ecritureComptableSample);

        assertEquals("Nouveau libelle 1234", comptabiliteDao.getEcritureComptableByRef("AC-2016/00001").getLibelle());
    }

    @Test
    @Order(9)
    void deleteEcritureComptableTest() throws NotFoundException {
        EcritureComptable ecritureComptableSample = new EcritureComptable();
        ecritureComptableSample.setJournal(new JournalComptable("AC", "Achat"));
        ecritureComptableSample.setDate(new Date());
        ecritureComptableSample.setLibelle("Libelle");
        ecritureComptableSample.setReference("AC-2019/99999");

        comptabiliteDao.insertEcritureComptable(ecritureComptableSample);

        comptabiliteDao.deleteEcritureComptable(comptabiliteDao.getEcritureComptableByRef("AC-2019/99999").getId());

        assertThrows(NotFoundException.class, () -> comptabiliteDao.getEcritureComptableByRef("AC-2019/99999"));
    }

    @Test
    @Order(10)
    void deleteListLigneEcritureComptableTest() throws NotFoundException {
        comptabiliteDao.insertListLigneEcritureComptable(ecritureComptableSample);

        comptabiliteDao.deleteListLigneEcritureComptable(comptabiliteDao.getEcritureComptableByRef("AC-2019/00001").getId());

        assertEquals(0, comptabiliteDao.getEcritureComptableByRef("AC-2019/00001").getListLigneEcriture().size());
    }
}