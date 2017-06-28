/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.wcms.service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.egov.wcms.model.Donation;
import org.egov.wcms.producers.WaterMasterProducer;
import org.egov.wcms.repository.DonationRepository;
import org.egov.wcms.web.contract.DonationGetRequest;
import org.egov.wcms.web.contract.DonationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DonationService {

    public static final Logger logger = LoggerFactory.getLogger(DonationService.class);

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private WaterMasterProducer waterMasterProducer;

    public DonationRequest create(final DonationRequest donationRequest) {
        return donationRepository.persistDonationDetails(donationRequest);
    }

    public DonationRequest update(final DonationRequest donationRequest) {
        return donationRepository.persistModifyDonationDetails(donationRequest);
    }

    public Donation sendMessage(final String topic, final String key, final DonationRequest donationRequest) {
        final ObjectMapper mapper = new ObjectMapper();
        String donationRequestValue = null;
        try {
            logger.info("Donation service::" + donationRequest);

            final Donation donation = getIdForRequestCodes(donationRequest);
            donationRequest.setDonation(donation);
            donationRequestValue = mapper.writeValueAsString(donationRequest);

            logger.info("Donation Request Value::" + donationRequestValue);
        } catch (final JsonProcessingException e) {
            logger.error("Exception Encountered : " + e);
        }
        try {
            waterMasterProducer.sendMessage(topic, key, donationRequestValue);
        } catch (final Exception ex) {
            logger.error("Exception Encountered : " + ex);
        }
        return donationRequest.getDonation();
    }

    public List<Donation> getDonationList(final DonationGetRequest donationGetRequest) {
        return donationRepository.getDonationList(donationGetRequest);
    }

    private Donation getIdForRequestCodes(final DonationRequest donationRequest) {
        // Hit the Property Tax APIs to verify and get the IDs for the Code.
        // Once APIs are available, remove random number function.
        final Donation donation = new Donation();
        ThreadLocalRandom.current().nextInt(1, 10 + 1);
        donation.setPropertyTypeId(2L);
        ThreadLocalRandom.current().nextInt(1, 10 + 1);
        donation.setUsageTypeId(3L);
        ThreadLocalRandom.current().nextInt(1, 10 + 1);
        donation.setCategoryTypeId(3L);
        ThreadLocalRandom.current().nextInt(1, 10 + 1);
        donation.setMinHSCPipeSizeId(1);
        ThreadLocalRandom.current().nextInt(1, 10 + 1);
        donation.setMaxHSCPipeSizeId(2);
        return donation;
    }

    private Donation getIdForRequestCodes(final DonationGetRequest donationGetRequest) {
        // Hit the Property Tax APIs to verify and get the IDs for the Code.
        // Once APIs are available, remove random number function.

        // Create a Donation Object. Transfer all the values from Donation Get Request to Donation
        // Return the Donation Object so that Get Donation List fetches the value for these
        // parameters.

        final Donation donation = new Donation();
        int randomNum = ThreadLocalRandom.current().nextInt(1, 10 + 1);
        donation.setPropertyTypeId(randomNum);
        randomNum = ThreadLocalRandom.current().nextInt(1, 10 + 1);
        donation.setUsageTypeId(randomNum);
        randomNum = ThreadLocalRandom.current().nextInt(1, 10 + 1);
        donation.setCategoryTypeId(randomNum);
        randomNum = ThreadLocalRandom.current().nextInt(1, 10 + 1);
        donation.setMinHSCPipeSizeId(randomNum);
        randomNum = ThreadLocalRandom.current().nextInt(1, 10 + 1);
        donation.setMaxHSCPipeSizeId(randomNum);
        return donation;
    }

}