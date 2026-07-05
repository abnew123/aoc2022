package aoc2022;

import java.util.*;

public class Day23 extends DayTemplate {
    static int[] DR = {-1, 1, 0, 0}, DC = {0, 0, -1, 1};

    public String solve(boolean part1, Scanner in) {
        Set<Long> elves = new HashSet<>();
        for (int r = 0; in.hasNextLine(); r++) {
            String s = in.nextLine();
            for (int c = 0; c < s.length(); c++) if (s.charAt(c) == '#') elves.add(pos(r, c));
        }
        for (int round = 0;; round++) {
            Map<Long, Long> move = new HashMap<>();
            Map<Long, Integer> count = new HashMap<>();
            for (long e : elves) {
                int r = row(e), c = col(e);
                if (!near(elves, r, c)) continue;
                for (int k = 0; k < 4; k++) {
                    int d = (round + k) & 3;
                    if (clear(elves, r, c, d)) {
                        long to = pos(r + DR[d], c + DC[d]);
                        move.put(e, to);
                        count.merge(to, 1, Integer::sum);
                        break;
                    }
                }
            }
            boolean moved = false;
            for (Map.Entry<Long, Long> e : move.entrySet()) if (count.get(e.getValue()) == 1) {
                elves.remove(e.getKey());
                elves.add(e.getValue());
                moved = true;
            }
            if (part1 && round == 9) return "" + empty(elves);
            if (!part1 && !moved) return "" + (round + 1);
        }
    }

    boolean near(Set<Long> elves, int r, int c) {
        for (int dr = -1; dr <= 1; dr++) for (int dc = -1; dc <= 1; dc++) {
            if ((dr != 0 || dc != 0) && elves.contains(pos(r + dr, c + dc))) return true;
        }
        return false;
    }

    boolean clear(Set<Long> elves, int r, int c, int d) {
        for (int x = -1; x <= 1; x++) {
            int nr = r + (d < 2 ? DR[d] : x), nc = c + (d < 2 ? x : DC[d]);
            if (elves.contains(pos(nr, nc))) return false;
        }
        return true;
    }

    long empty(Set<Long> elves) {
        int minR = 9999, maxR = -9999, minC = 9999, maxC = -9999;
        for (long e : elves) {
            minR = Math.min(minR, row(e));
            maxR = Math.max(maxR, row(e));
            minC = Math.min(minC, col(e));
            maxC = Math.max(maxC, col(e));
        }
        return (long) (maxR - minR + 1) * (maxC - minC + 1) - elves.size();
    }

    static long pos(int r, int c) {
        return ((long) r << 32) ^ (c & 0xffffffffL);
    }

    static int row(long p) {
        return (int) (p >> 32);
    }

    static int col(long p) {
        return (int) p;
    }
}
