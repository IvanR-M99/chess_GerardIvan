package com.gerardivan.chess.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gerardivan.chess.Main;
import com.gerardivan.chess.model.Board;
import com.gerardivan.chess.model.Piece;
import com.gerardivan.chess.util.Utils;

import java.util.ArrayList;

public class GameScreen implements Screen {

    private Main game;

    private Stage stage;
    private Skin skin;

    private SpriteBatch batch;
    private Texture boardTexture, highlightTexture;


    private OrthographicCamera camera;
    private Viewport viewport;

    private static final float WORLD_WIDTH = 800;
    private static final float WORLD_HEIGHT = 800;

    private final float MARGIN = 80; // margen en píxeles para botones/espacio UI

    private final float BOARD_SIZE = Math.min(WORLD_WIDTH, WORLD_HEIGHT) - MARGIN;

    private float boardX;
    private float boardY;

    private float tileSize;
    private final Vector2 touch = new Vector2();

    Board board = new Board();
    ArrayList<int[]> possibleMoviments = new ArrayList<>();
    int[] casellaClicada;
    int[] novaCasellaClicada;
    boolean gameFinished = false;

    public GameScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        highlightTexture = new Texture("chess.com-boards-and-pieces/resaltado.png");
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

        stage = new Stage(viewport, batch);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        crearUI();

        Gdx.input.setInputProcessor(null);

        InputMultiplexer mux = new InputMultiplexer();
        mux.addProcessor(stage);
        mux.addProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                return false; // deja pasar al juego
            }
        });
        Gdx.input.setInputProcessor(mux);
    }

    /**
     * Botó sortir dins de la pantalla del joc
     */
    private void crearUI() {
        Table table = new Table();
        table.setFillParent(true);
        table.top().right();
        table.padRight(40); // margen desde el borde

        TextButton btnReiniciar = new TextButton("Reinciar partida", skin);
        btnReiniciar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });

        TextButton btnSortir = new TextButton("Sortir", skin);
        btnSortir.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.getActualMenuScreen().recreateStage();
                game.setScreen(game.getActualMenuScreen());
            }
        });

        table.add(btnReiniciar).width(140).height(30).padRight(10); // margen entre botones
        table.add(btnSortir).width(90).height(30);

        stage.addActor(table);
    }

    /**
     * Retorna la casella (x, y) on es fa el click.
     * Retorna null si el clic no està dins del taulell
     */
    private int[] getCasellaClicada() {
        if (!Gdx.input.justTouched())
            return null;

        // Convertir coordenadas de pantalla a món
        touch.set(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(touch);

        float worldX = touch.x;
        float worldY = touch.y;

        // Comprovar si el clic està dins del taulell
        if (worldX < boardX || worldX >= boardX + BOARD_SIZE ||
            worldY < boardY || worldY >= boardY + BOARD_SIZE) {
            return null;
        }

        // Calcular columna i fila
        int col = (int) ((worldX - boardX) / tileSize);
        int row = (int) ((worldY - boardY) / tileSize);
        // row = BOARD_TILES - 1 - row;

        return new int[]{col, row};
    }

    private void calcularPossiblesMoviments(Piece p) {
        possibleMoviments.clear();

        for (int col = 0; col < Utils.CELES_TAULER; col++) {
            for (int row = 0; row < Utils.CELES_TAULER; row++) {

                if (p.pucMoureA(board, col, row)
                    && board.tryMove(p, col, row)) {

                    possibleMoviments.add(new int[]{col, row});
                }
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        //pintem tauler
        batch.draw(boardTexture, boardX, boardY, BOARD_SIZE, BOARD_SIZE);

        // Movimientos possibles
        for (int[] move : possibleMoviments) {
            batch.draw(
                highlightTexture, //pintem el resaltat
                boardX + tileSize * move[0],
                boardY + tileSize * move[1],
                tileSize,
                tileSize
            );
        }

        //Pintem peces
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

        if (gameFinished) {
            stage.act(delta);
            stage.draw();
            return;
        }

        int[] tile = getCasellaClicada();
        if (tile != null) {
            if (casellaClicada == null) {
                // Primer clic: seleccionar pieza
                Piece p = board.getPiece(tile[0], tile[1]);
                if (p != null && p.getColor() == board.isWhiteTurn()) {
                    casellaClicada = tile;
                    calcularPossiblesMoviments(p);
                    System.out.println("Pieza seleccionada: " + tile[0] + ", " + tile[1]);
                }
            } else { //si has fet el primer click
                // Segon clic: moure peça
                novaCasellaClicada = tile;
                Piece p = board.getPiece(casellaClicada[0], casellaClicada[1]);

                if (p.pucMoureA(board, novaCasellaClicada[0], novaCasellaClicada[1]) &&
                    board.tryMove(p, novaCasellaClicada[0], novaCasellaClicada[1])) {
                    board.mourePiece(p, novaCasellaClicada[0], novaCasellaClicada[1]);
                    //revisem el jaque o ofegat
                    if (board.isCheckMate(!p.getColor())) {
                        mostraDialogFiJoc(
                            "Jaque mate.\nGanan las " + (p.getColor() ? "BLANCAS" : "NEGRAS")
                        );
                    } else if (board.isStalemate(!p.getColor())) {
                        mostraDialogFiJoc("Rey ahogado.\nEmpate");
                    }
                    //Alternem torn (si no ha acabat la partida)
                    if (!gameFinished) {
                        board.seguentTorn();
                    }
                }
                // Limpiar selección
                casellaClicada = null;
                novaCasellaClicada = null;
                possibleMoviments.clear();
            }
        }

        stage.act(delta);
        stage.draw();
    }

    private void mostraDialogFiJoc(String message) {
        gameFinished = true;
        Dialog dialog = new Dialog("Fin de la partida", skin) {

            @Override
            protected void result(Object object) {
                String action = (String) object;

                switch (action) {
                    case "replay":
                        game.setScreen(new GameScreen(game));
                        break;

                    case "menu":
                        game.getActualMenuScreen().recreateStage();
                        game.setScreen(game.getActualMenuScreen());
                        break;

                    case "exit":
                        Gdx.app.exit();
                        break;
                }
            }
        };

        dialog.text(message);
        dialog.button("Volver a jugar", "replay");
        dialog.button("Menú principal", "menu");
        dialog.button("Salir", "exit");

        dialog.getContentTable().pad(20);
        dialog.getButtonTable().pad(10);

        dialog.show(stage);
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
