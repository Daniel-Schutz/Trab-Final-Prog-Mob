package com.progmob.android.friendkeeper.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * ImageUtil é uma classe utilitária que fornece métodos para manipulação de imagens,
 * como salvar uma imagem no armazenamento externo.
 */
public class ImageUtil {

    // Tag para logs
    private static final String TAG = "ImageUtil";

    /**
     * Salva um Bitmap no armazenamento externo do dispositivo e retorna a URI do arquivo salvo.
     *
     * @param context O contexto atual.
     * @param bitmap O Bitmap da imagem a ser salva.
     * @return A URI do arquivo salvo, ou null se houver um erro ao salvar.
     */
    public static Uri saveImageToExternalStorage(Context context, Bitmap bitmap) {
        String savedImagePath = null; // Caminho onde a imagem será salva
        String imageFileName = "JPEG_" + System.currentTimeMillis() + ".jpg"; // Nome do arquivo da imagem com um timestamp único
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES) + "/FriendKeeper"); // Diretório onde a imagem será salva

        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs(); // Cria o diretório se ele não existir
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName); // Cria um novo arquivo de imagem
            savedImagePath = imageFile.getAbsolutePath(); // Obtém o caminho absoluto do arquivo de imagem
            try {
                FileOutputStream fOut = new FileOutputStream(imageFile); // Abre um fluxo de saída para o arquivo
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut); // Comprime e escreve o Bitmap no arquivo
                fOut.close(); // Fecha o fluxo de saída
            } catch (IOException e) {
                Log.d(TAG, "saveImageToExternalStorage: " + e); // Exibe o erro no log em caso de falha ao salvar a imagem
            }
            return Uri.fromFile(imageFile); // Retorna a URI do arquivo salvo
        }
        return null; // Retorna null se houver um erro ao criar o diretório
    }
}
