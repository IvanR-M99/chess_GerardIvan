package com.gerardivan.chess.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gerardivan.chess.Main;
import com.gerardivan.chess.util.Utils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.graphics.GL20;

public class MenuScreen implements Screen {

    private final Main game;

    private final Stage stage;
    private final Skin skin;

    private final float MARGIN = 80; // margen en píxeles para botones/espacio UI

    private final float BOARD_SIZE = Math.min(WORLD_WIDTH, WORLD_HEIGHT) - MARGIN;
    private float boardX, boardY;
    private Texture boardTexture;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private static final float WORLD_WIDTH = 800;
    private static final float WORLD_HEIGHT = 800;

    public MenuScreen(Main game) {
        this.game = game;
        this.batch = game.batch;

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        boardTexture = new Texture(Utils.IMATGE_TAULER);

        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));


        boardX = (WORLD_WIDTH - BOARD_SIZE) / 2f; // centrar horizontal
        boardY = (WORLD_HEIGHT - BOARD_SIZE) / 2f; // centrar vertical
        crearUI();
    }

    /**
     * Interfície de menú d'opcions inicial
     */
    private void crearUI() {
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        Label titulo = new Label("Escacs", skin);
        titulo.setFontScale(3f);

        TextButton btnJugar = new TextButton("Jugar", skin);
        btnJugar.getLabel().setFontScale(1.5f);
        btnJugar.setColor(Color.GREEN);
        TextButton btnOpcions = new TextButton("Opcions", skin);
        btnOpcions.getLabel().setFontScale(1.5f);
        TextButton btnSalir = new TextButton("Sortir", skin);
        btnSalir.setColor(Color.RED);
        btnSalir.getLabel().setFontScale(1.5f);

        btnSalir.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        btnJugar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });

        btnOpcions.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new OptionScreen(game));
            }
        });

        table.add(titulo).padBottom(40);
        table.row();

        table.add(btnJugar).width(300).height(80).pad(10);
        table.row();

        table.add(btnOpcions).width(300).height(80).pad(10);
        table.row();

        table.add(btnSalir).width(300).height(80).pad(10);

        stage.addActor(table);

    }

    /**
     * Sirve para cuando vuelvas a esta pantalla se pueda seguir usando
     */
    public void recreateStage() {
        stage.clear(); // Limpia todo
        crearUI(); // Vuelve a crear la tabla y los botones
        Gdx.input.setInputProcessor(stage); // Reinicia input
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        // Tablero centrado con margen
        batch.draw(boardTexture, boardX, boardY, BOARD_SIZE, BOARD_SIZE);
        batch.end();

        // Dibuja UI encima
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        boardX = (WORLD_WIDTH - BOARD_SIZE) / 2f;
        boardY = (WORLD_HEIGHT - BOARD_SIZE) / 2f;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply();

        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        camera.update();

        boardTexture = new Texture(Utils.IMATGE_TAULER);

        // Mantener tablero centrado con márgenes
        boardX = (WORLD_WIDTH - BOARD_SIZE) / 2f;
        boardY = (WORLD_HEIGHT - BOARD_SIZE) / 2f;
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        boardTexture.dispose();
    }
}
