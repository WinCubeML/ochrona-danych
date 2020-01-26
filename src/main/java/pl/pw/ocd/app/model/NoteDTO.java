package pl.pw.ocd.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NoteDTO {
    private String noteText;

    private int type;

    private String sharedLogins;
}
