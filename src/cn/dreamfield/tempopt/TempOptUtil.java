package cn.dreamfield.tempopt;

import java.util.ArrayList;

import cn.dreamfield.model.NetInfo;
import cn.jinren.test.KK;

public class TempOptUtil {
	public static void trimTitleAndIntro(ArrayList<NetInfo> netInfos) {
		for(NetInfo netInfo : netInfos) {
			String newName = netInfo.getInfoName().substring(1, netInfo.getInfoName().length() - 1);
			netInfo.setInfoName(newName);
			KK.DEBUG(newName);
			String newIntro = netInfo.getInfoIntro().trim();
			newIntro = newIntro.replace("...(", "");
			netInfo.setInfoIntro(newIntro);
			KK.DEBUG(newIntro);
		}
	}
}
