package akrupych.aomsider;

import android.content.Context;
import android.graphics.BitmapFactory;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;

import java.io.IOException;
import java.io.InputStream;

public class BitmapLoader {

    private Context context;
    private TextureManager textureManager;

    public BitmapLoader(Context context, TextureManager textureManager) {
        this.context = context;
        this.textureManager = textureManager;
    }

    public TextureRegion loadTexture(String fileName) {
        return loadTexture(fileName, TextureOptions.DEFAULT);
    }

    public TextureRegion loadTexture(String fileName, TextureOptions textureOptions) {
        Size bitmapSize = getBitmapSize(fileName);
        BitmapTextureAtlas atlas = new BitmapTextureAtlas(textureManager,
                bitmapSize.getWidth(), bitmapSize.getHeight(), textureOptions);
        TextureRegion result = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, context, fileName, 0, 0);
        atlas.load();
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
}
