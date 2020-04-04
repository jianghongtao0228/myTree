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

	@Modifying
	@Query(value = "update Tree set TREE_LEFT = TREE_LEFT - (:treeRight - :treeLeft + 1) where TREE_LEFT > :treeLeft and id not in (select id from tree_id)", nativeQuery = true)
	void moveModeStep2(@Param("treeLeft") Integer treeLeft,@Param("treeRight") Integer treeRight);

	@Modifying
	@Query(value = "update Tree set TREE_RIGHT = TREE_RIGHT - (:treeRight - :treeLeft + 1) where TREE_RIGHT > :treeRight and id not in (select id from tree_id)", nativeQuery = true)
	void moveNodeStep3(@Param("treeLeft") Integer treeLeft,@Param("treeRight") Integer treeRight);

	/**
	 * 插入到末尾-步骤1
	 * @param treeLeft
	 * @param treeRight
	 * @param parentRight
	 */
	@Modifying
	@Query(value = "update Tree set TREE_RIGHT = TREE_RIGHT + (:treeRight - :treeLeft + 1) where TREE_RIGHT >= :parentRight and id not in (select id from tree_id)", nativeQuery = true)
	void moveToEndSetp1(@Param("treeLeft") Integer treeLeft,@Param("treeRight") Integer treeRight,@Param("parentRight") Integer parentRight);

	/**
	 * 插入到末尾-步骤2
	 * @param treeLeft
	 * @param treeRight
	 * @param parentRight
	 */
	@Modifying
	@Query(value = "update Tree set TREE_LEFT = TREE_LEFT + (:treeRight - :treeLeft + 1) where TREE_LEFT >= :parentRight and id not in (select id from tree_id)", nativeQuery = true)
	void moveToEndSetp2(@Param("treeLeft") Integer treeLeft,@Param("treeRight") Integer treeRight,@Param("parentRight") Integer parentRight);

	/**
	 * 插入到末尾-步骤3
	 * @param treeLeft
	 * @param parentRight
	 */
	@Modifying
	@Query(value = "update Tree set TREE_RIGHT = TREE_RIGHT - (:treeLeft - :parentRight), TREE_LEFT = TREE_LEFT - (:treeLeft - :parentRight) where id in (select id from tree_id)", nativeQuery = true)
	void moveToEndSetp3(@Param("treeLeft") Integer treeLeft,@Param("parentRight") Integer parentRight);

	/**
	 * 插入到开头-步骤1
	 * @param treeLeft
	 * @param treeRight
	 * @param parentLeft
	 */
	@Modifying
	@Query(value = "update Tree set TREE_RIGHT = TREE_RIGHT + (:treeRight - :treeLeft + 1) where TREE_RIGHT > :parentLeft and id not in (select id from tree_id)", nativeQuery = true)
	void moveToStartSetp1(@Param("treeLeft") Integer treeLeft,@Param("treeRight") Integer treeRight,@Param("parentLeft") Integer parentLeft);

	/**
	 * 插入到开头-步骤2
	 * @param treeLeft
	 * @param treeRight
	 * @param parentLeft
	 */
	@Modifying
	@Query(value = "update Tree set TREE_LEFT = TREE_LEFT + (:treeRight - :treeLeft + 1) where TREE_LEFT > :parentLeft and id not in (select id from tree_id)", nativeQuery = true)
	void moveToStartSetp2(@Param("treeLeft") Integer treeLeft,@Param("treeRight") Integer treeRight,@Param("parentLeft") Integer parentLeft);

	/**
	 * 插入到开头-步骤3
	 * @param treeLeft
	 * @param parentLeft
	 */
	@Modifying
	@Query(value = "update Tree set TREE_RIGHT = TREE_RIGHT - (:treeLeft - :parentLeft - 1), TREE_LEFT = TREE_LEFT - (:treeLeft - :parentLeft - 1) where id in (select id from tree_id)", nativeQuery = true)
	void moveToStartSetp3(@Param("treeLeft") Integer treeLeft,@Param("parentLeft") Integer parentLeft);

	/**
	 * 插入到指定位置-步骤1
	 * @param treeLeft
	 * @param treeRight
	 * @param siblingRight
	 */
	@Modifying
	@Query(value = "update Tree set TREE_RIGHT = TREE_RIGHT + (:treeRight - :treeLeft + 1) where TREE_RIGHT > :siblingRight and id not in (select id from tree_id)", nativeQuery = true)
	void moveToSiblingSetp1(@Param("treeLeft") Integer treeLeft,@Param("treeRight") Integer treeRight,@Param("siblingRight") Integer siblingRight);

	/**
	 * 插入到指定位置-步骤2
	 * @param treeLeft
	 * @param treeRight
	 * @param siblingRight
	 */
	@Modifying
	@Query(value = "update Tree set TREE_LEFT = TREE_LEFT + (:treeRight - :treeLeft + 1) where TREE_LEFT > :siblingRight and id not in (select id from tree_id)", nativeQuery = true)
	void moveToSiblingSetp2(@Param("treeLeft") Integer treeLeft,@Param("treeRight") Integer treeRight,@Param("siblingRight") Integer siblingRight);

	/**
	 * 插入到指定位置-步骤3
	 * @param treeLeft
	 * @param siblingRight
	 */
	@Modifying
	@Query(value = "update Tree set TREE_RIGHT = TREE_RIGHT - (:treeLeft - :siblingRight - 1), TREE_LEFT = TREE_LEFT - (:treeLeft - :siblingRight - 1) where id in (select id from tree_id)", nativeQuery = true)
	void moveToSiblingSetp3(@Param("treeLeft") Integer treeLeft,@Param("siblingRight") Integer siblingRight);
}
