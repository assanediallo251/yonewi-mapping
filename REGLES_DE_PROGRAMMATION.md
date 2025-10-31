# RÈGLES DE DÉVELOPPEMENT

## 1. ENVIRONNEMENT TECHNIQUE
1. On est sur du Java 21

## 2. RÈGLES DE LOGGING
2. Pour les logs, mettre "[nom de la classe] [nom de la methode] attribut : {} ", valeur
3. Logger à chaque fois avec valeurs d'entrée et valeur de sortie
4. Faire des logs pour chaque affectation de valeur à une variable

## 3. RÈGLES D'ARCHITECTURE
4. Si le service n'a pas d'injection, le faire dans une classe static
5. Toujours déclarer avec des variables littérales
6. Privilégier try-catch par rapport à Optional
7. Appeler directement les repository dans les services
8. Les méthodes doivent être parlantes
9. Respecter les normes SOLID
10. Ne pas faire de sous-classes à l'intérieur des classes (sauf pour les configurations comme @ConfigurationProperties)
11. Privilégier les conditions if par rapport aux expressions lambda (comme ifPresentOrElse)
12. Pour les classes de transformation, toujours utiliser try-catch (privilégier try-catch sur Optional)
13. Toujours encapsuler les opérations de base de données dans des try-catch
14. Combinaison Optional + try-catch autorisée pour les méthodes utilitaires où l'échec est normal (évite null, force la vérification, permet logging sans propagation immédiate) 

## 4. 🚫 ERREURS À ÉVITER AVEC JOLT

### 10. Ne jamais utiliser des clés avec crochets dans `default`
```json
// ❌ MAUVAIS - génère des erreurs Jackson
{
  "operation": "default",
  "spec": {
    "type[0].coding[0].system": "valeur"
  }
}
```

### 11. Ne jamais utiliser `modify-overwrite-beta` avec des chemins complexes
```json
// ❌ MAUVAIS - "too many [] references"
{
  "operation": "modify-overwrite-beta",
  "spec": {
    "type[0].coding[0].display": "=tempValue"
  }
}
```

## 5. ✅ SOLUTION CORRECTE JOLT

### 12. Utiliser une seule opération `shift` avec la syntaxe spéciale :

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

### 13. Syntaxe clé :
- `#valeur` = constante littérale
- `@` = valeur du champ d'entrée
- `"champ": ["dest1", "dest2"]` = dupliquer vers plusieurs destinations

## 6. 📝 RÈGLE D'OR JOLT
**14. Pour des structures complexes FHIR : toujours privilégier une seule opération `shift` qui construit directement la structure finale, plutôt que plusieurs opérations qui risquent de générer des clés avec crochets.**