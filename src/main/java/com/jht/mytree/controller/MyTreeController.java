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
}
