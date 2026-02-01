package com.gerardivan.chess.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Utils {

    public static final int CELES_TAULER = 8;

    /**
     * Indica la ruta del tauler
     */
    public static String IMATGE_TAULER = "chess.com-boards-and-pieces/boards/1_dark_wood.png";

    public static String IMATGE_PEON_BLANC = "chess.com-boards-and-pieces/pieces/1_classic/wp.png";
    public static String IMATGE_PEON_NEGRE = "chess.com-boards-and-pieces/pieces/1_classic/bp.png";

    public static String IMATGE_TORRE_BLANC = "chess.com-boards-and-pieces/pieces/1_classic/wr.png";
    public static String IMATGE_TORRE_NEGRE = "chess.com-boards-and-pieces/pieces/1_classic/br.png";

    public static String IMATGE_CAVALL_BLANC = "chess.com-boards-and-pieces/pieces/1_classic/wn.png";
    public static String IMATGE_CAVALL_NEGRE = "chess.com-boards-and-pieces/pieces/1_classic/bn.png";

    public static String IMATGE_ALFIL_BLANC = "chess.com-boards-and-pieces/pieces/1_classic/wb.png";
    public static String IMATGE_ALFIL_NEGRE = "chess.com-boards-and-pieces/pieces/1_classic/bb.png";

    public static String IMATGE_REINA_BLANC = "chess.com-boards-and-pieces/pieces/1_classic/wq.png";
    public static String IMATGE_REINA_NEGRE = "chess.com-boards-and-pieces/pieces/1_classic/bq.png";

    public static String IMATGE_REI_BLANC = "chess.com-boards-and-pieces/pieces/1_classic/wk.png";
    public static String IMATGE_REI_NEGRE = "chess.com-boards-and-pieces/pieces/1_classic/bk.png";

    public static void setTauler(String s) {
        IMATGE_TAULER = "chess.com-boards-and-pieces/boards/" + s + ".png";
    }

    /**
     * Per carregar imatges de peces
     * @param s ens dona el skin triat per a les peces
     */
    public static void setPeçes(String s) {
        IMATGE_PEON_BLANC = "chess.com-boards-and-pieces/pieces/" + s + "/wp.png";
        IMATGE_PEON_NEGRE = "chess.com-boards-and-pieces/pieces/" + s + "/bp.png";
        IMATGE_TORRE_BLANC = "chess.com-boards-and-pieces/pieces/" + s + "/wr.png";
        IMATGE_TORRE_NEGRE = "chess.com-boards-and-pieces/pieces/" + s + "/br.png";
        IMATGE_CAVALL_BLANC = "chess.com-boards-and-pieces/pieces/" + s + "/wn.png";
        IMATGE_CAVALL_NEGRE = "chess.com-boards-and-pieces/pieces/" + s + "/bn.png";
        IMATGE_ALFIL_BLANC = "chess.com-boards-and-pieces/pieces/" + s + "/wb.png";
        IMATGE_ALFIL_NEGRE = "chess.com-boards-and-pieces/pieces/" + s + "/bb.png";
        IMATGE_REINA_BLANC = "chess.com-boards-and-pieces/pieces/" + s + "/wq.png";
        IMATGE_REINA_NEGRE = "chess.com-boards-and-pieces/pieces/" + s + "/bq.png";
        IMATGE_REI_BLANC = "chess.com-boards-and-pieces/pieces/" + s + "/wk.png";
        IMATGE_REI_NEGRE = "chess.com-boards-and-pieces/pieces/" + s + "/bk.png";
    }

    /**
     * Metode per obtenir llistat taulers
     * @return
     */
    public static String[] getNomTaulers() {
        final String DIRECOTIR_TAULERS = "chess.com-boards-and-pieces/boards";

        List<String> imatges = cercarImatges(DIRECOTIR_TAULERS);

        String[] imatgesArr = new String[imatges.size()];

        for (int i = 0; i < imatges.size(); i++) {
            String imatge = imatges.get(i);
            imatge = imatge.substring(0, imatge.length() - 4);
            imatgesArr[i] = imatge;
        }

        return imatgesArr;
    }

    /**
     * Metode per obtenir llistat peces
     * @return
     */
    public static String[] getNomPeçes() {
        final String DIRECTORI_PEÇES = "chess.com-boards-and-pieces/pieces";

        List<String> imatges = cercarImatges(DIRECTORI_PEÇES);

        String[] imatgesArr = new String[imatges.size()];

        for (int i = 0; i < imatges.size(); i++) {
            String imatge = imatges.get(i);
            imatgesArr[i] = imatge;
        }

        return imatgesArr;
    }

    /**
     * Mètode per cercar imatges tant de peces com de taulers, ja que és cridat des de tots dos llocs
     * @param path
     * @return la lllista de noms
     */
    private static List<String> cercarImatges(String path) {
        final String DIRECTORI = path;

        List<String> noms = new ArrayList<>();

        FileHandle directori = Gdx.files.internal("chess.com-boards-and-pieces/boards");

        if (directori.exists() && directori.isDirectory()) {
            FileHandle[] arxius = directori.list();
            Arrays.sort(arxius, Comparator.comparing(FileHandle::name));
            for (FileHandle arxiu : arxius) {
                noms.add(arxiu.name());
            }
        }

        return noms;
    }

}
