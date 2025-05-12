package ru.yandex.practicum.iteractionapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    String country;
    String city;
    String street;
    String house;
    String flat;
}
