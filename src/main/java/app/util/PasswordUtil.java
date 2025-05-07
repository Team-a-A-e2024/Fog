package app.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static boolean checkPlainPassword(String plainPassword, String PasswordFromDB) {
        return plainPassword.equals(PasswordFromDB);
    }
    public static boolean checkHashedPassword(String plainPassword, String hashedPasswordFromDB) {
        return BCrypt.checkpw(plainPassword, hashedPasswordFromDB);
    }
}
