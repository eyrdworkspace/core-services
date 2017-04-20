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
package org.egov.eis.persistence.repository;

import java.util.List;
import java.util.Set;

import org.egov.eis.persistence.entity.Designation;
import org.egov.eis.persistence.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Long>, DesignationCustomRepository {

	@Query("select d from Designation d where upper(d.name)=:designationname")
	Designation findByNameUpperCase(@Param("designationname") String designationName);

	List<Designation> findByNameContainingIgnoreCaseOrderByNameAsc(String designationName);

	List<Designation> findAllByOrderByNameAsc();

	@Query("select distinct d.roles from Designation d where d.name=:designationName ")
	Set<Role> getRolesByDesignation(@Param("designationName") String designationName);

	@Query("select d from Designation d where upper(d.name) in :designationnames")
	List<Designation> getDesignationsByNames(@Param("designationnames") List<String> designationNames);

	@Query("select d from Designation d where tenantId=:tenantId and upper(d.name) like upper(:name)")
	List<Designation> getDesignationsByName(@Param("name") final String name, @Param("tenantId") final String tenantId);
	
	List<Designation> findAllByTenantId(String tenantId);
	
	Designation findByIdAndTenantId(Long id,String tenantId);

}
