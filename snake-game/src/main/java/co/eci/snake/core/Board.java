package co.eci.snake.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public final class Board {
  private final int width;
  private final int height;

  private final Set<Position> mice = new HashSet<>();
  private final Set<Position> obstacles = new HashSet<>();
  private final Set<Position> turbo = new HashSet<>();
  private final Map<Position, Position> teleports = new HashMap<>();
  private final Object lockStep = new Object();
  private final Set<Snake> allSnakes = new HashSet<>();

    public enum MoveResult { MOVED, ATE_MOUSE, HIT_OBSTACLE, ATE_TURBO, TELEPORTED, DEAD }

  public Board(int width, int height) {
    if (width <= 0 || height <= 0) throw new IllegalArgumentException("Board dimensions must be positive");
    this.width = width;
    this.height = height;
    for (int i=0;i<6;i++) mice.add(randomEmpty());
    for (int i=0;i<4;i++) obstacles.add(randomEmpty());
    for (int i=0;i<3;i++) turbo.add(randomEmpty());
    createTeleportPairs(2);
  }

  public int width() { return width; }
  public int height() { return height; }

  public Set<Position> mice() { return new HashSet<>(mice); }
  public Set<Position> obstacles() { return new HashSet<>(obstacles); }
  public Set<Position> turbo() { return new HashSet<>(turbo); }
  public Map<Position, Position> teleports() { return new HashMap<>(teleports); }

  public void registerSnake(Snake snake) {
    synchronized (lockStep) {
      allSnakes.add(snake);
    }
  }

  public  MoveResult step(Snake snake) {
      Objects.requireNonNull(snake, "snake");

      if (!snake.isAlive()) {
          return MoveResult.DEAD;
      }
      
      var head = snake.head();
      var dir = snake.direction();
      Position next = new Position(head.x() + dir.dx, head.y() + dir.dy).wrap(width, height);

      boolean ateMouse = false;
      boolean ateTurbo = false;
      boolean teleported = false;

      boolean hitObstacle = false;
      
      synchronized (lockStep) {
          if (obstacles.contains(next)) {
              hitObstacle = true;
          } else {
              for (Snake other : allSnakes) {
                  if (other != snake && other.isAlive()) {
                      var otherBody = other.snapshot();
                      if (otherBody.contains(next)) {
                          snake.markDead();
                          return MoveResult.DEAD;
                      }
                  }
              }

              var myBody = snake.snapshot();
              myBody.pollFirst();
              if (myBody.contains(next)) {
                  snake.markDead();
                  return MoveResult.DEAD;
              }

              if (teleports.containsKey(next)) {
                  next = teleports.get(next);
                  teleported = true;
              }

              ateMouse = mice.remove(next);
              ateTurbo = turbo.remove(next);

              if (ateMouse) {
                  mice.add(randomEmpty());
                  obstacles.add(randomEmpty());
                  if (ThreadLocalRandom.current().nextDouble() < 0.2) turbo.add(randomEmpty());
              }
          }
      }

      if (hitObstacle) {
          return MoveResult.HIT_OBSTACLE;
      }

      snake.advance(next, ateMouse);

      if (ateTurbo) return MoveResult.ATE_TURBO;
      if (ateMouse) return MoveResult.ATE_MOUSE;
      if (teleported) return MoveResult.TELEPORTED;

      return MoveResult.MOVED;
  }

  private void createTeleportPairs(int pairs) {
    for (int i=0;i<pairs;i++) {
      Position a = randomEmpty();
      Position b = randomEmpty();
      teleports.put(a, b);
      teleports.put(b, a);
    }
  }

  private Position randomEmpty() {
    var rnd = ThreadLocalRandom.current();
    Position p;
    int guard = 0;
    do {
      p = new Position(rnd.nextInt(width), rnd.nextInt(height));
      guard++;
      if (guard > width*height*2) break;
    } while (mice.contains(p) || obstacles.contains(p) || turbo.contains(p) || teleports.containsKey(p));
    return p;
  }
}
