package com.maisi.video.obj;

public class WXAPIConst {
	//不合法的调用凭证
	public final int invalidcredential = 40001;
	//不合法的grant_type
	public final int invalidgrant_type = 40002;
	//不合法的OpenID
	public final int invalidopenid = 40003;
	//不合法的媒体文件类型
	public final int invalidmediatype = 40004;
	//不合法的media_id
	public final int invalidmedia_id = 40007;
	//不合法的message_type
	public final int invalidmessagetype = 40008;
	//不合法的图片大小
	public final int invalidimagesize = 40009;
	//不合法的语音大小
	public final int invalidvoicesize = 40010;
	//不合法的视频大小
	public final int invalidvideosize = 40011;
	//不合法的缩略图大小
	public final int invalidthumbsize = 40012;
	//不合法的AppID
	public final int invalidappid = 40013;
	//不合法的access_token
	public final int invalidaccess_token = 40014;
	//不合法的菜单类型
	public final int invalidmenutype = 40015;
	//不合法的菜单按钮个数
	public final int invalidbuttonsize = 40016;
	//不合法的按钮类型
	public final int invalidbuttontype = 40017;
	//不合法的按钮名称长度
	public final int invalidbuttonnamesize = 40018;
	//不合法的按钮KEY长度
	public final int invalidbuttonkeysize = 40019;
	//不合法的url长度
	public final int invalidbuttonurlsize = 40020;
	//不合法的子菜单按钮个数
	public final int invalidsubbuttonsize = 40023;
	//不合法的子菜单类型
	public final int invalidsubbuttontype = 40024;
	//不合法的子菜单按钮名称长度
	public final int invalidsubbuttonnamesize = 40025;
	//不合法的子菜单按钮KEY长度
	public final int invalidsubbuttonkeysize = 40026;
	//不合法的子菜单按钮url长度
	public final int invalidsubbuttonurlsize = 40027;
	//不合法或已过期的code
	public final int invalidcode = 40029;
	//不合法的refresh_token
	public final int invalidrefresh_token = 40030;
	//不合法的template_id长度
	public final int invalidtemplate_idsize = 40036;
	//不合法的template_id
	public final int invalidtemplate_id = 40037;
	//不合法的url长度
	public final int invalidurlsize = 40039;
	//不合法的url域名
	public final int invalidurldomain = 40048;
	//不合法的子菜单按钮url域名
	public final int invalidsubbuttonurldomain = 40054;
	//不合法的菜单按钮url域名
	public final int invalidbuttonurldomain = 40055;
	//不合法的url
	public final int invalidurl = 40066;
	//缺失access_token参数
	public final int access_tokenmissing = 41001;
	//缺失appid参数
	public final int appidmissing = 41002;
	//缺失refresh_token参数
	public final int refresh_tokenmissing = 41003;
	//缺失secret参数
	public final int appsecretmissing = 41004;
	//缺失二进制媒体文件
	public final int mediadatamissing = 41005;
	//缺失media_id参数
	public final int media_idmissing = 41006;
	//缺失子菜单数据
	public final int sub_menudatamissing = 41007;
	//缺失code参数
	public final int missingcode = 41008;
	//缺失openid参数
	public final int missingopenid = 41009;
	//缺失url参数
	public final int missingurl = 41010;
	//access_token超时
	public final int access_tokenexpired = 42001;
	//refresh_token超时
	public final int refresh_tokenexpired = 42002;
	//code超时
	public final int codeexpired = 42003;
	//需要使用GET方法请求
	public final int requireGETmethod = 43001;
	//需要使用POST方法请求
	public final int requirePOSTmethod = 43002;
	//需要使用HTTPS
	public final int requirehttps = 43003;
	//需要订阅关系
	public final int requiresubscribe = 43004;
	//空白的二进制数据
	public final int emptymediadata = 44001;
	//空白的POST数据
	public final int emptypostdata = 44002;
	//空白的news数据
	public final int emptynewsdata = 44003;
	//空白的内容
	public final int emptycontent = 44004;
	//空白的列表
	public final int emptylistsize = 44005;
	//二进制文件超过限制
	public final int mediasizeoutoflimit = 45001;
	//content参数超过限制
	public final int contentsizeoutoflimit = 45002;
	//title参数超过限制
	public final int titlesizeoutoflimit = 45003;
	//description参数超过限制
	public final int descriptionsizeoutoflimit = 45004;
	//url参数长度超过限制
	public final int urlsizeoutoflimit =  45005;
	//picurl参数超过限制
	public final int picurlsizeoutoflimit = 45006;
	//播放时间超过限制（语音为60s最大）
	public final int playtimeoutoflimit = 45007;
	//article参数超过限制
	public final int articlesizeoutoflimit = 45008;
	//接口调动频率超过限制
	public final int apifreqoutoflimit = 45009;
	//建立菜单被限制
	public final int createmenulimit = 45010;
	//频率限制
	public final int apilimit = 45011;
	//模板大小超过限制
	public final int templatesizeoutoflimit = 45012;
	//不能修改默认组
	public final int cantmodifysysgroup = 45016;
	//修改组名过长
	public final int cantsetgroupnametoolongsysgroup = 45017;
	//组数量过多
	public final int toomanygroupnownoneedtoaddnew = 45018;
	//接口未授权
	public final int apiunauthorized =50001;

	public static final String APP_ID = "wx89b88892ebddf590";
	public static final String AppSecret = "46db7877b95c1f98f0bc1ce1b108bcdb";
	public static final String AppPay_ID = "1503378551";
}
