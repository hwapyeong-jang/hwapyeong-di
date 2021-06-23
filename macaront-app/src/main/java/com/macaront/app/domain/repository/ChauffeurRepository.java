package com.macaront.app.domain.repository;

import com.macaront.app.domain.entity.Chauffeur;
import com.macaront.framework.web.annotation.Autowired;
import com.macaront.framework.web.annotation.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ChauffeurRepository {

    private static final Logger logger = LoggerFactory.getLogger(ChauffeurRepository.class);

    private static ConcurrentHashMap<Long, Chauffeur> chauffeurs;

    private ChauffeurRepository() {
        initChauffeurs();
    }

    public void deleteAll() {
        chauffeurs.clear();
    }

    public void initChauffeurs() {
        chauffeurs = new ConcurrentHashMap<>();
        Chauffeur chauffeur = getChauffeur();
        chauffeurs.put(chauffeur.getId(), chauffeur);
    }

    private Chauffeur getChauffeur() {
        return new Chauffeur(1L, "장화평", "010-4104-7721");
    }

    public int getChauffeursSize() {
        return chauffeurs.size();
    }

    public List<Chauffeur> findAll() {
        logger.info("Chauffeur Repository - Get Chauffeur List");
        return new ArrayList<>(chauffeurs.values());
    }

    public Optional<Chauffeur> findById(Long id) {
        logger.info("Chauffeur Repository - Get Chauffeur Data, ID: {}", id);
        return Optional.ofNullable(chauffeurs.get(id));
    }

    public Chauffeur save(Chauffeur chauffeur) {
        logger.info("Chauffeur Repository - Add/Modify Chauffeur, chauffeur: {}", chauffeur);

        // Add Chauffeur - Auto Increment
        if (chauffeur.isEmptyId()) {
            chauffeurs.entrySet().stream().max(Map.Entry.comparingByKey()).map(Map.Entry::getKey);
            Long maxId = chauffeurs.entrySet().stream()
                    .max(Map.Entry.comparingByKey())
                    .map(Map.Entry::getKey).orElse(0L);
            chauffeur.setId(++maxId);
            chauffeurs.putIfAbsent(maxId, chauffeur);
            logger.info("Add Chauffeur Auto Increment, ID: {}", maxId);
            return chauffeur;
        }

        // Modify Chauffeur
        for (Map.Entry<Long, Chauffeur> entry: chauffeurs.entrySet()) {
            if (entry.getKey().equals(chauffeur.getId())) {
                entry.setValue(chauffeur);
                logger.info("Modify Chauffeur, ID: {}", chauffeur.getId());
                return entry.getValue();
            }
        }

        // Add Chauffeur - No Auto Increment
        chauffeurs.putIfAbsent(chauffeur.getId(), chauffeur);
        logger.info("Add Chauffeur No Auto Increment, ID: {}", chauffeur.getId());
        return chauffeur;
    }

    public void delete(Long id) {
        logger.info("Chauffeur Repository - Delete Chauffeur, ID: {}", id);
        chauffeurs.remove(id);
    }

    public static class Mock extends ChauffeurRepository {

        public List<Chauffeur> chauffeurList = null;
        public Chauffeur chauffeur = null;

        @Override
        public List<Chauffeur> findAll() {
            return chauffeurList;
        }

        @Override
        public Optional<Chauffeur> findById(Long id) {
            return Optional.ofNullable(chauffeur);
        }

        @Override
        public Chauffeur save(Chauffeur chauffeur) {
            return chauffeur;
        }

        @Override
        public void delete(Long id) {}

    }

}
