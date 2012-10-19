package cn.jinren.filter;

import cn.dreamfield.conf.KKConf;
import cn.dreamfield.model.NetInfo;
import cn.dreamfield.utils.ImageCutUtil;

public class ImageCutFilter implements StrFilter{
	
	private NetInfo netInfo;
	public ImageCutFilter(NetInfo netInfo) {
		this.netInfo = netInfo;
	}
	
	@Override
	public String doFilter(String str) {
		if(netInfo.getInfoImgUrl().startsWith("image")) {
			//…˙≥…Õº∆¨Àı¬‘Õº
			String sImgUrl = ImageCutUtil.ImageCut(KKConf.FILE_ROOT + netInfo.getInfoImgUrl(), KKConf.IMG_SMALL_WIDTH, KKConf.IMG_SMALL_HIGTH);
			netInfo.setInfoImgUrl("ico/" + sImgUrl);
		}
		return str;
	}

}
