package hu.progmasters.gmistore.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
    private final SessionRegistry sessionRegistry;

    @Autowired
    public AdminService(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    public List<String> getUsersFromSessionRegistry() {
        List<String> sessions = sessionRegistry.getAllPrincipals().stream()
                .filter(u -> !sessionRegistry.getAllSessions(u, false).isEmpty())
                .map(Object::toString)
                .collect(Collectors.toList());
        LOGGER.info(String.valueOf(sessions.size()));
        return sessions;
    }
}
