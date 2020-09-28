package hu.progmasters.gmistore.service;

import org.cloudinary.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DateService {

    public CreateDates stringToDate(String criteria){
        JSONObject obj = new JSONObject(criteria);

        String start = obj.getString("start");
        String end = obj.getString("end");
        String startDate = start.substring(0, start.length() - 1);
        String endDate = end.substring(0, end.length() - 1);
        LocalDateTime dateTimeStart = LocalDateTime.parse(startDate);
        LocalDateTime dateTimeEnd = LocalDateTime.parse(endDate);

        return new CreateDates(dateTimeStart, dateTimeEnd);
    }

    public static class CreateDates {
        private final LocalDateTime start;
        private final LocalDateTime end;
        public CreateDates(LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd) {
            this.start = dateTimeStart;
            this.end = dateTimeEnd;
        }

        public LocalDateTime getStart() {
            return start;
        }

        public LocalDateTime getEnd() {
            return end;
        }
    }
}
