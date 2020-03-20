package com.ruoyi.product.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author chenm
 * @create 2019-08-30 16:01
 */
@Getter
@Setter
public class MaterialList {

    private List<Material> materialList;

    public List<Material> getMaterialList() {
        return materialList;
    }

    public MaterialList() {
    }

    public MaterialList(List<Material> materialList) {
        super();
        this.materialList = materialList;
    }
}
