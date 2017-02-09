package org.egov.pgr.web.adapters.error;

import org.egov.pgr.web.contract.ErrorResponse;

/**
 * This is to transform the model errors to web channel specific errors
 *
 * @param <T>
 */
public interface ErrorAdapter<T> {
    ErrorResponse adapt(T model);
}
