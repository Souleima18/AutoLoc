package tn.esprit.autoloc.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.*;
import tn.esprit.autoloc.service.interfaces.IAgenceService;
import tn.esprit.autoloc.service.interfaces.IContratService;
import tn.esprit.autoloc.service.interfaces.IVehiculeService;
import tn.esprit.autoloc.service.interfaces.IPaiementService;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@AllArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final IVehiculeService vehiculeService;
    private final IContratService contratService;
    private final IPaiementService paiementService;
    private final IAgenceService agenceService;

    @Override
    public void run(String... args) throws Exception {
        log.info("========== STARTING CRUD TESTS ==========");

        // Créer une agence d'abord
        Agence agence = createTestAgence();

        testVehiculeCrud(agence);
        testContratCrud();

        log.info("========== CRUD TESTS COMPLETED ==========");
    }

    // ============ CREATE TEST AGENCE ============
    private Agence createTestAgence() {
        log.info("\n========== CREATING TEST AGENCE ==========");

        Agence agence = new Agence();
        agence.setNom("Agence Tunis Centre");
        agence.setVille("Tunis");
        agence.setAdresse("123 Avenue Habib Bourguiba");
        agence.setTelephone("+216 71 123 456");

        Agence created = agenceService.createAgence(agence);
        log.info("Created agence: ID={}, Nom={}", created.getIdAgence(), created.getNom());

        return created;
    }

    // ============ TEST VEHICULE CRUD ============
    private void testVehiculeCrud(Agence agence) {
        log.info("\n========== TEST VEHICULE CRUD ==========");

        // 1. CREATE
        log.info("1. CREATE - Creating 2 vehicules");
        Vehicule v1 = new Vehicule();
        v1.setImmatriculation("TN-123-ABC");
        v1.setMarque("Toyota");
        v1.setModele("Corolla");
        v1.setCategorie(CategorieVehicule.BERLINE);
        v1.setTarifJournalier(new BigDecimal("50.00"));
        v1.setStatut(StatutVehicule.DISPONIBLE);
        v1.setAgence(agence); // ✅ IMPORTANT : Assigner l'agence

        Vehicule createdV1 = vehiculeService.createVehicule(v1);
        log.info("Created vehicule: ID={}, Immatriculation={}", createdV1.getIdVehicule(), createdV1.getImmatriculation());

        Vehicule v2 = new Vehicule();
        v2.setImmatriculation("TN-456-DEF");
        v2.setMarque("Renault");
        v2.setModele("Clio");
        v2.setCategorie(CategorieVehicule.CITADINE);
        v2.setTarifJournalier(new BigDecimal("35.00"));
        v2.setStatut(StatutVehicule.DISPONIBLE);
        v2.setAgence(agence); // ✅ IMPORTANT : Assigner l'agence

        Vehicule createdV2 = vehiculeService.createVehicule(v2);
        log.info("Created vehicule: ID={}, Immatriculation={}", createdV2.getIdVehicule(), createdV2.getImmatriculation());

        // 2. READ BY ID
        log.info("\n2. READ BY ID - Fetching vehicule with id={}", createdV1.getIdVehicule());
        var vehiculeOpt = vehiculeService.getVehiculeById(createdV1.getIdVehicule());
        vehiculeOpt.ifPresentOrElse(
                v -> log.info("Found vehicule: {} {} (Agence: {})", v.getMarque(), v.getModele(), v.getAgence().getNom()),
                () -> log.warn("Vehicule not found")
        );

        // 3. READ ALL
        log.info("\n3. READ ALL - Fetching all vehicules");
        var allVehicules = vehiculeService.getAllVehicules();
        log.info("Total vehicules: {}", allVehicules.size());
        allVehicules.forEach(v -> log.info("  - {} {} ({}) - Agence: {}",
                v.getMarque(), v.getModele(), v.getImmatriculation(), v.getAgence().getNom()));

        // 4. READ BY IMMATRICULATION
        log.info("\n4. READ BY IMMATRICULATION");
        try {
            Vehicule v = vehiculeService.getVehiculeByImmatriculation("TN-123-ABC");
            log.info("Found: {} {} at tarif {}", v.getMarque(), v.getModele(), v.getTarifJournalier());
        } catch (RuntimeException e) {
            log.error("Error: {}", e.getMessage());
        }

        // 5. UPDATE
        log.info("\n5. UPDATE - Updating vehicule tarif");
        Vehicule updateData = new Vehicule();
        updateData.setImmatriculation("TN-123-ABC");
        updateData.setMarque("Toyota");
        updateData.setModele("Corolla");
        updateData.setCategorie(CategorieVehicule.BERLINE);
        updateData.setTarifJournalier(new BigDecimal("60.00"));
        updateData.setStatut(StatutVehicule.LOUE);

        Vehicule updated = vehiculeService.updateVehicule(createdV1.getIdVehicule(), updateData);
        log.info("Updated vehicule: Tarif={}, Statut={}", updated.getTarifJournalier(), updated.getStatut());

        // 6. EXISTS
        log.info("\n6. EXISTS - Checking if vehicule exists");
        boolean exists = vehiculeService.vehiculeExists(createdV1.getIdVehicule());
        log.info("Vehicule exists: {}", exists);

        // 7. COUNT
        log.info("\n7. COUNT - Total vehicules");
        long count = vehiculeService.countVehicules();
        log.info("Total: {}", count);

        // 8. DELETE
        log.info("\n8. DELETE - Deleting vehicule");
        vehiculeService.deleteVehicule(createdV2.getIdVehicule());
        log.info("Vehicule deleted");

        // 9. COUNT AFTER DELETE
        log.info("\n9. COUNT AFTER DELETE");
        long countAfter = vehiculeService.countVehicules();
        log.info("Total after delete: {} (was {})", countAfter, count);
    }

    // ============ TEST CONTRAT CRUD ============
    private void testContratCrud() {
        log.info("\n========== TEST CONTRAT CRUD ==========");

        // 1. CREATE
        log.info("1. CREATE - Creating 2 contrats");
        Contrat c1 = new Contrat();
        c1.setDateSignature(LocalDate.now());
        c1.setMontantTotal(new BigDecimal("1500.00"));
        c1.setValide(false);

        Contrat createdC1 = contratService.createContrat(c1);
        log.info("Created contrat: ID={}, Montant={}, Valide={}", createdC1.getIdContrat(), createdC1.getMontantTotal(), createdC1.getValide());

        Contrat c2 = new Contrat();
        c2.setDateSignature(LocalDate.now().minusDays(5));
        c2.setMontantTotal(new BigDecimal("2000.00"));
        c2.setValide(true);

        Contrat createdC2 = contratService.createContrat(c2);
        log.info("Created contrat: ID={}, Montant={}, Valide={}", createdC2.getIdContrat(), createdC2.getMontantTotal(), createdC2.getValide());

        // 2. READ BY ID
        log.info("\n2. READ BY ID - Fetching contrat with id={}", createdC1.getIdContrat());
        var contratOpt = contratService.getContratById(createdC1.getIdContrat());
        contratOpt.ifPresentOrElse(
                c -> log.info("Found contrat: Montant={}, Valide={}", c.getMontantTotal(), c.getValide()),
                () -> log.warn("Contrat not found")
        );

        // 3. READ ALL
        log.info("\n3. READ ALL - Fetching all contrats");
        var allContrats = contratService.getAllContrats();
        log.info("Total contrats: {}", allContrats.size());
        allContrats.forEach(c -> log.info("  - ID={}, Montant={}, Valide={}", c.getIdContrat(), c.getMontantTotal(), c.getValide()));

        // 4. READ BY VALIDE
        log.info("\n4. READ BY VALIDE - Fetching validated contrats");
        var validContrats = contratService.getContratsByValide(true);
        log.info("Validated contrats: {}", validContrats.size());
        validContrats.forEach(c -> log.info("  - ID={}, Montant={}", c.getIdContrat(), c.getMontantTotal()));

        // 5. UPDATE
        log.info("\n5. UPDATE - Updating contrat montant");
        Contrat updateData = new Contrat();
        updateData.setDateSignature(createdC1.getDateSignature());
        updateData.setMontantTotal(new BigDecimal("1800.00"));
        updateData.setValide(false);

        Contrat updated = contratService.updateContrat(createdC1.getIdContrat(), updateData);
        log.info("Updated contrat: Montant={}", updated.getMontantTotal());

        // 6. VALIDATE
        log.info("\n6. VALIDATE - Validating contrat");
        Contrat validated = contratService.validateContrat(createdC1.getIdContrat());
        log.info("Contrat validated: Valide={}", validated.getValide());

        // 7. COUNT
        log.info("\n7. COUNT - Total contrats");
        long count = contratService.countContrats();
        log.info("Total: {}", count);

        // 8. COUNT BY VALIDE
        log.info("\n8. COUNT BY VALIDE");
        long validCount = contratService.countContratsByValide(true);
        log.info("Validated contrats: {}", validCount);

        // 9. EXISTS
        log.info("\n9. EXISTS - Checking if contrat exists");
        boolean exists = contratService.contratExists(createdC1.getIdContrat());
        log.info("Contrat exists: {}", exists);

        // 10. DELETE
        log.info("\n10. DELETE - Deleting contrat");
        contratService.deleteContrat(createdC2.getIdContrat());
        log.info("Contrat deleted");

        // 11. COUNT AFTER DELETE
        log.info("\n11. COUNT AFTER DELETE");
        long countAfter = contratService.countContrats();
        log.info("Total after delete: {} (was {})", countAfter, count);
    }
}