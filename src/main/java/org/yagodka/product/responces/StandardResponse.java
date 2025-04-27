package org.yagodka.product.responces;

import java.util.List;

public class StandardResponse {
    private String requestStatus;
    private List<String> errors;
    private Long id;

    public StandardResponse(String requestStatus, List<String> errors, Long id) {
        this.requestStatus = requestStatus;
        this.errors = errors;
        this.id = id;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
