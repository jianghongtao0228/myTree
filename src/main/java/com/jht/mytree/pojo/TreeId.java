package com.jht.mytree.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author jianghongtao
 * TREE_ID pojo类
 */
@Entity
@Table(name = "TREE_ID")
public class TreeId implements Serializable {

	private static final long serialVersionUID = 3522711886443389287L;

	@Id
	@Column(name = "ID", length = 32)
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
