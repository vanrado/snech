package snech.core.services;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;
import snech.core.exceptions.HashGenerationException;

/**
 *
 * @author Radovan Račák
 */
@Service
public class HashUtilsImpl implements IHashUtils {

    @Override
    public String hashPassword(String password, String salt) {
        String hashPassword;
        String finalHash = null;

        //hash((hash(string, "SHA-1") + hash(salt, "MD5")), "SHA-256")
        try {
            hashPassword = hashString(password, "SHA-1");
            finalHash = hashString(hashPassword + salt, "SHA-256");
            return finalHash;
        } catch (HashGenerationException ex) {
            Logger.getLogger(HashUtilsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return finalHash;
    }

    @Override
    public String getRandomSalt() {
        String salt = null;

        try {
            salt = hashString(randomStringGenerator(32), "MD5");
        } catch (HashGenerationException ex) {
            Logger.getLogger(HashUtilsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return salt;
    }

    @Override
    public String hashString(String message, String algorithm) throws HashGenerationException {

        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hashedBytes = digest.digest(message.getBytes("UTF-8"));

            return convertByteArrayToHexString(hashedBytes);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            throw new HashGenerationException("Could not generate hash from String", ex);
        }
    }

    /**
     * 
     * @param password
     * @return pole s detailami o novom hesle. Pozicia 0 obsahuje salt, pozicia 1 obsahuje heslo. Vsetko je uz zahashovane 
     */
    @Override
    public String[] createNewPassword(String password) {
        String[] passwordDetails = new String[2];
        passwordDetails[0] = getRandomSalt();
        passwordDetails[1] = hashPassword(password, passwordDetails[0]);
        return passwordDetails;
    }
    
    @Override
    public String randomStringGenerator(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < length; i++) {
            double index = Math.random() * characters.length();
            buffer.append(characters.charAt((int) index));
        }

        return buffer.toString();
    }

    private String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }
}
