package com.example.demo.dao;


import com.example.demo.entity.db.Item;
import com.example.demo.entity.db.ItemType;
import com.example.demo.entity.db.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FavoriteDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void setFavoriteItem(String userId, Item item) {
        Session session = null;

        try {
            session = sessionFactory.openSession();
            User user = session.get(User.class, userId);
            user.getItemSet().add(item);

            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();

        } catch (Exception exception) {
            exception.printStackTrace();
            if (session != null) session.getTransaction().rollback();
        } finally {
            if (session != null) session.close();
        }
    }

    public void unsetFavoriteItem(String userId, String itemId) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            User user = session.get(User.class, userId);
            Item item = session.get(Item.class, itemId);
            user.getItemSet().remove(item);

            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();

        } catch (Exception ex) {
            ex.printStackTrace();
            if (session != null) session.getTransaction().rollback();

        } finally {
            if (session != null) session.close();
        }
    }

    public Set<Item> getFavoriteItems(String userId) {
        Session session = null;

        try {
            session = sessionFactory.openSession();
            User user = session.get(User.class, userId);
            if (user != null) {
                return user.getItemSet();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (session != null) session.close();
        }

        return new HashSet<>();
    }

    public Set<String> getFavoriteItemIds(String userId) {
        Set<String> itemIds = new HashSet<>();

        try (Session session = sessionFactory.openSession()) {
            Set<Item> items = session.get(User.class, userId).getItemSet();
            for (Item item : items) {
                itemIds.add(item.getId());
            }
        } catch (Exception exception)   {
            exception.printStackTrace();
        }
        return itemIds;

    }

    public Map<String, List<String>> getFavoriteGameIds(Set<String> favoriteIdemIds) {
        Map<String, List<String>> itemMap = new HashMap<>();
        for (ItemType type : ItemType.values()) {
            itemMap.put(type.toString(), new ArrayList<>());
        }

        try (Session session = sessionFactory.openSession()) {
            for (String itemId : favoriteIdemIds) {
                Item item = session.get(Item.class, itemId);
                itemMap.get(item.getItemType().toString()).add(item.getGameId());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return itemMap;

    }

}
