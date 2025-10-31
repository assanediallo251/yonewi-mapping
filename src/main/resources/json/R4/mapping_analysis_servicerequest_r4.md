# Mapping SI ANALYSIS/RADIOLOGY vers ServiceRequest FHIR R4/R5

**Document** : Spécification de Mapping Complète  
**Version** : 1.0.0  
**Date** : 31 Octobre 2025  
**Organisation** : Eyone Health System  
**Standard FHIR** : R4 (4.0.1) et R5 (5.0.0)  
**Système Source** : SI Medical Eyone  
**Message Type** : `UPDATE_PRESTATION`  
**Actions** : `ANALYSIS`, `RADIOLOGY`

---

## Table des Matières

1. [Vue d'Ensemble](#1-vue-densemble)
2. [Mapping SI → FHIR (Commun R4/R5)](#2-mapping-si--fhir-commun-r4r5)
3. [Différences R4 vs R5](#3-différences-r4-vs-r5)
4. [Mapping Détaillé des Champs](#4-mapping-détaillé-des-champs)
5. [Mapping des Extensions](#5-mapping-des-extensions)
6. [Transformations](#6-transformations)
7. [Exemples Complets](#7-exemples-complets)
8. [Conversion R5 ↔ R4](#8-conversion-r5--r4)
9. [Validation](#9-validation)

---

## 1. Vue d'Ensemble

### 1.1 Contexte

**Message source** : `UPDATE_PRESTATION`  
**Actions supportées** : `ANALYSIS` (Analyses médicales), `RADIOLOGY` (Examens radiologiques)  
**Ressource FHIR cible** : `ServiceRequest`  
**Workflow** : Création → SCHEDULED → ON_GOING → VALIDATED

### 1.2 Principes

- **Conservation totale** : Aucune perte de données entre SI et FHIR
- **Bidirectionnalité** : Support R4 et R5 avec conversion possible
- **Extensions** : Données métier Eyone stockées en extensions
- **Références** : Toutes les entités référencées avec identifiers complets

---

## 2. Mapping SI → FHIR (Commun R4/R5)

### 2.1 Vue Synthétique

| Domaine | Source SI | Cible FHIR | Notes |
|---------|-----------|------------|-------|
| **Identification** | data.identifier, eyoneInternalId | id, identifier[] | Mapping direct |
| **Statut** | data.status.code | status | VALIDATED → completed |
| **Type demande** | Fixe | intent | Toujours "order" |
| **Catégorie** | data.medicalActCategory | category[], code | ANALYSIS ou RADIOLOGY |
| **Patient** | data.patient | subject | Référence complète |
| **Prescripteur** | data.doctor | requester | Référence Practitioner |
| **Exécutant** | data.organism | performer[] | Référence Organization |
| **Lieu** | data.relatedService | location / locationReference | **Différence R4/R5** |
| **Dates** | data.startDate, endDate | occurrencePeriod | Conversion format |
| **Extensions** | Multiples | extension[] | ~40 extensions |

---

## 3. Différences R4 vs R5

### 3.1 Tableau Comparatif

| Champ | R4 | R5 | Impact |
|-------|----|----|--------|
| **code** | CodeableConcept | CodeableReference | Structure différente |
| **location** | `locationReference[]` | `location[]` | **Nom changé** |
| **location structure** | Reference avec identifier, display | Reference simple | **Simplification en R5** |

### 3.2 Détail des Changements

#### 3.2.1 Code (Type d'examen)

**R4** : CodeableConcept direct
```json
{
  "code": {
    "coding": [
      {
        "system": "http://eyone.sn/fhir/CodeSystem/medical-act",
        "code": "ANALYSIS",
        "display": "Analyse"
      }
    ]
  }
}
```

**R5** : CodeableReference avec concept
```json
{
  "code": {
    "concept": {
      "coding": [
        {
          "system": "http://eyone.sn/fhir/CodeSystem/medical-act",
          "code": "ANALYSIS",
          "display": "Analyse"
        }
      ]
    }
  }
}
```

#### 3.2.2 Location (Lieu d'exécution)

**R4** : `locationReference[]` avec structure complète
```json
{
  "locationReference": [
    {
      "reference": "Location/13853",
      "identifier": {
        "system": "http://eyone.sn/fhir/identifier/location",
        "value": "13853"
      },
      "display": "Centre de Santé Renaissance"
    }
  ]
}
```

**R5** : `location[]` simplifié (identifier/display en extension)
```json
{
  "location": [
    {
      "reference": {
        "reference": "Location/13853"
      }
    }
  ],
  "extension": [
    {
      "url": "http://eyone.sn/fhir/StructureDefinition/locationDetails",
      "extension": [
        {
          "url": "identifier",
          "valueString": "13853"
        },
        {
          "url": "display",
          "valueString": "Centre de Santé Renaissance"
        }
      ]
    }
  ]
}
```

---

## 4. Mapping Détaillé des Champs

### 4.1 Métadonnées et Identifiants

| Champ SI | Chemin SI | Type SI | Chemin FHIR R4/R5 | Type FHIR | Transformation |
|----------|-----------|---------|-------------------|-----------|----------------|
| **Ressource** |
| Type | - | - | `resourceType` | string | Fixe: "ServiceRequest" |
| ID | `data.eyoneInternalId` | string | `id` | id | Direct |
| Version | - | - | `meta.versionId` | string | Fixe: "1" |
| Dernière MAJ | `data.creationDate` | string | `meta.lastUpdated` | instant | Conversion ISO 8601 |
| Profil | - | - | `meta.profile[0]` | canonical | Fixe: ".../ServiceRequest-Analysis" |
| **Identifiants** |
| ID prestation | `data.identifier` | integer | `identifier[0].value` | string | toString() |
| Système | - | - | `identifier[0].system` | uri | "...identifier/prestation" |
| ID interne | `data.eyoneInternalId` | string | `identifier[1].value` | string | Direct |
| Système | - | - | `identifier[1].system` | uri | "...identifier/eyoneInternalId" |
| ID personnalisé | `data.customId` | string | `identifier[2].value` | string | Direct |
| Système | - | - | `identifier[2].system` | uri | "...identifier/customId" |
| ID organisme | `organismId` | string | `identifier[3].value` | string | Direct |
| Système | - | - | `identifier[3].system` | uri | "...identifier/organism" |

### 4.2 Statut et Classification

| Champ SI | Chemin SI | Chemin FHIR | Transformation |
|----------|-----------|-------------|----------------|
| **Statut** |
| Code statut | `data.status.code` | `status` | Mapping (voir 4.2.1) |
| **Intention** |
| - | - | `intent` | Fixe: "order" |
| **Catégorie** |
| Code catégorie | `data.medicalActCategory.code` | `category[0].coding[0].code` | Direct |
| Libellé | `data.medicalActCategory.label` | `category[0].coding[0].display` | Direct |
| Système | - | `category[0].coding[0].system` | "...CodeSystem/medical-act-category" |
| **Priorité** |
| - | - | `priority` | Fixe: "routine" |
| **Code acte** |
| Code | `data.medicalActCategory.code` | `code` | Voir 3.2.1 (diff R4/R5) |
| Libellé | `data.medicalActCategory.label` | `code` | Voir 3.2.1 (diff R4/R5) |

#### 4.2.1 Mapping des Statuts

| Code SI | Label SI | Code FHIR | Description |
|---------|----------|-----------|-------------|
| `SCHEDULED` | Programmé(e) | `active` | Demande active en attente |
| `ON_GOING` | En cours | `active` | Demande en cours d'exécution |
| `VALIDATED` | Validé(e) | `completed` | Demande terminée |
| `CANCELLED` | Annulé(e) | `revoked` | Demande annulée |
| `CREATED` | Créé(e) | `draft` | Demande en préparation |

### 4.3 Références

| Champ SI | Chemin SI | Chemin FHIR | Transformation |
|----------|-----------|-------------|----------------|
| **Patient** |
| Référence | `data.patient.identifier` | `subject.reference` | "Patient/{identifier}" |
| Identifier | `data.patient.identifier` | `subject.identifier.value` | toString() |
| Système | - | `subject.identifier.system` | "...identifier/patient" |
| Nom | `data.patient.firstName + lastName` | `subject.display` | Concaténation |
| **Prescripteur** |
| Référence | `data.doctor.identifier` | `requester.reference` | "Practitioner/{identifier}" |
| Identifier | `data.doctor.identifier` | `requester.identifier.value` | toString() |
| Système | - | `requester.identifier.system` | "...identifier/practitioner" |
| Nom | `data.doctor.doctorCompleteName` | `requester.display` | Direct |
| **Exécutant** |
| Référence | `data.organism.identifier` | `performer[0].reference` | "Organization/{identifier}" |
| Identifier | `data.organism.identifier` | `performer[0].identifier.value` | toString() |
| Système | - | `performer[0].identifier.system` | "...identifier/organization" |
| Nom | `data.organism.name` | `performer[0].display` | Direct |
| **Lieu** |
| R4 | `data.relatedService` | `locationReference[]` | Structure complète |
| R5 | `data.relatedService` | `location[]` + extension | Structure simplifiée |

### 4.4 Dates et Notes

| Champ SI | Chemin SI | Chemin FHIR | Transformation |
|----------|-----------|-------------|----------------|
| **Date création** |
| Date | `data.creationDate` | `authoredOn` | Conversion ISO 8601 |
| **Période** |
| Début | `data.startDate` | `occurrencePeriod.start` | Conversion ISO 8601 |
| Fin | `data.endDate` | `occurrencePeriod.end` | Conversion ISO 8601 |
| **Note** |
| Auteur | `createdBy` | `note[0].authorString` | Direct |
| Date | `data.creationDate` | `note[0].time` | Conversion ISO 8601 |
| Texte | Calculé | `note[0].text` | "Prestation créée par {createdBy}" |

### 4.5 Narrative

| Champ | Valeur |
|-------|--------|
| `text.status` | "generated" |
| `text.div` | HTML généré (voir 6.1) |

---

## 5. Mapping des Extensions

### 5.1 Extensions Message/Workflow

| Champ SI | Chemin SI | Extension URL | Value Type |
|----------|-----------|---------------|------------|
| Type message | `messageType` | `.../messageType` | valueString |
| Clé message | `messageKey` | `.../messageKey` | valueString |
| Action | `action` | `.../action` | valueString |
| Origine | `origin` | `.../origin` | valueString |
| Statut message | `status` | `.../messageStatus` | valueString |
| Créateur | `createdBy` | `.../createdBy` | valueString |
| Date création | `data.creationDate` | `.../creationDate` | valueDateTime |

### 5.2 Extension Catégorie d'Acte Médical

**URL** : `http://eyone.sn/fhir/StructureDefinition/medicalActCategory`

| Champ SI | Chemin SI | Sub-extension URL |
|----------|-----------|-------------------|
| Code | `data.medicalActCategory.code` | `code` |
| Libellé | `data.medicalActCategory.label` | `label` |
| Évacuation | `.evacuation` | `evacuation` |
| Modèle item | `.modeleItem` | `modeleItem` |
| Examen médical | `.medicalExam` | `medicalExam` |
| Pharmacie | `.pharmacyAsPrestation` | `pharmacyAsPrestation` |
| Hospitalisation | `.hospitalization` | `hospitalization` |
| Analyse | `.analysis` | `analysis` |
| Radiologie | `.radiology` | `radiology` |
| Devis | `.quote` | `quote` |
| Tous | `.all` | `all` |
| Éditable | `.editable` | `editable` |
| Chambre | `.room` | `room` |
| Médicament | `.medoc` | `medoc` |
| Modèle | `.modele` | `modele` |
| Visite | `.visit` | `visit` |
| Ambulatoire | `.ambulatory` | `ambulatory` |
| Consultation | `.consultation` | `consultation` |

### 5.3 Extension Détails du Statut

**URL** : `http://eyone.sn/fhir/StructureDefinition/statusDetails`

| Champ SI | Chemin SI | Sub-extension URL |
|----------|-----------|-------------------|
| Code | `data.status.code` | `code` |
| Libellé | `data.status.label` | `label` |
| Terminé | `.done` | `done` |
| Créé | `.created` | `created` |
| Annulé | `.cancelled` | `cancelled` |
| À confirmer | `.toBeConfirmed` | `toBeConfirmed` |
| Non accepté | `.notAccepted` | `notAccepted` |
| En cours | `.onGoing` | `onGoing` |
| Indisponible | `.unavailable` | `unavailable` |
| Non effectué | `.notPerformed` | `notPerformed` |

### 5.4 Extension Éditeur

**URL** : `http://eyone.sn/fhir/StructureDefinition/editor`

Structure complexe avec sous-extensions :
- `organism` : Organisme de l'éditeur (identifier, name)
- `userId`, `userName`, `completeName` : Infos utilisateur
- `identifier` : ID de l'éditeur
- `doctor` : Boolean indiquant si médecin
- `completeNameAndUserName` : Nom complet
- `userDetailsInfos` : Détails (hasSuperviseurRight, doctor)

### 5.5 Extension Docteur

**URL** : `http://eyone.sn/fhir/StructureDefinition/doctor`

Sous-extensions :
- `identifier`, `doctorId` : Identifiants
- `doctorUsername`, `doctorCompleteName` : Infos
- `ordonanceByDoctor`, `facturedByDoctor` : Booleans

### 5.6 Extension Dossier Médical

**URL** : `http://eyone.sn/fhir/StructureDefinition/medicalRecordFile`

36+ sous-extensions incluant :
- Facteurs de risque : hta, smoke, diabete, cholesterol, etc.
- Examens : doMapa, doEcg, doBiologie, etc.
- IMC : imc (decimal)
- Examens cliniques : hasGeneralExam, hasCardioVascularyExam, etc.

### 5.7 Extension Pharmacie

**URL** : `http://eyone.sn/fhir/StructureDefinition/pharmacySellPrestation`

Sous-extensions :
- `identifier`, `disable`, `nbrProducts`, `commentary`
- `pharmacyBillingMode` : Sous-extension complexe (code, label, scope, global, detail)
- `usedAsAPrestation`, `manageStock`, `documentGenerationRequired2`

### 5.8 Extension Service Associé

**URL** : `http://eyone.sn/fhir/StructureDefinition/relatedService`

Sous-extensions :
- `identifier` : ID du service
- `name` : Nom du service

### 5.9 Extension Claims

**URL** : `http://eyone.sn/fhir/StructureDefinition/claims`

Sous-extensions :
- `endpoint` : URL endpoint
- `method` : Nom de la méthode
- `class` : Classe Java complète

### 5.10 Extensions Booléennes Simples

| Extension URL | Champ SI |
|---------------|----------|
| `.../toothPrestation` | `data.toothPrestation` |
| `.../firstMedicalSellPrestation` | `data.firstMedicalSellPrestation` |
| `.../firstConsultation` | `data.firstConsultation` |
| `.../telePrestation` | `data.telePrestation` |
| `.../createByQueue` | `data.createByQueue` |

### 5.11 Extensions Simples Autres

| Extension URL | Champ SI | Value Type |
|---------------|----------|------------|
| `.../toothIndice` | `data.toothIndice` | valueInteger |
| `.../documentIssuerStatus` | `data.documentIssuerStatus` | valueString |

### 5.12 Extension Location Details (R5 uniquement)

**URL** : `http://eyone.sn/fhir/StructureDefinition/locationDetails`

Sous-extensions :
- `identifier` : ID location
- `display` : Nom location

---

## 6. Transformations

### 6.1 Génération du Narrative

**Algorithme** :
```javascript
function generateNarrative(data) {
  const patientName = `${data.patient.firstName} ${data.patient.lastName}`;
  const actType = data.medicalActCategory.label;
  const doctorName = data.doctor?.doctorCompleteName || data.createdBy;
  const organismName = data.organism.name;
  
  return `<div xmlns="http://www.w3.org/1999/xhtml">
    <p><b>${actType}</b></p>
    <p><b>Patient:</b> ${patientName}</p>
    <p><b>Demandé par:</b> ${doctorName}</p>
    <p><b>Centre de Santé:</b> ${organismName}</p>
  </div>`;
}
```

**Sortie** :
```html
<div xmlns="http://www.w3.org/1999/xhtml">
  <p><b>Analyse médicale</b></p>
  <p><b>Patient:</b> MAME MARAME VICTORINE NDIAYE</p>
  <p><b>Demandé par:</b> TEST DOCTEUR</p>
  <p><b>Centre de Santé:</b> Centre de Santé Renaissance</p>
</div>
```

### 6.2 Conversion des Dates

**Format source** : `DD/MM/YYYY HH:mm:ss`  
**Format cible** : `YYYY-MM-DDTHH:mm:ss+00:00`

```javascript
function convertDateTime(siDate) {
  if (!siDate) return null;
  const [datePart, timePart] = siDate.split(' ');
  const [day, month, year] = datePart.split('/');
  return `${year}-${month}-${day}T${timePart}+00:00`;
}
```

**Exemples** :
- `28/10/2025 11:34:46` → `2025-10-28T11:34:46+00:00`

---

## 7. Exemples Complets

### 7.1 Source SI (Simplifié)

```json
{
  "messageType": "UPDATE_PRESTATION",
  "messageKey": "ANALYSIS_ANLMED202500012_13853",
  "organismId": "13853",
  "action": "ANALYSIS",
  "createdBy": "TEST DOCTEUR",
  "creationDate": "28/10/2025 11:34:46",
  "data": {
    "identifier": 38162,
    "eyoneInternalId": "ANLMED202500012",
    "customId": "ANLMED202500012",
    "medicalActCategory": {
      "code": "ANALYSIS",
      "label": "Analyse",
      "analysis": true
    },
    "status": {
      "code": "VALIDATED",
      "label": "Validé(e)"
    },
    "patient": {
      "identifier": 14554,
      "eyoneInternalId": "PCSR202500004",
      "firstName": "MAME MARAME VICTORINE",
      "lastName": "NDIAYE"
    },
    "doctor": {
      "identifier": 14356,
      "doctorCompleteName": "TEST DOCTEUR"
    },
    "organism": {
      "identifier": 13853,
      "name": "Centre de Santé Renaissance"
    },
    "startDate": "28/10/2025 11:34:46",
    "endDate": "28/10/2025 11:34:46"
  }
}
```

### 7.2 Cible FHIR R4

```json
{
  "resourceType": "ServiceRequest",
  "id": "ANLMED202500012",
  "identifier": [
    {
      "system": "http://eyone.sn/fhir/identifier/prestation",
      "value": "38162"
    },
    {
      "system": "http://eyone.sn/fhir/identifier/eyoneInternalId",
      "value": "ANLMED202500012"
    }
  ],
  "status": "completed",
  "intent": "order",
  "category": [
    {
      "coding": [
        {
          "system": "http://eyone.sn/fhir/CodeSystem/medical-act-category",
          "code": "ANALYSIS",
          "display": "Analyse"
        }
      ]
    }
  ],
  "code": {
    "coding": [
      {
        "system": "http://eyone.sn/fhir/CodeSystem/medical-act",
        "code": "ANALYSIS",
        "display": "Analyse"
      }
    ]
  },
  "subject": {
    "reference": "Patient/14554",
    "display": "MAME MARAME VICTORINE NDIAYE"
  },
  "requester": {
    "reference": "Practitioner/14356",
    "display": "TEST DOCTEUR"
  },
  "performer": [
    {
      "reference": "Organization/13853",
      "display": "Centre de Santé Renaissance"
    }
  ],
  "locationReference": [
    {
      "reference": "Location/13853",
      "display": "Centre de Santé Renaissance"
    }
  ]
}
```

### 7.3 Cible FHIR R5

```json
{
  "resourceType": "ServiceRequest",
  "id": "ANLMED202500012",
  "identifier": [
    {
      "system": "http://eyone.sn/fhir/identifier/prestation",
      "value": "38162"
    },
    {
      "system": "http://eyone.sn/fhir/identifier/eyoneInternalId",
      "value": "ANLMED202500012"
    }
  ],
  "status": "completed",
  "intent": "order",
  "category": [
    {
      "coding": [
        {
          "system": "http://eyone.sn/fhir/CodeSystem/medical-act-category",
          "code": "ANALYSIS",
          "display": "Analyse"
        }
      ]
    }
  ],
  "code": {
    "concept": {
      "coding": [
        {
          "system": "http://eyone.sn/fhir/CodeSystem/medical-act",
          "code": "ANALYSIS",
          "display": "Analyse"
        }
      ]
    }
  },
  "subject": {
    "reference": "Patient/14554",
    "display": "MAME MARAME VICTORINE NDIAYE"
  },
  "requester": {
    "reference": "Practitioner/14356",
    "display": "TEST DOCTEUR"
  },
  "performer": [
    {
      "reference": "Organization/13853",
      "display": "Centre de Santé Renaissance"
    }
  ],
  "location": [
    {
      "reference": {
        "reference": "Location/13853"
      }
    }
  ]
}
```

---

## 8. Conversion R5 ↔ R4

### 8.1 Algorithme R5 → R4

```javascript
function convertServiceRequestR5ToR4(r5) {
  const r4 = { ...r5 };
  
  // 1. Convert code: CodeableReference → CodeableConcept
  if (r5.code?.concept) {
    r4.code = r5.code.concept;
  }
  
  // 2. Rename location → locationReference
  if (r5.location) {
    r4.locationReference = r5.location.map(loc => {
      // Extract from extension if present
      const locationExt = r5.extension?.find(
        ext => ext.url === "http://eyone.sn/fhir/StructureDefinition/locationDetails"
      );
      
      if (locationExt) {
        const identifier = locationExt.extension?.find(e => e.url === "identifier")?.valueString;
        const display = locationExt.extension?.find(e => e.url === "display")?.valueString;
        
        return {
          reference: loc.reference?.reference || loc.reference,
          identifier: identifier ? {
            system: "http://eyone.sn/fhir/identifier/location",
            value: identifier
          } : undefined,
          display: display
        };
      }
      
      return {
        reference: loc.reference?.reference || loc.reference
      };
    });
    delete r4.location;
  }
  
  // 3. Remove R5-specific extension
  if (r4.extension) {
    r4.extension = r4.extension.filter(
      ext => ext.url !== "http://eyone.sn/fhir/StructureDefinition/locationDetails"
    );
  }
  
  return r4;
}
```

### 8.2 Algorithme R4 → R5

```javascript
function convertServiceRequestR4ToR5(r4) {
  const r5 = { ...r4 };
  
  // 1. Convert code: CodeableConcept → CodeableReference
  if (r4.code && !r4.code.concept) {
    r5.code = {
      concept: r4.code
    };
  }
  
  // 2. Rename locationReference → location
  if (r4.locationReference) {
    // Extract identifier and display for extension
    const locationDetails = r4.locationReference.map(loc => ({
      identifier: loc.identifier?.value,
      display: loc.display
    }));
    
    // Simplified location
    r5.location = r4.locationReference.map(loc => ({
      reference: {
        reference: loc.reference
      }
    }));
    
    // Add extension for details
    if (locationDetails.some(ld => ld.identifier || ld.display)) {
      r5.extension = r5.extension || [];
      r5.extension.push({
        url: "http://eyone.sn/fhir/StructureDefinition/locationDetails",
        extension: locationDetails.filter(ld => ld.identifier || ld.display).map(ld => ({
          extension: [
            ld.identifier ? { url: "identifier", valueString: ld.identifier } : null,
            ld.display ? { url: "display", valueString: ld.display } : null
          ].filter(Boolean)
        }))[0].extension
      });
    }
    
    delete r5.locationReference;
  }
  
  return r5;
}
```

---

## 9. Validation

### 9.1 Checklist R4

- [ ] `resourceType` = "ServiceRequest"
- [ ] `status` valide (draft, active, completed, revoked)
- [ ] `intent` = "order"
- [ ] `code` est un CodeableConcept
- [ ] `locationReference` (pas `location`)
- [ ] Toutes les références ont identifier et display
- [ ] Dates au format ISO 8601
- [ ] Extensions présentes

### 9.2 Checklist R5

- [ ] `resourceType` = "ServiceRequest"
- [ ] `status` valide
- [ ] `intent` = "order"
- [ ] `code.concept` existe (CodeableReference)
- [ ] `location` (pas `locationReference`)
- [ ] Extension `locationDetails` si nécessaire
- [ ] Dates au format ISO 8601
- [ ] Extensions présentes

### 9.3 Checklist Conversion

- [ ] `code` structure convertie correctement
- [ ] `location` / `locationReference` renommé
- [ ] Extension `locationDetails` gérée
- [ ] Aucune perte de données
- [ ] Extensions personnalisées préservées

---

## 10. Références

| Ressource | URL |
|-----------|-----|
| FHIR R4 ServiceRequest | https://hl7.org/fhir/R4/servicerequest.html |
| FHIR R5 ServiceRequest | https://hl7.org/fhir/R5/servicerequest.html |
| R4 to R5 Diff | https://hl7.org/fhir/R5/diff.html |

---

**Version** : 1.0.0  
**Date** : 31 Octobre 2025  
**Auteur** : Eyone Technical Team  
**Statut** : Final