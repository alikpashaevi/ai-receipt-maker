package alik.receiptmaker.components;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

public class GetUsername {
    public static String getUsernameFromToken() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
