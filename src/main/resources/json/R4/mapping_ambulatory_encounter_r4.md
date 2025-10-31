# Mapping Prestation AMBULATORY vers Encounter FHIR R4

**Document** : Spécification de Mapping Détaillée  
**Version** : 1.0.0  
**Date** : 31 Octobre 2025  
**Organisation** : Eyone Health System  
**Standard FHIR** : R4 (4.0.1)  
**Système Source** : SI Medical Eyone  
**Message Type** : `UPDATE_PRESCRIPTION_FINANCIERE`  
**Action** : `AMBULATORY`, `CONSULTATION`

---

## Table des Matières

1. [Vue d'Ensemble](#1-vue-densemble)
2. [Mapping des Champs de Base](#2-mapping-des-champs-de-base)
3. [Mapping des Extensions](#3-mapping-des-extensions)
4. [Mapping des Données Financières](#4-mapping-des-données-financières)
5. [Mapping des Documents](#5-mapping-des-documents)
6. [Mapping des Assurances](#6-mapping-des-assurances)
7. [Transformations Spécifiques](#7-transformations-spécifiques)
8. [Exemple Complet](#8-exemple-complet)
9. [Validation](#9-validation)

---

## 1. Vue d'Ensemble

### 1.1 Contexte

**Message source** : `UPDATE_PRESCRIPTION_FINANCIERE`  
**Actions supportées** : `AMBULATORY`, `CONSULTATION`  
**Ressource FHIR cible** : `Encounter` (FHIR R4)  
**Workflow** : Création → ON_GOING → Facturation → VALIDATED

### 1.2 Principes

- **Conservation totale** : Aucune perte de données
- **Extensions** : Données métier Eyone stockées en extensions
- **Références** : Toutes les entités référencées avec identifiers complets
- **Dates** : Conversion DD/MM/YYYY HH:mm:ss → ISO 8601

---

## 2. Mapping des Champs de Base

### 2.1 Métadonnées et Identifiants

| Champ SI | Chemin SI | Type SI | Chemin FHIR R4 | Type FHIR | Cardinalité | Transformation |
|----------|-----------|---------|----------------|-----------|-------------|----------------|
| **Ressource** |
| Type | - | - | `resourceType` | string | 1..1 | Fixe: "Encounter" |
| ID | `data.eyoneInternalId` | string | `id` | id | 1..1 | Direct |
| Version | - | - | `meta.versionId` | string | 0..1 | Fixe: "1" |
| Dernière MAJ | `data.creationDate` | string | `meta.lastUpdated` | instant | 0..1 | Conversion date → ISO 8601 |
| Profil | - | - | `meta.profile[0]` | canonical | 0..* | Fixe: "http://eyone.sn/fhir/StructureDefinition/Encounter-Ambulatory" |
| **Identifiants** |
| ID prestation | `data.identifier` | integer | `identifier[0].value` | string | 0..* | Conversion: toString() |
| Système ID | - | - | `identifier[0].system` | uri | 0..1 | Fixe: "http://eyone.sn/fhir/identifier/prestation" |
| ID interne | `data.eyoneInternalId` | string | `identifier[1].value` | string | 0..* | Direct |
| Système ID interne | - | - | `identifier[1].system` | uri | 0..1 | Fixe: "http://eyone.sn/fhir/identifier/eyoneInternalId" |
| ID personnalisé | `data.customId` | string | `identifier[2].value` | string | 0..* | Direct |
| Système ID custom | - | - | `identifier[2].system` | uri | 0..1 | Fixe: "http://eyone.sn/fhir/identifier/customId" |
| ID organisme | `organismId` | string | `identifier[3].value` | string | 0..* | Direct |
| Système ID organisme | - | - | `identifier[3].system` | uri | 0..1 | Fixe: "http://eyone.sn/fhir/identifier/organism" |

### 2.2 Statut et Classification

| Champ SI | Chemin SI | Type SI | Chemin FHIR R4 | Type FHIR | Cardinalité | Transformation |
|----------|-----------|---------|----------------|-----------|-------------|----------------|
| **Statut** |
| Code statut | `data.status.code` | string | `status` | code | 1..1 | Mapping (voir 2.2.1) |
| Historique statut | - | - | `statusHistory[0].status` | code | 0..* | Même que status |
| Période historique | `data.startDate`, `data.endDate` | string | `statusHistory[0].period` | Period | 0..1 | Conversion dates |
| **Classe de rencontre** |
| Catégorie acte | `data.medicalActCategory.code` | string | `class.code` | code | 1..1 | Mapping (voir 2.2.2) |
| Système classe | - | - | `class.system` | uri | 0..1 | Fixe: "http://terminology.hl7.org/CodeSystem/v3-ActCode" |
| Libellé classe | - | - | `class.display` | string | 0..1 | Mapping (voir 2.2.2) |
| **Priorité** |
| Type programmation | `data.programmationType.code` | string | `priority.coding[0].code` | code | 0..1 | Mapping (voir 2.2.3) |
| Système priorité | - | - | `priority.coding[0].system` | uri | 0..1 | Fixe: "http://terminology.hl7.org/CodeSystem/v3-ActPriority" |
| **Type de rencontre** |
| Catégorie | `data.medicalActCategory.code` | string | `type[0].coding[0].code` | code | 0..* | Direct |
| Libellé | `data.medicalActCategory.label` | string | `type[0].coding[0].display` | string | 0..1 | Direct |
| Système | - | - | `type[0].coding[0].system` | uri | 0..1 | Fixe: "http://eyone.sn/fhir/CodeSystem/medical-act-category" |
| **Type de service** |
| Dénomination | `data.denomination` | string | `serviceType.coding[0].code` | code | 0..1 | Normalisation (espaces → underscores) |
| Libellé | `data.denomination` | string | `serviceType.coding[0].display` | string | 0..1 | Direct |
| Système | - | - | `serviceType.coding[0].system` | uri | 0..1 | Fixe: "http://eyone.sn/fhir/CodeSystem/service-denomination" |

#### 2.2.1 Mapping des Statuts

| Code SI | Label SI | Code FHIR R4 | Description FHIR |
|---------|----------|--------------|------------------|
| `ON_GOING` | En cours | `in-progress` | La rencontre est actuellement en cours |
| `VALIDATED` | Validé(e) | `finished` | La rencontre est terminée |
| `SCHEDULED` | Programmé(e) | `planned` | La rencontre est planifiée |
| `CANCELLED` | Annulé(e) | `cancelled` | La rencontre a été annulée |
| `CREATED` | Créé(e) | `planned` | La rencontre est en cours de création |

#### 2.2.2 Mapping des Classes Encounter

| Code SI (medicalActCategory) | Code FHIR R4 | System | Display |
|------------------------------|--------------|---------|---------|
| `AMBULATORY` | `AMB` | http://terminology.hl7.org/CodeSystem/v3-ActCode | ambulatory |
| `CONSULTATION` | `AMB` | http://terminology.hl7.org/CodeSystem/v3-ActCode | ambulatory |
| `HOSPITALIZATION` | `IMP` | http://terminology.hl7.org/CodeSystem/v3-ActCode | inpatient encounter |
| `VISIT` | `HH` | http://terminology.hl7.org/CodeSystem/v3-ActCode | home health |
| `EMERGENCY` | `EMER` | http://terminology.hl7.org/CodeSystem/v3-ActCode | emergency |

#### 2.2.3 Mapping des Priorités

| Code SI (programmationType) | Code FHIR R4 | Display |
|-----------------------------|--------------|---------|
| `EMERGENCY` | `EM` | emergency |
| `SCHEDULED` | `EL` | elective |
| `NOT_SCHEDULED` | `R` | routine |

### 2.3 Références et Relations

| Champ SI | Chemin SI | Type SI | Chemin FHIR R4 | Type FHIR | Cardinalité | Transformation |
|----------|-----------|---------|----------------|-----------|-------------|----------------|
| **Patient** |
| Référence patient | `data.patient.identifier` | integer | `subject.reference` | string | 1..1 | Format: "Patient/{identifier}" |
| Identifier patient | `data.patient.identifier` | integer | `subject.identifier.value` | string | 0..1 | Conversion: toString() |
| Système identifier | - | - | `subject.identifier.system` | uri | 0..1 | Fixe: "http://eyone.sn/fhir/identifier/patient" |
| Nom complet | `data.patient.firstName + lastName` | string | `subject.display` | string | 0..1 | Concaténation: "{firstName} {lastName}" |
| **Fournisseur de service** |
| Référence organisme | `data.organism.identifier` | integer | `serviceProvider.reference` | string | 0..1 | Format: "Organization/{identifier}" |
| Identifier organisme | `data.organism.identifier` | integer | `serviceProvider.identifier.value` | string | 0..1 | Conversion: toString() |
| Système identifier | - | - | `serviceProvider.identifier.system` | uri | 0..1 | Fixe: "http://eyone.sn/fhir/identifier/organization" |
| Nom organisme | `data.organism.name` | string | `serviceProvider.display` | string | 0..1 | Direct |

### 2.4 Participant (Éditeur)

| Champ SI | Chemin SI | Type SI | Chemin FHIR R4 | Type FHIR | Cardinalité | Transformation |
|----------|-----------|---------|----------------|-----------|-------------|----------------|
| **Type de participant** |
| Type | - | - | `participant[0].type[0].coding[0].code` | code | 0..* | Fixe: "ATND" (attender) |
| Système | - | - | `participant[0].type[0].coding[0].system` | uri | 0..1 | Fixe: "http://terminology.hl7.org/CodeSystem/v3-ParticipationType" |
| Libellé | - | - | `participant[0].type[0].coding[0].display` | string | 0..1 | Fixe: "attender" |
| **Période de participation** |
| Début | `data.startDate` | string | `participant[0].period.start` | dateTime | 0..1 | Conversion date → ISO 8601 |
| Fin | `data.endDate` | string | `participant[0].period.end` | dateTime | 0..1 | Conversion date → ISO 8601 |
| **Individu (Éditeur)** |
| Référence | `data.editor.userId` | integer | `participant[0].individual.reference` | string | 0..1 | Format: "Practitioner/{userId}" |
| Identifier | `data.editor.userId` | integer | `participant[0].individual.identifier.value` | string | 0..1 | Conversion: toString() |
| Système | - | - | `participant[0].individual.identifier.system` | uri | 0..1 | Fixe: "http://eyone.sn/fhir/identifier/practitioner" |
| Nom complet | `data.editor.completeName` | string | `participant[0].individual.display` | string | 0..1 | Direct |

### 2.5 Localisation

| Champ SI | Chemin SI | Type SI | Chemin FHIR R4 | Type FHIR | Cardinalité | Transformation |
|----------|-----------|---------|----------------|-----------|-------------|----------------|
| **Lieu** |
| Référence | `data.relatedService.identifier` | integer | `location[0].location.reference` | string | 0..* | Format: "Location/{identifier}" |
| Identifier | `data.relatedService.identifier` | integer | `location[0].location.identifier.value` | string | 0..1 | Conversion: toString() |
| Système | - | - | `location[0].location.identifier.system` | uri | 0..1 | Fixe: "http://eyone.sn/fhir/identifier/location" |
| Nom | `data.relatedService.name` | string | `location[0].location.display` | string | 0..1 | Direct |
| **Période** |
| Début | `data.startDate` | string | `location[0].period.start` | dateTime | 0..1 | Conversion date → ISO 8601 |
| Fin | `data.endDate` | string | `location[0].period.end` | dateTime | 0..1 | Conversion date → ISO 8601 |

### 2.6 Période de la Rencontre

| Champ SI | Chemin SI | Type SI | Chemin FHIR R4 | Type FHIR | Cardinalité | Transformation |
|----------|-----------|---------|----------------|-----------|-------------|----------------|
| Date début | `data.startDate` | string | `period.start` | dateTime | 0..1 | Conversion: DD/MM/YYYY HH:mm:ss → ISO 8601 |
| Date fin | `data.endDate` | string | `period.end` | dateTime | 0..1 | Conversion: DD/MM/YYYY HH:mm:ss → ISO 8601 |

### 2.7 Narrative (Text)

| Champ SI | Chemin SI | Type SI | Chemin FHIR R4 | Type FHIR | Cardinalité | Transformation |
|----------|-----------|---------|----------------|-----------|-------------|----------------|
| Statut | - | - | `text.status` | code | 1..1 | Fixe: "generated" |
| Contenu HTML | Calculé | - | `text.div` | xhtml | 1..1 | Génération (voir 7.1) |

---

## 3. Mapping des Extensions

### 3.1 Extensions Message/Workflow

| Champ SI | Chemin SI | Type SI | Extension URL | Value Type | Transformation |
|----------|-----------|---------|---------------|------------|----------------|
| Type message | `messageType` | string | `http://eyone.sn/fhir/StructureDefinition/messageType` | valueString | Direct |
| Origine | `origin` | string | `http://eyone.sn/fhir/StructureDefinition/origin` | valueString | Direct |
| Action | `action` | string | `http://eyone.sn/fhir/StructureDefinition/action` | valueString | Direct |
| Créateur | `data.createdBy` | string | `http://eyone.sn/fhir/StructureDefinition/createdBy` | valueString | Direct |
| Date création | `data.creationDate` | string | `http://eyone.sn/fhir/StructureDefinition/creationDate` | valueDateTime | Conversion → ISO 8601 |

### 3.2 Extension Catégorie d'Acte Médical

**URL** : `http://eyone.sn/fhir/StructureDefinition/medicalActCategory`

| Champ SI | Chemin SI | Type SI | Sub-extension URL | Value Type | Transformation |
|----------|-----------|---------|-------------------|------------|----------------|
| Code | `data.medicalActCategory.code` | string | `code` | valueString | Direct |
| Libellé | `data.medicalActCategory.label` | string | `label` | valueString | Direct |
| Évacuation | `data.medicalActCategory.evacuation` | boolean | `evacuation` | valueBoolean | Direct |
| Modèle item | `data.medicalActCategory.modeleItem` | boolean | `modeleItem` | valueBoolean | Direct |
| Examen médical | `data.medicalActCategory.medicalExam` | boolean | `medicalExam` | valueBoolean | Direct |
| Pharmacie prestation | `data.medicalActCategory.pharmacyAsPrestation` | boolean | `pharmacyAsPrestation` | valueBoolean | Direct |
| Hospitalisation | `data.medicalActCategory.hospitalization` | boolean | `hospitalization` | valueBoolean | Direct |
| Analyse | `data.medicalActCategory.analysis` | boolean | `analysis` | valueBoolean | Direct |
| Radiologie | `data.medicalActCategory.radiology` | boolean | `radiology` | valueBoolean | Direct |
| Devis | `data.medicalActCategory.quote` | boolean | `quote` | valueBoolean | Direct |
| Tous | `data.medicalActCategory.all` | boolean | `all` | valueBoolean | Direct |
| Éditable | `data.medicalActCategory.editable` | boolean | `editable` | valueBoolean | Direct |
| Chambre | `data.medicalActCategory.room` | boolean | `room` | valueBoolean | Direct |
| Médicament | `data.medicalActCategory.medoc` | boolean | `medoc` | valueBoolean | Direct |
| Modèle | `data.medicalActCategory.modele` | boolean | `modele` | valueBoolean | Direct |
| Visite | `data.medicalActCategory.visit` | boolean | `visit` | valueBoolean | Direct |
| Ambulatoire | `data.medicalActCategory.ambulatory` | boolean | `ambulatory` | valueBoolean | Direct |
| Consultation | `data.medicalActCategory.consultation` | boolean | `consultation` | valueBoolean | Direct |

### 3.3 Extension Détails du Statut

**URL** : `http://eyone.sn/fhir/StructureDefinition/statusDetails`

| Champ SI | Chemin SI | Type SI | Sub-extension URL | Value Type | Transformation |
|----------|-----------|---------|-------------------|------------|----------------|
| Code | `data.status.code` | string | `code` | valueString | Direct |
| Libellé | `data.status.label` | string | `label` | valueString | Direct |
| Terminé | `data.status.done` | boolean | `done` | valueBoolean | Direct |
| Créé | `data.status.created` | boolean | `created` | valueBoolean | Direct |
| Annulé | `data.status.cancelled` | boolean | `cancelled` | valueBoolean | Direct |
| À confirmer | `data.status.toBeConfirmed` | boolean | `toBeConfirmed` | valueBoolean | Direct |
| Non accepté | `data.status.notAccepted` | boolean | `notAccepted` | valueBoolean | Direct |
| En cours | `data.status.onGoing` | boolean | `onGoing` | valueBoolean | Direct |
| Indisponible | `data.status.unavailable` | boolean | `unavailable` | valueBoolean | Direct |
| En attente | `data.status.waiting` | boolean | `waiting` | valueBoolean | Direct (si présent) |
| Non effectué | `data.status.notPerformed` | boolean | `notPerformed` | valueBoolean | Direct |

### 3.4 Extension Éditeur

**URL** : `http://eyone.sn/fhir/StructureDefinition/editor`

| Champ SI | Chemin SI | Type SI | Sub-extension URL | Value Type | Transformation |
|----------|-----------|---------|-------------------|------------|----------------|
| **Organisme** | | | `organism` | Extension complexe | |
| → Identifier | `data.editor.organism.identifier` | integer | `identifier` | valueInteger | Direct |
| → Nom | `data.editor.organism.name` | string | `name` | valueString | Direct |
| → External ID | `data.editor.organism.eyoneExternalId` | string | `eyoneExternalId` | valueString | Direct |
| User ID | `data.editor.userId` | integer | `userId` | valueInteger | Direct |
| Username | `data.editor.userName` | string | `userName` | valueString | Direct |
| Nom complet | `data.editor.completeName` | string | `completeName` | valueString | Direct |
| Identifier | `data.editor.identifier` | integer | `identifier` | valueInteger | Direct |
| Nom et username | `data.editor.completeNameAndUserName` | string | `completeNameAndUserName` | valueString | Direct |
| **Détails utilisateur** | | | `userDetailsInfos` | Extension complexe | |
| → Droit superviseur | `data.editor.userDetailsInfos.hasSuperviseurRight` | boolean | `hasSuperviseurRight` | valueBoolean | Direct |
| → Médecin | `data.editor.userDetailsInfos.doctor` | boolean | `doctor` | valueBoolean | Direct |

### 3.5 Extension Service Associé

**URL** : `http://eyone.sn/fhir/StructureDefinition/relatedService`

| Champ SI | Chemin SI | Type SI | Sub-extension URL | Value Type | Transformation |
|----------|-----------|---------|-------------------|------------|----------------|
| Identifier | `data.relatedService.identifier` | integer | `identifier` | valueInteger | Direct |
| Nom | `data.relatedService.name` | string | `name` | valueString | Direct |
| Sync auto assurance | `data.relatedService.billingInsuranceAutoSynchronization` | boolean | `billingInsuranceAutoSynchronization` | valueBoolean | Direct |

### 3.6 Extension Claims

**URL** : `http://eyone.sn/fhir/StructureDefinition/claims`

| Champ SI | Chemin SI | Type SI | Sub-extension URL | Value Type | Transformation |
|----------|-----------|---------|-------------------|------------|----------------|
| Méthode | `claims.method` | string | `method` | valueString | Direct |
| Classe | `claims.class` | string | `class` | valueString | Direct |

### 3.7 Extensions Booléennes Simples

| Champ SI | Chemin SI | Extension URL | Value Type |
|----------|-----------|---------------|------------|
| Prestation dentaire | `data.toothPrestation` | `http://eyone.sn/fhir/StructureDefinition/toothPrestation` | valueBoolean |
| Indice dentaire | `data.toothIndice` | `http://eyone.sn/fhir/StructureDefinition/toothIndice` | valueInteger |
| Première prestation | `data.firstMedicalSellPrestation` | `http://eyone.sn/fhir/StructureDefinition/firstMedicalSellPrestation` | valueBoolean |
| Première consultation | `data.firstConsultation` | `http://eyone.sn/fhir/StructureDefinition/firstConsultation` | valueBoolean |
| Télé-prestation | `data.telePrestation` | `http://eyone.sn/fhir/StructureDefinition/telePrestation` | valueBoolean |
| Créé par file | `data.createByQueue` | `http://eyone.sn/fhir/StructureDefinition/createByQueue` | valueBoolean |
| Patient a assurance | `data.indicateThatPatientHasCare` | `http://eyone.sn/fhir/StructureDefinition/indicateThatPatientHasCare` | valueBoolean |
| Devis médical lié | `data.hasRelatedMedicalQuote` | `http://eyone.sn/fhir/StructureDefinition/hasRelatedMedicalQuote` | valueBoolean |
| Patient auto-payant | `data.patientSelf` | `http://eyone.sn/fhir/StructureDefinition/patientSelf` | valueBoolean |
| Génération doc requise | `data.documentGenerationRequired` | `http://eyone.sn/fhir/StructureDefinition/documentGenerationRequired` | valueBoolean |
| Génération doc requise 2 | `data.documentGenerationRequired2` | `http://eyone.sn/fhir/StructureDefinition/documentGenerationRequired2` | valueBoolean |
| Payer quote-part patient | `data.selectedPayPatientQuotePart` | `http://eyone.sn/fhir/StructureDefinition/selectedPayPatientQuotePart` | valueBoolean |

### 3.8 Extensions Entières/Décimales Simples

| Champ SI | Chemin SI | Extension URL | Value Type |
|----------|-----------|---------------|------------|
| Numéro file d'attente | `data.queueOrderNumber` | `http://eyone.sn/fhir/StructureDefinition/queueOrderNumber` | valueInteger |
| Réductions totales | `data.totalReductions` | `http://eyone.sn/fhir/StructureDefinition/totalReductions` | valueDecimal |
| Nombre jours | `data.nbDay` | `http://eyone.sn/fhir/StructureDefinition/nbDay` | valueInteger |

### 3.9 Extensions Chaînes Simples

| Champ SI | Chemin SI | Extension URL | Value Type |
|----------|-----------|---------------|------------|
| Date partie médicale | `data.linkRelatedMedicalPartDate` | `http://eyone.sn/fhir/StructureDefinition/linkRelatedMedicalPartDate` | valueString |

---

## 4. Mapping des Données Financières

### 4.1 Extension Prix et Tarification

**URL** : `http://eyone.sn/fhir/StructureDefinition/pricing`

| Champ SI | Chemin SI | Type SI | Sub-extension URL | Value Type | Transformation |
|----------|-----------|---------|-------------------|------------|----------------|
| Prix | `data.price` | number | `price` | valueDecimal | Direct |
| Total pharmacie | `data.totalPharmaciePrice` | number | `totalPharmacyPrice` | valueDecimal | Direct |
| Total actes médicaux | `data.totalMedicalActPrice` | number | `totalMedicalActPrice` | valueDecimal | Direct |
| Total pharmacie sans acte | `data.totalPharmacyPriceWithoutAct` | number | `totalPharmacyPriceWithoutAct` | valueDecimal | Direct |
| Total radiologie | `data.totalRadiologiePrice` | number | `totalRadiologyPrice` | valueDecimal | Direct |
| Total analyses | `data.totalAnalysisPrice` | number | `totalAnalysisPrice` | valueDecimal | Direct |
| Prix total | `data.totalPrice` | number | `totalPrice` | valueDecimal | Direct |
| Prix total items | `data.itemsTotalPrice` | number | `itemsTotalPrice` | valueDecimal | Direct |

### 4.2 Extension Facturation Patient

**URL** : `http://eyone.sn/fhir/StructureDefinition/patientBilling`

| Champ SI | Chemin SI | Type SI | Sub-extension URL | Value Type | Transformation |
|----------|-----------|---------|-------------------|------------|----------------|
| Identifier | `data.patientBilling.identifier` | integer | `identifier` | valueInteger | Direct |
| Date création | `data.patientBilling.creationDate` | string | `creationDate` | valueDateTime | Conversion → ISO 8601 |
| Montant payé TTC | `data.patientBilling.payedAmountTTC` | number | `payedAmountTTC` | valueDecimal | Direct |
| Montant non payé TTC | `data.patientBilling.notPayedAmountTTC` | number | `notPayedAmountTTC` | valueDecimal | Direct |
| Montant total assurance | `data.patientBilling.careTotalAmount` | number | `careTotalAmount` | valueDecimal | Direct |
| Montant assurance payé | `data.patientBilling.careTotalPayedAmount` | number | `careTotalPayedAmount` | valueDecimal | Direct |
| Montant assurance rejeté | `data.patientBilling.careTotalRejectAmount` | number | `careTotalRejectAmount` | valueDecimal | Direct |
| Montant total patient | `data.patientBilling.patientTotalAmount` | number | `patientTotalAmount` | valueDecimal | Direct |
| Montant patient payé | `data.patientBilling.patientTotalPayedAmount` | number | `patientTotalPayedAmount` | valueDecimal | Direct |
| Assurance complémentaire total | `data.patientBilling.complementaryCareTotalAmount` | number | `complementaryCareTotalAmount` | valueDecimal | Direct |
| Assurance complémentaire payé | `data.patientBilling.complementaryCareTotalPayedAmount` | number | `complementaryCareTotalPayedAmount` | valueDecimal | Direct |
| Assurance complémentaire rejeté | `data.patientBilling.complementaryCareTotalRejectAmount` | number | `complementaryCareTotalRejectAmount` | valueDecimal | Direct |
| Patient complémentaire total | `data.patientBilling.complementaryPatientTotalAmount` | number | `complementaryPatientTotalAmount` | valueDecimal | Direct |
| Patient complémentaire payé | `data.patientBilling.complementaryPatientTotalPayedAmount` | number | `complementaryPatientTotalPayedAmount` | valueDecimal | Direct |
| Nombre rejets | `data.patientBilling.nbrRejects` | integer | `nbrRejects` | valueInteger | Direct |
| Montant caution total | `data.patientBilling.cautionTotalAmount` | number | `cautionTotalAmount` | valueDecimal | Direct |
| Montant caution en cours | `data.patientBilling.cautionOnGoingAmount` | number | `cautionOnGoingAmount` | valueDecimal | Direct |
| Nombre échéances patient | `data.patientBilling.nbrPatientSchedules` | integer | `nbrPatientSchedules` | valueInteger | Direct |
| Nombre échéances assurance | `data.patientBilling.nbrCareSchedules` | integer | `nbrCareSchedules` | valueInteger | Direct |
| Retour patient | `data.patientBilling.returnToPatientAmount` | number | `returnToPatientAmount` | valueDecimal | Direct |

### 4.3 Extension Statut de Paiement

**URL** : `http://eyone.sn/fhir/StructureDefinition/prestationPaymentStatus`

| Champ SI | Chemin SI | Type SI | Sub-extension URL | Value Type | Transformation |
|----------|-----------|---------|-------------------|------------|----------------|
| Code | `data.prestationPaymentStatus.code` | string | `code` | valueString | Direct |
| Libellé | `data.prestationPaymentStatus.label` | string | `label` | valueString | Direct |
| Totalement payé | `data.prestationPaymentStatus.totallyPayed` | boolean | `totallyPayed` | valueBoolean | Direct |
| En paiement | `data.prestationPaymentStatus.paying` | boolean | `paying` | valueBoolean | Direct |
| Non payé | `data.prestationPaymentStatus.notPayed` | boolean | `notPayed` | valueBoolean | Direct |
| Payé avec rejet | `data.prestationPaymentStatus.payedWithReject` | boolean | `payedWithReject` | valueBoolean | Direct |

### 4.4 Extension Type de Programmation

**URL** : `http://eyone.sn/fhir/StructureDefinition/programmationType`

| Champ SI | Chemin SI | Type SI | Sub-extension URL | Value Type | Transformation |
|----------|-----------|---------|-------------------|------------|----------------|
| Code | `data.programmationType.code` | string | `code` | valueString | Direct |
| Libellé | `data.programmationType.label` | string | `label` | valueString | Direct |
| Non programmé | `data.programmationType.notScheduled` | boolean | `notScheduled` | valueBoolean | Direct |
| Urgence | `data.programmationType.emergency` | boolean | `emergency` | valueBoolean | Direct |
| Programmé | `data.programmationType.scheduled` | boolean | `scheduled` | valueBoolean | Direct |

---

## 5. Mapping des Documents

### 5.1 Extension Facture de Prestation

**URL** : `http://eyone.sn/fhir/StructureDefinition/prestationBill`

| Champ SI | Chemin SI | Type SI | Sub-extension URL | Value Type | Transformation |
|----------|-----------|---------|-------------------|------------|----------------|
| Identifier | `data.prestationBill.identifier` | integer | `identifier` | valueInteger | Direct |
| External ID | `data.prestationBill.eyoneExternalId` | string | `eyoneExternalId` | valueString | Direct |
| Désactivé | `data.prestationBill.disable` | boolean | `disable` | valueBoolean | Direct |
| Nom | `data.prestationBill.name` | string | `name` | valueString | Direct |
| Taille | `data.prestationBill.itemSize` | integer | `itemSize` | valueInteger | Direct |
| Nombre items | `data.prestationBill.nbrItems` | integer | `nbrItems` | valueInteger | Direct |
| Extension | `data.prestationBill.extension` | string | `fileExtension` | valueString | Direct |
| Éditable | `data.prestationBill.editable` | boolean | `editable` | valueBoolean | Direct |
| Chemin | `data.prestationBill.path` | string | `path` | valueString | Direct |
| Envoyé passeport | `data.prestationBill.sentToMedicalPassport` | boolean | `sentToMedicalPassport` | valueBoolean | Direct |
| Envoyé assureur | `data.prestationBill.sentToInsurer` | boolean | `sentToInsurer` | valueBoolean | Direct |
| **Type document médical** | | | `medicalDocumentType` | Extension complexe | |
| → Code | `data.prestationBill.medicalDocumentType.code` | string | `code` | valueString | Direct |
| → Libellé | `data.prestationBill.medicalDocumentType.label` | string | `label` | valueString | Direct |
| → Médical | `data.prestationBill.medicalDocumentType.medical` | boolean | `medical` | valueBoolean | Direct |
| → Financier | `data.prestationBill.medicalDocumentType.financial` | boolean | `financial` | valueBoolean | Direct |
| → Facture | `data.prestationBill.medicalDocumentType.bill` | boolean | `bill` | valueBoolean | Direct |
| → Ordonnance | `data.prestationBill.medicalDocumentType.prescription` | boolean | `prescription` | valueBoolean | Direct |
| → Prescription acte | `data.prestationBill.medicalDocumentType.actPrescription` | boolean | `actPrescription` | valueBoolean | Direct |
| → Devis médical | `data.prestationBill.medicalDocumentType.medicalQuote` | boolean | `medicalQuote` | valueBoolean | Direct |
| → Résultat examen | `data.prestationBill.medicalDocumentType.examResult` | boolean | `examResult` | valueBoolean | Direct |
| → Autre document | `data.prestationBill.medicalDocumentType.otherMedicalDocument` | boolean | `otherMedicalDocument` | valueBoolean | Direct |
| → Document scanné | `data.prestationBill.medicalDocumentType.scannedDocument` | boolean | `scannedDocument` | valueBoolean | Direct |

### 5.2 Extension Documents Attachés

**URL** : `http://eyone.sn/fhir/StructureDefinition/documents`

Pour chaque document dans `data.documents[]` :

| Champ SI | Chemin SI | Type SI | Sub-extension URL | Value Type | Transformation |
|----------|-----------|---------|-------------------|------------|----------------|
| URL | `url` | string | `url` | valueUrl | Direct |
| URL originale | `originalUrl` | string | `originalUrl` | valueUrl | Direct |
| Bucket | `bucketName` | string | `bucketName` | valueString | Direct |
| Taille octets | `sizeBytes` | integer | `sizeBytes` | valueInteger | Direct |
| Type fichier | `fileType` | string | `fileType` | valueString | Direct |
| Nom fichier | `fileName` | string | `fileName` | valueString | Direct |
| Type document | `documentType` | string | `documentType` | valueString | Direct |
| Code type document | `documentTypeCode` | string | `documentTypeCode` | valueString | Direct |
| File ID | `fileId` | integer | `fileId` | valueInteger | Direct |
| Entity ID | `entityId` | integer | `entityId` | valueInteger | Direct |
| External ID | `eyoneExternalId` | string | `eyoneExternalId` | valueString | Direct |

---

## 6. Mapping des Assurances

### 6.1 Extension Pharmacie Prestation

**URL** : `http://eyone.sn/fhir/StructureDefinition/pharmacySellPrestation`

| Champ SI | Chemin SI | Type SI | Sub-extension URL | Value Type | Transformation |
|----------|-----------|---------|-------------------|------------|----------------|
| Identifier | `data.pharmacySellPrestation.identifier` | integer | `identifier` | valueInteger | Direct |
| Désactivé | `data.pharmacySellPrestation.disable` | boolean | `disable` | valueBoolean | Direct |
| Prix total | `data.pharmacySellPrestation.totalPrice` | number | `totalPrice` | valueDecimal | Direct |
| Nombre produits | `data.pharmacySellPrestation.nbrProducts` | integer | `nbrProducts` | valueInteger | Direct |
| Commentaire | `data.pharmacySellPrestation.commentary` | string | `commentary` | valueString | Direct |
| Utilisé comme prestation | `data.pharmacySellPrestation.usedAsAPrestation` | boolean | `usedAsAPrestation` | valueBoolean | Direct |
| Assurance complémentaire | `data.pharmacySellPrestation.useComplementaryCare` | boolean | `useComplementaryCare` | valueBoolean | Direct |
| Gestion stock | `data.pharmacySellPrestation.manageStock` | boolean | `manageStock` | valueBoolean | Direct |
| Génération doc requise | `data.pharmacySellPrestation.documentGenerationRequired2` | boolean | `documentGenerationRequired2` | valueBoolean | Direct |
| **Mode facturation pharmacie** | | | `pharmacyBillingMode` | Extension complexe | |
| → Code | `data.pharmacySellPrestation.pharmacyBillingMode.code` | string | `code` | valueString | Direct |
| → Libellé | `data.pharmacySellPrestation.pharmacyBillingMode.label` | string | `label` | valueString | Direct |
| → Portée | `data.pharmacySellPrestation.pharmacyBillingMode.scope` | string | `scope` | valueString | Direct |
| → Global | `data.pharmacySellPrestation.pharmacyBillingMode.global` | boolean | `global` | valueBoolean | Direct |
| → Détail | `data.pharmacySellPrestation.pharmacyBillingMode.detail` | boolean | `detail` | valueBoolean | Direct |
| **Assurance** | | | `care` | Extension complexe | Voir 6.2 |

### 6.2 Extension Assurance (Care)

**URL** : `http://eyone.sn/fhir/StructureDefinition/care`

| Champ SI | Chemin SI | Type SI | Sub-extension URL | Value Type | Transformation |
|----------|-----------|---------|-------------------|------------|----------------|
| Identifier | `data.care.identifier` | integer | `identifier` | valueInteger | Direct |
| External ID | `data.care.eyoneExternalId` | string | `eyoneExternalId` | valueString | Direct |
| Désactivé | `data.care.disable` | boolean | `disable` | valueBoolean | Direct |
| Date début | `data.care.startDate` | string | `startDate` | valueDate | Conversion: DD/MM/YYYY → YYYY-MM-DD |
| Date fin | `data.care.endDate` | string | `endDate` | valueDate | Conversion: DD/MM/YYYY → YYYY-MM-DD |
| Période string | `data.care.carePeriodAsStr` | string | `carePeriodAsStr` | valueString | Direct |
| Numéro assureur | `data.care.insurerNumber` | string | `insurerNumber` | valueString | Direct |
| Pourcentage | `data.care.carePercentage` | integer | `carePercentage` | valueInteger | Direct |
| Limite | `data.care.careLimit` | number | `careLimit` | valueDecimal | Direct |
| A un participant | `data.care.hasAParticipant` | boolean | `hasAParticipant` | valueBoolean | Direct |
| Usage unique | `data.care.oneTimeUsage` | boolean | `oneTimeUsage` | valueBoolean | Direct |
| Bénéficiaire External ID | `data.care.beneficiaryEyoneExternalId` | string | `beneficiaryEyoneExternalId` | valueString | Direct |
| Police External ID | `data.care.policeExternalId` | string | `policeExternalId` | valueString | Direct |
| **Assureur** | | | `insurer` | Extension complexe | |
| → Identifier | `data.care.insurer.identifier` | integer | `identifier` | valueInteger | Direct |
| → Désactivé | `data.care.insurer.disable` | boolean | `disable` | valueBoolean | Direct |
| → Nom | `data.care.insurer.name` | string | `name` | valueString | Direct |
| → Insurer ID | `data.care.insurer.insurerId` | integer | `insurerId` | valueInteger | Direct |
| → Télétransmission | `data.care.insurer.activateTeletransmission` | boolean | `activateTeletransmission` | valueBoolean | Direct |

---

## 7. Transformations Spécifiques

### 7.1 Génération du Narrative (text.div)

**Algorithme** :
```javascript
function generateNarrative(data) {
  const patientName = `${data.patient.firstName} ${data.patient.lastName}`;
  const status = mapStatus(data.status.code);
  const serviceName = data.denomination || data.medicalActCategory.label;
  const organizationName = data.organism.name;
  
  return `<div xmlns="http://www.w3.org/1999/xhtml">
    <p><b>${serviceName}</b></p>
    <p><b>Statut:</b> ${status}</p>
    <p><b>Patient:</b> ${patientName} (${data.patient.eyoneInternalId})</p>
    <p><b>Organisation:</b> ${organizationName}</p>
    ${data.editor ? `<p><b>Éditeur:</b> ${data.editor.completeName}</p>` : ''}
  </div>`;
}
```

**Exemple de sortie** :
```html
<div xmlns="http://www.w3.org/1999/xhtml">
  <p><b>CONSULTATION GENERAL</b></p>
  <p><b>Statut:</b> in-progress</p>
  <p><b>Patient:</b> Rosalie FAYE (PCSR202500041)</p>
  <p><b>Organisation:</b> Centre de Santé Renaissance</p>
  <p><b>Éditeur:</b> admincentre admincentre</p>
</div>
```

### 7.2 Conversion des Dates

**Format source** : `DD/MM/YYYY HH:mm:ss`  
**Format cible** : `YYYY-MM-DDTHH:mm:ss+00:00`

**Algorithme** :
```javascript
function convertDateTime(siDate) {
  if (!siDate) return null;
  
  // Format: "28/10/2025 10:44:41"
  const [datePart, timePart] = siDate.split(' ');
  const [day, month, year] = datePart.split('/');
  
  return `${year}-${month}-${day}T${timePart}+00:00`;
}
```

**Exemples** :
- `28/10/2025 10:44:41` → `2025-10-28T10:44:41+00:00`
- `28/10/2025 10:44:00` → `2025-10-28T10:44:00+00:00`

### 7.3 Conversion de Date Simple

**Format source** : `DD/MM/YYYY`  
**Format cible** : `YYYY-MM-DD`

**Algorithme** :
```javascript
function convertDate(siDate) {
  if (!siDate) return null;
  
  // Format: "01/01/2025"
  const [day, month, year] = siDate.split('/');
  
  return `${year}-${month}-${day}`;
}
```

### 7.4 Normalisation des Codes

**Algorithme** :
```javascript
function normalizeCode(text) {
  if (!text) return null;
  
  // "CONSULTATION GENERAL" → "CONSULTATION_GENERAL"
  return text
    .trim()
    .toUpperCase()
    .replace(/\s+/g, '_');
}
```

---

## 8. Exemple Complet

### 8.1 Source SI (Simplifié)

```json
{
  "messageType": "UPDATE_PRESCRIPTION_FINANCIERE",
  "organismId": "13853",
  "action": "CONSULTATION",
  "data": {
    "identifier": 51957,
    "eyoneInternalId": "AMBUL202500023",
    "createdBy": "admincentre admincentre",
    "creationDate": "28/10/2025 10:44:41",
    "price": 1500,
    "denomination": "CONSULTATION GENERAL",
    "medicalActCategory": {
      "code": "AMBULATORY",
      "label": "Ambulatoire",
      "ambulatory": true
    },
    "status": {
      "code": "ON_GOING",
      "label": "En cours",
      "onGoing": true
    },
    "programmationType": {
      "code": "NOT_SCHEDULED",
      "notScheduled": true
    },
    "patient": {
      "identifier": 18751,
      "eyoneInternalId": "PCSR202500041",
      "firstName": "Rosalie",
      "lastName": "FAYE"
    },
    "startDate": "28/10/2025 10:44:00",
    "endDate": "28/10/2025 10:44:00"
  }
}
```

### 8.2 Cible FHIR R4

```json
{
  "resourceType": "Encounter",
  "id": "AMBUL202500023",
  "meta": {
    "versionId": "1",
    "lastUpdated": "2025-10-28T10:44:41+00:00",
    "profile": ["http://eyone.sn/fhir/StructureDefinition/Encounter-Ambulatory"]
  },
  "text": {
    "status": "generated",
    "div": "<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>CONSULTATION GENERAL</b></p><p><b>Statut:</b> in-progress</p><p><b>Patient:</b> Rosalie FAYE (PCSR202500041)</p><p><b>Organisation:</b> Centre de Santé Renaissance</p></div>"
  },
  "extension": [
    {
      "url": "http://eyone.sn/fhir/StructureDefinition/messageType",
      "valueString": "UPDATE_PRESCRIPTION_FINANCIERE"
    },
    {
      "url": "http://eyone.sn/fhir/StructureDefinition/action",
      "valueString": "CONSULTATION"
    },
    {
      "url": "http://eyone.sn/fhir/StructureDefinition/pricing",
      "extension": [
        {
          "url": "price",
          "valueDecimal": 1500
        },
        {
          "url": "totalPrice",
          "valueDecimal": 1500
        }
      ]
    }
  ],
  "identifier": [
    {
      "system": "http://eyone.sn/fhir/identifier/prestation",
      "value": "51957"
    },
    {
      "system": "http://eyone.sn/fhir/identifier/eyoneInternalId",
      "value": "AMBUL202500023"
    }
  ],
  "status": "in-progress",
  "statusHistory": [
    {
      "status": "in-progress",
      "period": {
        "start": "2025-10-28T10:44:00+00:00",
        "end": "2025-10-28T10:44:00+00:00"
      }
    }
  ],
  "class": {
    "system": "http://terminology.hl7.org/CodeSystem/v3-ActCode",
    "code": "AMB",
    "display": "ambulatory"
  },
  "priority": {
    "coding": [
      {
        "system": "http://terminology.hl7.org/CodeSystem/v3-ActPriority",
        "code": "R",
        "display": "routine"
      }
    ]
  },
  "type": [
    {
      "coding": [
        {
          "system": "http://eyone.sn/fhir/CodeSystem/medical-act-category",
          "code": "AMBULATORY",
          "display": "Ambulatoire"
        }
      ]
    }
  ],
  "serviceType": {
    "coding": [
      {
        "system": "http://eyone.sn/fhir/CodeSystem/service-denomination",
        "code": "CONSULTATION_GENERAL",
        "display": "CONSULTATION GENERAL"
      }
    ]
  },
  "subject": {
    "reference": "Patient/18751",
    "identifier": {
      "system": "http://eyone.sn/fhir/identifier/patient",
      "value": "PCSR202500041"
    },
    "display": "Rosalie FAYE"
  },
  "period": {
    "start": "2025-10-28T10:44:00+00:00",
    "end": "2025-10-28T10:44:00+00:00"
  }
}
```

---

## 9. Validation

### 9.1 Checklist de Validation

**Identifiants** :
- [ ] `resourceType` = "Encounter"
- [ ] `id` = `data.eyoneInternalId`
- [ ] Tous les `identifier[]` ont un `system` et `value`
- [ ] `identifier[].value` sont des strings

**Statut** :
- [ ] `status` est un code valide FHIR R4 (in-progress, finished, cancelled, planned)
- [ ] Mapping correct depuis `data.status.code`
- [ ] `statusHistory` ajouté avec période

**Classe** :
- [ ] `class` est un Coding (pas un array)
- [ ] `class.code` mappé correctement depuis `medicalActCategory.code`
- [ ] `class.system` = "http://terminology.hl7.org/CodeSystem/v3-ActCode"

**ServiceType** :
- [ ] `serviceType` est un CodeableConcept (pas un array)
- [ ] Code normalisé (espaces → underscores)

**Références** :
- [ ] `subject.reference` = "Patient/{patient.identifier}"
- [ ] `subject.identifier` présent avec system et value
- [ ] `subject.display` = "{firstName} {lastName}"
- [ ] `serviceProvider.reference` = "Organization/{organism.identifier}"

**Participant** :
- [ ] `participant[].individual` (pas `actor`)
- [ ] Référence correcte vers Practitioner

**Dates** :
- [ ] `period.start` au format ISO 8601
- [ ] `period.end` au format ISO 8601
- [ ] `meta.lastUpdated` au format ISO 8601

**Extensions** :
- [ ] Extension `messageType` présente
- [ ] Extension `action` présente
- [ ] Extensions financières présentes si données disponibles
- [ ] Toutes les sub-extensions correctement imbriquées

**Narrative** :
- [ ] `text.status` = "generated"
- [ ] `text.div` est du XHTML valide
- [ ] Namespace XHTML présent

### 9.2 Règles de Cohérence

1. **Identifiants** : `id` doit correspondre à `identifier[]` où system = "eyoneInternalId"
2. **Dates** : `period.end` >= `period.start`
3. **Statut** : Cohérence entre `status` et extensions `statusDetails`
4. **Prix** : Si `pricing.price` présent, `pricing.totalPrice` aussi
5. **Patient** : `subject.reference` doit pointer vers un Patient valide

### 9.3 Erreurs Courantes

| Erreur | Cause | Solution |
|--------|-------|----------|
| Invalid status code | Mapping statut incorrect | Vérifier table 2.2.1 |
| Missing reference.identifier | Référence incomplète | Ajouter identifier avec system et value |
| Invalid date format | Date non ISO 8601 | Appliquer convertDateTime() |
| Malformed XHTML | HTML narrative invalide | Utiliser fonction generateNarrative() |
| Wrong class structure | class en array | class doit être un Coding unique |
| Wrong serviceType structure | serviceType en array | serviceType doit être CodeableConcept unique |
| participant.actor exists | Nom de champ R5 | Utiliser participant.individual en R4 |

---

## 10. Annexes

### 10.1 Références FHIR

| Ressource | URL |
|-----------|-----|
| FHIR R4 Specification | https://hl7.org/fhir/R4/ |
| Encounter Resource R4 | https://hl7.org/fhir/R4/encounter.html |
| v3-ActCode ValueSet | https://terminology.hl7.org/CodeSystem-v3-ActCode.html |
| Encounter Status R4 | https://hl7.org/fhir/R4/valueset-encounter-status.html |

### 10.2 CodeSystems Utilisés

| Concept | System | Usage |
|---------|--------|-------|
| Encounter Status | http://hl7.org/fhir/encounter-status | Encounter.status |
| ActCode | http://terminology.hl7.org/CodeSystem/v3-ActCode | Encounter.class |
| ActPriority | http://terminology.hl7.org/CodeSystem/v3-ActPriority | Encounter.priority |
| ParticipationType | http://terminology.hl7.org/CodeSystem/v3-ParticipationType | participant.type |
| Medical Act Category | http://eyone.sn/fhir/CodeSystem/medical-act-category | type |
| Service Denomination | http://eyone.sn/fhir/CodeSystem/service-denomination | serviceType |

### 10.3 Extensions Eyone

Toutes les extensions personnalisées Eyone utilisent le préfixe :
`http://eyone.sn/fhir/StructureDefinition/`

**Liste complète des extensions documentées** (60+) :
- messageType, action, origin, createdBy, creationDate
- medicalActCategory, statusDetails, programmationType
- editor, relatedService, claims
- pricing, patientBilling, prestationPaymentStatus
- prestationBill, documents
- pharmacySellPrestation, care
- Et 40+ autres extensions simples

---

**Version** : 1.0.0  
**Date** : 31 Octobre 2025  
**Auteur** : Eyone Technical Team  
**Statut** : Final