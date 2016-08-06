package com.credit.manage.reward.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.credit.manage.entity.Reward;
import com.credit.manage.entity.WebUser;
import com.credit.manage.filemanager.service.UploadFileService;
import com.credit.manage.reward.service.RewardWebService;
import com.credit.manage.util.CitiesEnum;
import com.credit.manage.util.ProvinceEnum;
import com.credit.manage.webUser.service.WebUserWebService;
import com.gvtv.manage.base.controller.BaseController;
import com.gvtv.manage.base.util.DataUtil;
import com.gvtv.manage.base.util.PageData;
import com.gvtv.manage.base.util.PropertiesUtil;

@Controller
@RequestMapping(value="/reward")
public class RewardController extends BaseController{
	
	private static Logger logger = LoggerFactory.getLogger(RewardController.class);

	@Resource
	private RewardWebService rewardWebService;
	
	@Resource
	UploadFileService uploadFileService;
	@Resource
	private WebUserWebService webUserWebService;
	
	/**
	 * 跳转到列表
	 * @return
	 */
	@RequestMapping
	private ModelAndView page(){
		ModelAndView mv = super.getModelAndView();
		mv.setViewName("credit/reward/reward_list");
		return mv;
	}
	/**
	 * 导航债权首页
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@ResponseBody
	public PageData list(HttpServletRequest request){
		
		PageData result = null;
		try {
			PageData pd = super.getPageData();
			result = rewardWebService.pageList(pd);
		} catch (Exception e) {
			logger.error("list reward error", e);
			result = new PageData();
		}
		return result;
	}
	
	/**
	 * 跳转到新增悬赏页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/saveReward",method =RequestMethod.GET)
	public ModelAndView toSaveReward(Integer userId) throws Exception{
		
		List<String> provinceList = ProvinceEnum.takeAllValues();//省份list
		ModelAndView mv = this.getModelAndView();
		mv.addObject("provinceList", provinceList);
		mv.addObject("userId", userId);
		mv.setViewName("credit/reward/reward_add");
		return mv;
	}
	
	/**
	 * 新增悬赏信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/saveReward",method =RequestMethod.POST)
	@ResponseBody
	public PageData saveReward(Reward reward) throws Exception{
		PageData result = new PageData();
		try{
			reward.setRewardStatus((short)1);
			reward.setCreateTime(new Date());
			String images = "";
			if(null != reward.getUploadFiles()){
				for(int i=0;i< reward.getUploadFiles().length;i++){
					if(0 != reward.getUploadFiles()[i].getSize()){
						
						String newFileName = DataUtil.getRandomStr();
						String fileName = reward.getUploadFiles()[i].getOriginalFilename();
						fileName = "reward"+newFileName + fileName.substring(fileName.lastIndexOf("."), fileName.length());
						
						uploadFileService.uploadFile(PropertiesUtil.getValue("saveImgPath")+"uploadFile/reward", reward.getUploadFiles()[i], fileName);
						if(i == 0){
							images += "uploadFile/reward/"+fileName;
						}else{
							images += ";uploadFile/reward/"+fileName;
						}
					}
				}
			}
			if(null != reward.getId() && 0 != reward.getId()){
				if(StringUtils.isNotEmpty(images)){
					reward.setImages(images);
				}
				rewardWebService.updateReward(reward);
			}else{
				reward.setImages(images);
				reward.setCreateTime(new Date());
				rewardWebService.rewardSave(reward);
			}
			result.put("status", 1);
			
		}catch(Exception e){
			logger.error("addOrUpd reward error", e);
			result.put("status", 0);
			result.put("msg", "操作失败");
		}
		
		return result;
	}
	
	@RequestMapping(value="/updStatus")
	@ResponseBody
	public PageData updStatus(Integer id,Short rewardStatus){
		PageData result = new PageData();
		try{
			Reward reward = new Reward();
			reward.setId(id);
			reward.setRewardStatus(rewardStatus);
			rewardWebService.updateStatus(reward);
			result.put("status", 1);
		}catch(Exception e){
			logger.error("delete sample error", e);
			result.put("status", 0);
			result.put("msg", "删除失败");
		}
		return result;
	}
	
	/**
	 * 查询悬赏详情
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/rewardDetails",method =RequestMethod.GET)
	public ModelAndView rewardDetails() throws Exception{
		String id =super.getRequest().getParameter("id");
		if(id!=null&&id!=""){
			WebUser user = null;
			Reward reward = rewardWebService.findById(Integer.valueOf(id));
			if(null != reward.getUserId()){
				user = webUserWebService.getUserById(reward.getUserId());
			}
			if(null != reward.getImages()){
				reward.setImagesArry(reward.getImages().split(";"));
			}
			if(null == user){
				user = new WebUser();
				user.setNickname("未找到发布者信息");
			}
			ModelAndView mv = this.getModelAndView();
			mv.addObject("user",user);
			mv.addObject("reward", reward);
			mv.setViewName("/user/user_reward_details");
			return mv;
		}
		return null;
	}
	
	/**
	 * 查询悬赏详情
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/mlRewardInfo")
	@ResponseBody
	public Reward mlRewardDetails(String id) throws Exception{
		Reward reward = null;
		if(id!=null&&id!=""){
			reward = rewardWebService.findById(Integer.valueOf(id));
			if(null != reward.getImages()){
				reward.setImagesArry(reward.getImages().split(";"));
			}
			return reward;
		}
		return null;
	}
	/**
	 * 根据省份加载城市
	 * @param proName
	 * @return
	 */
	@RequestMapping(value="/loadCity")
	@ResponseBody
	public List<String> loadCityData(String proName){
		List<String> list = CitiesEnum.getCityByProvince(proName);
		return list;
	}
	
}
