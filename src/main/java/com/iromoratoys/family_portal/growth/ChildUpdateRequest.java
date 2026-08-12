package com.iromoratoys.family_portal.growth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class ChildUpdateRequest {

    @NotBlank(message = "名前は必須です by spring")
    private String name;

    @NotNull(message = "生年月日は必須です by spring")
    private LocalDate birthDate;

    public String getName() { return name; }
    public LocalDate getBirthDate() { return birthDate; }
}
