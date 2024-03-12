package edu.uoc.epcsd.user.config;

public class ErrorResponse {
    private String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

    // Getters y setters
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

