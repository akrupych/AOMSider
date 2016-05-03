package akrupych.aomsider;

import android.content.Context;

import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.region.TextureRegion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class AtlasLoader {

    private final Context context;
    private final TextureManager textureManager;

    public AtlasLoader(Context context, TextureManager textureManager) {
        this.context = context;
        this.textureManager = textureManager;
    }

    public Map<String, TextureRegion> loadTexture(String atlasName) {
        Map<String, TextureRegion> result = null;
        try {
            AssetBitmapTexture texture = new AssetBitmapTexture(textureManager, context.getAssets(), atlasName + ".png");
            result = getTextureRegions(texture, atlasName);
            texture.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Map<String, Rect> getTextureBounds(String atlasName) {
        Map<String, Rect> result = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(atlasName + ".txt")));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] mapping = line.split(" = ");
                String name = mapping[0];
                String[] bounds = mapping[1].split(" ");
                result.put(name, new Rect(Integer.valueOf(bounds[0]), Integer.valueOf(bounds[1]),
                        Integer.valueOf(bounds[2]), Integer.valueOf(bounds[3])));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Map<String, TextureRegion> getTextureRegions(Texture texture, String atlasName) {
        Map<String, Rect> textureBounds = getTextureBounds(atlasName);
        Map<String, TextureRegion> result = new HashMap<>(textureBounds.size());
        for (Map.Entry<String, Rect> entry : textureBounds.entrySet()) {
            String name = entry.getKey();
            Rect bounds = entry.getValue();
            result.put(name, new TextureRegion(texture, bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight()));
        }
        return result;
    }
}
