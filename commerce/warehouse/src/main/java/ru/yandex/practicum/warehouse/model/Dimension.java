package ru.yandex.practicum.warehouse.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Builder
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Dimension {
    Double width;
    Double height;
    Double depth;
}
