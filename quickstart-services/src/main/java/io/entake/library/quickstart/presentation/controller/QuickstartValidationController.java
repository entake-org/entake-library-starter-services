package io.entake.library.quickstart.presentation.controller;

import com.smartystreets.api.exceptions.SmartyException;
import io.entake.library.quickstart.presentation.model.ErrorResponseDTO;
import io.entake.library.quickstart.presentation.model.AddressDTO;
import io.entake.library.quickstart.presentation.model.QuickstartSubmissionDTO;
import io.entake.particle.core.model.IdDTO;
import io.entake.particle.smartystreets.model.AddressInputDTO;
import io.entake.particle.smartystreets.model.AddressResultDTO;
import io.entake.particle.smartystreets.services.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class QuickstartValidationController {

    private AddressService addressService;

    @Value("#{new Boolean('${smartystreets.enabled}')}")
    private boolean smartyStreetsEnabled;

    @Autowired(required = false)
    public void setAddressService(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/validate")
    public ResponseEntity<?> doFormValidation(@RequestBody QuickstartSubmissionDTO submission) {
        boolean hasNyAddress = false;
        if (submission.getAddresses() != null) {
            for (AddressDTO address : submission.getAddresses()){
                if ("NY".equalsIgnoreCase(address.getState())) {
                    hasNyAddress = true;
                    break;
                }
            }
        }

        if (!hasNyAddress) {
            List<String> errors = new ArrayList<>();
            errors.add("Must have a residence in New York to be eligible.");
            return new ResponseEntity<>(new ErrorResponseDTO(errors), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(new IdDTO(""), HttpStatus.OK);
    }

    @PostMapping("/validate/address")
    public ResponseEntity<?> doAddressValidation(@RequestBody AddressDTO address) {
        if (smartyStreetsEnabled) {
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
            errors.add("This address could not be validated at this time");
            return new ResponseEntity<>(new ErrorResponseDTO(errors), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    private ResponseEntity<?> doFakeAddressValidation(AddressDTO address) {
        if ("bad address".equalsIgnoreCase(address.getStreetLine1())) {
            List<String> errors = new ArrayList<>();
            errors.add("100");
            //errors.add("This address could not be found by the USPS");
            return new ResponseEntity<>(new ErrorResponseDTO(errors), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        address.setStreetLine1(StringUtils.toRootUpperCase(address.getStreetLine1()));
        address.setStreetLine2(StringUtils.toRootUpperCase(address.getStreetLine2()));
        address.setCity(StringUtils.toRootUpperCase(address.getCity()));
        address.setState(StringUtils.toRootUpperCase(address.getState()));
        address.setZipPlus4(RandomStringUtils.randomNumeric(4));
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

