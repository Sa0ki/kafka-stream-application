package ma.kinan.saad.kafkaapplication.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @author Eren
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class PageEvent {
    private String pageName;
    private String userName;
    private Date date;
    private int duration;
}
