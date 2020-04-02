package com.jht.mytree.dao;

import com.jht.mytree.pojo.Tree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Tree dao
 * @author jianghongtao
 */
@Repository
public interface MyTreeRepository extends JpaRepository<Tree,String> {

	/**
	 * 添加节点第一步: right+2
	 * @param parentNodeRight
	 */
	@Modifying
	@Query(value = "update Tree t set t.TREE_RIGHT= t.TREE_RIGHT + 2 where t.TREE_RIGHT >= :parentNodeRight", nativeQuery = true)
	void addNodeFirstStep(@Param("parentNodeRight")Integer parentNodeRight);

	/**
	 * 添加节点第二步: left+2
	 * @param parentNodeRight
	 */
	@Modifying
	@Query(value = "update Tree t set t.TREE_LEFT= t.TREE_LEFT + 2 where t.TREE_LEFT >= :parentNodeRight", nativeQuery = true)
	void addNodeSecondStep(@Param("parentNodeRight")Integer parentNodeRight);

	/**
	 * 删除节点第一步： 删除子节点
	 * @param treeLeft
	 * @param treeRight
	 */
	@Query(value = "delete from Tree where TREE_LEFT >=:treeLeft and TREE_RIGHT <= :treeRight", nativeQuery = true)
	@Modifying
	void deleteFirstStep(@Param("treeLeft") Integer treeLeft,@Param("treeRight") Integer treeRight);

	/**
	 * 删除节点第二步
	 * @param treeLeft
	 * @param treeRight
	 */
	@Modifying
	@Query(value = "update Tree t set t.TREE_LEFT = t.TREE_LEFT - (:treeRight - :treeLeft + 1) where t.TREE_LEFT > :treeLeft", nativeQuery = true)
	void delSecondStep(@Param("treeLeft") Integer treeLeft,@Param("treeRight") Integer treeRight);

	/**
	 * 删除节点第三步
	 * @param treeLeft
	 * @param treeRight
	 */
	@Modifying
	@Query(value = "update Tree t set t.TREE_RIGHT = t.TREE_RIGHT - (:treeRight - :treeLeft + 1) where t.TREE_RIGHT > :treeRight", nativeQuery = true)
	void delThirdStep(@Param("treeLeft") Integer treeLeft,@Param("treeRight") Integer treeRight);

	/**
	 * 查询，获取族谱路径
	 * @param treeLeft
	 * @param treeRight
	 * @return
	 */
	@Query(value = "SELECT * FROM Tree where TREE_LEFT < :treeLeft and TREE_RIGHT > :treeRight order by TREE_LEFT ASC", nativeQuery = true)
	List<Tree> getPath(@Param("treeLeft") Integer treeLeft,@Param("treeRight") Integer treeRight);

	/**
	 * 算某节点所处层级
	 * @param treeLeft
	 * @param treeRight
	 * @return
	 */
	@Query(value = "SELECT count(*) FROM Tree where TREE_LEFT <= :treeLeft and TREE_RIGHT >= :treeRight order by TREE_LEFT ASC", nativeQuery = true)
	Integer getLevel(@Param("treeLeft") Integer treeLeft,@Param("treeRight") Integer treeRight);
}
