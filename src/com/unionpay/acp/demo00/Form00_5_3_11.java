package com.unionpay.acp.demo00;

import java.util.HashMap;
import java.util.Map;

import com.unionpay.acp.sdk.SDKConfig;

/**
 * 名称：机构规范<br>
 * 功能：5.3.11　代收类交易<br>
 * 后台类交易类<br>
 * 版本： 5.0<br>
 * 日期： 2014-07<br>
 * 作者： 中国银联ACP团队<br>
 * 版权： 中国银联<br>
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考。<br>
 */
public class Form00_5_3_11 extends DemoBase {

	
	public static Map<String, Object> setFormDate() {

		Map<String, Object> contentData = new HashMap<String, Object>();
		
		//固定填写
		contentData.put("version", "5.0.0");//M
		//默认取值：UTF-8
		contentData.put("encoding", "UTF-8");//M
		//取值：01（表示采用RSA）
		contentData.put("signMethod", "01");//M
		//取值：11 
		contentData.put("txnType", "11");//M
		//默认：00
		contentData.put("txnSubType", "00");//M
		contentData.put("bizType", "000801");//M
		contentData.put("channelType", "07");//M

		//交易后台返回商户结果时使用，如上送，则发送商户后台交易结果通知
		contentData.put("backUrl", backUrl);//M
		//0：普通商户直连接入1：收单机构接入2：平台类商户接入
		contentData.put("accessType", "1");//M
		//接入类型为收单机构接入时需上送
		contentData.put("acqInsCode", "00010000");//C
		//接入类型为收单机构接入时需上送
		contentData.put("merCatCode", "7011");//C
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
		//交易
		contentData.put("orderId", getOrderId());// M
		//交易
		contentData.put("txnTime", getCurrentTime());// M
		//　
		contentData.put("accType", "");//O
		//非绑定类交易时需上送卡号
		contentData.put("accNo", "");//M
		//　
		contentData.put("txnAmt", "1");//M
		//默认为156交易，填写参考数据元说明
		contentData.put("currencyCode", "156");//M
		//　
		contentData.put("customerInfo", getCustomer(encoding));//O
		//用于唯一标识绑定关系
		contentData.put("bindId", "");//O
		//填值参看数据元说明
		contentData.put("billType", "");//O
		//账单查询/支付类交易中填写具体账单号码用法一：账单查询/支付类交易中网上缴税用法，填写纳税人编码用法二：账单查询/支付类交易中信用卡还款用法，填写信用卡卡号
		contentData.put("billNo", "");//O
		//前台交易，有IP防钓鱼要求的商户上送
		contentData.put("customerIp", "");//C
		//格式为：yyyyMMdd-yyyyMMdd
		contentData.put("billPeriod", "");//O
		//商户自定义保留域，交易应答时会原样返回
		contentData.put("reqReserved", "");//O
		//格式如下：{子域名1=值&子域名2=值&子域名3=值} 移动支付参考消费
		contentData.put("reserved", "");//O
		//格式如下：{子域名1=值&子域名2=值&子域名3=值}有风险级别要求的商户必填 风险级别 {riskLevel=XX}
		contentData.put("riskRateInfo", "");//O
		//当使用银联公钥加密密码等信息时，需上送加密证书的CertID
		contentData.put("encryptCertId", "");//C
		//存折交易时必填
		contentData.put("issInsCode", "");//C
		//　
		contentData.put("termId", "");//O
		//有卡交易必填
		contentData.put("cardTransData", "");//C


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
