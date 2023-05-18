package mk.genius.testspringboot.service.error;


public class ResourceNotFoundException extends RuntimeException {
    private String error;
    private String fieldName;

    public ResourceNotFoundException(String error, String fieldName) {
        super(String.format("%s has error %s", fieldName, error));
        this.error = error;
        this.fieldName = fieldName;
    }
}
