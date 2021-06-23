package com.macaront.app.domain.repository;

import com.macaront.app.domain.entity.Chauffeur;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

public class ChauffeurRepositoryTest {

    private static ChauffeurRepository chauffeurRepository;

    @BeforeAll
    public static void initRepository() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<ChauffeurRepository> constructor = ChauffeurRepository.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        chauffeurRepository = constructor.newInstance();
    }

    @Test
    public void testFindAll_exist_case() {

        // when
        List<Chauffeur> resList = chauffeurRepository.findAll();

        // then
        Assertions.assertFalse(resList.isEmpty());

    }

    @Test
    public void testFindAll_not_exist_case() {

        // given
        chauffeurRepository.deleteAll();

        // when
        List<Chauffeur> resList = chauffeurRepository.findAll();

        // then
        Assertions.assertNotNull(resList);
        Assertions.assertTrue(resList.isEmpty());

        chauffeurRepository.initChauffeurs();
    }

    @Test
    public void testFindById_exist_case() {

        // given
        Long givenId = 1L;

        // when
        Optional<Chauffeur> resChauffeur = chauffeurRepository.findById(givenId);

        // then
        Assertions.assertEquals(givenId, resChauffeur.map(Chauffeur::getId).orElse(null));

    }

    @Test
    public void testFindById_not_exist_case() {

        // given
        Long givenId = 9999L;

        // when
        Optional<Chauffeur> resChauffeur = chauffeurRepository.findById(givenId);

        // then
        Assertions.assertFalse(resChauffeur.isPresent());

    }

    @Test
    public void testSave_add_chauffeur_by_auto_increment() {

        // given
        chauffeurRepository.save(new Chauffeur(2L, "장화평1", "010-4104-7721"));
        chauffeurRepository.save(new Chauffeur(5L, "장화평2", "010-4104-7721"));
        chauffeurRepository.save(new Chauffeur(8L, "장화평3", "010-4104-7721"));

        Long expectedId = 9L;
        Chauffeur givenChauffeur = new Chauffeur(null, "장화평", "010-4104-7721");

        // when
        Chauffeur resChauffeur = chauffeurRepository.save(givenChauffeur);

        // then
        Assertions.assertTrue(resChauffeur.isEqualsId(expectedId));
        Assertions.assertTrue(resChauffeur.isEqualsValues(givenChauffeur));

        chauffeurRepository.initChauffeurs();
    }

    @Test
    public void testSave_add_chauffeur_by_no_auto_increment() {

        // given
        Long givenId = 1234L;
        int expectedChauffeursSize = 2;
        Chauffeur givenChauffeur = new Chauffeur(givenId, "장화평", "010-4104-7721");

        // when
        Chauffeur resChauffeur = chauffeurRepository.save(givenChauffeur);

        // then
        Assertions.assertEquals(expectedChauffeursSize, chauffeurRepository.getChauffeursSize());
        Assertions.assertTrue(resChauffeur.isEqualsId(givenId));
        Assertions.assertTrue(resChauffeur.isEqualsValues(givenChauffeur));

        chauffeurRepository.initChauffeurs();
    }

    @Test
    public void testSave_modify_chauffeur() {

        // given
        Long givenId = 1L;
        int expectedChauffeursSize = 1;
        Chauffeur givenChauffeur = new Chauffeur(givenId, "장화평", "010-4104-7721");

        // when
        Chauffeur resChauffeur = chauffeurRepository.save(givenChauffeur);

        // then
        Assertions.assertEquals(expectedChauffeursSize, chauffeurRepository.getChauffeursSize());
        Assertions.assertTrue(resChauffeur.isEqualsId(givenId));
        Assertions.assertTrue(resChauffeur.isEqualsValues(givenChauffeur));

        chauffeurRepository.initChauffeurs();
    }


    @Test
    public void testDelete() {

        // given
        int expectedChauffeursSize = 0;
        Long givenId = 1L;

        // when
        chauffeurRepository.delete(givenId);

        // then
        Assertions.assertEquals(expectedChauffeursSize, chauffeurRepository.getChauffeursSize());

        chauffeurRepository.initChauffeurs();
    }

}
