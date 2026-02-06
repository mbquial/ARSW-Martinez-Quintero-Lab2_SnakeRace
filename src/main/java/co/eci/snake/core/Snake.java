package co.eci.snake.core;

import java.util.ArrayDeque;
import java.util.Deque;

public final class Snake {
  private static int nextId = 1;
  private final int id;
  private final Deque<Position> body = new ArrayDeque<>();
  private volatile Direction direction;
  private int maxLength = 5;
  private long deathTimestamp = -1;

  private Snake(Position start, Direction dir) {
    synchronized (Snake.class) {
      this.id = nextId++;
    }
    body.addFirst(start);
    this.direction = dir;
  }

  public static Snake of(int x, int y, Direction dir) {
    return new Snake(new Position(x, y), dir);
  }

  public synchronized Direction direction() { return direction; }

  public synchronized void turn(Direction dir) {
    if ((direction == Direction.UP && dir == Direction.DOWN) ||
        (direction == Direction.DOWN && dir == Direction.UP) ||
        (direction == Direction.LEFT && dir == Direction.RIGHT) ||
        (direction == Direction.RIGHT && dir == Direction.LEFT)) {
      return;
    }
    this.direction = dir;
  }

  public Position head() { return body.peekFirst(); }

  public synchronized Deque<Position> snapshot() { return new ArrayDeque<>(body); }

  public synchronized void advance(Position newHead, boolean grow) {
    body.addFirst(newHead);
    if (grow) maxLength++;
    while (body.size() > maxLength) body.removeLast();
  }

  public int getId() { return id; }

  public synchronized boolean isAlive() {
    return deathTimestamp == -1;
  }

  public synchronized void markDead() {
    if (deathTimestamp == -1) {
      deathTimestamp = System.currentTimeMillis();
    }
  }

  public synchronized int getLength() {
    return body.size();
  }

  public synchronized long getDeathTime() {
    return deathTimestamp;
  }
}
