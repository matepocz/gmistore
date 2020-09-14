package hu.progmasters.gmistore.response;

public class GenericResponse {
    private String message;
    private String error;

    public GenericResponse(String message) {
        this.message = message;
    }

    public GenericResponse(String message, String error) {
        this.message = message;
        this.error = error;
    }
}
