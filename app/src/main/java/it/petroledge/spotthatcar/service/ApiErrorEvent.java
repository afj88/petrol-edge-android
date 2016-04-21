package it.petroledge.spotthatcar.service;

/**
 * Created by friz on 03/04/16.
 */
public class ApiErrorEvent {

    private String mError;

    public ApiErrorEvent(String error) {
        this.mError = error;
    }

    public String getError() {
        return mError;
    }
}
