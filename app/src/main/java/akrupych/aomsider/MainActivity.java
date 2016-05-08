package akrupych.aomsider;

import android.annotation.TargetApi;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.RepeatingSpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends SimpleBaseGameActivity {

    private static final int ICON_PANEL_HEIGHT = 200;
    private static final int TILE_ROWS = 4;
    private static final int TILE_COLUMNS = 8;

    private static int cameraWidth;
    private static int cameraHeight;
    private static int centerX;
    private static int centerY;

    private RepeatingSpriteBackground background;
    private List<Sprite> units = new ArrayList<>();
    private List<Sprite> tiles = new ArrayList<>();
    private IconsPanel iconsPanel;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(size);
        cameraWidth = size.x;
        cameraHeight = size.y;
        centerX = cameraWidth / 2;
        centerY = cameraHeight / 2;
        super.onCreate(pSavedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        Camera camera = new Camera(0, 0, cameraWidth, cameraHeight);
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR,
                new RatioResolutionPolicy(camera.getWidth(), camera.getHeight()), camera);
    }

    @Override
    protected void onCreateResources() throws IOException {
        TextureLoader textureLoader = new TextureLoader(this, getTextureManager());

        background = new RepeatingSpriteBackground(cameraWidth, cameraHeight,
                textureLoader.loadBitmap("dirt_grass_50.png", TextureOptions.REPEATING_BILINEAR), getVertexBufferObjectManager());
        iconsPanel = new IconsPanel(centerX, ICON_PANEL_HEIGHT / 2, cameraWidth, ICON_PANEL_HEIGHT,
                textureLoader.loadBitmap("panel_left.png"),
                textureLoader.loadBitmap("panel_center.png"),
                textureLoader.loadBitmap("panel_right.png"),
                getVertexBufferObjectManager());

        TextureRegion tileTextureRegion = textureLoader.loadBitmap("tile.png");
        float tileWidth = tileTextureRegion.getWidth();
        float tileHeight = tileTextureRegion.getHeight();
        for (int row = 0; row < TILE_ROWS; row++) {
            for (int column = 0; column < TILE_COLUMNS; column++) {
                float x = centerX + (column - TILE_COLUMNS / 2f) * tileWidth + tileWidth / 2f;
                float y = centerY + (row - TILE_ROWS / 2f) * tileHeight + tileWidth / 2f + iconsPanel.getHeight() / 2f;
                tiles.add(new Sprite(x, y, tileTextureRegion, getVertexBufferObjectManager()));
            }
        }

        Map<String, TextureRegion> unitsTextureRegions = textureLoader.loadAtlas("units.png", "units.txt");
        Random random = new Random();
        for (Map.Entry<String, TextureRegion> entry : unitsTextureRegions.entrySet()) {
            Sprite randomTile = tiles.get(random.nextInt(tiles.size()));
            Sprite sprite = new Sprite(randomTile.getX(), randomTile.getY(), entry.getValue(), getVertexBufferObjectManager());
            if (entry.getKey().endsWith("icon")) {
                iconsPanel.attachChild(sprite);
            } else {
                units.add(sprite);
            }
        }
    }

    @Override
    protected Scene onCreateScene() {
        Scene scene = new Scene();
        scene.setBackground(background);
        for (Sprite sprite : tiles) {
            scene.attachChild(sprite);
        }
        for (Sprite sprite : units) {
            scene.attachChild(sprite);
        }
        scene.attachChild(iconsPanel);
        return scene;
    }

}
