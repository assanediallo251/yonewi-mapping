# Documentation - Prestation Ambulatory Schema

## Vue d'Ensemble

Ce document décrit le schéma de données pour les prestations avec **facturation directe** de type **AMBULATORY** (Ambulatoire), **CONSULTATION** et **VISIT** (Visite). Ces prestations ont une facturation immédiate avec génération de documents PDF.

---

## Types de Prestations Couvertes

### Prestations Principales
- **AMBULATORY** (Ambulatoire) - `caredMedicalSellPrestationType: true`
- **CONSULTATION** (Consultation) - `caredMedicalSellPrestationType: true`
- **VISIT** (Visite) - `caredMedicalSellPrestationType: true`

### Caractéristiques Communes
- Workflow : ON_GOING → Facturation immédiate
- Origine : MEDICAL_SI_BOOT_BILLING
- Type de message : UPDATE_PRESCRIPTION_FINANCIERE
- Génération automatique de facture PDF
- Gestion de l'assurance et des quotes-parts

---

## Workflow

```
┌─────────────────┐
│  1. ON_GOING    │  ← Consultation en cours
│  (En cours)     │     Patient reçu en consultation
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  2. BILLING     │  ← Facturation immédiate
│  (Facturation)  │     Action: generateSettlementBillFile
└────────┬────────┘     Calcul quotes-parts assurance/patient
         │              Génération facture PDF
         ▼
┌─────────────────┐
│  3. PAYMENT     │  ← Paiement
│  (Paiement)     │     Status: NOT_PAYED → PAYED
└─────────────────┘
```

---

## Structure du Document

### Niveau Racine (6 champs)

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `messageType` | string | ✅ | Type de message - Valeur: "UPDATE_PRESCRIPTION_FINANCIERE" |
| `organismId` | string | ✅ | Identifiant de l'organisme |
| `organismName` | string | ✅ | Nom de l'organisme |
| `action` | string | ✅ | Action - Valeur: "CONSULTATION" |
| `data` | object | ✅ | Données principales de la prestation |
| `origin` | string | ✅ | Origine - Valeur: "MEDICAL_SI_BOOT_BILLING" |
| `claims` | object | ✅ | Actions à effectuer côté serveur |

---

## Objet `data` (61 champs de premier niveau)

### Identifiants

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `identifier` | number | ✅ | ID unique de la prestation |
| `eyoneInternalId` | string | ✅ | ID interne Eyone - Format: "AMBUL{année}{numéro}" |
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
| `organism.billingInsuranceAutoSynchronization` | boolean | ✅ | Synchronisation auto assurance |
| `relatedService` | object | ✅ | Service lié |
| `relatedService.identifier` | number | ✅ | ID du service |
| `relatedService.name` | string | ✅ | Nom du service |
| `relatedService.billingInsuranceAutoSynchronization` | boolean | ✅ | Synchronisation auto assurance |

### Tarification

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `price` | number | ✅ | Prix de la prestation |
| `denomination` | string | ✅ | Dénomination de la prestation - Ex: "CONSULTATION GENERAL" |
| `totalPrice` | number | ✅ | Prix total |
| `itemsTotalPrice` | number | ✅ | Prix total des items |
| `totalReductions` | number | ✅ | Réductions totales |
| `totalPharmaciePrice` | number | ✅ | Prix total pharmacie |
| `totalMedicalActPrice` | number | ✅ | Prix total acte médical |
| `totalPharmacyPriceWithoutAct` | number | ✅ | Prix pharmacie sans acte |
| `totalRadiologiePrice` | number | ✅ | Prix radiologie |
| `totalAnalysisPrice` | number | ✅ | Prix analyses |

### Catégorie d'Acte Médical

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `medicalActCategory` | object | ✅ | Catégorie de l'acte médical |
| `medicalActCategory.code` | string | ✅ | Code - Valeur: "AMBULATORY" |
| `medicalActCategory.label` | string | ✅ | Libellé - Valeur: "Ambulatoire" |
| `medicalActCategory.evacuation` | boolean | ✅ | false |
| `medicalActCategory.modeleItem` | boolean | ✅ | false |
| `medicalActCategory.medicalExam` | boolean | ✅ | false |
| `medicalActCategory.pharmacyAsPrestation` | boolean | ✅ | false |
| `medicalActCategory.hospitalization` | boolean | ✅ | false |
| `medicalActCategory.analysis` | boolean | ✅ | false |
| `medicalActCategory.radiology` | boolean | ✅ | false |
| `medicalActCategory.quote` | boolean | ✅ | false |
| `medicalActCategory.all` | boolean | ✅ | false |
| `medicalActCategory.editable` | boolean | ✅ | false |
| `medicalActCategory.room` | boolean | ✅ | false |
| `medicalActCategory.medoc` | boolean | ✅ | false |
| `medicalActCategory.modele` | boolean | ✅ | false |
| `medicalActCategory.visit` | boolean | ✅ | false |
| `medicalActCategory.ambulatory` | boolean | ✅ | true |
| `medicalActCategory.consultation` | boolean | ✅ | false |

### Statut de la Prestation

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `status` | object | ✅ | Statut de la prestation |
| `status.code` | string | ✅ | Code - Valeur: "ON_GOING" |
| `status.label` | string | ✅ | Libellé - Valeur: "En cours" |
| `status.done` | boolean | ✅ | Prestation terminée |
| `status.created` | boolean | ✅ | Prestation créée |
| `status.cancelled` | boolean | ✅ | Prestation annulée |
| `status.toBeConfirmed` | boolean | ✅ | À confirmer |
| `status.notAccepted` | boolean | ✅ | Non acceptée |
| `status.onGoing` | boolean | ✅ | En cours (true pour AMBULATORY) |
| `status.unavailable` | boolean | ✅ | Indisponible |
| `status.waiting` | boolean | ✅ | En attente |
| `status.notPerformed` | boolean | ✅ | Non effectuée |

### Statut de Paiement

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `prestationPaymentStatus` | object | ✅ | Statut de paiement de la prestation |
| `prestationPaymentStatus.code` | string | ✅ | Code - Valeurs: "NOT_PAYED", "PAYED", "PAYING" |
| `prestationPaymentStatus.label` | string | ✅ | Libellé - Ex: "Non Payé(e)" |
| `prestationPaymentStatus.totallyPayed` | boolean | ✅ | Totalement payé |
| `prestationPaymentStatus.paying` | boolean | ✅ | En cours de paiement |
| `prestationPaymentStatus.notPayed` | boolean | ✅ | Non payé |
| `prestationPaymentStatus.payedWithReject` | boolean | ✅ | Payé avec rejet |

### Type de Programmation

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `programmationType` | object | ✅ | Type de programmation |
| `programmationType.code` | string | ✅ | Code - Valeurs: "NOT_SCHEDULED", "SCHEDULED", "EMERGENCY" |
| `programmationType.label` | string | ✅ | Libellé - Ex: "Sans Rendez-vous" |
| `programmationType.notScheduled` | boolean | ✅ | Sans rendez-vous |
| `programmationType.emergency` | boolean | ✅ | Urgence |
| `programmationType.scheduled` | boolean | ✅ | Avec rendez-vous |

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

### Facturation Patient

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `patientBilling` | object | ✅ | Facturation du patient |
| `patientBilling.identifier` | number | ✅ | ID de la facturation |
| `patientBilling.creationDate` | string | ✅ | Date de création |
| `patientBilling.payedAmountTTC` | number | ✅ | Montant payé TTC |
| `patientBilling.notPayedAmountTTC` | number | ✅ | Montant non payé TTC |
| `patientBilling.careTotalAmount` | number | ✅ | Montant total assurance |
| `patientBilling.careTotalPayedAmount` | number | ✅ | Montant payé par assurance |
| `patientBilling.careTotalRejectAmount` | number | ✅ | Montant rejeté par assurance |
| `patientBilling.patientTotalAmount` | number | ✅ | Montant total patient (quote-part) |
| `patientBilling.patientTotalPayedAmount` | number | ✅ | Montant payé par patient |
| `patientBilling.complementaryCareTotalAmount` | number | ✅ | Montant assurance complémentaire |
| `patientBilling.complementaryCareTotalPayedAmount` | number | ✅ | Montant payé assurance complémentaire |
| `patientBilling.complementaryCareTotalRejectAmount` | number | ✅ | Montant rejeté assurance complémentaire |
| `patientBilling.complementaryPatientTotalAmount` | number | ✅ | Montant patient complémentaire |
| `patientBilling.complementaryPatientTotalPayedAmount` | number | ✅ | Montant payé patient complémentaire |
| `patientBilling.nbrRejects` | number | ✅ | Nombre de rejets |
| `patientBilling.cautionTotalAmount` | number | ✅ | Montant total caution |
| `patientBilling.cautionOnGoingAmount` | number | ✅ | Montant caution en cours |
| `patientBilling.nbrPatientSchedules` | number | ✅ | Nombre d'échéances patient |
| `patientBilling.nbrCareSchedules` | number | ✅ | Nombre d'échéances assurance |
| `patientBilling.returnToPatientAmount` | number | ✅ | Montant à retourner au patient |

### Facture (Document PDF)

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `prestationBill` | object | ✅ | Facture de la prestation |
| `prestationBill.identifier` | number | ✅ | ID de la facture |
| `prestationBill.eyoneExternalId` | string | ✅ | ID externe Eyone |
| `prestationBill.disable` | boolean | ✅ | Désactivée |
| `prestationBill.name` | string | ✅ | Nom du fichier - Ex: "AMBUL202500023.pdf" |
| `prestationBill.itemSize` | number | ✅ | Taille de l'item |
| `prestationBill.nbrItems` | number | ✅ | Nombre d'items |
| `prestationBill.extension` | string | ✅ | Extension - Valeur: "pdf" |
| `prestationBill.editable` | boolean | ✅ | Éditable |
| `prestationBill.path` | string | ✅ | Chemin du fichier sur le serveur |
| `prestationBill.sentToMedicalPassport` | boolean | ✅ | Envoyé au passeport médical |
| `prestationBill.sentToInsurer` | boolean | ✅ | Envoyé à l'assureur |
| `prestationBill.medicalDocumentType` | object | ✅ | Type de document médical |
| `prestationBill.medicalDocumentType.code` | string | ✅ | Code - Valeur: "BILL" |
| `prestationBill.medicalDocumentType.label` | string | ✅ | Libellé - Valeur: "Facture" |
| `prestationBill.medicalDocumentType.medical` | boolean | ✅ | Document médical |
| `prestationBill.medicalDocumentType.financial` | boolean | ✅ | Document financier (true) |
| `prestationBill.medicalDocumentType.bill` | boolean | ✅ | Facture (true) |
| `prestationBill.medicalDocumentType.prescription` | boolean | ✅ | Prescription |
| `prestationBill.medicalDocumentType.actPrescription` | boolean | ✅ | Prescription d'acte |
| `prestationBill.medicalDocumentType.medicalQuote` | boolean | ✅ | Devis médical |
| `prestationBill.medicalDocumentType.examResult` | boolean | ✅ | Résultat d'examen |
| `prestationBill.medicalDocumentType.otherMedicalDocument` | boolean | ✅ | Autre document médical |
| `prestationBill.medicalDocumentType.scannedDocument` | boolean | ✅ | Document scanné |
| `prestationBill.documentType` | object | ✅ | Type de document (doublon de medicalDocumentType) |

### Documents (Liste)

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `documents` | array | ✅ | Liste des documents générés |
| `documents[0].url` | string | ✅ | URL signée du document (avec expiration) |
| `documents[0].originalUrl` | string | ✅ | URL originale du document |
| `documents[0].bucketName` | string | ✅ | Nom du bucket S3 |
| `documents[0].sizeBytes` | number | ✅ | Taille en octets |
| `documents[0].fileType` | string | ✅ | Type MIME - Ex: "application/pdf" |
| `documents[0].fileName` | string | ✅ | Nom du fichier |
| `documents[0].documentType` | string | ✅ | Type de document - Ex: "Facture" |
| `documents[0].documentTypeCode` | string | ✅ | Code du type - Ex: "BILL" |
| `documents[0].fileId` | number | ✅ | ID du fichier |
| `documents[0].entityId` | number | ✅ | ID de l'entité liée |
| `documents[0].eyoneExternalId` | string | ✅ | ID externe Eyone |

### Prestation Pharmacie

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `pharmacySellPrestation` | object | ✅ | Prestation pharmacie associée |
| `pharmacySellPrestation.identifier` | number | ✅ | ID de la prestation pharmacie |
| `pharmacySellPrestation.disable` | boolean | ✅ | Désactivée |
| `pharmacySellPrestation.totalPrice` | number | ✅ | Prix total |
| `pharmacySellPrestation.nbrProducts` | number | ✅ | Nombre de produits |
| `pharmacySellPrestation.commentary` | string | ✅ | Commentaire |
| `pharmacySellPrestation.pharmacyBillingMode` | object | ✅ | Mode de facturation |
| `pharmacySellPrestation.pharmacyBillingMode.code` | string | ✅ | Code - Ex: "DETAIL" |
| `pharmacySellPrestation.pharmacyBillingMode.label` | string | ✅ | Libellé |
| `pharmacySellPrestation.pharmacyBillingMode.scope` | string | ✅ | Portée |
| `pharmacySellPrestation.pharmacyBillingMode.global` | boolean | ✅ | Mode global |
| `pharmacySellPrestation.pharmacyBillingMode.detail` | boolean | ✅ | Mode détaillé |
| `pharmacySellPrestation.care` | object | ✅ | Assurance (dans pharmacySellPrestation) |
| `pharmacySellPrestation.usedAsAPrestation` | boolean | ✅ | Utilisé comme prestation |
| `pharmacySellPrestation.useComplementaryCare` | boolean | ✅ | Utilise assurance complémentaire |
| `pharmacySellPrestation.manageStock` | boolean | ✅ | Gestion du stock |
| `pharmacySellPrestation.documentGenerationRequired2` | boolean | ✅ | Génération de document requise |

### Assurance (Care)

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `care` | object | ✅ | Informations d'assurance principale |
| `care.identifier` | number | ✅ | ID de l'assurance |
| `care.eyoneExternalId` | string | ✅ | ID externe Eyone |
| `care.disable` | boolean | ✅ | Désactivée |
| `care.startDate` | string | ✅ | Date de début - Format: "DD/MM/YYYY" |
| `care.endDate` | string | ✅ | Date de fin - Format: "DD/MM/YYYY" |
| `care.carePeriodAsStr` | string | ✅ | Période - Format: "DD/MM/YYYY-DD/MM/YYYY" |
| `care.insurer` | object | ✅ | Assureur |
| `care.insurer.identifier` | number | ✅ | ID de l'assureur |
| `care.insurer.disable` | boolean | ✅ | Désactivé |
| `care.insurer.name` | string | ✅ | Nom de l'assureur |
| `care.insurer.insurerId` | number | ✅ | ID de l'assureur (doublon) |
| `care.insurer.activateTeletransmission` | boolean | ✅ | Télétransmission activée |
| `care.insurerNumber` | string | ✅ | Numéro d'assuré |
| `care.carePercentage` | number | ✅ | Pourcentage de prise en charge (Ex: 60) |
| `care.careLimit` | number | ✅ | Plafond de prise en charge |
| `care.hasAParticipant` | boolean | ✅ | A un ayant droit |
| `care.oneTimeUsage` | boolean | ✅ | Usage unique |
| `care.beneficiaryEyoneExternalId` | string | ✅ | ID externe du bénéficiaire |
| `care.policeExternalId` | string | ✅ | ID externe de la police |

### Éditeur

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `editor` | object | ✅ | Utilisateur ayant édité la prestation |
| `editor.organism` | object | ✅ | Organisme de l'éditeur |
| `editor.organism.identifier` | number | ✅ | ID de l'organisme |
| `editor.organism.name` | string | ✅ | Nom de l'organisme |
| `editor.organism.eyoneExternalId` | string | ✅ | ID externe de l'organisme |
| `editor.userId` | number | ✅ | ID de l'utilisateur |
| `editor.userName` | string | ✅ | Nom d'utilisateur (email) |
| `editor.completeName` | string | ✅ | Nom complet |
| `editor.userDetailsInfos` | object | ✅ | Informations détaillées |
| `editor.userDetailsInfos.hasSuperviseurRight` | boolean | ✅ | Droits superviseur |
| `editor.userDetailsInfos.doctor` | boolean | ✅ | Est un docteur |
| `editor.identifier` | number | ✅ | Identifiant |
| `editor.completeNameAndUserName` | string | ✅ | Nom complet et username |

### Champs Additionnels

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `toothPrestation` | boolean | ✅ | Prestation dentaire |
| `indicateThatPatientHasCare` | boolean | ✅ | Indique que le patient a une assurance |
| `hasRelatedMedicalQuote` | boolean | ✅ | A un devis médical lié |
| `queueOrderNumber` | number | ✅ | Numéro d'ordre dans la file |
| `linkRelatedMedicalPartDate` | string | ✅ | Date de la partie médicale liée |
| `nbDay` | number | ✅ | Nombre de jours |
| `patientSelf` | boolean | ✅ | Patient sans assurance |
| `documentGenerationRequired` | boolean | ✅ | Génération de document requise |
| `documentGenerationRequired2` | boolean | ✅ | Génération de document requise (v2) |
| `selectedPayPatientQuotePart` | boolean | ✅ | Quote-part patient sélectionnée |

---

## Objet `claims` (2 champs)

| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `method` | string | ✅ | Méthode à appeler - Valeur: "generateSettlementBillFile" |
| `class` | string | ✅ | Classe du contrôleur - Valeur: "com.eyone.medicalbilling.controller.prestation.MedicalSellPrestationController" |

---

## Calcul de la Facturation

### Formule de Base

```
Prix Total = price (ou totalPrice)

Quote-part Assurance = Prix Total × (carePercentage / 100)
Quote-part Patient = Prix Total - Quote-part Assurance
```

### Exemple

Pour une consultation à **1,500 FCFA** avec une assurance à **60%** :

```
Prix Total = 1,500 FCFA

Quote-part Assurance = 1,500 × 0.60 = 900 FCFA
Quote-part Patient = 1,500 - 900 = 600 FCFA
```

Ceci se reflète dans :
```json
{
  "patientBilling": {
    "notPayedAmountTTC": 1500.00,
    "careTotalAmount": 900.00,
    "patientTotalAmount": 600.00
  }
}
```

---

## Statuts Possibles

### Code de Statut Principal (`status.code`)

| Code | Label | Description |
|------|-------|-------------|
| `ON_GOING` | En cours | Consultation en cours |
| `DONE` | Terminé | Consultation terminée |

### Code de Statut de Paiement (`prestationPaymentStatus.code`)

| Code | Label | Description |
|------|-------|-------------|
| `NOT_PAYED` | Non Payé(e) | Pas encore payé |
| `PAYING` | En cours de paiement | Paiement en cours |
| `PAYED` | Payé(e) | Totalement payé |
| `PAYED_WITH_REJECT` | Payé avec rejet | Payé mais avec rejets assurance |

### Type de Programmation (`programmationType.code`)

| Code | Label | Description |
|------|-------|-------------|
| `NOT_SCHEDULED` | Sans Rendez-vous | Pas de rendez-vous préalable |
| `SCHEDULED` | Avec Rendez-vous | Rendez-vous programmé |
| `EMERGENCY` | Urgence | Consultation urgente |

---

## Actions et Endpoints

### Action: Générer la Facture

```json
{
  "claims": {
    "method": "generateSettlementBillFile",
    "class": "com.eyone.medicalbilling.controller.prestation.MedicalSellPrestationController"
  }
}
```

Cette action :
1. Calcule les quotes-parts (assurance/patient)
2. Génère le PDF de facture
3. Stocke le document dans le bucket S3
4. Retourne l'URL signée du document

---

## Exemple d'Utilisation

### Consultation Ambulatoire avec Assurance

```json
{
  "messageType": "UPDATE_PRESCRIPTION_FINANCIERE",
  "organismId": "13853",
  "action": "CONSULTATION",
  "data": {
    "identifier": 51957,
    "eyoneInternalId": "AMBUL202500023",
    "price": 1500,
    "denomination": "CONSULTATION GENERAL",
    "medicalActCategory": {
      "code": "AMBULATORY",
      "ambulatory": true
    },
    "status": {
      "code": "ON_GOING",
      "onGoing": true
    },
    "prestationPaymentStatus": {
      "code": "NOT_PAYED",
      "notPayed": true
    },
    "programmationType": {
      "code": "NOT_SCHEDULED",
      "notScheduled": true
    },
    "patient": {
      "identifier": 18751,
      "firstName": "Rosalie",
      "lastName": "FAYE"
    },
    "patientBilling": {
      "notPayedAmountTTC": 1500.00,
      "careTotalAmount": 900.00,
      "patientTotalAmount": 600.00
    },
    "care": {
      "insurerNumber": "LRX-43415P",
      "carePercentage": 60,
      "careLimit": 15000,
      "insurer": {
        "name": "EYASSUR-IPM"
      }
    },
    "prestationBill": {
      "name": "AMBUL202500023.pdf",
      "extension": "pdf"
    },
    "documents": [
      {
        "url": "https://...",
        "fileName": "AMBUL202500023.pdf",
        "documentTypeCode": "BILL"
      }
    ]
  },
  "claims": {
    "method": "generateSettlementBillFile"
  }
}
```

---

## Règles de Validation

### Champs Requis

✅ Tous les champs marqués comme "Requis" doivent être présents

### Contraintes de Valeurs

- `messageType` doit être exactement "UPDATE_PRESCRIPTION_FINANCIERE"
- `action` doit être "CONSULTATION"
- `origin` doit être "MEDICAL_SI_BOOT_BILLING"
- `status.code` doit être "ON_GOING" ou "DONE"
- `medicalActCategory.code` doit être "AMBULATORY", "CONSULTATION" ou "VISIT"
- `prestationPaymentStatus.code` doit être dans ["NOT_PAYED", "PAYING", "PAYED", "PAYED_WITH_REJECT"]
- `patient.sex.code` doit être "male" ou "female"

### Règles Métier

1. Si le patient a une assurance (`care` présent), alors :
   - `patientBilling.careTotalAmount` = `price` × (`care.carePercentage` / 100)
   - `patientBilling.patientTotalAmount` = `price` - `careTotalAmount`

2. Le document PDF doit être généré automatiquement (`prestationBill` et `documents`)

3. `documentGenerationRequired2` doit être `true` pour déclencher la génération

4. L'URL du document dans `documents[0].url` expire après 1 heure (3600 secondes)

---

## Différences avec Prestation Analysis

| Aspect | Ambulatory | Analysis |
|--------|------------|----------|
| **MessageType** | UPDATE_PRESCRIPTION_FINANCIERE | UPDATE_PRESTATION |
| **Origin** | MEDICAL_SI_BOOT_BILLING | MEDICAL_SI_BOOT_PATIENT_FILE |
| **Workflow** | Facturation directe | Prescription → Validation |
| **Médecin** | Non requis | Requis (`doctor`) |
| **Dossier Médical** | Non présent | Requis (`medicalRecordFile`) |
| **Facturation** | Immédiate (`patientBilling`) | Ultérieure |
| **Documents PDF** | Oui (`prestationBill`, `documents`) | Non |
| **Prix** | Présent (`price`, `denomination`) | Non présent |
| **Assurance** | Détaillée (`care`) | Non détaillée |
| **Statut Paiement** | Présent (`prestationPaymentStatus`) | Non présent |
| **Type Programmation** | Présent (`programmationType`) | Non présent |

---

## Notes Importantes

⚠️ **Ce schéma est également valable pour** :
- **CONSULTATION** avec `medicalActCategory.code = "CONSULTATION"` et `medicalActCategory.consultation = true`
- **VISIT** avec `medicalActCategory.code = "VISIT"` et `medicalActCategory.visit = true`

⚠️ **Ce schéma NE couvre PAS** les prestations avec workflow médical (ANALYSIS, RADIOLOGY)

⚠️ **L'URL du document PDF** dans `documents[0].url` est une URL signée qui expire. Utiliser `originalUrl` pour une référence permanente

⚠️ **Le plafond de l'assurance** (`care.careLimit`) doit être vérifié avant chaque prestation pour s'assurer que le patient n'a pas dépassé son plafond annuel

---

## Statistiques

- **Nombre total de clés** : 230
- **Profondeur maximale** : 4 niveaux
- **Objets imbriqués** : 15
- **Champs booléens** : 48
- **Champs numériques** : 45
- **Champs string** : 137

---

**Version du document** : 1.0  
**Date de création** : 31/10/2025  
**Organisme** : Centre de Santé Renaissance  
**Compatibilité** : AMBULATORY, CONSULTATION, VISIT