package akrupych.aomsider;

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

    private RepeatingSpriteBackground background;
    private List<Sprite> sprites = new ArrayList<>();

    @Override
    public EngineOptions onCreateEngineOptions() {
        Camera camera = new Camera(0, 0, 1280, 800);
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR,
                new RatioResolutionPolicy(camera.getWidth(), camera.getHeight()), camera);

    }

    @Override
    protected void onCreateResources() throws IOException {
        TextureLoader textureLoader = new TextureLoader(this, getTextureManager());
        background = new RepeatingSpriteBackground(getEngine().getCamera().getWidth(), getEngine().getCamera().getHeight(),
                textureLoader.loadBitmap("dirt_grass_50.png", TextureOptions.REPEATING_BILINEAR), getVertexBufferObjectManager());
        Map<String, TextureRegion> textureRegions = textureLoader.loadAtlas("units.png", "units.txt");
        for (Map.Entry<String, TextureRegion> entry : textureRegions.entrySet()) {
            sprites.add(new Sprite(0, 0, entry.getValue(), getVertexBufferObjectManager()));
        }
    }

    @Override
    protected Scene onCreateScene() {
        Scene scene = new Scene();
        scene.setBackground(background);
        Random random = new Random();
        for (Sprite sprite : sprites) {
            sprite.setX(random.nextInt((int) getEngine().getCamera().getWidth() - 200) + 100);
            sprite.setY(random.nextInt((int) getEngine().getCamera().getHeight() - 200) + 100);
            scene.attachChild(sprite);
        }
        return scene;
    }

}
