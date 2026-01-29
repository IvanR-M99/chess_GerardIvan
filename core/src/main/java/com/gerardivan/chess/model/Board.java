package com.gerardivan.chess.model;

import com.gerardivan.chess.model.Piece.Tipus;
import com.gerardivan.chess.util.Utils;

import java.util.ArrayList;

public class Board {

    private static Piece[][] board; //el tauler és un array bidimensional de "peces", algunes null i altres no

    private boolean reiBlancAtacat = false;
    private boolean reiNegreAtacat = false;

    private boolean tornBlanques = true;

    public boolean isReiBlancAtacat() {
        return reiBlancAtacat;
    }

    public void setReiBlancAtacat(boolean reiBlancAtacat) {
        this.reiBlancAtacat = reiBlancAtacat;
    }

    public boolean isReiNegreAtacat() {
        return reiNegreAtacat;
    }

    public void setReiNegreAtacat(boolean reiNegreAtacat) {
        this.reiNegreAtacat = reiNegreAtacat;
    }

    public Board() {
        board = new Piece[Utils.CELES_TAULER][Utils.CELES_TAULER];
        setupInitialPosition();
    }

    /**
     * Inicialitza peces i les posiciona per començar la partida
     */

    private void setupInitialPosition() {
        // Peons
        for (int i = 0; i < Utils.CELES_TAULER; i++) {
            board[i][1] = new Piece(Piece.Tipus.Peon, true, i, 1);   // blanques
            board[i][6] = new Piece(Piece.Tipus.Peon, false, i, 6);  // negres
        }

        // Torres
        board[0][0] = new Piece(Piece.Tipus.Torre, true, 0, 0);
        board[7][0] = new Piece(Piece.Tipus.Torre, true, 7, 0);
        board[0][7] = new Piece(Piece.Tipus.Torre, false, 0, 7);
        board[7][7] = new Piece(Piece.Tipus.Torre, false, 7, 7);
        // Cavalls
        board[1][0] = new Piece(Piece.Tipus.Cavall, true, 1, 0);
        board[6][0] = new Piece(Piece.Tipus.Cavall, true, 6, 0);
        board[1][7] = new Piece(Piece.Tipus.Cavall, false, 1, 7);
        board[6][7] = new Piece(Piece.Tipus.Cavall, false, 6, 7);

        // Alfils
        board[2][0] = new Piece(Piece.Tipus.Alfil, true, 2, 0);
        board[5][0] = new Piece(Piece.Tipus.Alfil, true, 5, 0);
        board[2][7] = new Piece(Piece.Tipus.Alfil, false, 2, 7);
        board[5][7] = new Piece(Piece.Tipus.Alfil, false, 5, 7);
        // Reines
        board[3][0] = new Piece(Piece.Tipus.Reina, true, 3, 0);
        board[3][7] = new Piece(Piece.Tipus.Reina, false, 3, 7);

        // Reis
        board[4][0] = new Piece(Piece.Tipus.Rei, true, 4, 0);
        board[4][7] = new Piece(Piece.Tipus.Rei, false, 4, 7);
    }

    /** Obté la peça en una casella (null si vacía) */
    public Piece getPiece(int col, int row) {
        if (col < 0 || col >= Utils.CELES_TAULER || row < 0 || row >= Utils.CELES_TAULER) return null;
        return board[col][row];
    }

    public static Piece getRey(boolean esBlanc) {
        for (Piece[] linea : board) {
            for (Piece p : linea) {
                if (p.getTipus() == Tipus.Rei && p.getColor() == esBlanc) {
                    return p;
                }
            }
        }
        return null;
    }

    /** Mueve una pieza de una casilla a otra */
    public void movePiece (Piece p, int toCol, int toRow){
        if (p == null) return;

        board[toCol][toRow] = p;
        ArrayList<Integer> list = (ArrayList<Integer>) p.getPosicio();
        board[list.get(0)][list.get(1)] = null;


        p.setPosicio(toCol,toRow);

        Piece rei = getRey(!p.getColor());

        if (p.canMoveTo(this, rei.getPosicio().get(0), rei.getPosicio().get(1))) {
            if (rei.getColor()) {
                reiBlancAtacat = true;
            } else {
                reiNegreAtacat = true;
            }
        }
        
    }

    /**
     * Per mostrar el taulell per pantalla (desenvolupador)
     * @return el tauler pintat
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                sb.append(board[j][i]);
            }
            sb.append("\n");
        }
        return String.valueOf(sb);
    }

    /**
     * Ens diu si el camí està lliure per fer el moviment
     * @param x1 fila origen
     * @param y1 columna origen
     * @param x2 fila desti
     * @param y2 columna desti
     * @return true si està lliure i false si no
     */
    public boolean isPathClear(int x1, int y1, int x2, int y2) {
        int dx = Integer.signum(x2 - x1); //Retorna 1 si avança, -1 si retrocedeix i 0 si no es mou
        int dy = Integer.signum(y2 - y1); //Retorna 1 si dreta, -1 si esquerra i 0 si no es mou de columna

        //Si el dx=0, voldrà dir que el moviment és vertical, si el dy=0 el mov és horitzontal
        //Si canvien els dos, diagonal
        int cx = x1 + dx;
        int cy = y1 + dy;

        while (cx != x2 || cy != y2) { //mentre la caselle comprovadad no arribi a la de destí
            if (getPiece(cx, cy) != null) return false; //si hi ha peça, retorna false
            //Afegim desplaçament en la mateix direcció
            cx += dx;
            cy += dy;
        }
        return true;
    }



}
