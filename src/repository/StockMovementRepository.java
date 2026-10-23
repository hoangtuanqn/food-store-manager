package repository;
import model.StockMovement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class StockMovementRepository {
    private final List<StockMovement> movements = new ArrayList<>();
    public void add(StockMovement m) {
        movements.add(m);
    }
    public List<StockMovement> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(movements));
    }
}