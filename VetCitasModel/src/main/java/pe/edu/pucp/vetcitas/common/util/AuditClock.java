package pe.edu.pucp.vetcitas.common.util;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public final class AuditClock {
    private static final ZoneId LIMA_ZONE = ZoneId.of("America/Lima");

    private AuditClock() {
    }

    public static LocalDateTime now() {
        return LocalDateTime.now(LIMA_ZONE);
    }

    public static LocalDate today() {
        return LocalDate.now(LIMA_ZONE);
    }

    public static Timestamp timestampNow() {
        return Timestamp.valueOf(now());
    }
}
