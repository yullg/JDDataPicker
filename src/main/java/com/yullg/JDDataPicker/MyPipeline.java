package com.yullg.JDDataPicker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class MyPipeline implements Pipeline {

	private final PrintWriter printWriter;

	public MyPipeline() throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String fileName = dateFormat.format(new Date()) + ".txt";
		printWriter = new PrintWriter(new OutputStreamWriter(
				new FileOutputStream(new File(App.DATA_PATH, fileName), true), StandardCharsets.UTF_8), true);
	}

	@Override
	public void process(ResultItems resultItems, Task task) {
		JSONObject json = new JSONObject();
		json.put("data_url", resultItems.<String>get("data_url"));
		json.put("data_name", resultItems.<String>get("data_name"));
		json.put("data_price", resultItems.<String>get("data_price"));
		json.put("data_commentCount", resultItems.<String>get("data_commentCount"));
		printWriter.println(json.toJSONString());
	}

}