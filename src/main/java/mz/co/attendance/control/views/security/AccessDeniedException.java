package mz.co.attendance.control.views.security;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException() {
    }

    public AccessDeniedException(String message) {
        super(message);
    }
}