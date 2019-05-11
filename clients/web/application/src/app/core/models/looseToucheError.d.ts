/* tslint:disable */
// Generated using typescript-generator version 2.12.476 on 2019-05-11 23:30:27.

export interface LooseTouchError {
    type: LooseTouchErrorType;
    message: string;
    errors: LooseTouchErrorDetail[];
}

export interface LooseTouchErrorDetail {
    code: LooseTouchErrorCode;
    message: string;
}

export const enum LooseTouchErrorType {
    api_connection_error = "api_connection_error",
    api_error = "api_error",
    authentication_error = "authentication_error",
    invalid_request_error = "invalid_request_error",
    resource_not_found = "resource_not_found",
    rate_limit_error = "rate_limit_error",
    unspecified_error = "unspecified_error",
    unknown_error = "unknown_error",
}

export const enum LooseTouchErrorCode {
    email_required = "email_required",
    email_invalid = "email_invalid",
    contact_recurrence_type_required = "contact_recurrence_type_required",
    contact_recurrence_type_invalid = "contact_recurrence_type_invalid",
    contact_recurrence_value_required = "contact_recurrence_value_required",
    contact_recurrence_value_invalid = "contact_recurrence_value_invalid",
}
