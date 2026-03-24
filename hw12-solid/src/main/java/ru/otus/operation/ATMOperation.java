package ru.otus.operation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.otus.banknote.Banknote;
import ru.otus.cell.BanknoteCell;
import ru.otus.cell.Cell;

public class ATMOperation implements Operation {

    private final Map<Integer, Cell> cells = new HashMap<>();

    @Override
    public int getBalance() {
        return cells.values().stream().mapToInt(Cell::getAvailableAmount).sum();
    }

    @Override
    public void deposit(Banknote banknote, int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be positive");
        }

        int denomination = banknote.getDenomination();
        Cell cell = cells.computeIfAbsent(denomination, key -> new BanknoteCell(banknote, 0));
        cell.add(count);
    }

    @Override
    public WithdrawalResult withdraw(int amount) {
        if (amount <= 0) {
            return WithdrawalResult.failure("Amount must be positive");
        }

        if (amount > getBalance()) {
            return WithdrawalResult.failure("Insufficient funds");
        }

        Map<Banknote, Integer> withdrawalResult = withdraw(cells, amount);

        if (withdrawalResult.isEmpty()) {
            return WithdrawalResult.failure("Cannot withdraw amount");
        }

        cellRemove(withdrawalResult);

        return WithdrawalResult.success(withdrawalResult);
    }

    private Map<Banknote, Integer> withdraw(Map<Integer, Cell> cells, int amount) {
        Map<Banknote, Integer> result = new HashMap<>();

        List<Cell> sortedCells = cells.values().stream()
                .sorted((a, b) -> Integer.compare(
                        b.getBanknote().getDenomination(), a.getBanknote().getDenomination()))
                .toList();

        Map<Integer, Integer> availableBanknote = new HashMap<>();
        for (Cell cell : sortedCells) {
            availableBanknote.put(cell.getBanknote().getDenomination(), cell.getCount());
        }

        for (Cell cell : sortedCells) {
            int denomination = cell.getBanknote().getDenomination();
            int count = availableBanknote.get(denomination);
            int neededCount = amount / denomination;
            int useCount = Math.min(neededCount, count);

            result.put(cell.getBanknote(), useCount);

            amount -= useCount * denomination;
            availableBanknote.put(denomination, count - useCount);

            if (amount == 0) {
                break;
            }
        }

        return amount == 0 ? result : Collections.emptyMap();
    }

    private void cellRemove(Map<Banknote, Integer> withdrawal) {
        for (Map.Entry<Banknote, Integer> entry : withdrawal.entrySet()) {
            Banknote banknote = entry.getKey();
            Cell cell = cells.get(banknote.getDenomination());
            int count = entry.getValue();
            cell.remove(count);
        }
    }
}
