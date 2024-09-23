package suftware.tuitui.common.time;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeUtil {
    //  서울 시간대로 변환된 Timestamp 반환
    public static Timestamp getSeoulTimestamp(){
        ZonedDateTime seoulTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        return Timestamp.valueOf(seoulTime.toLocalDateTime());
    }
}
