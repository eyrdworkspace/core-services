/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.eis.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.eis.model.Designation;
import org.egov.eis.service.DesignationService;
import org.egov.eis.web.contract.DesignationGetRequest;
import org.egov.eis.web.contract.DesignationRequest;
import org.egov.eis.web.contract.DesignationResponse;
import org.egov.eis.web.contract.RequestInfo;
import org.egov.eis.web.contract.RequestInfoWrapper;
import org.egov.eis.web.contract.ResponseInfo;
import org.egov.eis.web.contract.factory.ResponseInfoFactory;
import org.egov.eis.web.errorhandlers.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/designations")
public class DesignationController {

	private static final Logger logger = LoggerFactory.getLogger(DesignationController.class);

	@Autowired
	private DesignationService designationService;

	@Autowired
	private ErrorHandler errHandler;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute @Valid DesignationGetRequest designationGetRequest,
			BindingResult modelAttributeBindingResult, @RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			BindingResult requestBodyBindingResult) {
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

		// validate input params
		if (modelAttributeBindingResult.hasErrors()) {
			return errHandler.getErrorResponseEntityForMissingParameters(modelAttributeBindingResult, requestInfo);
		}

		// validate input params
		if (requestBodyBindingResult.hasErrors()) {
			return errHandler.getErrorResponseEntityForMissingRequestInfo(requestBodyBindingResult, requestInfo);
		}

		// Call service
		List<Designation> designationsList = null;
		try {
			designationsList = designationService.getDesignations(designationGetRequest);
		} catch (Exception exception) {
			logger.error("Error while processing request " + designationGetRequest, exception);
			return errHandler.getResponseEntityForUnexpectedErrors(requestInfo);
		}

		return getSuccessResponse(designationsList, requestInfo);
	}

	/**
	 * Maps Post Requests for _create & returns ResponseEntity of either
	 * DesignationResponse type or ErrorResponse type
	 * 
	 * @param DesignationRequest
	 * @param BindingResult
	 * @return ResponseEntity<?>
	 */

	@PostMapping("_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody DesignationRequest designationRequest, BindingResult bindingResult) {

		ResponseEntity<?> errorResponseEntity = validateDesignationRequest(designationRequest, bindingResult);
		if (errorResponseEntity != null)
			return errorResponseEntity;

		return designationService.createDesignation(designationRequest);
	}

	/**
	 * Maps Post Requests for _create & returns ResponseEntity of either
	 * DesignationDesignationResponse type or ErrorResponse type
	 * 
	 * @param DesignationRequest
	 * @param BindingResult
	 * @return ResponseEntity<?>
	 */

	@PostMapping("/{id}/_update")
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody DesignationRequest designationRequest,
			@PathVariable(required = true, name = "id") Long id, BindingResult bindingResult) {

		ResponseEntity<?> errorResponseEntity = validateDesignationRequest(designationRequest, bindingResult);
		if (errorResponseEntity != null)
			return errorResponseEntity;
		designationRequest.getDesignation().setId(id);
		return designationService.updateDesignation(designationRequest);
	}

	/**
	 * Populate Response object and returndesignationsList
	 * 
	 * @param designationsList
	 * @return
	 */
	private ResponseEntity<?> getSuccessResponse(List<Designation> designationsList, RequestInfo requestInfo) {
		DesignationResponse designationRes = new DesignationResponse();
		designationRes.setDesignation(designationsList);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		designationRes.setResponseInfo(responseInfo);
		return new ResponseEntity<DesignationResponse>(designationRes, HttpStatus.OK);

	}

	/**
	 * Validate DesignationRequest object & returns ErrorResponseEntity if there
	 * are any errors or else returns null
	 * 
	 * @param DesignationRequest
	 * @param bindingResult
	 * @return ResponseEntity<?>
	 */
	private ResponseEntity<?> validateDesignationRequest(DesignationRequest designationRequest,
			BindingResult bindingResult) {
		// validate input params that can be handled by annotations
		if (bindingResult.hasErrors()) {
			return errHandler.getErrorResponseEntityForBindingErrors(bindingResult,
					designationRequest.getRequestInfo());
		}
		return null;
	}
}