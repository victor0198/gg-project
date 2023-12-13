package student.examples.uservice.api.business.game;

import java.util.HashSet;
import java.util.Set;

public class Space {
    private Set<Item> items;

    public Space() {
        super();
        this.items = new HashSet<>();
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public void addItem(Item items) {
        this.items.add(items);
    }
}
