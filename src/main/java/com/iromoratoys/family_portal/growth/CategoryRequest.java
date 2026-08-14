package com.iromoratoys.family_portal.growth;

import jakarta.validation.constraints.NotBlank;

public class CategoryRequest {

    @NotBlank(message = "カテゴリ名は必須です by spring")
    private String name;

    private Integer sortOrder;

    private String colorCode;

    public String getName() { return name; }
    public Integer getSortOrder() { return sortOrder; }
    public String getColorCode() { return colorCode; }
}
