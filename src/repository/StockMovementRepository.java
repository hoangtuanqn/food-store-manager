
package repository;
import model.MovementSource;
import model.StockMovement;

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
     private static final String SEPARATOR = "|";
    private final Path filePath;

    public StockMovementRepository() {
        this("data/stock_movements.txt");
    }

    public StockMovementRepository(String filePath) {
        this.filePath = Paths.get(filePath);
        ensureFileExists();
    }
    
    public void add(StockMovement m) {
        String line = m.getProductId() + SEPARATOR
                + m.getOldQuantity() + SEPARATOR
                + m.getNewQuantity() + SEPARATOR
                + m.getSource().name() + SEPARATOR
                + m.getTimestamp().toString() + SEPARATOR
                + (m.getOrderId() == null ? "" : m.getOrderId());
        try (BufferedWriter w = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            w.write(line);
            w.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Cannot write stock movement log: " + e.getMessage(), e);
        }
    }
    
    public List<StockMovement> findAll() {
        List<StockMovement> result = new ArrayList<>();
        try {
            for (String line : Files.readAllLines(filePath, StandardCharsets.UTF_8)) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                result.add(parse(line));
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot read stock movement log: " + e.getMessage(), e);
        }
        return result;
    }

    private StockMovement parse(String line) {
     
        String[] f = line.split("\\|", -1);
        String orderId = f[5].isEmpty() ? null : f[5];
        return new StockMovement(
                f[0],
                Integer.parseInt(f[1]),
                Integer.parseInt(f[2]),
                MovementSource.valueOf(f[3]),
                LocalDateTime.parse(f[4]),
                orderId);
    }

    private void ensureFileExists() {
        try {
            Path parent = filePath.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot create log file: " + e.getMessage(), e);
        }
    }
    
}
