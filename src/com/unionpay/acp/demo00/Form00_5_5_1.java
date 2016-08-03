package com.unionpay.acp.demo00;

import java.util.HashMap;
import java.util.Map;

import com.unionpay.acp.sdk.SDKConfig;

/**
 * 名称：机构规范<br>
 * 功能：5.5.1　文件传输类交易<br>
 * 后台类交易类<br>
 * 版本： 5.0<br>
 * 日期： 2014-07<br>
 * 作者： 中国银联ACP团队<br>
 * 版权： 中国银联<br>
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考。<br>
 */
public class Form00_5_5_1 extends DemoBase {

	
	public static Map<String, Object> setFormDate() {

		Map<String, Object> contentData = new HashMap<String, Object>();
		//固定填写
		contentData.put("version", "5.0.0");//M
		//默认取值：UTF-8
		contentData.put("encoding", "UTF-8");//M
		//取值：01（表示采用RSA）
		contentData.put("signMethod", "01");//M
		//取值:76
		contentData.put("txnType", "76");//M
		//01：对账文件下载
		contentData.put("txnSubType", "01");//M
		//默认:000000
		contentData.put("bizType", "000000");//M
		//0：普通商户直连接入1：收单机构接入
		contentData.put("accessType", "1");//M
		//商户类型为商户接入时必须上送
		contentData.put("merId", "802290049000180");//C
		//接入类型为收单机构接入时需上送
		contentData.put("acqInsCode", "00010000");//C
		//　
		contentData.put("settleDate", "1212");//M
		//　
		contentData.put("txnTime", getCurrentTime());// M
		//依据实际业务情况定义参考5.5.1.1：商户/机构索取的文件类型约定
		contentData.put("fileType", "00");//M
		//商户自定义保留域，交易应答时会原样返回
		contentData.put("reqReserved", "");//O



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
		 String requestFileTransUrl = SDKConfig.getConfig()
				.getFileTransUrl();
		Map<String, String> resmap = submitDate(setFormDate(),requestFileTransUrl);

		System.out.println(resmap.toString());
		
		deCodeFileContent(resmap);
	}

}
