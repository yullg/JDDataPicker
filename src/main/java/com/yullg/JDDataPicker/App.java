package com.yullg.JDDataPicker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

public class App {

	public static final String DATA_PATH = "C:\\JDDataPicker\\data";

	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
	private static final Scanner SCANNER = new Scanner(System.in);

	public static void main(String[] args) {
		try {
			if (args != null && args.length >= 0 && "init".equalsIgnoreCase(args[0])) {
				LOGGER.info("初始化中...");
				init();
				LOGGER.info("初始化完成");
			} else if (args != null && args.length >= 0 && "excel".equalsIgnoreCase(args[0])) {
				LOGGER.info("转存Excel中...");
				ExcelSupport.convert();
				LOGGER.info("转存Excel完成");
			} else if (args != null && args.length >= 1 && "pick".equalsIgnoreCase(args[0])) {
				System.setProperty("selenuim_config", "C:\\JDDataPicker\\config.ini");
				System.setProperty("webdriver.chrome.driver", "C:\\JDDataPicker\\chromedriver.exe");
				Spider.create(new MyPageProcessor()).addPipeline(new ConsolePipeline()).addPipeline(new MyPipeline())
						.setDownloader(new SeleniumDownloader()).addUrl(args[1]).thread(1).run();
			} else {
				LOGGER.warn("未指定操作模式");
			}
			System.out.println("程序已结束，按 Enter 键退出！");
			SCANNER.nextLine();
		} catch (Exception e) {
			LOGGER.error("程序异常退出", e);
		}
	}

	public static void init() throws Exception {
		new File(DATA_PATH).mkdirs();
		try (InputStream is = App.class.getClassLoader().getResourceAsStream("config.ini");
				OutputStream os = new FileOutputStream("C:\\JDDataPicker\\config.ini")) {
			IOUtils.copy(is, os);
		}
	}

}