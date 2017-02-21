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
package org.egov.eis.persistence.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "egeis_assignment")
@SequenceGenerator(name = Assignment.SEQ_ASSIGNMENT, sequenceName = Assignment.SEQ_ASSIGNMENT, allocationSize = 1)
public class Assignment extends AbstractAuditable {

	public static final String SEQ_ASSIGNMENT = "SEQ_EGEIS_ASSIGNMENT";
	private static final long serialVersionUID = -2720951718725134740L;
	@Id
	@GeneratedValue(generator = SEQ_ASSIGNMENT, strategy = GenerationType.SEQUENCE)
	private Long id;

	@JsonIgnore
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "position")
	private Position position;
	private Long functionary;
	private Long fund;
	private Long function;

	@JsonIgnore
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "designation")
	private Designation designation;
	@JsonIgnore
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department")
	private Department department;
	@Column(name = "isprimary")
	private boolean primary;
	@NotNull
	private Date fromDate;
	@NotNull
	private Date toDate;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "grade")
	private GradeMaster grade;
	@JsonIgnore
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee")
	private Employee employee;
	@JsonIgnore
	@OneToMany(mappedBy = "assignment", orphanRemoval = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private final List<HeadOfDepartments> deptSet = new ArrayList<>(0);

	@Transient
	private List<HeadOfDepartments> hodList = new ArrayList<>(0);

	@Override
	public Long getId() {
		return id;
	}

	public List<HeadOfDepartments> getHodList() {
		return hodList;
	}

	public void setHodList(final List<HeadOfDepartments> hodLists) {
		hodList = hodLists;
	}

	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	public Designation getDesignation() {
		return designation;
	}

	public void setDesignation(final Designation designation) {
		this.designation = designation;
	}

	public Long getFunctionary() {
		return functionary;
	}

	public void setFunctionary(final Long functionary) {
		this.functionary = functionary;
	}

	public Long getFunction() {
		return function;
	}

	public void setFunction(final Long function) {
		this.function = function;
	}

	public Long getFund() {
		return fund;
	}

	public void setFund(final Long fund) {
		this.fund = fund;
	}

	public GradeMaster getGrade() {
		return grade;
	}

	public void setGrade(final GradeMaster gradeId) {
		grade = gradeId;
	}

	public boolean getPrimary() {
		return primary;
	}

	public void setPrimary(final boolean primary) {
		this.primary = primary;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(final Position position) {
		this.position = position;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(final Department department) {
		this.department = department;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(final Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(final Date toDate) {
		this.toDate = toDate;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(final Employee employee) {
		this.employee = employee;
	}

	public List<HeadOfDepartments> getDeptSet() {
		return deptSet;
	}

	public void setDeptSet(final List<HeadOfDepartments> deptSet) {
		this.deptSet.clear();
		if (deptSet != null)
			this.deptSet.addAll(deptSet);
	}

}
