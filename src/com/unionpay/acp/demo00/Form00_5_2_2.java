package com.unionpay.acp.demo00;

import java.util.HashMap;
import java.util.Map;

import com.unionpay.acp.sdk.SDKConfig;

/**
 * 名称：机构规范<br>
 * 功能： 5.2.2　交易状态查询类交易<br>
 * 后台类交易类<br>
 * 版本： 5.0<br>
 * 日期： 2014-07<br>
 * 作者： 中国银联ACP团队<br>
 * 版权： 中国银联<br>
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考。<br>
 */
public class Form00_5_2_2 extends DemoBase {

	
	public static Map<String, Object> setFormDate() {

		Map<String, Object> contentData = new HashMap<String, Object>();

		//固定填写
		contentData.put("version", "5.0.0");//M
		//默认取值：UTF-8
		contentData.put("encoding", "UTF-8");//M
		//取值：01（表示采用RSA）
		contentData.put("signMethod", "01");//M
		//交易类型 00
		contentData.put("txnType", "00");//M
		//默认00
		contentData.put("txnSubType", "00");//M
		//默认:000000
		contentData.put("bizType", "000000");//M
		//接入类型为收单机构接入时需上送
		contentData.put("acqInsCode", "00010000");//C
		//0：普通商户直连接入1：收单机构接入2：平台类商户接入
		contentData.put("accessType", "1");//M
		//　
		contentData.put("merId", "802290049000180");//M
		//被查询交易的交易时间
		contentData.put("txnTime", getCurrentTime());// M
		//被查询交易的订单号
		contentData.put("orderId", getOrderId());// M
		//待查询交易的流水号
		contentData.put("queryId", "");//C
		contentData.put("reserved", "");//O
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
