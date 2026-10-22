package tn.esprit.autoloc.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " introuvable avec l'identifiant: " + id);
    }
}