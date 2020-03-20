package com.ruoyi.product.mapper;

import com.ruoyi.product.domain.Material;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MaterialMapper {
    int removeMaterialByIds(Long[] ids);

    int removeMaterialwhenUpdate(Material material);

    int removeMaterialByMid(Long id);

    int insertMaterial(Material material);

    Material selectMaterialById(Long id);

    Material selectMaterialByProductUUid(Material material);

    List<Material> selectMaterial(Material material);

    Integer maxCount(Material material);

    int insertMatList(@Param("listMaterial") List<Material> listMaterial, @Param("productId") Long productId, @Param("uuid") String uuid, @Param("comment_code") String comment_code);


    /*
    List<Material> selectMaterialListProductIdIsNull(Material material);
    */

    int updateMobileMaterial(Material material);

    int updateMaterial(Material material);

    int updateDrift(Material material);

    int deleteByProductId(Long productId);

    /*
    int count(Material material);
    */

    /*
    List<Material> materialList(Material material);
    */

    List<Material> mobileMaterial(Material material);

    List<Material> mobileSelectMaterials(Material material);

    List<Material> selectByProductNameAndType(Material material);

    /*
    int materialCount(Long productId);
    */

    /**
     * @author: louk
     * @Description: :检查物料名称是否重复
     * @date: 2019/9/20 14:27
     */
    List<Material> checkName(Material material);


    int updatesign(Material materia);

    int sortMaterial(List<Material> listmaterial);

    Integer sumMaterial(@Param("productId") Long productId,@Param("type")String type);

    int updateMajor(Material materia);
}