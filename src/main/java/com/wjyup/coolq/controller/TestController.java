package com.wjyup.coolq.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wjyup.coolq.util.CQSDK;
import com.wjyup.coolq.vo.FriendListVO;
import com.wjyup.coolq.vo.GroupListVO;
import com.wjyup.coolq.vo.GroupMemberInfoVO;
import com.wjyup.coolq.vo.GroupMemberListVO;
import com.wjyup.coolq.vo.StrangerInfoVO;

/**
 * 供测试用的Controller
 * @author WJY
 */
@Controller
@RequestMapping(value = "testAction")
public class TestController {

	//获取群成员的信息
	@RequestMapping(value = "getGroupMemberInfo")
	public void getGroupMemberInfo(){
		GroupMemberInfoVO info = CQSDK.getGroupMemberInfo("215400054", "1066231345", true);
		System.out.println("群昵称："+info.getCard());
		System.out.println("昵称："+info.getName());
		System.out.println("QQ："+info.getQQ());
		System.out.println("等级："+info.getLevel());
	}
	
	//获取陌生人信息
	@RequestMapping(value = "getStrangerInfo")
	public void getStrangerInfo(){
		StrangerInfoVO info = CQSDK.getStrangerInfo("1066231345", true);
		System.out.println("昵称："+info.getName());
		System.out.println("性别："+(info.getGender() == 0 ? "男":"女"));
		System.out.println("QQ："+info.getQQ());
		System.out.println("年龄："+info.getOld());
	}
	
	//获取群列表
	@RequestMapping(value = "getGroupList")
	public void getGroupList(){
		List<GroupListVO> list = CQSDK.getGroupList();
		System.out.println("我共有"+list.size()+"个群");
		for(GroupListVO vo : list){
			System.out.println("=>群号:"+vo.getGroup());
			System.out.println("=>群昵称:"+vo.getGroupNickName());
			System.out.println("=>群创建者QQ:"+vo.getGroupOwner());
			System.out.println();
		}
	}
	
	//获取群成员列表1
	@RequestMapping(value = "getGroupMemberList1")
	public void getGroupMemberList1(){
		List<GroupMemberInfoVO> list = CQSDK.getGroupMemberList1("215400054");
		System.out.println("共有"+list.size()+"个成员");
		for(GroupMemberInfoVO vo : list){
			System.out.println("=>群号："+vo.getGroup());
			System.out.println("=>QQ："+vo.getQQ());
			System.out.println("=>昵称："+vo.getName());
			System.out.println("=>群名片："+vo.getCard());
			System.out.println("=>Q龄："+vo.getOld());
			System.out.println("=>等级："+vo.getLevel());
			System.out.println("=>性别："+(vo.getGender() == 0 ? "男":"女"));
			System.out.println("=>管理权限："+vo.getPower());
//			System.out.println("=>"+vo.getPower());
			System.out.println();
		}
	}
	
	//获取群成员列表2
	@RequestMapping(value = "getGroupMemberList2")
	public void getGroupMemberList2(){
		List<GroupMemberListVO> list = CQSDK.getGroupMemberList2("215400054");
		System.out.println("共有"+list.size()+"个成员");
		for(GroupMemberListVO vo : list){
			System.out.println("=>群名片："+vo.getName());
			System.out.println("=>QQ："+vo.getQq());
			System.out.println("=>是创建者："+(vo.getIsCreater() == 1 ? "是":"否"));
			System.out.println("=>是管理员："+(vo.getIsManager() == 1 ? "是":"否"));
			System.out.println();
		}
	}
	
	//获取好友列表的方法
	@RequestMapping(value = "getFriendList")
	public void getFriendList(){
		List<FriendListVO> list = CQSDK.getFriendList();
		System.out.println("共有"+list.size()+"个好友");
		for(FriendListVO vo : list){
			System.out.println("=>昵称："+vo.getName());
			System.out.println("=>QQ："+vo.getQq());
			System.out.println();
		}
	}
}
