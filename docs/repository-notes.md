# Atelier 3 - Spring Data JPA : Repositories

## Interfaces Repository créées

| Interface | Entend | Justification |
|-----------|--------|---------------|
| IContratRepository | JpaRepository<Contrat, Long> | CRUD complet, findAll renvoie une List, saveAndFlush disponible. |
| IAgenceRepository | JpaRepository<Agence, Long> | CRUD complet, gestion des relations avec Vehicule et Employe. |
| IEmployeRepository | JpaRepository<Employe, Long> | CRUD complet pour les employés. |
| IVehiculeRepository | JpaRepository<Vehicule, Long> | CRUD complet, gestion N-N avec Equipement. |
| IEquipementRepository | JpaRepository<Equipement, Long> | CRUD complet pour les équipements. |
| IClientRepository | JpaRepository<Client, Long> | CRUD complet pour les clients. |
| IReservationRepository | JpaRepository<Reservation, Long> | CRUD complet pour les réservations. |
| IPaiementRepository | JpaRepository<Paiement, Long> | CRUD complet pour les paiements. |
| IMaintenanceRepository | JpaRepository<Maintenance, Long> | CRUD complet pour les maintenances. |

## Justification du choix JpaRepository

**JpaRepository** offre :
- CRUD complet (save, findById, delete, etc.)
- findAll renvoie une **List** (plus pratique que Iterable)
- Pagination et tri (findAll(Pageable), findAll(Sort))
- Méthodes JPA avancées (flush, saveAndFlush, getReferenceById)
- QueryByExampleExecutor

## Anomalies SonarQube for IDE détectées et corrigées

| Anomalie | Règle | Correction |
|----------|-------|-----------|
| Import inutilisé dans IContratRepository | S1128 | Aucun import inutilisé détecté |
| Pas de javadoc sur les interfaces | S1141 | Javadoc optionnelle pour les interfaces Repository |
| Pas de validations | - | Validations gérées dans la couche Service |

## Observations

- Aucune classe d'implémentation n'a été écrite : **Spring Data génère les proxies à l'exécution**
- Les logs au démarrage confirment : `Found 9 JPA repository interfaces`
- Les repositories sont des interfaces simples et pures sans logique métier

## Notes d'apprentissage

### CrudRepository vs JpaRepository
CrudRepository
├── save(), findById(), delete()
└── findAll() retourne Iterable<T>

JpaRepository (choisi pour AutoLoc)
├── Tout ce que CrudRepository propose
├── findAll() retourne List<T>
├── findAll(Sort), findAll(Pageable)
├── flush(), saveAndFlush()
└── Plus complet pour une application complète

### Quand utiliser quelle interface ?

| Besoin | Interface |
|--------|-----------|
| Exposer 2-3 méthodes choisies | `Repository<T, ID>` |
| CRUD basique | `CrudRepository<T, ID>` |
| CRUD + Listes | `ListCrudRepository<T, ID>` |
| Tri et Pagination | `PagingAndSortingRepository<T, ID>` |
| **CRUD complet + Listes + Tri + Pagination** | **`JpaRepository<T, ID>`** |