package akrupych.aomsider;

import android.content.Context;
import android.graphics.BitmapFactory;

import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.region.TextureRegion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class TextureLoader {

    private final Context context;
    private final TextureManager textureManager;

    public TextureLoader(Context context, TextureManager textureManager) {
        this.context = context;
        this.textureManager = textureManager;
    }

    public TextureRegion loadBitmap(String fileName) {
        return loadBitmap(fileName, TextureOptions.DEFAULT);
    }

    public TextureRegion loadBitmap(String fileName, TextureOptions textureOptions) {
        Size bitmapSize = getBitmapSize(fileName);
        BitmapTextureAtlas atlas = new BitmapTextureAtlas(textureManager,
                bitmapSize.getWidth(), bitmapSize.getHeight(), textureOptions);
        TextureRegion result = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, context, fileName, 0, 0);
        atlas.load();
        return result;
    }

    public Map<String, TextureRegion> loadAtlas(String imageFileName, String mapFileName) {
        Map<String, TextureRegion> result = null;
        try {
            AssetBitmapTexture texture = new AssetBitmapTexture(textureManager, context.getAssets(), imageFileName);
            result = getTextureRegions(texture, mapFileName);
            texture.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Size getBitmapSize(String fileName) {
        InputStream stream = null;
        try {
            stream = context.getAssets().open(fileName);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(stream, null, options);
            return new Size(options.outWidth, options.outHeight);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new Size(0, 0);
    }

    private Map<String, Rect> getTextureBounds(String mapFileName) {
        Map<String, Rect> result = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(mapFileName)));
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

    private Map<String, TextureRegion> getTextureRegions(Texture texture, String mapFileName) {
        Map<String, Rect> textureBounds = getTextureBounds(mapFileName);
        Map<String, TextureRegion> result = new HashMap<>(textureBounds.size());
        for (Map.Entry<String, Rect> entry : textureBounds.entrySet()) {
            String name = entry.getKey();
            Rect bounds = entry.getValue();
            result.put(name, new TextureRegion(texture, bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight()));
        }
        return result;
    }
}
