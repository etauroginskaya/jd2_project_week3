package com.gmail.etauroginskaya.service.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ItemDTO {

    private Long id;

    @NotBlank(message = "Name cannot be null, empty, or consist of whitespaces only")
    @Size(max = 40, message = "Name should not be greater than 40")
    private String name;

    @NotBlank(message = "Status cannot be null, empty, or consist of whitespaces only")
    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ItemDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
