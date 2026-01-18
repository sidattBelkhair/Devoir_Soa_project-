
# Devoir SOA Project (Microservices + SOAP/REST + Eureka + Spring Boot Admin)

Ce projet implémente une architecture **SOA / microservices** simulant un **système de transaction bancaire**.
Il combine **SOAP et REST**, avec **Service Discovery (Eureka)** et **Monitoring (Spring Boot Admin)**.

Le projet contient les services suivants :
- **transactionservice (SOAP + REST)** : reçoit une transaction et décide **AUTORISÉE / BLOQUÉE**
- **cardservice (REST + JPA)** : gestion des cartes bancaires (solde, état, expiration…)
- **fraudservice (REST)** : détection de fraude (règles simples)
- **eureka-server** : registre de services (service discovery)
- **admin-server** : monitoring & observabilité (Spring Boot Admin)

---

## 1) Architecture & Communication

### Flux principal : Transaction (SOAP)

1. Le client envoie une requête **SOAP** au **transactionservice**
2. **transactionservice** appelle :
   - **fraudservice (REST)** pour vérifier si le montant est suspect
   - **cardservice (REST)** pour vérifier :
     - carte existante
     - carte active
     - carte non expirée
     - solde suffisant
3. **transactionservice** retourne une réponse **SOAP** :
   - `SUCCESS` → transaction autorisée
   - `BLOCKED` → transaction refusée
   - `ERROR` → problème technique

### Logs des transactions (REST)

Le **transactionservice** conserve des logs en mémoire :
- toutes les transactions
- transactions autorisées
- transactions bloquées

---

## 2) Démarrage rapide (Run)

### Prérequis
- Java **21**
- Maven Wrapper inclus (`./mvnw`)
- PostgreSQL (si utilisé par cardservice) ou adapter `application.properties`

### Ordre recommandé de lancement
1. **eureka-server**
2. **admin-server**
3. **cardservice**
4. **fraudservice**
5. **transactionservice**

### Commande de démarrage (dans chaque service)

```bash
./mvnw -DskipTests spring-boot:run
````

---

## 3) Endpoints par service

---

### 3.1 cardservice (REST)

**Base URL :**
`http://localhost:8081/api`

**Endpoints :**

* **POST `/addcard`** : créer une carte
* **GET `/getcard/{cardNumber}`** : récupérer une carte par numéro
* **GET `/all`** : liste de toutes les cartes
* **PUT `/updatecard/{cardNumber}`** : mise à jour d’une carte
* **DELETE `/deletecard/{cardNumber}`** : supprimer une carte

**Swagger :**

* `http://localhost:8081/swagger-ui.html`
* `http://localhost:8081/v3/api-docs`

---

### 3.2 fraudservice (REST)

**Base URL :**
`http://localhost:8082/api/fraud`

**Endpoint :**

* **GET `/check?amount=5000`**

**Réponse typique :**

```json
{
  "fraudulent": false,
  "reason": "Transaction OK"
}
```

**Swagger (si activé) :**

* `http://localhost:8082/swagger-ui.html`
* `http://localhost:8082/v3/api-docs`

---

### 3.3 transactionservice (SOAP + REST Logs)

#### SOAP Endpoint

**URL :**
`http://localhost:8083/ws`

**WSDL :**
`http://localhost:8083/ws/transactions.wsdl`

#### Headers requis (Postman / curl)

* `Content-Type: text/xml; charset=utf-8`
* `Accept: text/xml`
* `SOAPAction: ""`

#### Exemple SOAP Request

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:tns="http://example.com/transactionservice">
  <soapenv:Header/>
  <soapenv:Body>
    <tns:TransactionRequest>
      <tns:cardNumber>44171165</tns:cardNumber>
      <tns:amount>5000</tns:amount>
    </tns:TransactionRequest>
  </soapenv:Body>
</soapenv:Envelope>
```

#### Réponse SOAP (exemple)

```xml
<ns2:TransactionResponse xmlns:ns2="http://example.com/transactionservice">
  <ns2:status>SUCCESS</ns2:status>
  <ns2:message>Transaction Autorisée</ns2:message>
</ns2:TransactionResponse>
```

---

#### REST Endpoints (Logs)

**Base URL :**
`http://localhost:8083/api/transactions`

* **GET `/all`** : toutes les transactions
* **GET `/authorized`** : transactions autorisées
* **GET `/blocked`** : transactions bloquées

**Exemple réponse JSON :**

```json
[
  {
    "cardNumber": "44171165",
    "amount": 5000.0,
    "status": "SUCCESS",
    "message": "Transaction Autorisée",
    "createdAt": "2026-01-18T15:55:12.123"
  }
]
```

**Swagger (REST logs) :**

* `http://localhost:8083/swagger-ui.html`
* `http://localhost:8083/v3/api-docs`

---

### 3.4 eureka-server

**Dashboard :**
`http://localhost:8761`

**Rôle :**

* Service Discovery
* Enregistrement automatique des microservices

---

### 3.5 admin-server (Spring Boot Admin)

**UI :**
`http://localhost:9090` *(selon configuration)*

**Rôle :**

* Monitoring
* Health checks
* Metrics
* Logs
* Statut des services

---

## 4) Tests rapides (curl)

### Tester une transaction SOAP

```bash
curl -X POST "http://localhost:8083/ws" \
-H "Content-Type: text/xml; charset=utf-8" \
-H "Accept: text/xml" \
-H "SOAPAction: \"\"" \
-d '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:tns="http://example.com/transactionservice">
  <soapenv:Header/>
  <soapenv:Body>
    <tns:TransactionRequest>
      <tns:cardNumber>44171165</tns:cardNumber>
      <tns:amount>5000</tns:amount>
    </tns:TransactionRequest>
  </soapenv:Body>
</soapenv:Envelope>'
```

### Voir toutes les transactions

```bash
curl http://localhost:8083/api/transactions/all
```

### Voir les transactions autorisées

```bash
curl http://localhost:8083/api/transactions/authorized
```

### Voir les transactions bloquées

```bash
curl http://localhost:8083/api/transactions/blocked
```

---

## 5) Auteur

Projet réalisé dans le cadre du **Devoir SOA**
Architecture **SOAP / REST / Microservices / Spring Boot**

```


```
