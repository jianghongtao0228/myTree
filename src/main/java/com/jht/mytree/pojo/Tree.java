package com.jht.mytree.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author jianghongtao
 * TREE pojo类
 */
@Entity
@Table(name = "TREE")
public class Tree implements Serializable {

	private static final long serialVersionUID = 3522711886454789287L;

	@Id
	@Column(name = "ID", length = 32)
	private String id;

	@Column(name = "NAME", length = 256)
	private String name;

	@Column(name = "PARENT_ID", length = 32)
	private String parentId;

	@Column(name = "TREE_LEFT")
	private Integer treeLeft;

	@Column(name = "TREE_RIGHT")
	private Integer treeRight;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getTreeLeft() {
		return treeLeft;
	}

	public void setTreeLeft(Integer treeLeft) {
		this.treeLeft = treeLeft;
	}

	public Integer getTreeRight() {
		return treeRight;
	}

	public void setTreeRight(Integer treeRight) {
		this.treeRight = treeRight;
	}
}
