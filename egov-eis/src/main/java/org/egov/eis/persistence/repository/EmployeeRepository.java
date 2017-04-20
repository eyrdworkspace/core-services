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

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.egov.eis.persistence.entity.Employee;
import org.egov.eis.persistence.entity.enums.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	public Employee findByCodeAndTenantId(String code, String tenantId);

	public List<Employee> findByEmployeeStatus(EmployeeStatus status);

	public List<Employee> findByEmployeeType_Id(Long id);

	public Employee findByUsername(String userName);

	@Query(" select distinct EMP from Employee EMP inner join EMP.assignments ASSIGN inner join fetch EMP.jurisdictions as JRDN "
			+ " where ASSIGN.department.id=:deptId and ASSIGN.designation.id=:desigId and ASSIGN.fromDate<=current_date and ASSIGN.toDate>=current_date "
			+ " and JRDN.boundary.id in :boundaryIds and EMP.active=true order by EMP.code")
	List<Employee> findByDepartmentDesignationAndBoundary(@Param("deptId") final Long deptId,
			@Param("desigId") final Long desigId, @Param("boundaryIds") final Set<Long> boundaryIds);

	@Query(" select distinct EMP from Employee EMP inner join EMP.assignments ASSIGN "
			+ " where ASSIGN.department.code=:deptCode and ASSIGN.designation.code=:desigCode and ASSIGN.fromDate<=current_date and ASSIGN.toDate>=current_date "
			+ " and ASSIGN.tenantId=:tenantId and EMP.tenantId=:tenantId and EMP.active=true order by EMP.code")
	List<Employee> findByDepartmentDesignation(@Param("deptCode") final String deptCode,
			@Param("desigCode") final String desigCode, @Param("tenantId") final String tenantId);

	@Query(" select distinct EMP from Employee EMP inner join EMP.assignments ASSIGN "
			+ " where ASSIGN.position.id=:positionId and ASSIGN.fromDate<=:asOnDate and ASSIGN.toDate>=:asOnDate "
			+ " and ASSIGN.tenantId=:tenantId and EMP.tenantId=:tenantId and EMP.active=true order by EMP.code")
	List<Employee> findByPositionAndAsOnDate(@Param("positionId") final Long positionId,
			@Param("asOnDate") final Date asOnDate, @Param("tenantId") final String tenantId);

	public List<Employee> findByNameLikeOrCodeLike(String name, String code);

	public Employee findByName(String name);

	@Query("from Employee  where  upper(code) like '%'||upper(:code)||'%' order by id")
	List<Employee> findEmployeeByCodeLike(@Param("code") String code);

	@Query("from Employee  where  upper(code) like '%'||upper(:code)||'%'  and active=true order by id")
	List<Employee> findActiveEmployeeByCodeLike(@Param("code") String code);

	@Query("select distinct emp from Employee emp, IN (emp.roles) role where role.tenantId=:tenantId and emp.tenantId=:tenantId and role.name = :roleName ")
	Set<Employee> findEmployeesByRoleName(@Param("roleName") String roleName, @Param("tenantId") final String tenantId);

	public Employee findByIdAndTenantId(Long userId,String tenantId);
}
