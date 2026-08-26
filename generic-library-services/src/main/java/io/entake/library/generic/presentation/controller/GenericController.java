package io.entake.library.generic.presentation.controller;

import com.smartystreets.api.exceptions.SmartyException;
import io.entake.library.generic.persistence.repository.GenericRepository;
import io.entake.library.generic.presentation.model.*;
import io.entake.particle.core.model.IdDTO;
import io.entake.particle.smartystreets.model.AddressInputDTO;
import io.entake.particle.smartystreets.model.AddressResultDTO;
import io.entake.particle.smartystreets.services.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class GenericController {

    private final GenericRepository repository;
    private final ObjectMapper objectMapper;
    private final AddressService addressService;

    @Value("${smartystreets.auth.id}")
    private String smartyStreetsAuthId;

    @PostMapping("/submissions")
    public ResponseEntity<IdDTO> addSubmission(@RequestBody JsonNode payload) {
        EntakeTransactionDTO baseData = objectMapper.treeToValue(payload, EntakeTransactionDTO.class);
        EntakeMetadataDTO metadata = baseData.getEntakeMetadata();
        String json = payload.toString();

        return ResponseEntity.ok(repository.addSubmission(metadata, json));
    }

    @GetMapping("/submissions")
    public ResponseEntity<PaginatedContainerDTO<EntakeSubmissionResultDTO>> findSubmissions(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "25") Integer pageSize,
            @RequestParam(required = false) String locale,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String environment,
            @RequestParam(required = false) String formId
    ) {
        return ResponseEntity.ok(repository.findSubmissions(page, pageSize, locale, status, environment, formId));
    }

    @PostMapping("/entrypoint")
    public ResponseEntity<EntrypointResponseDTO> handleEntrypoint(@RequestBody JsonNode payload) {
        AddressDTO address1 = new AddressDTO();
        address1.setStreetLine1("123 Main St.");
        address1.setStreetLine2("Suite 100");
        address1.setCity("New York");
        address1.setState("NY");
        address1.setZipcode("10001");

        AddressDTO address2 = new AddressDTO();
        address2.setStreetLine1("456 Nowhere Rd.");
        address2.setStreetLine2("Apt. 2");
        address2.setCity("Los Angeles");
        address2.setState("CA");
        address2.setZipcode("99999");

        return ResponseEntity.ok(
                EntrypointResponseDTO.builder()
                        .firstName("Test")
                        .middleName("Ing")
                        .lastName("User")
                        .addresses(List.of(address1, address2))
                        .homeAddress(address2)
                        .build()
        );
    }

    @PostMapping("/validate")
    public ResponseEntity<?> doFormValidation(@RequestBody JsonNode payload) {
        String json = payload.toString();
        if (json.contains("bad data")) {
            List<String> errors = new ArrayList<>();
            errors.add("100");
            return ResponseEntity.unprocessableContent().body(new ErrorResponseDTO(errors));
        }

        return ResponseEntity.ok(new IdDTO(""));
    }

    @PostMapping("/validate/address")
    public ResponseEntity<?> doAddressValidation(@RequestBody AddressDTO address) {
        if (StringUtils.isNotBlank(smartyStreetsAuthId)) {
            return doRealAddressValidation(address);
        } else {
            return doFakeAddressValidation(address);
        }
    }

    private ResponseEntity<?> doRealAddressValidation(AddressDTO address) {
        AddressInputDTO input = new AddressInputDTO();
        input.setStreet(address.getStreetLine1());
        input.setStreet2(address.getStreetLine2());
        input.setCity(address.getCity());
        input.setState(address.getState());
        input.setZipcode(address.getZipcode());

        try {
            AddressResultDTO result = addressService.checkAddress(input);
            address.setStreetLine1(result.getLine1());
            address.setStreetLine2(result.getLine2());
            address.setCity(result.getCity());
            address.setState(result.getState());
            address.setLatitude(String.valueOf(result.getCoordinates().getLatitude()));
            address.setLongitude(String.valueOf(result.getCoordinates().getLongitude()));
            address.setZipcode(result.getZipcode());
            address.setZipPlus4(result.getZip4());
        } catch (SmartyException | IOException | InterruptedException e) {
            log.error(e.getMessage(), e);

            List<String> errors = new ArrayList<>();
            errors.add("200");
            return ResponseEntity.unprocessableContent().body(new ErrorResponseDTO(errors));
        }

        return ResponseEntity.ok(address);
    }

    private ResponseEntity<?> doFakeAddressValidation(AddressDTO address) {
        if ("bad address".equalsIgnoreCase(address.getStreetLine1())) {
            List<String> errors = new ArrayList<>();
            errors.add("200");
            return ResponseEntity.unprocessableContent().body(new ErrorResponseDTO(errors));
        }

        address.setStreetLine1(StringUtils.toRootUpperCase(address.getStreetLine1()));
        address.setStreetLine2(StringUtils.toRootUpperCase(address.getStreetLine2()));
        address.setCity(StringUtils.toRootUpperCase(address.getCity()));
        address.setState(StringUtils.toRootUpperCase(address.getState()));
        address.setZipPlus4(RandomStringUtils.secure().nextNumeric(4));
        assignRandomCoordinates(address);

        return new ResponseEntity<>(address, HttpStatus.OK);
    }


    private void assignRandomCoordinates(AddressDTO address) {
        double minLat = -90.00;
        double maxLat = 90.00;
        double latitude = minLat + (Math.random() * ((maxLat - minLat) + 1));
        double minLon = 0.00;
        double maxLon = 180.00;
        double longitude = minLon + (Math.random() * ((maxLon - minLon) + 1));
        DecimalFormat df = new DecimalFormat("#.#####");

        address.setLatitude(df.format(latitude));
        address.setLongitude(df.format(longitude));
    }

}

