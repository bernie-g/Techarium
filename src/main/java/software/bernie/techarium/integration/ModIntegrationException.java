package software.bernie.techarium.integration;

public class ModIntegrationException extends RuntimeException {

    public ModIntegrationException(String modID) {
        super("Mod: " + modID + " is not present, cannot use integration!");
    }
}
