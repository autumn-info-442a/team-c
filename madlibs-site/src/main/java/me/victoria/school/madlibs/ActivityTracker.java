package me.victoria.school.madlibs;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ActivityTracker {
    static ConcurrentLinkedQueue<String> openLobbies = new ConcurrentLinkedQueue<>();
    static ConcurrentHashMap<String, Long> lastActivity = new ConcurrentHashMap<>();
    static ConcurrentHashMap<String, Boolean> started = new ConcurrentHashMap<>();

    public synchronized static void logActivity(String lobby) {
        if (openLobbies.contains(lobby)) {
            lastActivity.put(lobby, System.currentTimeMillis());
        }
    }

    public synchronized static boolean hasGameStarted(String lobby) {
        return started.containsKey(lobby) && started.get(lobby);
    }

    public synchronized static void startLobby(String lobby) {
        started.put(lobby, true);
        lastActivity.put(lobby, System.currentTimeMillis());
    }

    public synchronized static void stopLobby(String lobby) {
        openLobbies.remove(lobby);
        lastActivity.remove(lobby);
        started.remove(lobby);
    }

    public synchronized static void backInLobby(String lobby) {
        started.put(lobby, false);
    }

    public synchronized static boolean isValidCode(String code) {
        return openLobbies.contains(code);
    }

    public synchronized static String generateLobbyCode() {
        String generated = RandomStringUtils.randomAlphanumeric(12).toUpperCase();
        StringBuilder sb = new StringBuilder(generated);
        sb.insert(4, '-');
        sb.insert(9, '-');
        generated = sb.toString();
        if (openLobbies.contains(generated)) {
            return generateLobbyCode();
        }
        openLobbies.add(generated);
        lastActivity.put(generated, System.currentTimeMillis());
        started.put(generated, false);
        return generated;
    }

    public synchronized static void checkForInactivity() {
        lastActivity.forEach((s, aLong) -> {
            long current = System.currentTimeMillis();
            if (current - aLong >= 1200000) { //30000) {
                Broadcaster.broadcast(s + "|SHUTDOWN_INACTIVITY");
                stopLobby(s);
            }
        });
    }


}
