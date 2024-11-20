package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    Map<Long, Map<Long, Item>> userItems = new HashMap<>();
    Map<Long, Item> items = new HashMap<>();
    Long id = 1L;

    @Override
    public Item create(Item item) {
        item.setId(generatedId());
        items.put(item.getId(), item);

        userItems.compute(item.getOwner().getId(), (ownerId, ownerItemsMap) -> {
            if (ownerItemsMap == null) {
                ownerItemsMap = new HashMap<>();
            }
            ownerItemsMap.put(item.getId(), item);
            return ownerItemsMap;
        });

        return items.get(item.getId());

    }

    @Override
    public Item update(Item item) {
        Item oldItem = items.get(item.getId());
        if (item.getName() != null) {
            oldItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            oldItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            oldItem.setAvailable(item.getAvailable());
        }
        items.put(oldItem.getId(), oldItem);
        Map<Long, Item> itemsMap = userItems.get(oldItem.getOwner().getId());
        itemsMap.put(oldItem.getId(), oldItem);
        userItems.put(oldItem.getOwner().getId(), itemsMap);
        return oldItem;
    }

    @Override
    public Item getByItemId(Long itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> getUserItems(Long userId) {
        return userItems.get(userId).values().stream().toList();
    }

    @Override
    public List<Item> search(String text) {
        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains((text.toLowerCase())))
                .toList();
    }

    private Long generatedId() {
        return id++;
    }
}
