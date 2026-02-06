package co.eci.snake.concurrency;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public final class GameCoordinator {
    private final List<SnakeRunner> runners;
    private final AtomicBoolean pauseRequested = new AtomicBoolean(false);
    private CountDownLatch pause;

    public GameCoordinator(List<SnakeRunner> runners) {
        this.runners = runners;
    }

    public void pauseAll() throws InterruptedException {
        if (pauseRequested.getAndSet(true)) {
            return;
        }
        
        int aliveCount = 0;
        for (SnakeRunner runner : runners) {
            if (runner.isAlive()) {
                aliveCount++;
            }
        }
        
        pause = new CountDownLatch(aliveCount);

        for (SnakeRunner runner : runners) {
            runner.requestPause(pause);
        }

        pause.await();
    }

    public void resumeAll() {
        if (!pauseRequested.getAndSet(false)) {
            return;
        }

        for (SnakeRunner runner : runners) {
            runner.resume();
        }
    }

    public boolean isPaused() {
        return pauseRequested.get();
    }
}
