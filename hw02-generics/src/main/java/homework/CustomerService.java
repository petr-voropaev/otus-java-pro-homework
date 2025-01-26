package homework;

import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class CustomerService {
    private NavigableMap<Customer, String> map = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> firstEntry = map.firstEntry();

        if (firstEntry == null) {
            return null;
        }

        return Map.entry(
                new Customer(
                        firstEntry.getKey().getId(),
                        firstEntry.getKey().getName(),
                        firstEntry.getKey().getScores()),
                firstEntry.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> higherEntry = map.higherEntry(customer);

        if (higherEntry == null) {
            return null;
        }

        return Map.entry(
                new Customer(
                        higherEntry.getKey().getId(),
                        higherEntry.getKey().getName(),
                        higherEntry.getKey().getScores()),
                higherEntry.getValue());
    }

    public void add(Customer customer, String data) {
        map.put(customer, data);
    }
}
