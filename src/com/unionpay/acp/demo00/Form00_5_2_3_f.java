package com.unionpay.acp.demo00;

import java.util.HashMap;
import java.util.Map;

import com.unionpay.acp.sdk.SDKConfig;

/**
 * 名称：机构规范<br>
 * 功能： 5.2.3　银行卡余额查询<br>
 * 前台类交易类<br>
 * 版本： 5.0<br>
 * 日期： 2014-07<br>
 * 作者： 中国银联ACP团队<br>
 * 版权： 中国银联<br>
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考。<br>
 */
public class Form00_5_2_3_f extends DemoBase {

	
	public static Map<String, Object> setFormDate() {

		Map<String, Object> contentData = new HashMap<String, Object>();

		//固定填写
		contentData.put("version", "5.0.0");//M
		//默认取值：UTF-8
		contentData.put("encoding", "UTF-8");//M
		//取值：01（表示采用RSA）
		contentData.put("signMethod", "01");//M
		//取值:71
		contentData.put("txnType", "71");//M
		//默认：00
		contentData.put("txnSubType", "00");//M
		contentData.put("bizType", "000801");//M
		contentData.put("channelType", "07");//M
		//前台交易需填写
		contentData.put("frontUrl", frontUrl);//C
		//0：普通商户直连接入1：收单机构接入2：平台类商户接入
		contentData.put("accessType", "1");//M
		//　
		contentData.put("merId", "802290049000180");//M
		//接入类型为收单机构接入时需上送
		contentData.put("acqInsCode", "00010000");//C
		//交易填写MCC码，接入类型为收单机构接入时需上送
		contentData.put("merCatCode", "7011");//C
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
		contentData.put("orderId", getOrderId());// M
		//　
		contentData.put("txnTime", getCurrentTime());// M
		//默认156
		contentData.put("currencyCode", "156");//M
		//IC卡交易填写“03”
		contentData.put("accType", "");//O
		//　
		contentData.put("accNo", "");//M
		contentData.put("customerInfo", getCustomer(encoding));//C
		//　
		contentData.put("reqReserved", "");//O
		//　
		contentData.put("reserved", "");//O
		//当使用银联公钥加密密码等信息时，需上送加密证书的CertID
		contentData.put("encryptCertId", "");//C
		//移动支付业务需要上送
		contentData.put("userMac", "");//O
		//移动支付特殊使用标识支付要素使用的加密方式
		contentData.put("securityType", "");//C
		//有卡交易必填有卡交易信息域（组合域，用法详见数据元说明）
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
		 String requestFrontUrl = SDKConfig.getConfig()
				.getFrontRequestUrl();
		
		 /**
			 * 创建表单
			 */
			String html = createHtml(requestFrontUrl, signData(setFormDate()));
			System.out.println(html);

	}

}
