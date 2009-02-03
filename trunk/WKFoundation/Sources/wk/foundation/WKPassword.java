// Class WkPassword.
// 

package wk.foundation;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** Creates an encrypted password and compares a clear text test password
to a previously encrypted password.
@version 1.0 */
public class WKPassword
{
    /** Performs SHA encryption on a String
    @param clearTextPassword the string to be encrypted
    @return the encrypted password */
    public static String getShaEncodedPassword(String clearTextPassword) throws NoSuchAlgorithmException {
        return getEncodedPassword(clearTextPassword,"SHA");
    }
    
    /** Encrypts a string
    @param clearTextPassword the string to be encrypted
    @param algorithm the encryption algorithm, for example "SHA"
    @return the encrypted password */
    public static String getEncodedPassword(String clearTextPassword, String algorithm)
    throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        
        md.update(clearTextPassword.getBytes());
        
        return HexString.bufferToHex(md.digest());
    }
    
    /** Checks a cleartext password against an encrypted password to see if they are equal
    @param clearTextTestPassword the clear password
    @param encodedActualPassword the encrypted password
    @param algorithm the encryption algorithm to use, for example "SHA"
    @return true if paswords are the same, else false*/
    public static boolean testPassword(String clearTextTestPassword,
                                       String encodedActualPassword,
                                       String algorithm)
    throws NoSuchAlgorithmException {
        String encodedTestPassword = WKPassword.getEncodedPassword(
                                                                   clearTextTestPassword,
                                                                   algorithm);
        
        // We should ignore case since the encoded item is hexadecimal
        // and while the java encryption functions may return upper case
        // for the A, B, C, etc. hexadecimal characters, databases such as
        // MySQL will output lower case a, b, c, etc hex characters.
        // In reality, capital A B C D E F G should be used for hexadecimal,
        // but it is safer to ignore case (just in case!)
        return (encodedTestPassword.equalsIgnoreCase(encodedActualPassword));
    }
}