package net.ramgames.tomereader;

public interface LecternAccess {

    int tomeReader$getTicks();

    float tomeReader$getNextPageAngle();

    float tomeReader$getPageAngle();

    float tomeReader$getFlipRandom();

    float tomeReader$getFlipTurn();

    float tomeReader$getNextPageTurningSpeed();

    float tomeReader$getPageTurningSpeed();

    float tomeReader$getBookRotation();

    float tomeReader$getLastBookRotation();

    float tomeReader$getTargetBookRotation();

    void tomeReader$setTicks(int ticks);

    void tomeReader$setNextPageAngle(float nextPageAngle);

    void tomeReader$setPageAngle(float pageAngle);

    void tomeReader$setFlipRandom(float flipRandom);

    void tomeReader$setFlipTurn(float flipTurn);

    void tomeReader$setNextPageTurningSpeed(float nextPageTurningSpeed);

    void tomeReader$setPageTurningSpeed(float pageTurningSpeed);

    void tomeReader$setBookRotation(float bookRotation);

    void tomeReader$setLastBookRotation(float lastBookRotation);

    void tomeReader$setTargetBookRotation(float targetBookRotation);

}
