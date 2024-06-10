package com.progmob.android.friendkeeper.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * PasswordUtil
 * <p>
 * Classe utilitária para gerenciamento de senhas no aplicativo.
 * Fornece métodos para gerar um salt, hashear senhas e verificar senhas.
 */
public class PasswordUtil {

    /**
     * Método getSalt
     * <p>
     * Gera um salt aleatório para ser utilizado no hashing da senha.
     * O salt é codificado em Base64 e retornado como uma string.
     *
     * @return Uma string representando o salt codificado em Base64.
     */
    public static String getSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Método hashPassword
     * <p>
     * Hashea uma senha utilizando o algoritmo SHA-256.
     * O salt fornecido é decodificado de Base64 e adicionado ao MessageDigest antes do hashing.
     *
     * @param password A senha a ser hasheada.
     * @param salt O salt a ser utilizado no hashing.
     * @return Uma string representando a senha hasheada codificada em Base64.
     */
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(Base64.getDecoder().decode(salt));
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Método verifyPassword
     * <p>
     * Verifica se uma senha fornecida corresponde ao hash armazenado.
     * A senha é hasheada com o salt fornecido e comparada ao hash armazenado.
     *
     * @param password A senha a ser verificada.
     * @param salt O salt utilizado para hashear a senha.
     * @param storedHash O hash armazenado para comparação.
     * @return true se a senha fornecida corresponder ao hash armazenado, caso contrário, false.
     */
    public static boolean verifyPassword(String password, String salt, String storedHash) {
        String hash = hashPassword(password, salt);
        return hash.equals(storedHash);
    }
}
