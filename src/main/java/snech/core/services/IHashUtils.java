package snech.core.services;

import snech.core.exceptions.HashGenerationException;

/**
 *
 * @author Radovan Račák
 */
public interface IHashUtils {
    public String hashPassword(String password, String salt);
    public String hashString(String message, String algorithm) throws HashGenerationException;
}
