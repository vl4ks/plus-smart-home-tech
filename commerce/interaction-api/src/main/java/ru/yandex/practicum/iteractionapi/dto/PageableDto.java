package ru.yandex.practicum.iteractionapi.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageableDto {
    @Min(0)
    Integer page;
    @Min(1)
    Integer size;
    List<String> sort;
}
