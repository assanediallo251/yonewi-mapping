# Documentation - Prestation Analysis Schema

## Vue d'Ensemble

Ce document décrit le schéma de données pour les prestations de type **ANALYSIS** (Analyse médicale) et **RADIOLOGY** (Radiologie). Ces prestations suivent un workflow médical avec prescription et validation.

---

## Types de Prestations Couvertes

### Prestations Principales
- **ANALYSIS** (Analyse) - `caredMedicalSellPrestationType: true`
- **RADIOLOGY** (Radiologie) - `caredMedicalSellPrestationType: true`

### Caractéristiques Communes
- Workflow : SCHEDULED → Exécution → VALIDATED
- Origine : MEDICAL_SI_BOOT_PATIENT_FILE
- Type de message : UPDATE_PRESTATION
- Prescription par un médecin requise

---

## Workflow

```
┌─────────────────┐
│  1. SCHEDULED   │  ← Prescription par le médecin
│  (Programmé)    │     Action: handleMedicalSellPrestation
└────────┬────────┘     Endpoint: /assignments
         │
         ▼
┌─────────────────┐
│  2. EXECUTION   │  ← Réalisation de l'analyse au laboratoire
│  (Au labo)      │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  3. VALIDATED   │  ← Validation des résultats
│  (Validé)       │     Action: changePrestationStatus
└────────┬────────┘     Endpoint: /medical-workflow-status
         │
         ▼
┌─────────────────┐
│  4. BILLING     │  ← Facturation ultérieure
│  (Facturation)  │
└─────────────────┘
```

---

## Structure du Document

### Niveau Racine (11 champs)

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `messageType` | string | ✅ | Type de message - Valeur: "UPDATE_PRESTATION" |
| `messageKey` | string | ✅ | Clé unique du message - Format: "ANALYSIS_{eyoneInternalId}_{organismId}" |
| `organismId` | string | ✅ | Identifiant de l'organisme |
| `organismName` | string | ✅ | Nom de l'organisme |
| `action` | string | ✅ | Action - Valeur: "ANALYSIS" |
| `createdBy` | string | ✅ | Nom du créateur |
| `creationDate` | string | ✅ | Date de création - Format: "DD/MM/YYYY HH:mm:ss" |
| `data` | object | ✅ | Données principales de la prestation |
| `origin` | string | ✅ | Origine - Valeur: "MEDICAL_SI_BOOT_PATIENT_FILE" |
| `status` | string | ✅ | Statut du message - Valeur: "PENDING" |
| `claims` | object | ✅ | Actions à effectuer côté serveur |

---

## Objet `data` (37 champs de premier niveau)

### Identifiants

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `identifier` | number | ✅ | ID unique de la prestation |
| `eyoneInternalId` | string | ✅ | ID interne Eyone - Format: "ANLMED{année}{numéro}" |
| `customId` | string | ✅ | ID personnalisé (généralement identique à eyoneInternalId) |

### Informations de Création

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `createdBy` | string | ✅ | Nom du créateur |
| `creationDate` | string | ✅ | Date de création - Format: "DD/MM/YYYY HH:mm:ss" |

### Organismes et Services

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `organism` | object | ✅ | Organisme de santé |
| `organism.identifier` | number | ✅ | ID de l'organisme |
| `organism.name` | string | ✅ | Nom de l'organisme |
| `relatedService` | object | ✅ | Service lié (souvent identique à organism) |
| `relatedService.identifier` | number | ✅ | ID du service |
| `relatedService.name` | string | ✅ | Nom du service |

### Catégorie d'Acte Médical

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `medicalActCategory` | object | ✅ | Catégorie de l'acte médical |
| `medicalActCategory.code` | string | ✅ | Code - Valeur: "ANALYSIS" |
| `medicalActCategory.label` | string | ✅ | Libellé - Valeur: "Analyse" |
| `medicalActCategory.evacuation` | boolean | ✅ | false |
| `medicalActCategory.modeleItem` | boolean | ✅ | false |
| `medicalActCategory.medicalExam` | boolean | ✅ | true |
| `medicalActCategory.pharmacyAsPrestation` | boolean | ✅ | false |
| `medicalActCategory.hospitalization` | boolean | ✅ | false |
| `medicalActCategory.analysis` | boolean | ✅ | true |
| `medicalActCategory.radiology` | boolean | ✅ | false |
| `medicalActCategory.quote` | boolean | ✅ | false |
| `medicalActCategory.all` | boolean | ✅ | false |
| `medicalActCategory.editable` | boolean | ✅ | false |
| `medicalActCategory.room` | boolean | ✅ | false |
| `medicalActCategory.medoc` | boolean | ✅ | false |
| `medicalActCategory.modele` | boolean | ✅ | false |
| `medicalActCategory.visit` | boolean | ✅ | false |
| `medicalActCategory.ambulatory` | boolean | ✅ | false |
| `medicalActCategory.consultation` | boolean | ✅ | false |

### Statut de la Prestation

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `status` | object | ✅ | Statut de la prestation |
| `status.code` | string | ✅ | Code - Valeurs: "SCHEDULED", "VALIDATED" |
| `status.label` | string | ✅ | Libellé - Ex: "Programmé(e)", "Validé(e)" |
| `status.done` | boolean | ✅ | Prestation terminée |
| `status.created` | boolean | ✅ | Prestation créée |
| `status.cancelled` | boolean | ✅ | Prestation annulée |
| `status.toBeConfirmed` | boolean | ✅ | À confirmer |
| `status.notAccepted` | boolean | ✅ | Non acceptée |
| `status.onGoing` | boolean | ✅ | En cours |
| `status.unavailable` | boolean | ✅ | Indisponible |
| `status.notPerformed` | boolean | ✅ | Non effectuée |

### Patient

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `patient` | object | ✅ | Informations du patient |
| `patient.identifier` | number | ✅ | ID du patient |
| `patient.eyoneInternalId` | string | ✅ | ID interne Eyone du patient |
| `patient.firstName` | string | ✅ | Prénom |
| `patient.lastName` | string | ✅ | Nom |
| `patient.sex` | object | ✅ | Sexe du patient |
| `patient.sex.code` | string | ✅ | Code - Valeurs: "male", "female" |
| `patient.sex.label` | string | ✅ | Libellé - Valeurs: "M", "F" |

### Dates

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `startDate` | string | ✅ | Date de début - Format: "DD/MM/YYYY HH:mm:ss" |
| `endDate` | string | ✅ | Date de fin - Format: "DD/MM/YYYY HH:mm:ss" |

### Prestation Pharmacie

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `pharmacySellPrestation` | object | ✅ | Prestation pharmacie associée |
| `pharmacySellPrestation.identifier` | number | ✅ | ID de la prestation pharmacie |
| `pharmacySellPrestation.disable` | boolean | ✅ | Désactivée |
| `pharmacySellPrestation.nbrProducts` | number | ✅ | Nombre de produits |
| `pharmacySellPrestation.commentary` | string | ✅ | Commentaire |
| `pharmacySellPrestation.pharmacyBillingMode` | object | ✅ | Mode de facturation |
| `pharmacySellPrestation.pharmacyBillingMode.code` | string | ✅ | Code - Ex: "DETAIL" |
| `pharmacySellPrestation.pharmacyBillingMode.label` | string | ✅ | Libellé |
| `pharmacySellPrestation.pharmacyBillingMode.scope` | string | ✅ | Portée |
| `pharmacySellPrestation.pharmacyBillingMode.global` | boolean | ✅ | Mode global |
| `pharmacySellPrestation.pharmacyBillingMode.detail` | boolean | ✅ | Mode détaillé |
| `pharmacySellPrestation.usedAsAPrestation` | boolean | ✅ | Utilisé comme prestation |
| `pharmacySellPrestation.manageStock` | boolean | ✅ | Gestion du stock |
| `pharmacySellPrestation.documentGenerationRequired2` | boolean | ✅ | Génération de document requise |

### Éditeur

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `editor` | object | ✅ | Utilisateur ayant édité la prestation |
| `editor.organism` | object | ✅ | Organisme de l'éditeur |
| `editor.organism.identifier` | number | ✅ | ID de l'organisme |
| `editor.organism.name` | string | ✅ | Nom de l'organisme |
| `editor.userId` | number | ✅ | ID de l'utilisateur |
| `editor.userName` | string | ✅ | Nom d'utilisateur (email) |
| `editor.completeName` | string | ✅ | Nom complet |
| `editor.userDetailsInfos` | object | ✅ | Informations détaillées |
| `editor.userDetailsInfos.hasSuperviseurRight` | boolean | ✅ | Droits superviseur |
| `editor.userDetailsInfos.doctor` | boolean | ✅ | Est un docteur |
| `editor.identifier` | number | ✅ | Identifiant |
| `editor.doctor` | boolean | ❌ | Est un docteur (optionnel) |
| `editor.completeNameAndUserName` | string | ✅ | Nom complet et username |

### Médecin Prescripteur

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `doctor` | object | ✅ | Médecin prescripteur |
| `doctor.identifier` | number | ✅ | ID du médecin |
| `doctor.doctorId` | number | ✅ | ID du médecin (doublon) |
| `doctor.doctorUsername` | string | ✅ | Username du médecin (email) |
| `doctor.doctorCompleteName` | string | ✅ | Nom complet du médecin |
| `doctor.ordonanceByDoctor` | boolean | ✅ | Ordonnance par le médecin |
| `doctor.facturedByDoctor` | boolean | ✅ | Facturé par le médecin |

### Dossier Médical

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `medicalRecordFile` | object | ✅ | Dossier médical du patient |
| `medicalRecordFile.identifier` | number | ✅ | ID du dossier médical |
| `medicalRecordFile.hta` | boolean | ✅ | Hypertension artérielle |
| `medicalRecordFile.smoke` | boolean | ✅ | Fumeur |
| `medicalRecordFile.cop` | boolean | ✅ | BPCO |
| `medicalRecordFile.makeSport` | boolean | ✅ | Fait du sport |
| `medicalRecordFile.cholesterol` | boolean | ✅ | Cholestérol |
| `medicalRecordFile.stress` | boolean | ✅ | Stress |
| `medicalRecordFile.diabete` | boolean | ✅ | Diabète |
| `medicalRecordFile.heredity` | boolean | ✅ | Hérédité |
| `medicalRecordFile.excessWeight` | boolean | ✅ | Surpoids |
| `medicalRecordFile.asthma` | boolean | ✅ | Asthme |
| `medicalRecordFile.inactivity` | boolean | ✅ | Inactivité |
| `medicalRecordFile.saos` | boolean | ✅ | SAOS |
| `medicalRecordFile.preeclampsie` | boolean | ✅ | Prééclampsie |
| `medicalRecordFile.doMapa` | boolean | ✅ | Faire MAPA |
| `medicalRecordFile.doEcg` | boolean | ✅ | Faire ECG |
| `medicalRecordFile.doBiologie` | boolean | ✅ | Faire biologie |
| `medicalRecordFile.doHolterEcg` | boolean | ✅ | Faire Holter ECG |
| `medicalRecordFile.doECHOCARDIOGRAPHIE` | boolean | ✅ | Faire échocardiographie |
| `medicalRecordFile.imc` | number | ✅ | IMC (Indice de Masse Corporelle) |
| `medicalRecordFile.relatedMedicalSellPrestationId` | number | ✅ | ID de la prestation liée |
| `medicalRecordFile.hasGeneralExam` | boolean | ✅ | Examen général |
| `medicalRecordFile.hasCardioVascularyExam` | boolean | ✅ | Examen cardiovasculaire |
| `medicalRecordFile.hasBreathingExam` | boolean | ✅ | Examen respiratoire |
| `medicalRecordFile.hasDigestifExam` | boolean | ✅ | Examen digestif |
| `medicalRecordFile.hasUrologyExam` | boolean | ✅ | Examen urologique |
| `medicalRecordFile.hasGynecologyExam` | boolean | ✅ | Examen gynécologique |
| `medicalRecordFile.hasSplenoGanglionaireExam` | boolean | ✅ | Examen spléno-ganglionnaire |
| `medicalRecordFile.hasLocomoteurExam` | boolean | ✅ | Examen locomoteur |
| `medicalRecordFile.hasNeurologicalExam` | boolean | ✅ | Examen neurologique |
| `medicalRecordFile.hasBuccodentaireExam` | boolean | ✅ | Examen buccodentaire |
| `medicalRecordFile.hasDermatoExam` | boolean | ✅ | Examen dermatologique |
| `medicalRecordFile.hasPilgrimFile` | boolean | ✅ | Dossier pèlerin |
| `medicalRecordFile.hasGenitalExam` | boolean | ✅ | Examen génital |
| `medicalRecordFile.referTo` | boolean | ✅ | Référer à |

### Champs Additionnels

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `toothPrestation` | boolean | ✅ | Prestation dentaire |
| `toothIndice` | number | ✅ | Indice dentaire |
| `firstMedicalSellPrestation` | boolean | ✅ | Première prestation médicale |
| `firstConsultation` | boolean | ✅ | Première consultation |
| `telePrestation` | boolean | ✅ | Téléprestation |
| `createByQueue` | boolean | ✅ | Créée par file d'attente |
| `documentIssuerStatus` | string | ✅ | Statut d'émission de document - Ex: "ON_GOING" |

---

## Objet `claims` (3 champs)

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `endpoint` | string | ✅ | Point de terminaison API - Valeurs: "/assignments", "/medical-workflow-status" |
| `method` | string | ✅ | Méthode à appeler - Valeurs: "handleMedicalSellPrestation", "changePrestationStatus" |
| `class` | string | ✅ | Classe du contrôleur - Valeur: "com.eyone.sipatientfile.controller.prestation.MedicalSellPrestationController" |

---

## Statuts Possibles

### Code de Statut Principal (`status.code`)

| Code | Label | Description | Workflow |
|------|-------|-------------|----------|
| `SCHEDULED` | Programmé(e) | Analyse programmée, en attente d'exécution | Étape 1 |
| `VALIDATED` | Validé(e) | Analyse effectuée et validée | Étape 3 |

### Transitions de Statut

```
SCHEDULED → [Exécution au labo] → VALIDATED
```

---

## Actions et Endpoints

### Action: Créer/Assigner une Prestation

**Statut concerné**: SCHEDULED

```json
{
  "claims": {
    "endpoint": "/assignments",
    "method": "handleMedicalSellPrestation",
    "class": "com.eyone.sipatientfile.controller.prestation.MedicalSellPrestationController"
  }
}
```

### Action: Changer le Statut

**Statut concerné**: VALIDATED

```json
{
  "claims": {
    "endpoint": "/medical-workflow-status",
    "method": "changePrestationStatus",
    "class": "com.eyone.sipatientfile.controller.prestation.MedicalSellPrestationController"
  }
}
```

---

## Exemples d'Utilisation

### Créer une Nouvelle Analyse (SCHEDULED)

```json
{
  "messageType": "UPDATE_PRESTATION",
  "messageKey": "ANALYSIS_ANLMED202500010_13853",
  "organismId": "13853",
  "action": "ANALYSIS",
  "data": {
    "identifier": 38151,
    "eyoneInternalId": "ANLMED202500010",
    "status": {
      "code": "SCHEDULED",
      "label": "Programmé(e)"
    },
    "patient": {
      "identifier": 19001,
      "firstName": "ALIOUNE",
      "lastName": "NDIAYE"
    },
    "doctor": {
      "identifier": 14356,
      "doctorCompleteName": "TEST DOCTEUR"
    }
  },
  "claims": {
    "endpoint": "/assignments",
    "method": "handleMedicalSellPrestation"
  }
}
```

### Valider une Analyse Existante (VALIDATED)

```json
{
  "messageType": "UPDATE_PRESTATION",
  "messageKey": "ANALYSIS_ANLMED202500012_13853",
  "organismId": "13853",
  "action": "ANALYSIS",
  "data": {
    "identifier": 38162,
    "eyoneInternalId": "ANLMED202500012",
    "status": {
      "code": "VALIDATED",
      "label": "Validé(e)"
    },
    "patient": {
      "identifier": 14554,
      "firstName": "MAME MARAME VICTORINE",
      "lastName": "NDIAYE"
    }
  },
  "claims": {
    "endpoint": "/medical-workflow-status",
    "method": "changePrestationStatus"
  }
}
```

---

## Règles de Validation

### Champs Requis

✅ Tous les champs marqués comme "Requis" doivent être présents

### Contraintes de Valeurs

- `messageType` doit être exactement "UPDATE_PRESTATION"
- `action` doit être "ANALYSIS"
- `origin` doit être "MEDICAL_SI_BOOT_PATIENT_FILE"
- `status.code` doit être "SCHEDULED" ou "VALIDATED"
- `medicalActCategory.code` doit être "ANALYSIS"
- `medicalActCategory.analysis` doit être `true`
- `patient.sex.code` doit être "male" ou "female"

### Règles Métier

1. Une prestation ANALYSIS nécessite obligatoirement un médecin prescripteur (`doctor`)
2. Le `medicalRecordFile` doit toujours être présent
3. `firstMedicalSellPrestation` est `true` uniquement pour la première prestation du patient
4. `toothIndice` est toujours à 5 pour les analyses (non dentaire)

---

## Différences avec Prestation Ambulatory

| Aspect | Analysis | Ambulatory |
|--------|----------|------------|
| **MessageType** | UPDATE_PRESTATION | UPDATE_PRESCRIPTION_FINANCIERE |
| **Origin** | MEDICAL_SI_BOOT_PATIENT_FILE | MEDICAL_SI_BOOT_BILLING |
| **Workflow** | Prescription → Validation | Facturation directe |
| **Médecin** | Requis (`doctor`) | Non requis |
| **Dossier Médical** | Requis (`medicalRecordFile`) | Non présent |
| **Facturation** | Ultérieure | Immédiate (`patientBilling`) |
| **Documents PDF** | Non | Oui (`prestationBill`, `documents`) |
| **Prix** | Non présent | Présent (`price`, `denomination`) |
| **Assurance** | Non détaillée | Détaillée (`care`) |
| **Statut Paiement** | Non présent | Présent (`prestationPaymentStatus`) |

---

## Notes Importantes

⚠️ **Ce schéma est également valable pour les prestations de type RADIOLOGY** avec `medicalActCategory.code = "RADIOLOGY"` et `medicalActCategory.radiology = true`

⚠️ **Ce schéma NE couvre PAS** les prestations avec facturation directe (AMBULATORY, CONSULTATION, VISIT)

⚠️ **Les valeurs d'exemple** (identifiants, noms, dates) doivent être remplacées par les valeurs réelles de votre système

---

## Statistiques

- **Nombre total de clés** : 142
- **Profondeur maximale** : 4 niveaux
- **Objets imbriqués** : 12
- **Champs booléens** : 62
- **Champs numériques** : 15
- **Champs string** : 65

---

**Dernière mise à jour** : Octobre 2025  
**Conformité FHIR** : SI (5.0.0)  
**Contact** : support@eyone.net  
**Documentation** : https://fhir.yonewi.eyone.net