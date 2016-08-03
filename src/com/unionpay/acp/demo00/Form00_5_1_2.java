package com.unionpay.acp.demo00;

import java.util.HashMap;
import java.util.Map;

import com.unionpay.acp.sdk.SDKConfig;

/**
 * 名称：机构规范<br>
 * 功能： 5.1.2　发送短信验证码交易<br>
 * 后台类交易类<br>
 * 版本： 5.0<br>
 * 日期： 2014-07<br>
 * 作者： 中国银联ACP团队<br>
 * 版权： 中国银联<br>
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考。<br>
 */
public class Form00_5_1_2 extends DemoBase {

	
	public static Map<String, Object> setFormDate() {

		Map<String, Object> contentData = new HashMap<String, Object>();

		//固定填写
		contentData.put("version", "5.0.0");//M
		//默认取值：UTF-8
		contentData.put("encoding", "UTF-8");//M
		//取值：01（表示采用RSA）
		contentData.put("signMethod", "01");//M
		//取值:77
		contentData.put("txnType", "77");//M
		//用于区分发送短信的类型：00——开通短信01——实名认证短信02——消费短信03——代扣短信04——预授权
		contentData.put("txnSubType", "00");//M
		contentData.put("bizType", "000801");//M
		//基于绑定的支付交易上送
		contentData.put("bindId", "");//C
		contentData.put("channelType", "07");//M
		//0：普通商户直连接入1：收单机构接入2：平台类商户接入
		contentData.put("accessType", "1");//M
		//接入类型为收单机构接入时需上送
		contentData.put("acqInsCode", "00010000");//C
		//交易填写MCC码，接入类型为收单机构接入时需上送
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
		//　
		contentData.put("accType", "");//O
		//消费、预授权交易，收单机构端采集账号时必填，上送全卡号若与银联有约定，可上送卡号后4位
		contentData.put("accNo", "");//C
		//需与后续消费交易订单号一致
		contentData.put("orderId", getOrderId());// M
		//需与后续消费交易订单发送时间一致
		contentData.put("txnTime", getCurrentTime());// M
		//默认156（参考数据元说明）可选，若交易金额上送，币种未上送，默认156
		contentData.put("currencyCode", "156");//C
		//短信模板中涉及金额的需上送
		contentData.put("txnAmt", "1");//C
		//商户自定义保留域，交易应答时会原样返回
		contentData.put("reqReserved", "");//O
		//格式如下：{子域名1=值&子域名2=值&子域名3=值} 移动支付参考消费
		contentData.put("reserved", "");//O
		//有风险级别要求的商户必填 风险级别 {riskLevel=XX}
		contentData.put("riskRateInfo", "");//O
		//除跨行收单外其他上送手机号时，放于此域
		contentData.put("customerInfo", getCustomer(encoding));//C
		//当使用银联公钥加密密码等信息时，需上送加密证书的CertID
		contentData.put("encryptCertId", "");//C
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
		 String requestBackUrl = SDKConfig.getConfig()
				.getBackRequestUrl();
		Map<String, String> resmap = submitDate(setFormDate(),requestBackUrl);

		System.out.println(resmap.toString());
	}

}
