package com.example.demo.dao;

import com.example.demo.entity.db.User;
import net.bytebuddy.implementation.bytecode.assign.TypeCasting;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LoginDao {

    @Autowired
    private SessionFactory sessionFactory;

    public String verifyLogin(String userId, String password) {
        String name = "";
        Session session = null;

        try {
            session = sessionFactory.openSession();
            User user = session.get(User.class, userId);
            if (user != null && user.getPassword().equals(password)) {
                name = user.getFirstName();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return name;
    }
}
