package com.gerardivan.chess.model;

import com.gerardivan.chess.model.Piece.Tipus;
import com.gerardivan.chess.util.Utils;

import java.util.ArrayList;

public class Board {

    private static Piece[][] board; // el tauler és un array bidimensional de "peces", algunes null i altres no

    private boolean tornBlanques = true;

    public Board() {
        board = new Piece[Utils.CELES_TAULER][Utils.CELES_TAULER];
        posicionamentInicial();
    }

    /**
     * Inicialitza peces i les posiciona per començar la partida
     */
    private void posicionamentInicial() {
        // Peons
        for (int i = 0; i < Utils.CELES_TAULER; i++) {
            board[i][1] = new Piece(Piece.Tipus.Peon, true, i, 1); // blanques
            board[i][6] = new Piece(Piece.Tipus.Peon, false, i, 6); // negres
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
        if (col < 0 || col >= Utils.CELES_TAULER || row < 0 || row >= Utils.CELES_TAULER)
            return null;
        return board[col][row];
    }

    public boolean isWhiteTurn() {
        return tornBlanques;
    }

    public void seguentTorn() {
        tornBlanques = !tornBlanques;
    }

    public static Piece getRey(boolean esBlanc) {
        for (Piece[] linea : board) {
            for (Piece p : linea) {
                if (p != null) {
                    if (p.getTipus() == Tipus.Rei && p.getColor() == esBlanc) {
                        return p;
                    }
                }
            }
        }
        return null;
    }

    /** Mueve una pieza de una casilla a otra */
    public void mourePiece(Piece p, int toCol, int toRow) {
        if (p == null)
            return;

        board[toCol][toRow] = p;
        ArrayList<Integer> list = (ArrayList<Integer>) p.getPosicio();
        board[list.get(0)][list.get(1)] = null;

        p.setPosicio(toCol, toRow);

        if (p.getTipus() == Piece.Tipus.Peon) {
            int y = p.getPosicio().get(1);
            if ((p.getColor() && y == 7) || (!p.getColor() && y == 0)) {
                promocioPeo(p);
            }
        }

        // ENROCAMENT: moure torre
        if (p.getTipus() == Piece.Tipus.Rei && Math.abs(toCol - list.get(0)) == 2) {
            boolean kingSide = toCol > list.get(0);

            int rookFrom = kingSide ? 7 : 0;
            int rookTo = kingSide ? toCol - 1 : toCol + 1;

            Piece rook = getPiece(rookFrom, toRow);

            board[rookTo][toRow] = rook;
            board[rookFrom][toRow] = null;

            rook.setPosicio(rookTo, toRow);
            rook.hasMoved = true;
        }

        p.hasMoved = true; //Marquem que la peça s'ha mogut almenys un cop
    }

    /**
     * Mètode per promocionar peó a reina
     * @param pawn
     */
    private void promocioPeo(Piece pawn) {
        int x = pawn.getPosicio().get(0);
        int y = pawn.getPosicio().get(1);
        boolean color = pawn.getColor();

        board[x][y] = new Piece(Piece.Tipus.Reina, color, x, y);
    }

    /**
     * Mètode per comprovar si el rei està en jaque
     * @param kingIsWhite passem el color del rei
     * @return true
     */
    public boolean estaReiJaque(boolean kingIsWhite) {
        Piece king = getRey(kingIsWhite);
        if (king == null) return false;

        int kx = king.getPosicio().get(0);
        int ky = king.getPosicio().get(1);

        for (Piece[] row : board) {
            for (Piece p : row) {
                if (p != null && p.getColor() != kingIsWhite) {
                    if (p.pucMoureA(this, kx, ky)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Possible moviment, però no el fa realment, ja que torna a la posició inicial,
     * si no que és per mirar si és un moviment possible
     * @param p
     * @param toX
     * @param toY
     * @return true si és possible, i false si no (
     */
    public boolean intentarMoviment(Piece p, int toX, int toY) {
        int fromX = p.getPosicio().get(0);
        int fromY = p.getPosicio().get(1);

        Piece captured = board[toX][toY];

        // Simular movimiento
        board[toX][toY] = p;
        board[fromX][fromY] = null;
        p.setPosicio(toX, toY);

        boolean enJaque = estaReiJaque(p.getColor());

        // Deshacer
        board[fromX][fromY] = p;
        board[toX][toY] = captured;
        p.setPosicio(fromX, fromY);

        return !enJaque; //si no està en jaque
    }

    /**
     * Per mostrar el taulell per pantalla (desenvolupador)
     *
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
     * Mètode per veure si (el rei ) té algun possible moviment (true) o no (false)
     * @param color
     * @return
     */
    public boolean tincMovimentLegal(boolean color) {
        for (Piece[] row : board) {
            for (Piece p : row) {
                if (p != null && p.getColor() == color) {

                    for (int col = 0; col < Utils.CELES_TAULER; col++) {
                        for (int row2 = 0; row2 < Utils.CELES_TAULER; row2++) {

                            if (p.pucMoureA(this, col, row2)
                                && intentarMoviment(p, col, row2)) {
                                return true; // si hi ha almenys un moviment legal
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Mètode per avaluar si és jaque mate (si està en jaque i no té cap possible moviment)
     * @param color
     * @return
     */
    public boolean isCheckMate(boolean color) {
        if (!estaReiJaque(color)) return false;
        return !tincMovimentLegal(color);
    }

    /**
     * Mètode per avaluar si el rei està ofegat (si no està en jaque i no té cap possible moviment)
     * @param color
     * @return
     */
    public boolean isStalemate(boolean color) {
        if (estaReiJaque(color)) return false;
        return !tincMovimentLegal(color);
    }

    /**
     * Ens diu si el camí està lliure per fer el moviment
     *
     * @param x1 fila origen
     * @param y1 columna origen
     * @param x2 fila desti
     * @param y2 columna desti
     * @return true si està lliure i false si no
     */
    public boolean isPathClear(int x1, int y1, int x2, int y2) {
        int dx = Integer.signum(x2 - x1); // Retorna 1 si avança, -1 si retrocedeix i 0 si no es mou
        int dy = Integer.signum(y2 - y1); // Retorna 1 si dreta, -1 si esquerra i 0 si no es mou de columna

        // Si el dx=0, voldrà dir que el moviment és vertical, si el dy=0 el mov és
        // horitzontal
        // Si canvien els dos, diagonal
        int cx = x1 + dx;
        int cy = y1 + dy;

        while (cx != x2 || cy != y2) { // mentre la caselle comprovadad no arribi a la de destí
            if (getPiece(cx, cy) != null)
                return false; // si hi ha peça, retorna false
            // Afegim desplaçament en la mateix direcció
            cx += dx;
            cy += dy;
        }
        return true;
    }

    /**
     * Mètode que valora si és possible l'enroc: primer si seria el curt o el llarg, després mira
     * que no hi hagi cap peça pel camí, ni que les caselles estiguin atacades
     * @param king
     * @param kingSide si es treu:enroc llarg, si es false: enroc curt
     * @return
     */
    public boolean pucEnrocar(Piece king, boolean kingSide) {

        if (king.hasMoved) return false;
        if (estaReiJaque(king.getColor())) return false;

        // en funció de l'enrroc que sigui, em mouré cap a la dreta o cap a l'esquerra
        int y = king.getPosicio().get(1);
        int rookX = kingSide ? 7 : 0; //la torre afectada
        int step = kingSide ? 1 : -1;

        Piece rook = getPiece(rookX, y); //obtinc la torre per veure si s'ha mogut
        if (rook == null || rook.getTipus() != Piece.Tipus.Torre || rook.hasMoved)
            return false;

        // cami lliure
        for (int x = king.getPosicio().get(0) + step; x != rookX; x += step) {
            if (getPiece(x, y) != null) return false;
        }

        // el rei no pot passar per casellas atacades
        for (int i = 1; i <= 2; i++) {
            int x = king.getPosicio().get(0) + step * i;
            if (casellaAtacada(x, y, !king.getColor()))
                return false;
        }

        return true;
    }

    /**
     * Per comprovar si una casella està amenaçada. Útil per comprovar si es pot enrocar.
     * @param x
     * @param y
     * @param byWhite
     * @return
     */
    public boolean casellaAtacada(int x, int y, boolean byWhite) {
        for (Piece[] row : board) {
            for (Piece p : row) {
                if (p != null && p.getColor() == byWhite) {
                    if (p.pucMoureA(this, x, y)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }



}
