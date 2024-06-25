package net.ramgames.tomereader;

public interface LecternAccess {

    int tomeReader$getTicks();

    float tomeReader$getNextPageAngle();

    float tomeReader$getPageAngle();

    float tomeReader$getFlipRandom();

    float tomeReader$getFlipTurn();

    float tomeReader$getBookRotation();

    float tomeReader$getTargetBookRotation();

    void tomeReader$setTicks(int ticks);

    void tomeReader$setNextPageAngle(float nextPageAngle);

    void tomeReader$setPageAngle(float pageAngle);

    void tomeReader$setFlipRandom(float flipRandom);

    void tomeReader$setFlipTurn(float flipTurn);

    void tomeReader$setBookRotation(float bookRotation);

    void tomeReader$setLastBookRotation(float lastBookRotation);

    void tomeReader$setTargetBookRotation(float targetBookRotation);

    boolean tomeReader$isTomeReaderLectern();

    void tomeReader$setIsTomeReaderLectern(boolean val);
}
