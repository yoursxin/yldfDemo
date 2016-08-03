package com.unionpay.acp.demo00;

import java.util.HashMap;
import java.util.Map;

import com.unionpay.acp.sdk.SDKConfig;

/**
 * 名称：机构规范<br>
 * 功能： 5.2.1　银联全渠道支付开通查询交易<br>
 * 后台类交易类<br>
 * 版本： 5.0<br>
 * 日期： 2014-07<br>
 * 作者： 中国银联ACP团队<br>
 * 版权： 中国银联<br>
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考。<br>
 */
public class Form00_5_2_1 extends DemoBase {

	
	public static Map<String, Object> setFormDate() {

		Map<String, Object> contentData = new HashMap<String, Object>();

		//固定填写
		contentData.put("version", "5.0.0");//M
		//默认取值：UTF-8
		contentData.put("encoding", "UTF-8");//M
		//取值：01（表示采用RSA）
		contentData.put("signMethod", "01");//M
		//取值：78
		contentData.put("txnType", "78");//M
		//00-  账号查询（默认账号查询）01-  手机号查询(只支持商户直连接入模式)
		contentData.put("txnSubType", "00");//M
		contentData.put("bizType", "000801");//M
		//0：普通商户直连接入1：收单机构接入2：平台类商户接入
		contentData.put("accessType", "1");//M
		contentData.put("channelType", "07");//M
		//接入类型为收单机构接入时需上送
		contentData.put("acqInsCode", "00010000");//C
		contentData.put("merId", "802290049000180");//M
		//接入类型为收单机构接入时需上送
		contentData.put("merCatCode", "7011");//C
		//接入类型为收单机构接入时需上送
		contentData.put("merName", "银联收单接入测试商户");//C
		//接入类型为收单机构接入时需上送
		contentData.put("merAbbr", "测试商户");//C
		//商户端生成
		contentData.put("orderId", getOrderId());// M
		//商户发送交易时间
		contentData.put("txnTime", getCurrentTime());// M
		//　
		contentData.put("accType", "");//O
		//交易子类00-  账号查询填写（接入类型为收单机构接入时不支持卡尾号查询）
		contentData.put("accNo", "");//C
		//交易子类01-  手机号查询填写请求时：如果是后台开通，借记卡手机号必填，证件类型，证件号码及姓名选填；贷记卡需上送：手机号，有效期，CVN2必填，证件类型，证件号，姓名选填后台开通时：需填写用户的短信验证码至该域
		contentData.put("customerInfo", getCustomer(encoding));//C
		//商户自定义保留域，交易应答时会原样返回
		contentData.put("reqReserved", "");//O
		//格式如下：{子域名1=值&子域名2=值&子域名3=值} 移动支付参考消费
		contentData.put("reserved", "");//O
		//当使用银联公钥加密密码等信息时，需上送加密证书的CertID
		contentData.put("encryptCertId", "");//C
		//渠道类型为语音支付时使用用法见VPC交易信息组合域子域用法 
		contentData.put("vpcTransData", "");//C
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
