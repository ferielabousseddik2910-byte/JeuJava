package com.jeu.babaisyou;

public enum GameObjectType {
    BABA("baba.png", false, null, null),
    WALL("wall.png", false, null, null),
    ROCK("rock.png", false, null, null),
    FLAG("flag.png", false, null, null),
    TEXT_BABA("text_baba.png", true, GameObjectType.BABA, null),
    TEXT_WALL("text_wall.png", true, GameObjectType.WALL, null),
    TEXT_ROCK("text_rock.png", true, GameObjectType.ROCK, null),
    TEXT_FLAG("text_flag.png", true, GameObjectType.FLAG, null),
    TEXT_IS("text_is.png", true, null, null),
    TEXT_YOU("text_you.png", true, null, Property.YOU),
    TEXT_PUSH("text_push.png", true, null, Property.PUSH),
    TEXT_STOP("text_stop.png", true, null, Property.STOP),
    TEXT_WIN("text_win.png", true, null, Property.WIN);

    private final String imageFileName;
    private final boolean isText;
    private final GameObjectType noun;
    private final Property property;

    GameObjectType(String imageFileName, boolean isText, GameObjectType noun, Property property) {
        this.imageFileName = imageFileName;
        this.isText = isText;
        this.noun = noun;
        this.property = property;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public boolean isText() {
        return isText;
    }

    public GameObjectType getNoun() {
        return noun;
    }

    public Property getProperty() {
        return property;
    }
}
