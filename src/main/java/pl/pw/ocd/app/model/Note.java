package pl.pw.ocd.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@RedisHash("note")
@Data
@NoArgsConstructor
public class Note {
    @Id
    private String noteId;

    private String noteText;

    private String owner;

    private boolean isPublic;

    private boolean isPrivate;

    private List<String> permitted;
}
