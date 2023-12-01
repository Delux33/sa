package com.sa.entity.swagger;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserForSwagger {

    @Hidden
    private Long id;

    private String name;

    private String lastname;

    private String surname;
}
