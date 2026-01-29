package com.ridesharing.service;

import com.ridesharing.dao.UserDAO;
import com.ridesharing.util.Session;

public class UserService {

    private UserDAO dao = new UserDAO();

    public void register(String n, String m, String p) {
        dao.register(n, m, p);
    }

    public boolean login(String m, String p) {
        int id = dao.login(m, p);
        if (id != -1) {
            Session.userId = id;
            return true;
        }
        return false;
    }
}
