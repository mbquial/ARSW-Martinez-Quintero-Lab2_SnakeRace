package co.eci.snake.concurrency;

import co.eci.snake.core.Board;
import co.eci.snake.core.Direction;
import co.eci.snake.core.Snake;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

public final class SnakeRunner implements Runnable {
  private final Snake snake;
  private final Board board;
  private final int baseSleepMs = 80;
  private final int turboSleepMs = 40;
  private int turboTicks = 0;
  private boolean isPaused = false;
  private CountDownLatch pauseLatch = null;
  private final Object pauseLock = new Object();

  public SnakeRunner(Snake snake, Board board) {
    this.snake = snake;
    this.board = board;
  }

  public SnakeRunner(Snake snake, Board board, boolean startPaused) {
    this.snake = snake;
    this.board = board;
    this.isPaused = startPaused;
  }

  @Override
  public void run() {
    try {
      while (!Thread.currentThread().isInterrupted() && snake.isAlive()) {
        synchronized (pauseLock) {
          if (pauseLatch != null) {
            pauseLatch.countDown();
          }
          while (isPaused) {
            pauseLock.wait();
          }
        }
        
        maybeTurn();
        var res = board.step(snake);
        if (res == Board.MoveResult.HIT_OBSTACLE) {
          randomTurn();
        } else if (res == Board.MoveResult.ATE_TURBO) {
          turboTicks = 100;
        } else if (res == Board.MoveResult.DEAD) {
          break;
        }
        int sleep = (turboTicks > 0) ? turboSleepMs : baseSleepMs;
        if (turboTicks > 0) turboTicks--;
        Thread.sleep(sleep);
      }
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
    }
  }

  public void requestPause(CountDownLatch latch) {
    synchronized (pauseLock) {
      isPaused = true;
      pauseLatch = latch;
    }
  }

  public void resume() {
    synchronized (pauseLock) {
      isPaused = false;
      pauseLatch = null;
      pauseLock.notifyAll();
    }
  }

  public boolean isAlive() {
    return snake.isAlive();
  }

  private void maybeTurn() {
    double p = (turboTicks > 0) ? 0.05 : 0.10;
    if (ThreadLocalRandom.current().nextDouble() < p) randomTurn();
  }

  private void randomTurn() {
    var dirs = Direction.values();
    snake.turn(dirs[ThreadLocalRandom.current().nextInt(dirs.length)]);
  }
}
