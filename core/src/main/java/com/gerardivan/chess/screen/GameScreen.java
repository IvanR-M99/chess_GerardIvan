package com.gerardivan.chess.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gerardivan.chess.model.Board;
import com.gerardivan.chess.model.Piece;
import com.gerardivan.chess.util.Utils;

public class GameScreen implements Screen {

    private SpriteBatch batch;
    private Texture boardTexture;

    private OrthographicCamera camera;
    private Viewport viewport;

    private static final float WORLD_WIDTH = 800;
    private static final float WORLD_HEIGHT = 600;

    private static final float BOARD_SIZE = 512;

    private float boardX;
    private float boardY;

    private float tileSize;
    private final Vector2 touch = new Vector2();

    Board board = new Board();

    int[] casellaClicada;
    int[] novaCasellaClicada;

    @Override
    public void show() {
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply();

        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        camera.update();

        boardTexture = new Texture(Utils.IMATGE_TAULER);

        // Centrar tablero con márgenes automáticos
        boardX = (WORLD_WIDTH - BOARD_SIZE) / 2f;
        boardY = (WORLD_HEIGHT - BOARD_SIZE) / 2f;

        tileSize = BOARD_SIZE / Utils.CELES_TAULER;
    }

    /**
     * Devuelve la casilla (col, row) donde se hizo clic.
     * Retorna null si el clic no está dentro del tablero.
     */
    private int[] getClickedTile() {
        if (!Gdx.input.justTouched())
            return null;

        // Convertir coordenadas de pantalla → mundo
        touch.set(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(touch);

        float worldX = touch.x;
        float worldY = touch.y;

        // Comprobar si el clic está dentro del tablero
        if (worldX < boardX || worldX >= boardX + BOARD_SIZE ||
                worldY < boardY || worldY >= boardY + BOARD_SIZE) {
            return null;
        }

        // Calcular columna y fila
        int col = (int) ((worldX - boardX) / tileSize);
        int row = (int) ((worldY - boardY) / tileSize);
        //row = BOARD_TILES - 1 - row;

        return new int[] { col, row };
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(boardTexture, boardX, boardY, BOARD_SIZE, BOARD_SIZE);

        for (int col = 0; col < Utils.CELES_TAULER; col++) {
            for (int row = 0; row < Utils.CELES_TAULER; row++) {
                Piece p = board.getPiece(col, row);
                if (p != null && p.getTexture() != null) {
                    batch.draw(
                            p.getTexture(),
                            boardX + tileSize * col,
                            boardY + tileSize * row, 
                            tileSize,
                            tileSize);
                }
            }
        }

        batch.end();

        int[] tile = getClickedTile();
        if (tile != null) {
            if (casellaClicada == null) {
                // Primer clic: seleccionar pieza
                Piece p = board.getPiece(tile[0], tile[1]);
                if (p != null) {
                    casellaClicada = tile;
                    System.out.println("Pieza seleccionada: " + tile[0] + ", " + tile[1]);
                }
            } else {
                // Segundo clic: mover pieza
                novaCasellaClicada = tile;

                // Mover en el Board
                board.movePiece(casellaClicada[0], casellaClicada[1],
                        novaCasellaClicada[0], novaCasellaClicada[1]);

                // Limpiar selección
                casellaClicada = null;
                novaCasellaClicada = null;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        boardTexture.dispose();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
}
