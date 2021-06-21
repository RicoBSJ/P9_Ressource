package com.dummy.myerp.model.bean.comptabilite;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class EcritureComptableTest {

    private EcritureComptable vEcritureComptableRight;
    private EcritureComptable vEcritureComptableWrong;

    @BeforeEach
    public void setUp() {
        vEcritureComptableRight = new EcritureComptable();
        vEcritureComptableRight.setLibelle("Equilibrée");
        vEcritureComptableRight.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
        vEcritureComptableRight.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
        vEcritureComptableRight.getListLigneEcriture().add(this.createLigne(2, null, "301"));
        vEcritureComptableRight.getListLigneEcriture().add(this.createLigne(2, "40", "7"));

        vEcritureComptableWrong = new EcritureComptable();
        vEcritureComptableWrong.setLibelle("Non équilibrée");
        vEcritureComptableWrong.getListLigneEcriture().add(this.createLigne(1, "10", null));
        vEcritureComptableWrong.getListLigneEcriture().add(this.createLigne(1, "20", "1"));
        vEcritureComptableWrong.getListLigneEcriture().add(this.createLigne(2, null, "30"));
        vEcritureComptableWrong.getListLigneEcriture().add(this.createLigne(2, "1", "2"));
    }

    @Test
    public void isEqual() {
        Assertions.assertTrue(vEcritureComptableRight.isEquilibree(), vEcritureComptableRight.toString());
    }

    @Test
    public void isNotEqual() {
        Assertions.assertFalse(vEcritureComptableWrong.isEquilibree(), vEcritureComptableWrong.toString());
    }

    private LigneEcritureComptable createLigne(Integer pCompteComptableNumero, String pDebit, String pCredit) {
        BigDecimal vDebit = pDebit == null ? null : new BigDecimal(pDebit);
        BigDecimal vCredit = pCredit == null ? null : new BigDecimal(pCredit);
        String vLibelle = ObjectUtils.defaultIfNull(vDebit, BigDecimal.ZERO)
                .subtract(ObjectUtils.defaultIfNull(vCredit, BigDecimal.ZERO)).toPlainString();
        LigneEcritureComptable vRetour = new LigneEcritureComptable(new CompteComptable(pCompteComptableNumero),
                vLibelle,
                vDebit, vCredit);
        return vRetour;
    }

}
