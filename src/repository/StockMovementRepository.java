
    package repository;

    import model.MovementSource;
    import model.StockMovement;
    import view.ConsoleView;
    import java.io.BufferedWriter;
    import java.io.IOException;
    import java.nio.charset.StandardCharsets;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.nio.file.StandardOpenOption;
    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.List;

    public class StockMovementRepository {

        private static final String FILE_PATH = "data/stock_movements.txt";
        private static final String SEPARATOR = "|";

        private final List<StockMovement> movements = new ArrayList<>();
        private final Path file = Paths.get(FILE_PATH);

        public StockMovementRepository() {
            load();
        }

        public void add(StockMovement movement) {
            movements.add(movement);
            append(movement);
        }

        public List<StockMovement> findAll() {
            return new ArrayList<>(movements);
        }
        private void load() {
            if (!Files.exists(file)) {
                return;
            }
            try {
                for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                    if (line.trim().isEmpty()) {
                        continue;
                    }
                    try {
                        movements.add(parse(line));
                    } catch (RuntimeException e) {
                        ConsoleView.showError("Skipped invalid stock movement line: " + line);
                    }
                }
            } catch (IOException e) {
                ConsoleView.showError("Cannot read stock movement file: " + e.getMessage());
            }
        }

        private void append(StockMovement m) {
            String line = m.getProductId() + SEPARATOR
                    + m.getOldQuantity() + SEPARATOR
                    + m.getNewQuantity() + SEPARATOR
                    + m.getSource().name() + SEPARATOR
                    + m.getTimestamp() + SEPARATOR
                    + (m.getOrderId() == null ? "" : m.getOrderId());
            try {
                if (file.getParent() != null) {
                    Files.createDirectories(file.getParent());
                }
                try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) {
                ConsoleView.showError("Cannot write stock movement file: " + e.getMessage());
            }
        }
        private StockMovement parse(String line) {
            String[] f = line.split("\\|", -1);
            String orderId = f[5].isEmpty() ? null : f[5];
            return new StockMovement(f[0],
                    Integer.parseInt(f[1]),
                    Integer.parseInt(f[2]),
                    MovementSource.valueOf(f[3]),
                    LocalDateTime.parse(f[4]),
                    orderId);
        }
    }