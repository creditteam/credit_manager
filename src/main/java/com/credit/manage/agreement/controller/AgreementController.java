package com.credit.manage.agreement.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.credit.manage.agreement.service.AgreementWebService;
import com.credit.manage.entity.Agreement;
import com.credit.manage.filemanager.service.UploadFileService;
import com.gvtv.manage.base.controller.BaseController;
import com.gvtv.manage.base.util.DataUtil;
import com.gvtv.manage.base.util.PageData;
import com.gvtv.manage.base.util.PropertiesUtil;

@Controller
@RequestMapping(value="/agreement")
public class AgreementController extends BaseController{

	private static Logger logger = LoggerFactory.getLogger(AgreementController.class);
	
	@Resource
	private AgreementWebService agreementWebService;
	@Resource
	private UploadFileService uploadFileService;
	
	
	@RequestMapping(value="/list")
	@ResponseBody
	public List<Agreement> list(){
		List<Agreement> result = null;
		try {
			PageData pd = super.getPageData();
			result = agreementWebService.list(pd);
		} catch (Exception e) {
			logger.error("list blog error", e);
			result = new ArrayList<Agreement>();
		}
		return result;
	}
	
	@RequestMapping(value="/page")
	public ModelAndView page(){
		ModelAndView mv = super.getModelAndView();
		String headUrl = PropertiesUtil.getValue("showImgPath");
		mv.addObject("headUrl", headUrl);
		mv.setViewName("credit/agreement/agree_add");
		return mv;
	}
	
	/**
	 * 签订居间服务协议(前期)
	 * @return
	 */
	@RequestMapping(value="/saveAgree")
	@ResponseBody
	public PageData signAgreeFront(Integer creditId,Integer userId){
		PageData result = new PageData();
		try {
			PageData pd = super.getPageData();
			pd.put("creditId", creditId);
			pd.put("userId", userId);
			pd.put("agreeType", 1);
			List<Agreement> agreeList = agreementWebService.list(pd);
			if(null != agreeList && agreeList.size() > 0){
				result.put("status", 0);
				result.put("msg", "已发起<居间服务协议(前期)>,请等待确认!");
			}else{
				Agreement agree = new Agreement();
				agree.setCreditId(creditId);
				agree.setUserId(userId);
				agree.setAgreeType((short)1);
				agree.setSignStatus((short)0);
				agree.setSignTime(new Date());
				agreementWebService.saveAgree(agree);
				result.put("status", 1);
			}
		} catch (Exception e) {
			logger.error("save agree error", e);
			result.put("status", 0);
			result.put("msg", "添加协议失败");
		}
		return result;
	}
	
	
	@RequestMapping(value="/addOrUpd", method=RequestMethod.POST)
	@ResponseBody
	public PageData saveOrUpd(Agreement agree){
		PageData result = new PageData();
		try{
			String images = "";
			if(null != agree.getUploadFiles()){
				for(int i=0;i< agree.getUploadFiles().length;i++){
					if(0 != agree.getUploadFiles()[i].getSize()){
						String newFileName = DataUtil.getRandomStr();
						String fileName = agree.getUploadFiles()[i].getOriginalFilename();
						fileName = "agree"+newFileName + fileName.substring(fileName.lastIndexOf("."), fileName.length());
						uploadFileService.uploadFile(PropertiesUtil.getValue("saveImgPath")+"uploadFile/agree", agree.getUploadFiles()[i], fileName);
						if(i == 0){
							images += "uploadFile/agree/"+fileName;
						}else{
							images += ";uploadFile/agree/"+fileName;
						}
					}
				}
				agree.setAgreeImg(images);
			}
			agree.setSignStatus((short)1);
			agree.setSignTime(new Date());
			agreementWebService.deleteByCreditId(agree);
			agreementWebService.saveAgree(agree);
			result.put("status", 1);
		}catch(Exception e){
			logger.error("addOrUpd blog error", e);
			result.put("status", 0);
			result.put("msg", "操作失败");
		}
		return result;
	}
	
	@RequestMapping(value="/delete")
	@ResponseBody
	public PageData delete(Integer id){
		PageData result = new PageData();
		try{
			int line = agreementWebService.deleteById(id);
			if(line>0){
				result.put("status", 1);
			}else{
				result.put("status", 0);
				result.put("msg", "删除失败或者为不可删除状态");
			}
		}catch(Exception e){
			logger.error("delete blog error", e);
			result.put("status", 0);
			result.put("msg", "删除失败");
		}
		return result;
	}

	@RequestMapping(value="/delebyCredit")
	@ResponseBody
	public PageData delebyCredit(Integer creditId,Short agreeType){
		PageData result = new PageData();
		try{
			Agreement agree = new Agreement();
			agree.setCreditId(creditId);
			agree.setAgreeType(agreeType);
			int line = agreementWebService.deleteByCreditId(agree);
			if(line>0){
				result.put("status", 1);
			}else{
				result.put("status", 0);
				result.put("msg", "删除失败或者为不可删除状态");
			}
		}catch(Exception e){
			logger.error("delete blog error", e);
			result.put("status", 0);
			result.put("msg", "删除失败");
		}
		return result;
	}
	
	@RequestMapping(value="/updStatus")
	@ResponseBody
	public PageData updStatus(Integer id,Short signStatus){
		PageData result = new PageData();
		try{
			Agreement agree = new Agreement();
			agree.setId(id);
			agree.setSignStatus(signStatus);
			agreementWebService.updateAgreeStatus(agree);
			result.put("status", 1);
		}catch(Exception e){
			logger.error("delete blog error", e);
			result.put("status", 0);
			result.put("msg", "删除失败");
		}
		return result;
	}
	
	
}
