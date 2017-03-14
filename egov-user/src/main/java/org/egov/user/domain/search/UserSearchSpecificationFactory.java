package org.egov.user.domain.search;

import org.egov.user.domain.model.UserSearch;
import org.egov.user.persistence.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class UserSearchSpecificationFactory {

    public Specification<User> getSpecification(UserSearch userSearch) {
        if(userSearch.isFuzzyLogic()) {
            return new FuzzyNameMatchingSpecification(userSearch);
        }
        return new MultiFieldsMatchingSpecification(userSearch);
    }
}
