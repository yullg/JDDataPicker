package com.yullg.JDDataPicker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ExcelSupport {

	public static void convert() throws Exception {
		File folder = new File(App.DATA_PATH);
		File[] files = folder.listFiles((dir, name) -> {
			return name.toLowerCase().endsWith(".txt");
		});
		for (int i = 0; i < files.length; i++) {
			List<Goods> goodses = new ArrayList<>();
			try (InputStream is = new FileInputStream(files[i])) {
				List<String> lines = IOUtils.readLines(is, "UTF-8");
				for (int j = 0, j_size = lines.size(); j < j_size; j++) {
					JSONObject json = JSONObject.parseObject(lines.get(j));
					Goods goods = new Goods();
					goods.setUrl(json.getString("data_url"));
					goods.setName(json.getString("data_name"));
					goods.setPrice(json.getString("data_price"));
					goods.setCommentCount(json.getString("data_commentCount"));
					goodses.add(goods);
				}
			}
			File excelFile = new File(files[i].getAbsolutePath() + ".xls");
			try (InputStream is = ExcelSupport.class.getClassLoader().getResourceAsStream("excel.xls");
					OutputStream os = new FileOutputStream(excelFile)) {
				Context context = new Context();
				context.putVar("goodses", goodses);
				JxlsHelper.getInstance().processTemplate(is, os, context);
			}
		}
	}

}