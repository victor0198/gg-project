package student.examples.uservice.business.util;

import com.example.grpc.AuthService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

public class TokenManager {
    public static String buildGGToken(UUID uuid, String email){
        String tokenStr = "";
        try {
            MessageDigest mesgD = MessageDigest.getInstance("SHA-256");
            String uuidAndEmail = uuid.toString().concat("|".concat(email));
            byte[] token = mesgD.digest(uuidAndEmail.getBytes());
            tokenStr = Base64.getEncoder().encodeToString(token);
            System.out.println("tokenStr:"+tokenStr);

        } catch ( NoSuchAlgorithmException e) {
            e.printStackTrace();
        }



        return tokenStr;
    }

    private static String decodeGGToken(String token){
        byte[] decodedBytes = Base64.getDecoder().decode(token);
        String decodedString = new String(decodedBytes);
        System.out.println("decoded "+decodedString);
        return decodedString;
    }
}
