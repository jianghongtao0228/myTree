package com.jht.mytree.dao;

import com.jht.mytree.pojo.TreeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * TreeId dao
 * @author jianghongtao
 */
@Repository
public interface TreeIdRepository extends JpaRepository<TreeId,String> {

	/**
	 * 将要移动的节点及其子节点进行id记录
	 * 移动节点，步骤1
	 * @param treeLeft
	 * @param treeRight
	 * @return
	 */
	@Modifying
	@Query(value = "insert into tree_id(id) select id from Tree where TREE_LEFT >= :treeLeft and TREE_RIGHT <= :treeRight",nativeQuery = true)
	int insertIds(@Param("treeLeft") Integer treeLeft, @Param("treeRight") Integer treeRight);
}
