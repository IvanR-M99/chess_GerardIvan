package com.gerardivan.chess.model;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.gerardivan.chess.util.Utils;

public class Piece {

    public enum Tipus {
        Peon, Torre, Cavall, Alfil, Reina, Rei
    }

    Tipus peça;
    boolean esBlanca;
    List<Integer> posicio;
    List<Integer> ultimaPosicio;

    Texture imatge;

    /**
     * Inicialitzem en funció del tipus, assignant imatge (texture)
     */

    public Piece(Tipus peça, boolean esBlanca, int x, int y) {
        this.peça = peça;

        switch (peça) {
            case Peon:
                imatge = (esBlanca) ? new Texture(Utils.IMATGE_PEON_BLANC) : new Texture(Utils.IMATGE_PEON_NEGRE);
                break;
            case Torre:
                imatge = (esBlanca) ? new Texture(Utils.IMATGE_TORRE_BLANC) : new Texture(Utils.IMATGE_TORRE_NEGRE);
                break;
            case Cavall:
                imatge = (esBlanca) ? new Texture(Utils.IMATGE_CAVALL_BLANC) : new Texture(Utils.IMATGE_CAVALL_NEGRE);
                break;
            case Alfil:
                imatge = (esBlanca) ? new Texture(Utils.IMATGE_ALFIL_BLANC) : new Texture(Utils.IMATGE_ALFIL_NEGRE);
                break;
            case Reina:
                imatge = (esBlanca) ? new Texture(Utils.IMATGE_REINA_BLANC) : new Texture(Utils.IMATGE_REINA_NEGRE);
                break;
            case Rei:
                imatge = (esBlanca) ? new Texture(Utils.IMATGE_REI_BLANC) : new Texture(Utils.IMATGE_REI_NEGRE);
                break;

            default:
                break;
        }

        this.esBlanca = esBlanca;

        // Assignem la posició
        List<Integer> p = new ArrayList<>();
        p.add(x);
        p.add(y);
        this.posicio = p; // posició inicial
        this.ultimaPosicio = posicio; // i actualizem la última posició també
    }

    public Tipus getTipus() {
        return peça;
    }

    /**
     * Getter de la posició
     */
    public List<Integer> getPosicio() {
        return posicio;
    }

    public boolean getColor() {
        return esBlanca;
    }

    /**
     * Setter de la posició
     */
    public void setPosicio(int x, int y) {
        List<Integer> p = new ArrayList<>();
        p.add(x);
        p.add(y);
        this.posicio = p;
    }

    /**
     * Getter de la imatge
     */
    public Texture getTexture() {
        return imatge;
    }

    /**
     * Retorna el tipus en String
     */
    @Override
    public String toString() {
        return String.valueOf(peça);
    }

    /**
     * Booleà per comprovar si la peça pot fer un moviment, ens envia al mètode
     * concret
     * de cada tipus de peça
     */
    public boolean canMoveTo(Board board, int destX, int destY) {
        int x = posicio.get(0);
        int y = posicio.get(1);

        if (x == destX && y == destY)
            return false; // si la posició destí i origen és la mateixa

        switch (peça) {
            case Peon:
                return canMovePeon(board, x, y, destX, destY);
            case Torre:
                return canMoveTorre(board, x, y, destX, destY);
            case Cavall:
                return canMoveCavall(board, x, y, destX, destY);
            case Alfil:
                return canMoveAlfil(board, x, y, destX, destY);
            case Reina:
                return canMoveReina(board, x, y, destX, destY);
            case Rei:
                return canMoveRei(board, x, y, destX, destY);
        }
        return false;
    }

    private boolean canMovePeon(Board board, int x, int y, int dx, int dy) {
        int dir = esBlanca ? 1 : -1; // Per controlar el sentit que pot avançar la peça
        int rowDiff = dy - y;
        int colDiff = dx - x;

        // Peça en el destí
        Piece target = board.getPiece(dx, dy);

        // Moviment recte (mateixa columna)
        if (colDiff == 0) {
            if (rowDiff == dir && target == null) // Si és avançar una i no hi ha cap peça
                return true;
            // si avança 2, no hi ha cap peça en el destí, i la posició és la inicial
            if (rowDiff == 2 * dir && target == null && ultimaPosicio.equals(posicio))
                // afegim condició, si no hi ha cap peça a la casella de just davant, retornarà
                // true
                return board.getPiece(x, y + dir) == null;
        }

        // Captura diagonal
        if (Math.abs(colDiff) == 1 && rowDiff == dir) {
            return target != null && target.esBlanca != this.esBlanca;
        }

        return false;
    }

    private boolean canMoveTorre(Board board, int x, int y, int dx, int dy) {
        // Peça en el destí
        Piece target = board.getPiece(dx, dy);

        if (target.esBlanca == this.esBlanca)
            return false;
        if (x != dx && y != dy)
            return false;
        return board.isPathClear(x, y, dx, dy);
    }

    private boolean canMoveCavall(Board board, int x, int y, int dx, int dy) {
        // Peça en el destí
        Piece target = board.getPiece(dx, dy);

        if (target.esBlanca == this.esBlanca)
            return false;

        int cx = Math.abs(dx - x);
        int cy = Math.abs(dy - y);
        return (cx == 2 && cy == 1) || (cx == 1 && cy == 2);
    }

    private boolean canMoveAlfil(Board board, int x, int y, int dx, int dy) {
        // Peça en el destí
        Piece target = board.getPiece(dx, dy);

        if (target.esBlanca == this.esBlanca)
            return false;

        if (Math.abs(dx - x) != Math.abs(dy - y))
            return false;
        return board.isPathClear(x, y, dx, dy);
    }

    private boolean canMoveReina(Board board, int x, int y, int dx, int dy) {
        // Peça en el destí
        Piece target = board.getPiece(dx, dy);

        if (target.esBlanca == this.esBlanca)
            return false;

        boolean recte = x == dx || y == dy;
        boolean diagonal = Math.abs(dx - x) == Math.abs(dy - y);

        if (!recte && !diagonal)
            return false;
        return board.isPathClear(x, y, dx, dy);
    }

    private boolean canMoveRei(Board board, int x, int y, int dx, int dy) {
        //Peça en el destí
        Piece target = board.getPiece(dx, dy);

        if (target.esBlanca == this.esBlanca) return false;

        return Math.abs(dx - x) <= 1 && Math.abs(dy - y) <= 1;
    }

}
