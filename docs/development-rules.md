# RÈGLES DE DÉVELOPPEMENT (Projet Yonewi Mapping)

## 1. ENVIRONNEMENT TECHNIQUE
- Java 21 (déjà configuré dans `pom.xml`: `<java.version>21</java.version>`)

## 2. RÈGLES DE LOGGING
- Format imposé: `[NomDeLaClasse] [nomDeLaMethode] attribut : {} , valeur`
- Toujours logger:
  - Les valeurs d'entrée (corps de requête, paramètres) → `log.info` via helper `in(...)`
  - Chaque affectation de valeur à une variable/attribut → `log.info` via helper `set(...)`
  - Les valeurs de sortie / réponse → `log.info` via helper `out(...)`
  - Les erreurs avec stacktrace → `log.error` via helper `error(...)`
- Utiliser exclusivement Lombok `@Slf4j` dans chaque classe; plus d'utilitaire séparé. Créer de petits helpers privés (`in/set/out/error`) qui wrap `log.*` pour garantir le format.

## 3. RÈGLES D'ARCHITECTURE
1. Si un service n'a pas d'injection, le faire en classe utilitaire `final` avec méthodes `static` (pas de `@Service`).
2. Toujours déclarer des constantes avec des variables littérales (`private static final String ...`).
3. Privilégier `try-catch` par rapport à `Optional` dans les flux principaux.
4. Appeler directement les `repository` dans les services (quand présents) et encapsuler toutes les opérations BD dans des `try-catch`.
5. Avoir des méthodes parlantes (noms explicites).
6. Respecter SOLID; pas de classes internes (sauf config type `@ConfigurationProperties`).
7. Privilégier les `if` classiques plutôt que les expressions lambda `ifPresentOrElse`, etc.
8. Pour les classes de transformation, toujours utiliser `try-catch` (préférer `try-catch` sur `Optional`).
9. Combinaison `Optional` + `try-catch` seulement dans utilitaires où l'échec est normal (évite `null`, force la vérification, log sans propagation immédiate).

## 4. ERREURS À ÉVITER AVEC JOLT
- Ne jamais utiliser des clés avec crochets dans `default` (provoque des erreurs Jackson).
  ```json
  {
    "operation": "default",
    "spec": {
      "type[0].coding[0].system": "valeur"
    }
  }
  ```
- Ne jamais utiliser `modify-overwrite-beta` avec des chemins complexes ("too many [] references").
  ```json
  {
    "operation": "modify-overwrite-beta",
    "spec": {
      "type[0].coding[0].display": "=tempValue"
    }
  }
  ```

## 5. SOLUTION CORRECTE JOLT
- Utiliser une seule opération `shift` avec la syntaxe spéciale:
  ```json
  [
    {
      "operation": "shift",
      "spec": {
        "#constante": "chemin.destination",
        "champInput": {
          "@": "chemin[0].valeur",
          "#constante1": "chemin[0].propriete1",
          "#constante2": "chemin[0].propriete2"
        }
      }
    }
  ]
  ```

### Rappels de syntaxe
- `#valeur` = constante littérale
- `@` = valeur du champ d'entrée
- `"champ": ["dest1", "dest2"]` = dupliquer vers plusieurs destinations

## 6. RÈGLE D'OR JOLT
- Pour des structures FHIR complexes, privilégier une seule opération `shift` qui construit directement la structure finale, plutôt que d'enchaîner plusieurs opérations risquant de générer des clés avec crochets.

## 7. APPLICATION AU PROJET
- `AmbulatoryEncounterMapper` est une classe utilitaire statique (pas d'injection) avec:
  - constantes littérales pour les systèmes/code FHIR,
  - `try-catch` englobant chaque transformation (`toR4`, `toR5`),
  - logs d'entrée/sortie et logs à chaque affectation via `@Slf4j` (helpers privés `in/set/out/error`).
- `EncounterMappingController` logge l'entrée, les erreurs de validation, la sortie; appelle directement les méthodes statiques du mapper.
- Si des repositories ou transformations JOLT sont introduits ultérieurement, appliquer strictement les règles ci-dessus.
