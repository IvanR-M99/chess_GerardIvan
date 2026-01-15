package com.gerardivan.chess.model;

import com.gerardivan.chess.util.Utils;

public class Board {

    private Piece[][] board;

    public Board() {
        board = new Piece[Utils.CELES_TAULER][Utils.CELES_TAULER];
        setupInitialPosition();
    }

    /** Inicializa piezas al inicio de la partida */
    private void setupInitialPosition() {
        // Pawns
        for (int i = 0; i < Utils.CELES_TAULER; i++) {
            board[i][1] = new Piece(Piece.Tipus.Peon, true, i, 1);   // blancas
            board[i][6] = new Piece(Piece.Tipus.Peon, false, i, 6);  // negras
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

    /** Obtiene la pieza en una casilla (null si vacía) */
    public Piece getPiece(int col, int row) {
        if (col < 0 || col >= Utils.CELES_TAULER || row < 0 || row >= Utils.CELES_TAULER) return null;
        return board[col][row];
    }

    /** Mueve una pieza de una casilla a otra */
    public void movePiece(int fromCol, int fromRow, int toCol, int toRow) {
        Piece p = getPiece(fromCol, fromRow);
        if (p == null) return;

        p.setPosicio(toCol, toRow);

        board[toCol][toRow] = p;
        board[fromCol][fromRow] = null;
    }

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

}
