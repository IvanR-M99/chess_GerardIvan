package com.gerardivan.chess.model;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.gerardivan.chess.util.Utils;

public class Piece {

    public enum Tipus { Peon, Torre, Cavall, Alfil, Reina, Rei }

    Tipus peça;
    boolean esBlanca;
    List<Integer> posicio;
    List<Integer> ultimaPosicio;

    Texture imatge;

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

        List<Integer> p = new ArrayList<>();
        p.add(x);
        p.add(y);

        this.posicio = p;
        this.ultimaPosicio = posicio;
    }

    public List<Integer> getPosicio() {
        return posicio;
    }

    public void setPosicio(int x, int y) {
        List<Integer> p = new ArrayList<>();
        p.add(x);
        p.add(y);
        this.posicio = p;
    }

    public Texture getTexture() {
        return imatge;
    }

    @Override
    public String toString() {
        return String.valueOf(peça);
    }

}
