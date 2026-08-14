package com.iromoratoys.family_portal.growth;

public class CategoryResponse {

    private Long id;
    private String name;
    private Integer sortOrder;
    private String colorCode;

    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.sortOrder = category.getSortOrder();
        this.colorCode = category.getColorCode();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Integer getSortOrder() { return sortOrder; }
    public String getColorCode() { return colorCode; }
}
