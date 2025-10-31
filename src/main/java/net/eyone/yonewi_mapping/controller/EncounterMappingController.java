package net.eyone.yonewi_mapping.controller;

import lombok.extern.slf4j.Slf4j;
import net.eyone.yonewi_mapping.model.input.Ambulatory;
import net.eyone.yonewi_mapping.model.output.EncounterR4;
import net.eyone.yonewi_mapping.model.output.EncounterR5;
import net.eyone.yonewi_mapping.service.AmbulatoryEncounterMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/encounter")
@Slf4j
public class EncounterMappingController {

    @PostMapping(path = "/r4", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> mapAmbulatoryToEncounterR4(@RequestBody Ambulatory ambulatory) {
        log.info("[EncounterMappingController] [mapAmbulatoryToEncounterR4] requestBody : {} , valeur", safeToString(ambulatory));
        try {
            if (ambulatory == null || ambulatory.getData() == null) {
                String msg = "Invalid payload: 'data' is required";
                log.info("[EncounterMappingController] [mapAmbulatoryToEncounterR4] validationError : {} , valeur", msg);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
            }
            EncounterR4 outRes = AmbulatoryEncounterMapper.toR4(ambulatory);
            log.info("[EncounterMappingController] [mapAmbulatoryToEncounterR4] mappingResult : {} , valeur", safeToString(outRes));
            log.info("[EncounterMappingController] [mapAmbulatoryToEncounterR4] response : {} , valeur", safeToString(outRes));
            return ResponseEntity.ok(outRes);
        } catch (Exception e) {
            log.info("[EncounterMappingController] [mapAmbulatoryToEncounterR4] exception : {} , valeur", e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Mapping error R4");
        }
    }

    @PostMapping(path = "/r5", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> mapAmbulatoryToEncounterR5(@RequestBody Ambulatory ambulatory) {
        log.info("[EncounterMappingController] [mapAmbulatoryToEncounterR5] requestBody : {} , valeur", safeToString(ambulatory));
        try {
            if (ambulatory == null || ambulatory.getData() == null) {
                String msg = "Invalid payload: 'data' is required";
                log.info("[EncounterMappingController] [mapAmbulatoryToEncounterR5] validationError : {} , valeur", msg);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
            }
            EncounterR5 outRes = AmbulatoryEncounterMapper.toR5(ambulatory);
            log.info("[EncounterMappingController] [mapAmbulatoryToEncounterR5] mappingResult : {} , valeur", safeToString(outRes));
            log.info("[EncounterMappingController] [mapAmbulatoryToEncounterR5] response : {} , valeur", safeToString(outRes));
            return ResponseEntity.ok(outRes);
        } catch (Exception e) {
            log.info("[EncounterMappingController] [mapAmbulatoryToEncounterR5] exception : {} , valeur", e.toString(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Mapping error R5");
        }
    }

    private String safeToString(Object value) {
        if (value == null) return "null";
        try { return String.valueOf(value); } catch (Exception e) { return value.getClass().getSimpleName(); }
    }
}
