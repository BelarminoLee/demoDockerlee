package mz.co.attendance.control.security;


import mz.co.attendance.control.dao.entities.security.User;

@FunctionalInterface
public interface CurrentUser {

    User getUser();
}