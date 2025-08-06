package com.ensias.facture.shared;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level= AccessLevel.PRIVATE) // tout est private (les  champs)
@Builder
public class ErrorMessage {
    String message;
    Date timestamp;
    Integer code;

}
