package pl.pw.ocd.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangePasswordDTO {
    private String old;
    private String newpass;
    private String repeatpass;
}
