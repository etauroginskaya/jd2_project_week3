package com.gmail.etauroginskaya.data.model;

public class Item {

    private Long id;
    private String name;
    private ItemStatusEnum statusEnum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStatusEnum getStatusEnum() {
        return statusEnum;
    }

    public void setStatusEnum(ItemStatusEnum statusEnum) {
        this.statusEnum = statusEnum;
    }
}
