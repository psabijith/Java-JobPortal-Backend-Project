package com.aitrich.JobPortalSystem.Security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


public final class OwnershipUtils {

    private OwnershipUtils() {}

    /** Returns the email of the currently authenticated user. */
    public static String currentEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /** Returns true if the current user has the ADMIN role. */
    public static boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }


    public static void check(String ownerEmail) {
        if (!isAdmin() && !currentEmail().equals(ownerEmail)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }
    }
}
