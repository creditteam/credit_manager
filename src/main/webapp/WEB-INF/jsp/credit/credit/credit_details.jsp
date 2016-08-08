<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 

<div class="modal-header">
    <div class='bootstrap-dialog-header'>
		<div class='bootstrap-dialog-close-button'
			style='display: block;'>
			<button class='close' data-dismiss='modal' aria-label='Close'>×</button>
		</div>
		<div class='bootstrap-dialog-title'>债权详情</div>
	</div>
</div>
<div class="modal-body">
    <div class="container-fluid">
    				         <b><font color="blue">基本信息</font></b>
                 		     <hr>
                                <dl class="dl-horizontal">
                                    <dt>债权类型：</dt>
                                    <dd>
                                    				<c:if test="${credit.crType==1 }">民间借贷</c:if>
												    <c:if test="${credit.crType==2 }">应收账款</c:if>
												    <c:if test="${credit.crType==3 }">银行借贷</c:if>
												    <c:if test="${credit.crType==4 }">互联网金融</c:if>
												    <c:if test="${credit.crType==5 }">小额信贷</c:if>
												    <c:if test="${credit.crType==6 }">典当担保</c:if>
												    <c:if test="${credit.crType==7 }">司法裁决</c:if>
                                    </dd>
                               </dl>
								<dl class="dl-horizontal">
									<dt>债权金额：</dt>
									<dd> ${credit.crAmount }(万元)</dd>
								</dl>
                               <dl class="dl-horizontal">
                                    <dt>处置方式：</dt>
                                    <dd>
                                    <c:forEach items="${credit.disTypes}" var="item">
										<c:if test="${item eq '1'}">诉讼</c:if>
										<c:if test="${item eq '2'}">催收</c:if>
										<c:if test="${item eq '3'}">债权转让</c:if>
									</c:forEach>
                                    </dd>
                               </dl>
								<dl class="dl-horizontal">
									<dt>支付佣金范围：</dt>
									<dd>${credit.commisionRange }</dd>
								</dl>
                               
                               <dl class="dl-horizontal">
									<dt>发布日期：</dt>
									<dd>
									<fmt:formatDate value="${credit.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
									</dd>
								</dl>
								<dl class="dl-horizontal">
									<dt>债权开始日期：</dt>
									<dd>
									<fmt:formatDate value="${credit.openDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
									</dd>
								</dl>

	                             <dl class="dl-horizontal">
	                                    <dt>债务人名称：</dt>
	                                    <dd> ${credit.contactName }</dd>
	                             </dl>
	                            <dl class="dl-horizontal">
	                                    <dt>债务人电话：</dt>
	                                    <dd> ${credit.contactNumber }</dd>
	                             </dl>
	                             
					      <b><font color="blue">债务方信息</font></b>
					      <hr>
								<dl class="dl-horizontal">
									<dt>债权方联系人：</dt>
									<dd> ${credit.debtName }</dd>
								</dl>
							    <dl class="dl-horizontal">
                                    <dt>所在省份：</dt>
                                    <dd>${credit.debtProvince }</dd>
                                </dl>
								<dl class="dl-horizontal">
									<dt>所在城市：</dt>
									<dd>${credit.debtCity }</dd>
								</dl>
								<dl class="dl-horizontal">
									<dt>债权方联系电话：</dt>
									<dd>${credit.debtPhone }</dd>
								</dl>
								<dl class="dl-horizontal">
									<dt>债权凭证：</dt>
									<dd><c:forEach items="${credit.debtProofs}" var="item">
										<c:if test="${not empty item}">
										<a onclick="showBigImage('${basePath}${item}')">
											<img alt="" src="${basePath}${item}" width="50px" height="50px"/>&nbsp;&nbsp;
										</a>
										</c:if>
									</c:forEach></dd>
								</dl>
								<dl class="dl-horizontal">
									<dt>债权描述：</dt>
									<dd> ${credit.description }</dd>
								</dl>
                               
    </div>
</div>
<div class="modal-footer">
    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
</div>