package akrupych.aomsider;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.clip.ClipEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class IconsPanel extends Entity {

    private Entity content;
    private float childrenWidth;

    public IconsPanel(float x, float y, float width, float height, TextureRegion leftTextureRegion,
                      TextureRegion centerTextureRegion, TextureRegion rightTextureRegion, VertexBufferObjectManager vbom) {
        super(x, y, width, height);
        float ratio = height / leftTextureRegion.getHeight();
        Sprite leftSprite = new Sprite(0, 0, leftTextureRegion, vbom);
        leftSprite.setSize(leftTextureRegion.getWidth() * ratio, height);
        leftSprite.setPosition(leftSprite.getWidth() / 2, y);
        super.attachChild(leftSprite);
        ratio = height / rightTextureRegion.getHeight();
        Sprite rightSprite = new Sprite(0, 0, rightTextureRegion, vbom);
        rightSprite.setSize(rightTextureRegion.getWidth() * ratio, height);
        rightSprite.setPosition(getWidth() - rightSprite.getWidth() / 2, y);
        super.attachChild(rightSprite);
        float repeatCount = 1;
        float repeatWidth = centerTextureRegion.getWidth();
        float spaceAvailable = getWidth() - leftSprite.getWidth() - rightSprite.getWidth();
        while (Math.abs(spaceAvailable - repeatWidth * repeatCount) > repeatWidth / 2f) {
            repeatCount++;
        }
        for (int i = 0; i < repeatCount; i++) {
            repeatWidth = spaceAvailable / repeatCount;
            float currentX = i * repeatWidth + repeatWidth / 2f + leftSprite.getWidth();
            super.attachChild(new Sprite(currentX, y, repeatWidth, height, centerTextureRegion, vbom));
        }
        content = new ClipEntity(getX(), getY(),
                getWidth() - leftSprite.getWidth() - rightSprite.getWidth(),
                getHeight());
        super.attachChild(content);
    }

    @Override
    public void attachChild(IEntity pEntity) {
        content.attachChild(pEntity);
        pEntity.setY(content.getY());
        childrenWidth += pEntity.getWidth();
        float widthUsed = 0;
        for (int i = 0; i < content.getChildCount(); i++) {
            IEntity current = content.getChildByIndex(i);
            widthUsed += current.getWidth();
            float x = content.getWidth() / 2f - (childrenWidth / 2f) + widthUsed - current.getWidth() / 2f;
            current.setX(x);
        }
    }
}
