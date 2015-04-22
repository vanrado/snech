package snech.core.services;

import snech.core.exceptions.HashGenerationException;

/**
 *
 * @author Radovan Račák
 */
public interface IHashUtils {
    /**
     * Hashovanie s pouzitim uz vygenerovaneho salt
     * @param password
     * @param salt
     * @return 
     */
    public String hashPassword(String password, String salt);
    
    public String[] createNewPassword(String password);
    
    public String getRandomSalt();
    
    public String hashString(String message, String algorithm) throws HashGenerationException;

    public String randomStringGenerator(int length);
}
