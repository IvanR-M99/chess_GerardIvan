package com.gerardivan.chess.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static final int CELES_TAULER = 8;

    /**
     * Indica la ruta del tauler
     */
    public static String IMATGE_TAULER = "assets/chess.com-boards-and-pieces/boards/1_dark_wood.png";

    public static String IMATGE_PEON_BLANC = "assets/chess.com-boards-and-pieces/pieces/1_classic/wp.png";
    public static String IMATGE_PEON_NEGRE = "assets/chess.com-boards-and-pieces/pieces/1_classic/bp.png";

    public static String IMATGE_TORRE_BLANC = "assets/chess.com-boards-and-pieces/pieces/1_classic/wr.png";
    public static String IMATGE_TORRE_NEGRE = "assets/chess.com-boards-and-pieces/pieces/1_classic/br.png";

    public static String IMATGE_CAVALL_BLANC = "assets/chess.com-boards-and-pieces/pieces/1_classic/wn.png";
    public static String IMATGE_CAVALL_NEGRE = "assets/chess.com-boards-and-pieces/pieces/1_classic/bn.png";

    public static String IMATGE_ALFIL_BLANC = "assets/chess.com-boards-and-pieces/pieces/1_classic/wb.png";
    public static String IMATGE_ALFIL_NEGRE = "assets/chess.com-boards-and-pieces/pieces/1_classic/bb.png";

    public static String IMATGE_REINA_BLANC = "assets/chess.com-boards-and-pieces/pieces/1_classic/wq.png";
    public static String IMATGE_REINA_NEGRE = "assets/chess.com-boards-and-pieces/pieces/1_classic/bq.png";

    public static String IMATGE_REI_BLANC = "assets/chess.com-boards-and-pieces/pieces/1_classic/wk.png";
    public static String IMATGE_REI_NEGRE = "assets/chess.com-boards-and-pieces/pieces/1_classic/bk.png";

    public static void setTauler(String s) {
        IMATGE_TAULER = "assets/chess.com-boards-and-pieces/boards/" + s + ".png";
    }

    public static void setPeçes(String s) {
        IMATGE_PEON_BLANC = "assets/chess.com-boards-and-pieces/pieces/" + s + "/wp.png";
        IMATGE_PEON_NEGRE = "assets/chess.com-boards-and-pieces/pieces/" + s + "/bp.png";
        IMATGE_TORRE_BLANC = "assets/chess.com-boards-and-pieces/pieces/" + s + "/wr.png";
        IMATGE_TORRE_NEGRE = "assets/chess.com-boards-and-pieces/pieces/" + s + "/br.png";
        IMATGE_CAVALL_BLANC = "assets/chess.com-boards-and-pieces/pieces/" + s + "/wn.png";
        IMATGE_CAVALL_NEGRE = "assets/chess.com-boards-and-pieces/pieces/" + s + "/bn.png";
        IMATGE_ALFIL_BLANC = "assets/chess.com-boards-and-pieces/pieces/" + s + "/wb.png";
        IMATGE_ALFIL_NEGRE = "assets/chess.com-boards-and-pieces/pieces/" + s + "/bb.png";
        IMATGE_REINA_BLANC = "assets/chess.com-boards-and-pieces/pieces/" + s + "/wq.png";
        IMATGE_REINA_NEGRE = "assets/chess.com-boards-and-pieces/pieces/" + s + "/bq.png";
        IMATGE_REI_BLANC = "assets/chess.com-boards-and-pieces/pieces/" + s + "/wk.png";
        IMATGE_REI_NEGRE = "assets/chess.com-boards-and-pieces/pieces/" + s + "/bk.png";
    }

    public static String[] getNomTaulers() {
        final String DIRECOTIR_TAULERS = "assets/chess.com-boards-and-pieces/boards";

        List<String> imatges = cercarImatges(DIRECOTIR_TAULERS);

        String[] imatgesArr = new String[imatges.size()];

        for (int i = 0; i < imatges.size(); i++) {
            String imatge = imatges.get(i);
            imatge = imatge.substring(0, imatge.length() - 4);
            imatgesArr[i] = imatge;
        }

        return imatgesArr;
    }

    public static String[] getNomPeçes() {
        final String DIRECTORI_PEÇES = "assets/chess.com-boards-and-pieces/pieces";

        List<String> imatges = cercarImatges(DIRECTORI_PEÇES);

        String[] imatgesArr = new String[imatges.size()];

        for (int i = 0; i < imatges.size(); i++) {
            String imatge = imatges.get(i);
            imatgesArr[i] = imatge;
        }

        return imatgesArr;
    }

    private static List<String> cercarImatges(String path) {
        final String DIRECTORI = path;

        List<String> noms = new ArrayList<>();

        File directori = new File(DIRECTORI);

        if (directori.exists() && directori.isDirectory()) {
            File[] arxius = directori.listFiles();
            if (arxius != null) {
                for (File arxiu : arxius) {
                    noms.add(arxiu.getName());
                }
            }
        }

        return noms;
    }

}
