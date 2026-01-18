# Devoir SOA Project (Microservices + SOAP/REST + Eureka + Spring Boot Admin)

Ce projet implémente une architecture SOA/microservices pour simuler un flux de transaction bancaire.
Il contient :
- **transactionservice** (SOAP) : reçoit une transaction et décide AUTORISÉE/BLOQUÉE
- **cardservice** (REST + JPA) : gestion des cartes (solde, état, expiration…)
- **fraudservice** (REST) : détection de fraude (règles simples)
- **eureka-server** : registre de services (découverte)
- **admin-server** : monitoring (Spring Boot Admin)

---

## 1) Architecture & Communication

### Flux principal : Transaction (SOAP)
1. Le client envoie une requête SOAP au **transactionservice**.
2. transactionservice appelle :
   - **fraudservice** (REST) pour vérifier si le montant est suspect
   - **cardservice** (REST) pour vérifier :
     - carte existe
     - carte active
     - carte non expirée
     - solde suffisant
3. transactionservice répond en SOAP :
   - `SUCCESS` (autorisée) ou `BLOCKED` (bloquée) ou `ERROR`

### Logs REST
transactionservice garde une liste des transactions (logs en mémoire) :
- toutes
- autorisées
- bloquées

---

## 2) Démarrage rapide (Run)

### Prérequis
- Java **21**
- Maven Wrapper inclus (`./mvnw`)
- PostgreSQL (si cardservice utilise postgres) OU adapter application.properties

### Ordre recommandé
1. **eureka-server**
2. **admin-server**
3. **cardservice**
4. **fraudservice**
5. **transactionservice**

### Commandes (dans chaque dossier service)
Exemple :
```bash
cd eureka-server
./mvnw -DskipTests spring-boot:run
