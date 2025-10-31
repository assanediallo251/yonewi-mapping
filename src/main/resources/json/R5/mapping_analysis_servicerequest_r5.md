# Mapping Prestations SI Médical vers FHIR R5

**Document** : Spécification de Mapping  
**Version** : 1.0.0  
**Date** : 31 Octobre 2025  
**Organisation** : Eyone Health System  
**Standard FHIR** : R5 (5.0.0)  
**Système Source** : SI Medical Eyone

---

## 1. Vue d'Ensemble

### 1.1 Objectif

Ce document spécifie la transformation des messages de prestations médicales du Système d'Information Eyone vers les ressources FHIR R5 standardisées, permettant l'interopérabilité avec les systèmes de santé conformes HL7 FHIR.

### 1.2 Portée

**Types de messages couverts** :
- `UPDATE_PRESTATION` : Prestations avec workflow médical (ANALYSIS, RADIOLOGY)
- `UPDATE_PRESCRIPTION_FINANCIERE` : Prestations avec facturation directe (CONSULTATION, AMBULATORY)

**Ressources FHIR R5 générées** :
- ServiceRequest : Demandes d'examens médicaux
- Encounter : Rencontres patient-médecin
- MedicationRequest : Prescriptions pharmaceutiques  
- DiagnosticReport : Résultats d'examens

### 1.3 Règles de Cardinalité

**Notation utilisée** :
- `1..1` : Obligatoire, une seule occurrence
- `0..1` : Optionnel, maximum une occurrence
- `0..*` : Optionnel, plusieurs occurrences possibles
- `1..*` : Obligatoire, plusieurs occurrences possibles

---

## 2. ServiceRequest - Prestations ANALYSIS/RADIOLOGY

### 2.1 Contexte d'Usage

**Message source** : `UPDATE_PRESTATION`  
**Actions** : `ANALYSIS`, `RADIOLOGY`  
**Workflow** : SCHEDULED → [Exécution] → VALIDATED

### 2.2 Mapping des Champs de Base

| Champ SI | Chemin SI | Card. SI | Chemin FHIR R5 | Type FHIR | Card. FHIR | Transformation |
|----------|-----------|----------|----------------|-----------|------------|----------------|
| **Identifiants** |
| ID prestation | `data.identifier` | 1..1 | `id` | id | 1..1 | Conversion: `data.eyoneInternalId` |
| ID interne | `data.eyoneInternalId` | 1..1 | `identifier[0].value` | string | 1..1 | Direct |
| ID personnalisé | `data.customId` | 0..1 | `identifier[1].value` | string | 0..1 | Direct |
| ID système | `data.identifier` | 1..1 | `identifier[2].value` | string | 1..1 | Conversion: toString() |
| **Statut et Catégorie** |
| Statut | `data.status.code` | 1..1 | `status` | code | 1..1 | Mapping statuts (voir 2.3) |
| Intention | - | - | `intent` | code | 1..1 | Fixe: "order" |
| Catégorie | `data.medicalActCategory.code` | 1..1 | `category[0].coding[0].code` | code | 1..* | Direct |
| Libellé catégorie | `data.medicalActCategory.label` | 1..1 | `category[0].coding[0].display` | string | 0..1 | Direct |
| Code acte | `data.medicalActCategory.code` | 1..1 | `code.coding[0].code` | code | 1..1 | Direct |
| Texte acte | `data.medicalActCategory.label` | 1..1 | `code.text` | string | 0..1 | Direct |
| **Références** |
| Patient | `data.patient` | 1..1 | `subject` | Reference(Patient) | 1..1 | Transformation complète (voir 6.1) |
| Prescripteur | `data.doctor` | 0..1 | `requester` | Reference(Practitioner) | 0..1 | Transformation complète (voir 6.2) |
| Exécutant | `data.organism` | 1..1 | `performer[0]` | Reference(Organization) | 0..* | Transformation complète (voir 6.3) |
| Lieu | `data.relatedService` | 0..1 | `location[0]` | Reference(Location) | 0..* | Transformation complète (voir 6.4) |
| **Dates** |
| Date création | `data.creationDate` | 1..1 | `authoredOn` | dateTime | 0..1 | Format: DD/MM/YYYY HH:mm:ss → ISO 8601 |
| Date début | `data.startDate` | 0..1 | `occurrencePeriod.start` | dateTime | 0..1 | Format: DD/MM/YYYY HH:mm:ss → ISO 8601 |
| Date fin | `data.endDate` | 0..1 | `occurrencePeriod.end` | dateTime | 0..1 | Format: DD/MM/YYYY HH:mm:ss → ISO 8601 |
| **Autres** |
| Créateur | `createdBy` | 1..1 | `note[0].authorString` | string | 0..* | Texte: "Créé par {createdBy}" |
| Narrative | - | - | `text.div` | xhtml | 1..1 | Génération automatique (voir 5.1) |

### 2.3 Mapping des Statuts ServiceRequest

| Code SI | Label SI | Code FHIR R5 | Description |
|---------|----------|--------------|-------------|
| `SCHEDULED` | Programmé(e) | `active` | Demande active en attente d'exécution |
| `ON_GOING` | En cours | `active` | Demande en cours d'exécution |
| `VALIDATED` | Validé(e) | `completed` | Demande terminée avec succès |
| `CANCELLED` | Annulé(e) | `revoked` | Demande annulée |
| `CREATED` | Créé(e) | `draft` | Demande en préparation |

### 2.4 Structure des Identifiers

**Format** :
```json
{
  "identifier": [
    {
      "use": "official",
      "system": "http://eyone.sn/fhir/identifier/eyoneInternalId",
      "value": "{data.eyoneInternalId}"
    },
    {
      "use": "official",
      "system": "http://eyone.sn/fhir/identifier/customId",
      "value": "{data.customId}"
    },
    {
      "system": "http://eyone.sn/fhir/identifier/prestation",
      "value": "{data.identifier}"
    }
  ]
}
```

### 2.5 Exemple Complet

**Source SI** :
```json
{
  "messageType": "UPDATE_PRESTATION",
  "action": "ANALYSIS",
  "data": {
    "identifier": 38162,
    "eyoneInternalId": "ANLMED202500012",
    "customId": "ANLMED202500012",
    "status": { "code": "VALIDATED" },
    "medicalActCategory": { "code": "ANALYSIS", "label": "Analyse" },
    "patient": {
      "identifier": 14554,
      "eyoneInternalId": "PCSR202500004",
      "firstName": "MAME MARAME VICTORINE",
      "lastName": "NDIAYE"
    },
    "creationDate": "28/10/2025 11:34:46"
  }
}
```

**Cible FHIR R5** :
```json
{
  "resourceType": "ServiceRequest",
  "id": "ANLMED202500012",
  "text": {
    "status": "generated",
    "div": "<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Service Request ANLMED202500012</b></p><p><b>Status:</b> completed</p><p><b>Category:</b> Analysis</p><p><b>Patient:</b> MAME MARAME VICTORINE NDIAYE (14554)</p></div>"
  },
  "identifier": [
    {
      "use": "official",
      "system": "http://eyone.sn/fhir/identifier/eyoneInternalId",
      "value": "ANLMED202500012"
    }
  ],
  "status": "completed",
  "intent": "order",
  "category": [{
    "coding": [{
      "system": "http://eyone.sn/fhir/CodeSystem/medical-act-category",
      "code": "ANALYSIS",
      "display": "Analyse"
    }]
  }],
  "code": {
    "coding": [{
      "system": "http://eyone.sn/fhir/CodeSystem/medical-act",
      "code": "ANALYSIS",
      "display": "Analyse"
    }]
  },
  "subject": {
    "reference": "Patient/14554",
    "type": "Patient",
    "display": "MAME MARAME VICTORINE NDIAYE"
  },
  "authoredOn": "2025-10-28T11:34:46+00:00"
}
```

---

## 3. Encounter - Prestations CONSULTATION/AMBULATORY

### 3.1 Contexte d'Usage

**Message source** : `UPDATE_PRESCRIPTION_FINANCIERE`  
**Actions** : `CONSULTATION`, `AMBULATORY`, `VISIT`  
**Workflow** : ON_GOING → [Facturation] → VALIDATED

### 3.2 Mapping des Champs de Base

| Champ SI | Chemin SI | Card. SI | Chemin FHIR R5 | Type FHIR | Card. FHIR | Transformation |
|----------|-----------|----------|----------------|-----------|------------|----------------|
| **Identifiants** |
| ID prestation | `data.identifier` | 1..1 | `id` | id | 1..1 | Conversion: `data.eyoneInternalId` |
| ID interne | `data.eyoneInternalId` | 1..1 | `identifier[0].value` | string | 0..* | Direct |
| ID personnalisé | `data.customId` | 0..1 | `identifier[1].value` | string | 0..* | Direct |
| **Statut et Classe** |
| Statut | `data.status.code` | 1..1 | `status` | code | 1..1 | Mapping statuts (voir 3.3) |
| Classe | `data.medicalActCategory.code` | 1..1 | `class[0].coding[0].code` | code | 1..* | Mapping classes (voir 3.4) |
| **Références** |
| Patient | `data.patient` | 1..1 | `subject` | Reference(Patient) | 1..1 | Transformation complète (voir 6.1) |
| Fournisseur | `data.organism` | 1..1 | `serviceProvider` | Reference(Organization) | 0..1 | Transformation complète (voir 6.3) |
| **Dates** |
| Date début | `data.startDate` | 0..1 | `actualPeriod.start` | dateTime | 0..1 | Format: DD/MM/YYYY HH:mm:ss → ISO 8601 |
| Date fin | `data.endDate` | 0..1 | `actualPeriod.end` | dateTime | 0..1 | Format: DD/MM/YYYY HH:mm:ss → ISO 8601 |
| **Raison** |
| Dénomination | `data.denomination` | 0..1 | `reason[0].value.concept.text` | string | 0..* | Direct |
| **Autres** |
| Narrative | - | - | `text.div` | xhtml | 0..1 | Génération automatique (voir 5.1) |

### 3.3 Mapping des Statuts Encounter

| Code SI | Label SI | Code FHIR R5 | Description |
|---------|----------|--------------|-------------|
| `ON_GOING` | En cours | `in-progress` | Rencontre en cours |
| `VALIDATED` | Validé(e) | `completed` | Rencontre terminée |
| `SCHEDULED` | Programmé(e) | `planned` | Rencontre planifiée |
| `CANCELLED` | Annulé(e) | `cancelled` | Rencontre annulée |

### 3.4 Mapping des Classes Encounter

| Code SI | Code FHIR R5 | System FHIR | Display |
|---------|--------------|-------------|---------|
| `AMBULATORY` | `AMB` | http://terminology.hl7.org/CodeSystem/v3-ActCode | ambulatory |
| `CONSULTATION` | `AMB` | http://terminology.hl7.org/CodeSystem/v3-ActCode | ambulatory |
| `HOSPITALIZATION` | `IMP` | http://terminology.hl7.org/CodeSystem/v3-ActCode | inpatient encounter |
| `VISIT` | `HH` | http://terminology.hl7.org/CodeSystem/v3-ActCode | home health |

---

## 4. MedicationRequest - Prestation Pharmacie

### 4.1 Contexte d'Usage

**Création** : Systématique pour toute prestation contenant `data.pharmacySellPrestation`  
**Source** : Objet `pharmacySellPrestation` dans le message SI

### 4.2 Mapping des Champs de Base

| Champ SI | Chemin SI | Card. SI | Chemin FHIR R5 | Type FHIR | Card. FHIR | Transformation |
|----------|-----------|----------|----------------|-----------|------------|----------------|
| **Identifiants** |
| ID pharmacie | `data.pharmacySellPrestation.identifier` | 1..1 | `id` | id | 1..1 | Format: "{parentId}-PHARMACY" |
| ID système | `data.pharmacySellPrestation.identifier` | 1..1 | `identifier[0].value` | string | 0..* | Conversion: toString() |
| **Statut et Catégorie** |
| Statut | - | - | `status` | code | 1..1 | Fixe: "active" |
| Intention | - | - | `intent` | code | 1..1 | Fixe: "order" |
| Catégorie | - | - | `category[0].coding[0].code` | code | 0..* | Fixe: "outpatient" |
| **Médicament** |
| Commentaire | `data.pharmacySellPrestation.commentary` | 0..1 | `medication.concept.text` | string | 1..1 | Direct ou "Pharmacy prestation" |
| **Références** |
| Patient | `data.patient` | 1..1 | `subject` | Reference(Patient) | 1..1 | Même que ressource parente |
| Prescripteur | `data.doctor` | 0..1 | `requester` | Reference(Practitioner) | 0..1 | Même que ressource parente |
| Exécutant | `data.organism` | 1..1 | `performer[0]` | Reference(Organization) | 0..* | Même que ressource parente |
| **Dates** |
| Date création | `data.creationDate` | 1..1 | `authoredOn` | dateTime | 0..1 | Format: DD/MM/YYYY HH:mm:ss → ISO 8601 |
| **Autres** |
| Narrative | - | - | `text.div` | xhtml | 0..1 | Génération automatique (voir 5.1) |

### 4.3 Règles de Création

**Règle #1** : Une MedicationRequest doit toujours être créée en parallèle de ServiceRequest ou Encounter.

**Règle #2** : L'ID de la MedicationRequest est formé de : `{ID_ressource_parente}-PHARMACY`

**Règle #3** : Toutes les références (Patient, Practitioner, Organization) sont identiques à la ressource parente.

---

## 5. DiagnosticReport - Résultats d'Analyse

### 5.1 Contexte d'Usage

**Condition de création** : Uniquement si `data.status.code == "VALIDATED"` et `action == "ANALYSIS" | "RADIOLOGY"`

### 5.2 Mapping des Champs de Base

| Champ SI | Chemin SI | Card. SI | Chemin FHIR R5 | Type FHIR | Card. FHIR | Transformation |
|----------|-----------|----------|----------------|-----------|------------|----------------|
| **Identifiants** |
| ID rapport | `data.eyoneInternalId` | 1..1 | `id` | id | 1..1 | Format: "{eyoneInternalId}-REPORT" |
| ID système | `data.identifier` | 1..1 | `identifier[0].value` | string | 0..* | Conversion: toString() |
| **Statut et Catégorie** |
| Statut | `data.status.code` | 1..1 | `status` | code | 1..1 | Si VALIDATED → "final" |
| Catégorie | `data.medicalActCategory.code` | 1..1 | `category[0].coding[0].code` | code | 0..* | Mapping: ANALYSIS→LAB, RADIOLOGY→RAD |
| Code | `data.medicalActCategory` | 1..1 | `code` | CodeableConcept | 1..1 | Même que ServiceRequest |
| **Références** |
| ServiceRequest | `data.eyoneInternalId` | 1..1 | `basedOn[0].reference` | Reference(ServiceRequest) | 0..* | Format: "ServiceRequest/{id}" |
| Patient | `data.patient` | 1..1 | `subject` | Reference(Patient) | 1..1 | Même que ServiceRequest |
| Exécutant | `data.doctor`, `data.organism` | 1..1 | `performer[]` | Reference | 0..* | Practitioner + Organization |
| Interprète | `data.doctor` | 0..1 | `resultsInterpreter[0]` | Reference(Practitioner) | 0..* | Médecin validateur |
| **Dates** |
| Date effective | `data.creationDate` | 1..1 | `effectiveDateTime` | dateTime | 0..1 | Format: DD/MM/YYYY HH:mm:ss → ISO 8601 |
| Date émission | `data.creationDate` | 1..1 | `issued` | instant | 0..1 | Format: DD/MM/YYYY HH:mm:ss → ISO 8601 |
| **Autres** |
| Narrative | - | - | `text.div` | xhtml | 0..1 | Génération automatique (voir 5.1) |

### 5.3 Mapping Catégorie DiagnosticReport

| Code SI | Code FHIR R5 | System | Display |
|---------|--------------|--------|---------|
| `ANALYSIS` | `LAB` | http://terminology.hl7.org/CodeSystem/v2-0074 | Laboratory |
| `RADIOLOGY` | `RAD` | http://terminology.hl7.org/CodeSystem/v2-0074 | Radiology |

---

## 6. Transformation des Références

### 6.1 Référence Patient

**Source SI** :
```json
{
  "patient": {
    "identifier": 14554,
    "eyoneInternalId": "PCSR202500004",
    "firstName": "MAME MARAME VICTORINE",
    "lastName": "NDIAYE",
    "sex": { "code": "female", "label": "F" }
  }
}
```

**Structure FHIR R5** :
```json
{
  "subject": {
    "reference": "Patient/14554",
    "type": "Patient",
    "identifier": {
      "system": "http://eyone.sn/fhir/identifier/patient",
      "value": "PCSR202500004"
    },
    "display": "MAME MARAME VICTORINE NDIAYE"
  }
}
```

**Règles** :
- `reference` : Format `Patient/{identifier}`
- `display` : Format `{firstName} {lastName}`
- `identifier.value` : Utiliser `eyoneInternalId`

### 6.2 Référence Practitioner

**Source SI** :
```json
{
  "doctor": {
    "identifier": 14356,
    "doctorUsername": "docteur.test@eyone.net",
    "doctorCompleteName": "TEST DOCTEUR"
  }
}
```

**Structure FHIR R5** :
```json
{
  "requester": {
    "reference": "Practitioner/14356",
    "type": "Practitioner",
    "identifier": {
      "system": "http://eyone.sn/fhir/identifier/practitioner",
      "value": "14356"
    },
    "display": "TEST DOCTEUR"
  }
}
```

**Règles** :
- `reference` : Format `Practitioner/{identifier}`
- `display` : Utiliser `doctorCompleteName`
- `identifier.value` : Conversion `identifier` en string

### 6.3 Référence Organization

**Source SI** :
```json
{
  "organism": {
    "identifier": 13853,
    "name": "Centre de Santé Renaissance"
  }
}
```

**Structure FHIR R5** :
```json
{
  "performer": [{
    "reference": "Organization/13853",
    "type": "Organization",
    "identifier": {
      "system": "http://eyone.sn/fhir/identifier/organization",
      "value": "13853"
    },
    "display": "Centre de Santé Renaissance"
  }]
}
```

**Règles** :
- `reference` : Format `Organization/{identifier}`
- `display` : Utiliser `name`
- `identifier.value` : Conversion `identifier` en string

### 6.4 Référence Location

**Source SI** :
```json
{
  "relatedService": {
    "identifier": 13853,
    "name": "Centre de Santé Renaissance"
  }
}
```

**Structure FHIR R5** :
```json
{
  "location": [{
    "reference": "Location/13853",
    "type": "Location",
    "identifier": {
      "system": "http://eyone.sn/fhir/identifier/location",
      "value": "13853"
    },
    "display": "Centre de Santé Renaissance"
  }]
}
```

**Règles** :
- `reference` : Format `Location/{identifier}`
- `display` : Utiliser `name`
- `identifier.value` : Conversion `identifier` en string

---

## 7. Règles de Transformation Générales

### 7.1 Conversion des Dates

**Format source** : `DD/MM/YYYY HH:mm:ss`  
**Format cible** : `YYYY-MM-DDTHH:mm:ss+00:00` (ISO 8601)

**Algorithme** :
```javascript
function convertDate(siDate) {
  const [date, time] = siDate.split(' ');
  const [day, month, year] = date.split('/');
  return `${year}-${month}-${day}T${time}+00:00`;
}
```

**Exemples** :
- Source : `28/10/2025 11:34:46`
- Cible : `2025-10-28T11:34:46+00:00`

### 7.2 Génération du Narrative

**Format requis** :
```html
<div xmlns="http://www.w3.org/1999/xhtml">
  <p><b>{Titre ressource}</b></p>
  <p><b>Status:</b> {status}</p>
  <p><b>Patient:</b> {patient}</p>
</div>
```

**Règles** :
- Status : "generated"
- HTML bien formé avec namespace XHTML
- Résumé concis des informations clés
- Pas de CSS inline ni JavaScript

### 7.3 Construction des Systems

**Format** : `http://eyone.sn/fhir/{type}/{resource-type}`

| Type | Exemple |
|------|---------|
| identifier | http://eyone.sn/fhir/identifier/patient |
| CodeSystem | http://eyone.sn/fhir/CodeSystem/medical-act-category |
| StructureDefinition | http://eyone.sn/fhir/StructureDefinition/ServiceRequest-Analysis |

---

## 8. Matrices de Décision

### 8.1 Ressources à Créer par Type de Message

| messageType | action | status | Ressources FHIR R5 |
|-------------|--------|--------|-------------------|
| UPDATE_PRESTATION | ANALYSIS | SCHEDULED | ServiceRequest + MedicationRequest |
| UPDATE_PRESTATION | ANALYSIS | VALIDATED | ServiceRequest + MedicationRequest + DiagnosticReport |
| UPDATE_PRESTATION | RADIOLOGY | * | ServiceRequest + MedicationRequest |
| UPDATE_PRESCRIPTION_FINANCIERE | CONSULTATION | * | Encounter + MedicationRequest |
| UPDATE_PRESCRIPTION_FINANCIERE | AMBULATORY | * | Encounter + MedicationRequest |

### 8.2 Mapping Statuts par Ressource

| Code SI | ServiceRequest | DiagnosticReport | Encounter |
|---------|----------------|------------------|-----------|
| SCHEDULED | active | - | planned |
| ON_GOING | active | registered | in-progress |
| VALIDATED | completed | final | completed |
| CANCELLED | revoked | cancelled | cancelled |
| CREATED | draft | - | planned |

---

## 9. Validation et Conformité

### 9.1 Checklist de Validation

Avant de considérer une transformation valide :

**Identifiants** :
- [ ] Tous les identifiers ont un system valide
- [ ] Les valeurs sont non-nulles et cohérentes

**Références** :
- [ ] Toutes les références contiennent reference, type, display
- [ ] Les identifier dans les références sont complets

**Dates** :
- [ ] Format ISO 8601 correct
- [ ] Timezone UTC (+00:00)

**Statuts** :
- [ ] Statuts mappés existent dans les ValueSets FHIR
- [ ] Cohérence entre statuts et états de la ressource

**Narrative** :
- [ ] Élément text.div présent
- [ ] HTML bien formé avec namespace XHTML
- [ ] Contenu résume les informations clés

**Structure** :
- [ ] Cardinalités respectées
- [ ] Champs obligatoires présents
- [ ] Types de données corrects

### 9.2 Erreurs Courantes

| Erreur | Cause | Solution |
|--------|-------|----------|
| Invalid date format | Date non ISO 8601 | Appliquer convertDate() |
| Missing reference.type | Référence incomplète | Ajouter type de ressource |
| Invalid status code | Code statut non mappé | Vérifier tableau mapping |
| Malformed XHTML | HTML narrative invalide | Utiliser format simple |
| Missing cardinality | Champ obligatoire absent | Vérifier cardinalités |

---

## 10. Systèmes de Codes

### 10.1 CodeSystems FHIR Standard

| Concept | System | Usage |
|---------|--------|-------|
| Request Status | http://hl7.org/fhir/request-status | ServiceRequest.status |
| Diagnostic Report Status | http://hl7.org/fhir/diagnostic-report-status | DiagnosticReport.status |
| Encounter Status | http://hl7.org/fhir/encounter-status | Encounter.status |
| Medication Request Status | http://hl7.org/fhir/CodeSystem/medicationrequest-status | MedicationRequest.status |
| ActCode | http://terminology.hl7.org/CodeSystem/v3-ActCode | Encounter.class |
| Diagnostic Service | http://terminology.hl7.org/CodeSystem/v2-0074 | DiagnosticReport.category |

### 10.2 CodeSystems Eyone Custom

| Concept | System | Usage |
|---------|--------|-------|
| Medical Act Category | http://eyone.sn/fhir/CodeSystem/medical-act-category | ServiceRequest.category, code |
| Identifier Type | http://eyone.sn/fhir/CodeSystem/identifier-type | identifier.type |

---

## 11. Extensions Eyone (Future)

Les extensions personnalisées Eyone seront documentées dans un document séparé après création des StructureDefinitions correspondantes.

**Référence** : Voir fichier `EXTENSIONS_EYONE_REFERENCE.md`

---

## 12. Annexes

### 12.1 Références

| Document | URL |
|----------|-----|
| FHIR R5 Specification | https://hl7.org/fhir/R5/ |
| ServiceRequest | https://hl7.org/fhir/R5/servicerequest.html |
| Encounter | https://hl7.org/fhir/R5/encounter.html |
| MedicationRequest | https://hl7.org/fhir/R5/medicationrequest.html |
| DiagnosticReport | https://hl7.org/fhir/R5/diagnosticreport.html |

### 12.2 Fichiers Sources Exemple

| Fichier | Description |
|---------|-------------|
| `prestation_analysis_schema.json` | Analyse VALIDATED |
| `prestation_ambulatory.json` | Consultation AMBULATORY |
| `prestation_analysis_fhir_r5.json` | ServiceRequest FHIR R5 exemple |

### 12.3 Contact

**Équipe Technique Eyone**  
Email: tech@eyone.net  
Documentation: https://docs.eyone.sn

---

**Version du document** : 1.0.0  
**Date de création** : 31 Octobre 2025  
**Organisme** : Eyone Health System  
**Compatibilité** : FHIR R5 (5.0.0)