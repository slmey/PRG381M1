import java.security.MessageDigest;
import java.util.Base64;

public class PasswordTest {
    public static void main(String[] args) {
        try {
            String password = "password123";
            String salt1 = "salt1";
            String salt2 = "salt2";
            String salt3 = "salt3";
            
            // Convert salt to Base64 for proper hashing
            String salt1Base64 = Base64.getEncoder().encodeToString(salt1.getBytes());
            String salt2Base64 = Base64.getEncoder().encodeToString(salt2.getBytes());
            String salt3Base64 = Base64.getEncoder().encodeToString(salt3.getBytes());
            
            System.out.println("Salt1 Base64: " + salt1Base64);
            System.out.println("Salt2 Base64: " + salt2Base64);
            System.out.println("Salt3 Base64: " + salt3Base64);
            
            // Hash passwords
            String hash1 = hashPassword(password, salt1Base64);
            String hash2 = hashPassword(password, salt2Base64);
            String hash3 = hashPassword(password, salt3Base64);
            
            System.out.println("Hash1: " + hash1);
            System.out.println("Hash2: " + hash2);
            System.out.println("Hash3: " + hash3);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(Base64.getDecoder().decode(salt));
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
