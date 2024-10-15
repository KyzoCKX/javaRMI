package com.dcom.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.*;

public class Token {
    private static String token;
    private static DecodedToken decodedToken;


    public static String getToken() {
        return token;
    }

    public static DecodedToken getDecodedToken() {
        return decodedToken;
    }

    public static void setToken(String token) {
        Token.token = token;
    }

    public static void setDecodedToken(DecodedToken decodedToken) {
        Token.decodedToken = decodedToken;
    }

    public static boolean isTokenPresent() {
        return token != null && !token.isEmpty();
    }

    public static void clearToken() {
        Token.token = null;
        Token.decodedToken = null;
    }

    public static DecodedToken decodeToken() {
        if (!isTokenPresent()) {
            // System.out.println("Token is not present.");
            return null;
        }

        try {
            DecodedJWT decodedJWT = JWT.decode(token);

            // Extract fields from the decoded token
            String userType = decodedJWT.getClaim("userType").asString();
            long exp = decodedJWT.getClaim("exp").asLong();
            int userId = decodedJWT.getClaim("userId").asInt();
            long iat = decodedJWT.getClaim("iat").asLong();

            return new DecodedToken(token, userType, exp, userId, iat); 
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Token decoding failed.");
            clearToken(); // Clear token information if decoding fails
            return null;
        }
    }

    public static void saveToFile(String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(decodeToken()); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadFromFile(String filePath) {
        // System.out.println("path: " + filePath);
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            DecodedToken token = (DecodedToken) ois.readObject();
            setToken(token.getTokenString());
            setDecodedToken(token);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void deleteTokenFile(String filePath) {
        File file = new File(filePath);
        file.delete();
    }
}
