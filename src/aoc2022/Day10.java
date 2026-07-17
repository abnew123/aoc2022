package aoc2022;

import java.math.BigInteger;
import java.util.Map;
import java.util.Scanner;

public class Day10 extends DayTemplate {
    private static final Map<String, Character> LETTERS = Map.ofEntries(
            Map.entry(".##..|#..#.|#..#.|####.|#..#.|#..#.", 'A'),
            Map.entry("###..|#..#.|###..|#..#.|#..#.|###..", 'B'),
            Map.entry(".##..|#..#.|#....|#....|#..#.|.##..", 'C'),
            Map.entry("####.|#....|###..|#....|#....|####.", 'E'),
            Map.entry("####.|#....|###..|#....|#....|#....", 'F'),
            Map.entry(".##..|#..#.|#....|#.##.|#..#.|.###.", 'G'),
            Map.entry("#..#.|#..#.|####.|#..#.|#..#.|#..#.", 'H'),
            Map.entry("..##.|...#.|...#.|...#.|#..#.|.##..", 'J'),
            Map.entry("#..#.|#.#..|##...|#.#..|#.#..|#..#.", 'K'),
            Map.entry("#....|#....|#....|#....|#....|####.", 'L'),
            Map.entry("###..|#..#.|#..#.|###..|#....|#....", 'P'),
            Map.entry("###..|#..#.|#..#.|###..|#.#..|#..#.", 'R'),
            Map.entry("#..#.|#..#.|#..#.|#..#.|#..#.|.##..", 'U'),
            Map.entry("####.|...#.|..#..|.#...|#....|####.", 'Z'));

    @Override
    public String[] fullSolve(Scanner in) {
        Analysis analysis = analyze(in);
        return new String[] {analysis.signal(), readScreen(analysis.screen())};
    }

    @Override
    public String solve(boolean part1, Scanner in) {
        Analysis analysis = analyze(in);
        return part1 ? analysis.signal() : readScreen(analysis.screen());
    }

    public String render(Scanner in) {
        return renderScreen(analyze(in).screen());
    }

    private String renderScreen(char[][] screen) {
        StringBuilder result = new StringBuilder(6 * 41 - 1);
        for (int row = 0; row < screen.length; row++) {
            if (row != 0) {
                result.append('\n');
            }
            for (char pixel : screen[row]) {
                result.append(pixel == '█' ? '#' : '.');
            }
        }
        return result.toString();
    }

    private Analysis analyze(Scanner in) {
        in.useDelimiter("\\A");
        String input = in.hasNext() ? in.next() : "";
        Register register = new Register();
        ExactTotal signal = new ExactTotal();
        char[][] screen = new char[6][40];
        int cycle = 0;
        int offset = 0;
        while (offset < input.length()) {
            int start = offset;
            while (offset < input.length() && input.charAt(offset) != '\n'
                    && input.charAt(offset) != '\r') {
                offset++;
            }
            int end = offset;
            if (offset < input.length()) {
                char ending = input.charAt(offset++);
                if (ending == '\r' && offset < input.length() && input.charAt(offset) == '\n') {
                    offset++;
                }
            }
            start = skipWhitespace(input, start, end);
            end = trimWhitespace(input, start, end);
            if (start == end) {
                throw new IllegalArgumentException("Blank instruction");
            }
            if (end - start == 4 && input.regionMatches(start, "noop", 0, 4)) {
                tick(register, signal, screen, cycle++);
                continue;
            }
            if (end - start < 6 || !input.regionMatches(start, "addx", 0, 4)
                    || !Character.isWhitespace(input.charAt(start + 4))) {
                throw new IllegalArgumentException("Malformed instruction");
            }
            int numberStart = skipWhitespace(input, start + 4, end);
            register.validateNumber(input, numberStart, end);
            tick(register, signal, screen, cycle++);
            tick(register, signal, screen, cycle++);
            register.add(input, numberStart, end);
        }
        return new Analysis(signal.toString(), screen);
    }

    private void tick(Register register, ExactTotal signal, char[][] screen, int zeroBasedCycle) {
        if (zeroBasedCycle < 240) {
            int column = zeroBasedCycle % 40;
            screen[zeroBasedCycle / 40][column] = register.near(column) ? '█' : ' ';
        }
        int cycle = zeroBasedCycle + 1;
        if (cycle >= 20 && cycle <= 220 && (cycle - 20) % 40 == 0) {
            signal.addProduct(register, cycle);
        }
    }

    private int skipWhitespace(String input, int start, int end) {
        while (start < end && Character.isWhitespace(input.charAt(start))) {
            start++;
        }
        return start;
    }

    private int trimWhitespace(String input, int start, int end) {
        while (end > start && Character.isWhitespace(input.charAt(end - 1))) {
            end--;
        }
        return end;
    }

    private String readScreen(char[][] screen) {
        StringBuilder result = new StringBuilder();
        for (int letter = 0; letter < screen[0].length / 5; letter++) {
            StringBuilder glyph = new StringBuilder();
            for (int row = 0; row < screen.length; row++) {
                if (row > 0) {
                    glyph.append('|');
                }
                for (int col = letter * 5; col < letter * 5 + 5; col++) {
                    glyph.append(screen[row][col] == '█' ? '#' : '.');
                }
            }
            Character decoded = LETTERS.get(glyph.toString());
            if (decoded == null) {
                return renderScreen(screen);
            }
            result.append(decoded);
        }
        return result.toString();
    }

    private record Analysis(String signal, char[][] screen) {}

    private static final class Register {
        private long small = 1;
        private BigInteger big;

        private boolean near(int column) {
            if (big != null) {
                return big.compareTo(BigInteger.valueOf(column - 1L)) >= 0
                        && big.compareTo(BigInteger.valueOf(column + 1L)) <= 0;
            }
            return small >= column - 1L && small <= column + 1L;
        }

        private void validateNumber(String input, int start, int end) {
            int cursor = start;
            if (cursor < end && (input.charAt(cursor) == '+' || input.charAt(cursor) == '-')) {
                cursor++;
            }
            int digitStart = cursor;
            while (cursor < end && Character.isDigit(input.charAt(cursor))) {
                cursor++;
            }
            if (cursor != end || cursor == digitStart) {
                throw new IllegalArgumentException("Malformed addx operand");
            }
        }

        private void add(String input, int start, int end) {
            if (big != null) {
                big = big.add(new BigInteger(input.substring(start, end)));
                return;
            }
            try {
                small = Math.addExact(small, Long.parseLong(input, start, end, 10));
            } catch (ArithmeticException | NumberFormatException exception) {
                big = BigInteger.valueOf(small).add(new BigInteger(input.substring(start, end)));
            }
        }
    }

    private static final class ExactTotal {
        private long small;
        private BigInteger big;

        private void addProduct(Register register, int cycle) {
            if (register.big != null) {
                add(register.big.multiply(BigInteger.valueOf(cycle)));
                return;
            }
            try {
                add(Math.multiplyExact(register.small, cycle));
            } catch (ArithmeticException exception) {
                add(BigInteger.valueOf(register.small).multiply(BigInteger.valueOf(cycle)));
            }
        }

        private void add(long value) {
            if (big != null) {
                big = big.add(BigInteger.valueOf(value));
                return;
            }
            try {
                small = Math.addExact(small, value);
            } catch (ArithmeticException exception) {
                big = BigInteger.valueOf(small).add(BigInteger.valueOf(value));
            }
        }

        private void add(BigInteger value) {
            big = (big == null ? BigInteger.valueOf(small) : big).add(value);
        }

        @Override
        public String toString() {
            return big == null ? Long.toString(small) : big.toString();
        }
    }
}
