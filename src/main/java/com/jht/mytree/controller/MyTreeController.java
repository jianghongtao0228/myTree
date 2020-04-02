package com.jht.mytree.controller;

import com.jht.mytree.dao.MyTreeRepository;
import com.jht.mytree.pojo.Tree;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.*;

/**
 * MyTree controller
 * @author jianghongtao
 */
@RestController
public class MyTreeController {

	@Autowired
	private MyTreeRepository myTreeRepository;

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	@ResponseBody
	public List<Tree> test(){
		return myTreeRepository.findAll();
	}

	/**
	 * 添加根节点-可以有多个跟节点
	 * @return
	 */
	@RequestMapping(value = "/addRoot", method = RequestMethod.GET)
	@ResponseBody
	public String addRoot(@RequestParam String name){
		String id = UUID.randomUUID().toString().replace("-", "");
		Tree tree = new Tree();
		tree.setId(id);
		tree.setName(name);
		tree.setParentId("-1");
		tree.setTreeLeft(1);
		tree.setTreeRight(2);
		myTreeRepository.save(tree);
		return id;
	}

	/**
	 * 为某节点创建子节点
	 * @param parentId
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/addOne", method = RequestMethod.GET)
	@ResponseBody
	@Transactional(rollbackOn = Exception.class)
	public String addOne(@RequestParam String parentId,String name){
		if(StringUtils.isBlank(parentId) || StringUtils.isBlank(name)){
			return "创建失败，参数为空";
		}
		try{
			Tree parent = myTreeRepository.getOne(parentId);
			if(parent == null){
				return "父节点不存在";
			}
			myTreeRepository.addNodeFirstStep(parent.getTreeRight());
			myTreeRepository.addNodeSecondStep(parent.getTreeRight());
			String id = UUID.randomUUID().toString().replace("-", "");
			Tree tree = new Tree();
			tree.setId(id);
			tree.setName(name);
			tree.setParentId(parentId);
			tree.setTreeLeft(parent.getTreeRight());
			tree.setTreeRight(parent.getTreeRight() + 1);
			myTreeRepository.save(tree);
			return id;
		}catch (Exception e){
			return "创建失败：" + e;
		}
	}

	/**
	 * 删除节点
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delNode", method = RequestMethod.GET)
	@ResponseBody
	@Transactional(rollbackOn = Exception.class)
	public Map<String,Object> delNode(@RequestParam String id){
		Map<String,Object> result = new HashMap<>();
		if(StringUtils.isBlank(id)){
			result.put("result","删除失败，参数为空");
			return result;
		}
		try{
			Tree node = myTreeRepository.getOne(id);
			if(node == null){
				result.put("result","删除失败，节点不存在");
				return result;
			}
			myTreeRepository.deleteFirstStep(node.getTreeLeft(),node.getTreeRight());
			myTreeRepository.delSecondStep(node.getTreeLeft(),node.getTreeRight());
			myTreeRepository.delThirdStep(node.getTreeLeft(),node.getTreeRight());
			result.put("result","删除成功");
			result.put("被删除的节点的个数",(node.getTreeRight() - node.getTreeLeft() + 1) / 2);
			return result;
		}catch (Exception e){
			result.put("result","删除失败：" + e);
			return result;
		}
	}

	@RequestMapping(value = "/getPath", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getPath(@RequestParam String id){
		Map<String,Object> result = new HashMap<>();
		try{
			Tree node = myTreeRepository.getOne(id);
			if(node == null){
				result.put("result","查询失败，参数为空");
				return result;
			}
			List<Tree> pathTree = myTreeRepository.getPath(node.getTreeLeft(),node.getTreeRight());
			result.put("路径",pathTree);
			result.put("所处层级",myTreeRepository.getLevel(node.getTreeLeft(),node.getTreeRight()));
			return result;
		}catch (Exception e){
			return result;
		}
	}

	/**
	 * 将节点移动到parent节点下的positon位置
	 * @param id
	 * @param newParentNodeId
	 * @param sublingId 前一个Node的ID 0为开头，-1位结尾，否则为某节点ID
	 * 将节点2移动到节点1下面开头的位置：move_node(2, 1, 0)
	 * 将节点2移动到节点1下面末尾的位置：move_node(2, 1, -1)
	 * 将节点2移动到节点1下面且跟在节点3后面的位置：move_node(2, 1, 3)
	 * @return
	 */
	@RequestMapping(value = "/moveNode", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> moveNode(String id,String newParentNodeId,String sublingId){
		Map<String,Object> result = new HashMap<>();
		try{
			Tree node = myTreeRepository.getOne(id);
			if(node == null){
				result.put("result","移动失败，当前节点为空");
				return result;
			}
			Tree newParent = myTreeRepository.getOne(newParentNodeId);
			if(newParent == null){
				result.put("result","移动失败，新的父节点为空");
				return result;
			}
			// 插入到父节点下的第一个位置
			if(StringUtils.equals(sublingId,"0")){

				return result;
			}
			// 插入到父节点的最后一个位置
			if(StringUtils.equals(sublingId,"-1")){

				return result;
			}
			// 插入到父节点下的指定节点的后面
			Tree sublingNode = myTreeRepository.getOne(sublingId);
			if(sublingNode == null){
				result.put("result","移动失败，被指定的前序节点为空");
				return result;
			}
			if(!StringUtils.equals(sublingNode.getParentId(),newParentNodeId)){
				result.put("result","移动失败，被指定的前序节点不在指定的父节点之下");
				return result;
			}


			return result;
		}catch (Exception e){
			return result;
		}
	}
}
