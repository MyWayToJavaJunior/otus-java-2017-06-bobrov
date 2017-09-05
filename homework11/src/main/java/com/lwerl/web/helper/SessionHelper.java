package com.lwerl.web.helper;

import javax.servlet.http.HttpSession;

/**
 * Created by lWeRl on 04.09.2017.
 */
public class SessionHelper {

    static public boolean hasLogin(HttpSession session) {
        return session.getAttribute("login") != null;
    }
}
