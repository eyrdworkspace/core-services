package org.egov.access.persistence.repository.rowmapper;

import org.egov.access.domain.model.Action;
import org.egov.access.web.contact.RoleAction;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class ActionRowMapper implements RowMapper<Action> {

    @Override
    public Action mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        final Action action = Action.builder().id(rs.getLong("a_id"))
                .name(rs.getString("a_name"))
                .url(rs.getString("a_url"))
                .displayName(rs.getString("a_displayname"))
                .parentModule(rs.getString("a_servicecode"))
                .enabled(rs.getBoolean("a_enabled"))
                .createdBy(rs.getLong("a_createdby"))
                .createdDate(rs.getDate("a_createddate"))
                .lastModifiedBy(rs.getLong("a_lastmodifiedby"))
                .lastModifiedDate(rs.getDate("a_lastmodifieddate"))
                .orderNumber(rs.getInt("a_ordernumber"))
                .queryParams(rs.getString("a_queryparams"))
                .tenantId(rs.getString("a_tenantId"))
                .build();

        final RoleAction roleAction = new RoleAction();
        roleAction.setAction(rs.getLong("ra_action"));
        roleAction.setRole(rs.getLong("ra_role"));
        return action;

    }
}