package io.entake.library.heap.presentation.controller;

import com.smartystreets.api.exceptions.SmartyException;
import io.entake.library.heap.presentation.model.*;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class HeapValidationController {

    private AddressService addressService;

    @Value("#{new Boolean('${smartystreets.enabled}')}")
    private boolean smartyStreetsEnabled;

    @Autowired(required = false)
    public void setAddressService(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/validate")
    public ResponseEntity<?> doFormValidation(@RequestBody HeapApplicationDTO application) {
        Set<String> errors = new LinkedHashSet<>();

        if ("Y".equalsIgnoreCase(application.getApplicant().getAppliedToHeapFlag())
                && application.getApplicant().getHeapApplicationDate() == null) {
            //errors.add("Because you indicated you have applied to HEAP before, you must provide your application date.");
            errors.add("100");
        }

        if ("Y".equalsIgnoreCase(application.getApplicant().getSnapOrTaFlag())
                && StringUtils.isBlank(application.getApplicant().getSnapOrTaCaseNumber())) {
            //errors.add("The applicant's SNAP or TA case number cannot be blank.");
            errors.add("200");
        }

        if ("O5".equalsIgnoreCase(application.getHousingInformation().getHomeType())
                && StringUtils.isBlank(application.getHousingInformation().getOtherLivingSituation())) {
            //errors.add("Because you specified a living situation of Other, you must provide information about it.");
            errors.add("300");
        }

        if ("H2".equalsIgnoreCase(application.getHousingInformation().getHomeType())
                && (application.getHousingInformation().getMultiFamilyCount() == null || application.getHousingInformation().getMultiFamilyCount() == 0)) {
            //errors.add("Because you specified you are in a multi-family home, you must disclose the number of units.");
            errors.add("400");
        }

        if ("N".equalsIgnoreCase(application.getHousingInformation().getHeatingBillInApplicantNameFlag())) {
            if (StringUtils.isBlank(application.getHousingInformation().getNameOnHeatingBill())) {
                //errors.add("You must provide the name on your heating bill.");
                errors.add("500");
            }

            if (StringUtils.isBlank(application.getHousingInformation().getRelationshipForHeatingBill())) {
                //errors.add("You must provide the relationship to the applicant for the name on the heating bill.");
                errors.add("600");
            }
        }

        if ("N".equalsIgnoreCase(application.getHousingInformation().getElectricBillInApplicantNameFlag())
                && StringUtils.isBlank(application.getHousingInformation().getNameOnElectricBill())) {
            //errors.add("You must provide the name on your electric bill.");
            errors.add("700");
        }

        if ("E".equalsIgnoreCase(application.getApplicant().getIncome().getSource())
                && StringUtils.isBlank(application.getApplicant().getIncome().getEmployer())) {
            //errors.add("You must provide the name of your employer.");
            errors.add("800");
        }

        if ("Y".equalsIgnoreCase(application.getApplicant().getIncome().getRentalIncomeFlag())
                && application.getApplicant().getIncome().getRentalIncomeAmount() == null) {
            //errors.add("You must provide the gross monthly amount of Rental Income you receive.");
            errors.add("900");
        }

        if ("Y".equalsIgnoreCase(application.getApplicant().getIncome().getRoomBoardFlag())
                && application.getApplicant().getIncome().getRoomBoardAmount() == null) {
            //errors.add("You must provide the gross monthly amount of Room/Board you receive.");
            errors.add("110");
        }

        if ("Y".equalsIgnoreCase(application.getApplicant().getIncome().getSelfEmploymentFlag())
                && StringUtils.isBlank(application.getApplicant().getIncome().getSelfEmploymentType())) {
            //errors.add("You must specify the type of business for your self-employment income.");
            errors.add("210");
        }

        if ("Y".equalsIgnoreCase(application.getApplicant().getIncome().getSelfEmploymentFlag()) ||
                "Y".equalsIgnoreCase(application.getApplicant().getIncome().getRoomBoardFlag()) ||
                "Y".equalsIgnoreCase(application.getApplicant().getIncome().getRentalIncomeFlag())) {
            if (StringUtils.isBlank(application.getApplicant().getIncome().getPreferredCalculationType())) {
                //errors.add("You must choose a preferred calculation type, because you are receiving rental or self-employment income.");
                errors.add("310");
            }
        }

        if (application.getHouseholdMembers() != null) {
            for (HeapHouseholdMemberDTO member : application.getHouseholdMembers()) {
                if ("Y".equalsIgnoreCase(member.getSnapOrTaFlag())
                        && StringUtils.isBlank(member.getSnapOrTaCaseNumber())) {
                    //errors.add("SNAP or TA case number cannot be blank for " + member.getFirstName() + " " + member.getLastName());
                    errors.add("410");
                }

                if ("E".equalsIgnoreCase(member.getIncome().getSource())
                        && StringUtils.isBlank(member.getIncome().getEmployer())) {
                    //errors.add("You must provide the name of the employer for " + member.getFirstName() + " " + member.getLastName());
                    errors.add("510");
                }
            }
        }

        if (!errors.isEmpty()) {
            return new ResponseEntity<>(new ErrorResponseDTO(errors.stream().toList()), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(new IdDTO(""), HttpStatus.OK);
    }

    @PostMapping("/validate/address")
    public ResponseEntity<?> doAddressValidation(@RequestBody HeapAddressDTO address) {
        if (smartyStreetsEnabled) {
            return doRealAddressValidation(address);
        } else {
            return doFakeAddressValidation(address);
        }
    }

    @PostMapping("/validate/snap")
    public ResponseEntity<?> doSnapTaCaseNumberValidation(@RequestBody CaseNumberDTO dto) {
        if (!StringUtils.startsWith(dto.getCaseNumber(), "NYS-")) {
            return new ResponseEntity<>(new ErrorResponseDTO(List.of("220")), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new  ResponseEntity<>(new IdDTO(""), HttpStatus.OK);
    }

    private ResponseEntity<?> doRealAddressValidation(HeapAddressDTO address) {
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

    private ResponseEntity<?> doFakeAddressValidation(HeapAddressDTO address) {
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


    private void assignRandomCoordinates(HeapAddressDTO address) {
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

