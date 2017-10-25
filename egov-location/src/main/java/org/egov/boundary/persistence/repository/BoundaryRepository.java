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

package org.egov.boundary.persistence.repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.egov.boundary.domain.model.Boundary;
import org.egov.boundary.domain.model.BoundarySearchRequest;
import org.egov.boundary.persistence.repository.querybuilder.BoundaryQueryBuilder;
import org.egov.boundary.persistence.repository.rowmapper.BoundaryIdRowMapper;
import org.egov.boundary.persistence.repository.rowmapper.BoundaryRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BoundaryRepository {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public BoundaryRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
		this.jdbcTemplate = jdbcTemplate;
	}

	private static final String SELECT_NEXT_BOUNDARY_SEQUENCE = "select nextval('seq_eg_boundary')";

	private Long getNextSequence() {
		return jdbcTemplate.queryForObject(SELECT_NEXT_BOUNDARY_SEQUENCE, Long.class);
	}

	public Boundary save(Boundary boundary) {

		Map<String, Object> parametersMap = new HashMap<String, Object>();
		boundary.setId(getNextSequence());
		parametersMap.put("id", boundary.getId());
		parametersMap.put("boundarynum", boundary.getBoundaryNum());
		if (boundary.getParent() != null && boundary.getParent().getId() != null) {
			parametersMap.put("parent", boundary.getParent().getId());
		} else {
			parametersMap.put("parent", boundary.getParent());
		}
		parametersMap.put("name", boundary.getName());
		parametersMap.put("code", boundary.getCode());

		parametersMap.put("boundarytype", Long.valueOf(boundary.getBoundaryType().getId()));
		parametersMap.put("localname", boundary.getLocalName());
		parametersMap.put("fromdate", boundary.getFromDate());
		parametersMap.put("todate", boundary.getToDate());
		parametersMap.put("bndryid", boundary.getBndryId());
		parametersMap.put("longitude", boundary.getLongitude());
		parametersMap.put("latitude", boundary.getLatitude());
		parametersMap.put("materializedpath", boundary.getMaterializedPath());
		parametersMap.put("ishistory", boundary.isHistory());
		parametersMap.put("tenantid", boundary.getTenantId());
		parametersMap.put("createddate", new Date());
		parametersMap.put("lastmodifieddate", new Date());
		parametersMap.put("createdby", 1);
		parametersMap.put("lastmodifiedby", 1);
		namedParameterJdbcTemplate.update(BoundaryQueryBuilder.getBoundaryInsertquery(), parametersMap);
		return findByTenantIdAndId(boundary.getTenantId(), boundary.getId());
	}

	public Boundary update(Boundary boundary) {

		Map<String, Object> parametersMap = new HashMap<String, Object>();
		parametersMap.put("boundarynum", boundary.getBoundaryNum());
		if (boundary.getParent() != null && boundary.getParent().getId() != null) {
			parametersMap.put("parent", boundary.getParent().getId());
		} else {
			parametersMap.put("parent", boundary.getParent());
		}
		parametersMap.put("name", boundary.getName());
		parametersMap.put("boundarytype", Long.valueOf(boundary.getBoundaryType().getId()));
		parametersMap.put("localname", boundary.getLocalName());
		parametersMap.put("fromdate", boundary.getFromDate());
		parametersMap.put("todate", boundary.getToDate());
		parametersMap.put("bndryid", boundary.getBndryId());
		parametersMap.put("longitude", boundary.getLongitude());
		parametersMap.put("latitude", boundary.getLatitude());
		parametersMap.put("materializedpath", boundary.getMaterializedPath());
		parametersMap.put("ishistory", boundary.isHistory());
		parametersMap.put("lastmodifieddate", new Date());
		parametersMap.put("lastmodifiedby", 1);
		parametersMap.put("code", boundary.getCode());
		parametersMap.put("tenantid", boundary.getTenantId());
		namedParameterJdbcTemplate.update(BoundaryQueryBuilder.getBoundaryUpdateQuery(), parametersMap);
		return findByTenantIdAndCode(boundary.getTenantId(), boundary.getCode());
	}

	public Boundary findByTenantIdAndId(String tenantId, Long id) {
		Map<String, Object> parametersMap = new HashMap<String, Object>();
		Boundary boundary = null;
		parametersMap.put("tenantId", tenantId);
		parametersMap.put("id", id);
		List<Boundary> boundaryList = namedParameterJdbcTemplate.query(BoundaryQueryBuilder.getBoundaryByIdAndTenant(),
				parametersMap, new BoundaryRowMapper());
		boundaryList = setBoundariesWithParents(boundaryList);
		if (boundaryList != null && !boundaryList.isEmpty())
			boundary = boundaryList.parallelStream().filter(i -> i.getId().equals(id)).findAny().get();
		return boundary;
	}

	public List<Boundary> findByTenantIdAndCodes(String tenantId, List<String> codes) {
		Map<String, Object> parametersMap = new HashMap<String, Object>();
		parametersMap.put("tenantId", tenantId);
		parametersMap.put("codes", codes);
		List<Boundary> boundaryList = namedParameterJdbcTemplate
				.query(BoundaryQueryBuilder.getBoundaryByCodesAndTenant(), parametersMap, new BoundaryRowMapper());
		boundaryList = setBoundariesWithParents(boundaryList);
		if (boundaryList != null && !boundaryList.isEmpty())
			boundaryList = boundaryList.stream().filter(i -> codes.contains(i.getCode())).collect(Collectors.toList());
		return boundaryList;
	}

	public Boundary findByTenantIdAndCode(String tenantId, String code) {
		Map<String, Object> parametersMap = new HashMap<String, Object>();
		Boundary boundary = null;
		parametersMap.put("tenantId", tenantId);
		parametersMap.put("code", code);
		List<Boundary> boundaryList = namedParameterJdbcTemplate
				.query(BoundaryQueryBuilder.getBoundaryByCodeAndTenant(), parametersMap, new BoundaryRowMapper());
		boundaryList = setBoundariesWithParents(boundaryList);
		if (boundaryList != null && !boundaryList.isEmpty())
			boundary = boundaryList.parallelStream().filter(i -> i.getCode().equals(code)).findAny().get();
		return boundary;
	}

	public List<Boundary> findAllByTenantId(String tenantId) {
		Map<String, Object> parametersMap = new HashMap<String, Object>();
		parametersMap.put("tenantId", tenantId);
		List<Boundary> boundaryList = namedParameterJdbcTemplate.query(BoundaryQueryBuilder.getAllByTenantId(),
				parametersMap, new BoundaryRowMapper());
		boundaryList = setBoundariesWithParents(boundaryList);
		return boundaryList;
	}

	public Boundary findBoundarieByBoundaryTypeAndBoundaryNum(Long boundaryTypeId, Long boundaryNum, String tenantId) {
		Map<String, Object> parametersMap = new HashMap<String, Object>();
		parametersMap.put("id", boundaryTypeId);
		parametersMap.put("boundaryNum", boundaryNum);
		parametersMap.put("tenantId", tenantId);
		List<Boundary> BoundaryList = namedParameterJdbcTemplate.query(
				BoundaryQueryBuilder.getBoundarieByBoundaryTypeAndBoundaryNumAndTenantId(), parametersMap,
				new BoundaryRowMapper());
		return BoundaryList.get(0);
	}

	public List<Boundary> getAllBoundariesByBoundaryTypeIdAndTenantId(final Long boundaryTypeId,
			final String tenantId) {
		Map<String, Object> parametersMap = new HashMap<String, Object>();
		parametersMap.put("boundaryTypeId", boundaryTypeId);
		parametersMap.put("tenantId", tenantId);
		List<Boundary> boundaryList = namedParameterJdbcTemplate.query(
				BoundaryQueryBuilder.getAllBoundarieByBoundaryTypeAndTenantId(), parametersMap,
				new BoundaryRowMapper());
		boundaryList = setBoundariesWithParents(boundaryList);
		if (boundaryList != null && !boundaryList.isEmpty()) {
			boundaryList = boundaryList.stream()
					.filter(p -> p.getBoundaryType().getId().equals(boundaryTypeId.toString()))
					.collect(Collectors.toList());
		}
		return boundaryList;
	}

	public List<Boundary> getBoundariesByBndryTypeNameAndHierarchyTypeNameAndTenantId(final String boundaryTypeName,
			final String hierarchyTypeName, final String tenantId) {
		Map<String, Object> parametersMap = new HashMap<String, Object>();
		parametersMap.put("boundaryTypeName", boundaryTypeName);
		parametersMap.put("hierarchyTypeName", hierarchyTypeName);
		parametersMap.put("tenantId", tenantId);
		List<Boundary> BoundaryList = namedParameterJdbcTemplate.query(
				BoundaryQueryBuilder.getBoundariesByBndryTypeNameAndHierarchyTypeNameAndTenantId(), parametersMap,
				new BoundaryIdRowMapper());
		return BoundaryList;
	}

	public List<Boundary> getBoundaryByTypeAndNumber(final long boundaryNumber, final long boundaryType) {
		Map<String, Object> parametersMap = new HashMap<String, Object>();
		parametersMap.put("id", boundaryType);
		parametersMap.put("boundaryNum", boundaryNumber);
		List<Boundary> boundaryList = namedParameterJdbcTemplate.query(
				BoundaryQueryBuilder.getBoundarieByBoundaryTypeAndBoundaryNum(), parametersMap,
				new BoundaryRowMapper());
		return boundaryList;
	}

	public List<Boundary> getAllBoundaryByTenantIdAndTypeIds(String tenantId, List<Long> boundaryTypeIds) {
		Map<String, Object> parametersMap = new HashMap<String, Object>();
		parametersMap.put("tenantId", tenantId);
		parametersMap.put("boundaryTypeIds", boundaryTypeIds);
		List<Boundary> boundaryList = namedParameterJdbcTemplate.query(
				BoundaryQueryBuilder.getAllBoundaryByTenantIdAndTypeIds(), parametersMap, new BoundaryRowMapper());
		boundaryList = setBoundariesWithParents(boundaryList);
		if (boundaryList != null && !boundaryList.isEmpty()) {
			boundaryList = boundaryList.stream()
					.filter(i -> boundaryTypeIds.contains(Long.valueOf(i.getBoundaryType().getId())))
					.collect(Collectors.toList());
		}
		return boundaryList;
	}

	public List<Boundary> findAllBoundariesByIdsAndTenant(final String tenantId, final List<Long> boundaryIds) {
		Map<String, Object> parametersMap = new HashMap<String, Object>();
		parametersMap.put("tenantId", tenantId);
		parametersMap.put("boundaryIds", boundaryIds);
		List<Boundary> boundaryList = namedParameterJdbcTemplate
				.query(BoundaryQueryBuilder.findAllBoundariesByIdsAndTenant(), parametersMap, new BoundaryRowMapper());
		boundaryList = setBoundariesWithParents(boundaryList);
		if(boundaryList!=null && !boundaryList.isEmpty())
		boundaryList = boundaryList.stream().filter(i -> boundaryIds.contains(i.getId())).collect(Collectors.toList());
		return boundaryList;
	}

	public List<Boundary> getAllBoundariesByIdsAndTypeAndNumberAndCodeAndTenant(
			BoundarySearchRequest boundarySearchRequest) {
		Map<String, Object> parametersMap = new HashMap<String, Object>();
		String Query = BoundaryQueryBuilder.getAllBoundariesByIdsAndTypeAndNumberAndCodeAndTenant(boundarySearchRequest,
				parametersMap);
		List<Boundary> boundaryList = namedParameterJdbcTemplate.query(Query, parametersMap, new BoundaryRowMapper());
		boundaryList = setBoundariesWithParents(boundaryList);

		if (boundarySearchRequest.getBoundaryIds() != null && !boundarySearchRequest.getBoundaryIds().isEmpty()) {
			boundaryList = boundaryList.stream().filter(p -> boundarySearchRequest.getBoundaryIds().contains(p.getId()))
					.collect(Collectors.toList());
		}

		if (boundarySearchRequest.getCodes() != null && !boundarySearchRequest.getCodes().isEmpty()) {
			boundaryList = boundaryList.stream().filter(p -> boundarySearchRequest.getCodes().contains(p.getCode()))
					.collect(Collectors.toList());
		}

		if (boundarySearchRequest.getBoundaryNumbers() != null
				&& !boundarySearchRequest.getBoundaryNumbers().isEmpty()) {
			boundaryList = boundaryList.stream()
					.filter(p -> boundarySearchRequest.getBoundaryNumbers().contains(p.getBoundaryNum()))
					.collect(Collectors.toList());
		}
		if (boundarySearchRequest.getBoundaryTypeIds() != null
				&& !boundarySearchRequest.getBoundaryTypeIds().isEmpty()) {
			boundaryList = boundaryList.stream().filter(
					p -> boundarySearchRequest.getBoundaryTypeIds().contains(Long.valueOf(p.getBoundaryType().getId())))
					.collect(Collectors.toList());
		}
		if (boundarySearchRequest.getHierarchyTypeIds() != null
				&& !boundarySearchRequest.getHierarchyTypeIds().isEmpty()) {
			boundaryList = boundaryList.stream().filter(p -> boundarySearchRequest.getHierarchyTypeIds()
					.contains(p.getBoundaryType().getHierarchyType().getId())).collect(Collectors.toList());
		}
		return boundaryList;
	}

	public List<Boundary> findAllParents() {
		List<Boundary> boundaryList = namedParameterJdbcTemplate.query(BoundaryQueryBuilder.getAllParents(),
				new BoundaryIdRowMapper());
		return boundaryList;
	}

	public List<Boundary> findActiveImmediateChildrenWithOutParent(Long parentId) {
		Map<String, Object> parametersMap = new HashMap<String, Object>();
		parametersMap.put("parentId", parentId);
		List<Boundary> boundaryList = namedParameterJdbcTemplate.query(
				BoundaryQueryBuilder.getActiveImmediateChildrenWithOutParent(), parametersMap,
				new BoundaryIdRowMapper());
		return boundaryList;
	}

	public List<Boundary> setBoundariesWithParents(List<Boundary> boundaryList) {
		for (int i = 0; i < boundaryList.size(); i++) {
			for (int j = 0; j < boundaryList.size(); j++) {
				if (null != boundaryList.get(i).getParent()
						&& boundaryList.get(i).getParent().getId().equals(boundaryList.get(j).getId())) {
					boundaryList.get(i).setParent(boundaryList.get(j));
				}
			}
		}
		return boundaryList;
	}
}