package com.unionpay.acp.demo00;

import java.util.HashMap;
import java.util.Map;

import com.unionpay.acp.sdk.SDKConfig;

/**
 * 名称：机构规范<br>
 * 功能： 5.3.1　消费交易<br>
 * 前台类交易类<br>
 * 版本： 5.0<br>
 * 日期： 2014-07<br>
 * 作者： 中国银联ACP团队<br>
 * 版权： 中国银联<br>
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考。<br>
 */
public class Form00_5_3_1_f extends DemoBase {

	
	public static Map<String, Object> setFormDate() {

		Map<String, Object> contentData = new HashMap<String, Object>();

		//固定填写
		contentData.put("version", "5.0.0");//M
		//默认取值：UTF-8
		contentData.put("encoding", "UTF-8");//M
		//取值：01（表示采用RSA）
		contentData.put("signMethod", "01");//M
		//取值：01 
		contentData.put("txnType", "01");//M
		//01：自助消费，通过地址的方式区分前台消费和后台消费（含无跳转支付）02：订购03：分期付款
		contentData.put("txnSubType", "00");//M
		// 
		contentData.put("bizType", "000801");//M
		contentData.put("channelType", "07");//M
		//前台返回商户结果时使用，前台类交易需上送
		contentData.put("frontUrl", frontUrl);//C
		//后台返回商户结果时使用，如上送，则发送商户后台交易结果通知
		contentData.put("backUrl", backUrl);//M
		//0：普通商户直连接入1：收单机构接入2：平台类商户接入
		contentData.put("accessType", "1");//M
		//接入类型为收单机构接入时需上送
		contentData.put("acqInsCode", "00010000");//C
		//填写MCC码，接入类型为收单机构接入时需上送
		contentData.put("merCatCode", "7011");//C
		//　
		contentData.put("merId", "802290049000180");//M
		//接入类型为收单机构接入时需上送
		contentData.put("merName", "银联收单接入测试商户");//C
		//接入类型为收单机构接入时需上送
		contentData.put("merAbbr", "测试商户");//C
		//商户类型为平台类商户接入时必须上送
		contentData.put("subMerId", "");//C
		//商户类型为平台类商户接入时必须上送
		contentData.put("subMerName", "");//C
		//商户类型为平台类商户接入时必须上送
		contentData.put("subMerAbbr", "");//C
		//商户端生成
		contentData.put("orderId", getOrderId());// M
		//商户发送交易时间
		contentData.put("txnTime", getCurrentTime());// M
		//后台类交易且卡号上送；跨行收单且收单机构收集银行卡信息时上送01：银行卡02：存折03：C卡默认取值：01取值“03”表示以IC终端发起的IC卡交易，IC作为普通银行卡进行支付时，此域填写为“01”
		contentData.put("accType", "01");//C
		//1、  后台类消费交易时上送全卡号或卡号后4位 2、  跨行收单且收单机构收集银行卡信息时上送、  3、前台类交易可通过配置后返回，卡号可选上送
		contentData.put("accNo", "");//C
		//交易单位为分
		contentData.put("txnAmt", "1");//M
		//默认为156交易 参考数据元说明
		contentData.put("currencyCode", "156");//M
		//1、后台类消费交易时上送2、跨行收单且收单机构收集银行卡信息时上送3、认证支付2.0，后台交易时可选Key=value格式（具体填写参考数据元说明）
		contentData.put("customerInfo", getCustomer(encoding));//C
		//PC1、前台类消费交易时上送2、认证支付2.0，后台交易时可选
		contentData.put("orderTimeout", "");//O
		//PC超过此时间用户支付成功的交易，不通知商户，系统自动退款，大约5个工作日金额返还到用户账户
		contentData.put("payTimeout", "");//O
		//　
		contentData.put("termId", "");//O
		//商户自定义保留域，交易应答时会原样返回
		contentData.put("reqReserved", "");//O
		//子域名： 活动号 marketId  移动支付订单推送时，特定商户可以通过该域上送该订单支付参加的活动号
		contentData.put("reserved", "");//O
		//格式如下：{子域名1=值&子域名2=值&子域名3=值}
		contentData.put("riskRateInfo", "");//O
		//当使用银联公钥加密密码等信息时，需上送加密证书的CertID；说明一下？目前商户、机构、页面统一套
		contentData.put("encryptCertId", "");//C
		//前台消费交易若商户上送此字段，则在支付失败时，页面跳转至商户该URL（不带交易信息，仅跳转）
		contentData.put("frontFailUrl", "");//O
		//分期付款交易，商户端选择分期信息时，需上送 组合域，填法见数据元说明
		contentData.put("instalTransInfo", "");//C
		//C  取值参考数据元说明
		contentData.put("defaultPayType", "");//O
		//C当账号类型为02-存折时需填写在前台类交易时填写默认银行代码，支持直接跳转到网银商户发卡银行控制系统应答返回
		contentData.put("issInsCode", "");//O
		//仅仅pc使用，使用哪种支付方式 由收单机构填写，取值为以下内容的一种或多种，通过逗号（，）分割。取值参考数据元说明
		contentData.put("supPayType", "");//O
		//移动支付业务需要上送
		contentData.put("userMac", "");//O
		//前台交易，有IP防钓鱼要求的商户上送
		contentData.put("customerIp", "");//C
		//绑定消费 需做绑定时填写 用于唯一标识绑定关系
		contentData.put("bindId", "");//O
		//绑定消费特殊商户交易控制用（如借贷分离）
		contentData.put("payCardType", "");//C
		//机构有卡接入使用，标识支付要素使用的加密方式01:RSA密钥体系
		contentData.put("securityType", "");//C
		//有卡交易必填有卡交易信息域
		contentData.put("cardTransData", "");//C
		//渠道类型为语音支付时使用
		contentData.put("vpcTransData", "");//C
		//移动支付上送
		contentData.put("orderDesc", "");//C
		return contentData;
	}

	public static void main(String[] args) {
		
	/**
		 * 参数初始化
		 * 在java main 方式运行时必须每次都执行加载
		 * 如果是在web应用开发里,这个方写在可使用监听的方式写入缓存,无须在这出现
		 */
		SDKConfig.getConfig().loadPropertiesFromSrc();

		
		/**
		 * 交易请求url 从配置文件读取
		 */
		 String requestFrontUrl = SDKConfig.getConfig()
				.getFrontRequestUrl();
		
		 /**
			 * 创建表单
			 */
			String html = createHtml(requestFrontUrl, signData(setFormDate()));
			System.out.println(html);

	}

}
