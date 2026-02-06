package co.eci.snake.core;

import java.util.Comparator;
import java.util.List;

public final class GameStats {

    public static Snake getLongestAliveSnake(List<Snake> snakes) {
        return snakes.stream()
            .filter(Snake::isAlive)
            .max(Comparator.comparingInt(Snake::getLength))
            .orElse(null);
    }

    public static Snake getFirstDeadSnake(List<Snake> snakes) {
        return snakes.stream()
            .filter(s -> !s.isAlive())
            .min(Comparator.comparingLong(Snake::getDeathTime))
            .orElse(null);
    }

    public static long countAliveSnakes(List<Snake> snakes) {
        return snakes.stream()
        .filter(Snake::isAlive).count();
    }

    public static long countDeadSnakes(List<Snake> snakes) {
        return snakes.stream()
        .filter(s -> !s.isAlive()).count();
    }
}
