package com.unionpay.acp.demo00;

import java.util.HashMap;
import java.util.Map;

import com.unionpay.acp.sdk.SDKConfig;

/**
 * 名称：机构规范<br>
 * 功能：5.3.10　账单支付交易<br>
 * 前台类交易类<br>
 * 版本： 5.0<br>
 * 日期： 2014-07<br>
 * 作者： 中国银联ACP团队<br>
 * 版权： 中国银联<br>
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考。<br>
 */
public class Form00_5_3_10_f extends DemoBase {

	
	public static Map<String, Object> setFormDate() {

		Map<String, Object> contentData = new HashMap<String, Object>();

		//固定填写
		contentData.put("version", "5.0.0");//M
		//默认取值：UTF-8
		contentData.put("encoding", "UTF-8");//M
		//取值：01（表示采用RSA）
		contentData.put("signMethod", "01");//M
		//13
		contentData.put("txnType", "13");//M
		//01：便民缴费02：网上缴税03：信用卡还款04：保险缴费05：现金缴费（单销账）
		contentData.put("txnSubType", "00");//M
		//默认：000000 000601 ：账单缴费
		contentData.put("bizType", "000601");//M
		//07：互联网08：移动
		contentData.put("channelType", "07");//M
		//交易前台返回商户结果时使用，前台类交易需上送
	//	contentData.put("frontUrl", frontUrl);//C
		//交易后台返回商户结果时使用，如上送，则发送商户后台交易结果通知
		contentData.put("backUrl", backUrl);//M
		
		//0：普通商户直连接入1：收单机构接入2：平台类商户接入
		contentData.put("accessType", "1");//M
		//接入类型为收单机构接入时需上送
		contentData.put("acqInsCode", "00010000");//C
		//　
		contentData.put("merId", "802290049000180");//M
		//接入类型为收单机构接入时需上送
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
		//交易金额上送时，交易币种必送默认156 参看数据元说明
		contentData.put("currencyCode", "156");//C
		//后台类交易且卡号上送时上送；01：银行卡02：存折03：IC卡默认取值：01取值“03”表示以IC终端发起的IC卡交易，IC作为普通银行卡进行支付时，此域填写为“01”
		contentData.put("accType", "");//C
		//后台类交易需上送
		contentData.put("accNo", "");//C
		//网银支付，直接跳转至银行时必填（银行前移模式）
		contentData.put("issInsCode", "");//C
		//直缴模式必须填写
		contentData.put("txnAmt", "1");//C
		//buss_code 三元组：账单类型_地区码_附加地区码示例：D1_3300_0000 账单类型、地区码、附加地区码参考获取账单业务要素（BIZ）说明
		contentData.put("bussCode", "");//M
		//根据获取到的BIZ文件要求填写 账单查询/支付类交易中用于上送其他未定义的账单要素参看子域填法
		contentData.put("billQueryInfo", "");//C
		//填写参考数据元相关子域说明
		contentData.put("customerInfo", getCustomer(encoding));//O
		//商户自定义保留域，交易应答时会原样返回
		contentData.put("reqReserved", "");//O
		//包含多个子域，所有子域需用“{}”包含，子域间以“&”符号链接。格式如下：{子域名1=值&子域名2=值&子域名3=值}。具体子域的名称、取值根据商户不同而定。
		contentData.put("reserved", "");//O
		//前台交易，有IP防钓鱼要求的商户上送
		contentData.put("customerIp", "");//O
		//当使用银联公钥加密密码等信息时，需上送加密证书的CertID
		contentData.put("encryptCertId", "");//C
		//移动支付业务需要上送
		contentData.put("userMac", "");//O
		//先查询后缴费的交易，需上送查询交易的
		contentData.put("origQryId", "201408201508395217");//C
		//　
		contentData.put("termId", "");//C
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
		 String requestFrontUrl = SDKConfig.getConfig()
				.getFrontRequestUrl();
		
		 /**
			 * 创建表单
			 */
			String html = createHtml(requestFrontUrl, signData(setFormDate()));
			System.out.println(html);

	}

}
