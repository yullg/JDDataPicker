package com.yullg.JDDataPicker;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public class MyPageProcessor implements PageProcessor {

	private final Logger logger = LoggerFactory.getLogger(MyPageProcessor.class);
	private final MyItemPageProcessor myItemPageProcessor = new MyItemPageProcessor();
	private final MyListPageProcessor myListPageProcessor = new MyListPageProcessor();

	@Override
	public void process(Page page) {
		try {
			String url = page.getUrl().get();
			logger.info(Progress.index() + "/" + Progress.total() + "\t" + url);
			if (url.startsWith("https://item.jd.com/")) {
				myItemPageProcessor.process(page);
			} else {
				myListPageProcessor.process(page);
			}
		} catch (Exception e) {
			page.setSkip(true);
			logger.error("页面信息处理失败：" + page.getUrl(), e);
		}
	}

	@Override
	public Site getSite() {
		return Site.me().setRetryTimes(3).setSleepTime(1000);
	}

	class MyItemPageProcessor {
		public void process(Page page) throws Exception {
			try {
				String data_url = page.getUrl().get();
				String data_name = parseElementText(page, ".product-intro .itemInfo-wrap .sku-name");
				String data_price = parseElementText(page,
						".product-intro .itemInfo-wrap .summary-price-wrap .p-price");
				String data_commentCount = parseElementText(page,
						".product-intro .itemInfo-wrap .summary-price-wrap #comment-count .count");
				if (data_url != null || data_name != null || data_price != null || data_commentCount != null) {
					page.putField("data_url", data_url);
					page.putField("data_name", data_name);
					page.putField("data_price", data_price);
					page.putField("data_commentCount", data_commentCount);
				} else {
					page.setSkip(true);
					throw new IllegalStateException("没有检测到商品信息！");
				}
			} finally {
				Progress.addIndex(1);
			}
		}

		private String parseElementText(Page page, String selector) {
			try {
				return Jsoup.parseBodyFragment(page.getHtml().$(selector).get()).text().trim();
			} catch (Exception e) {
				return null;
			}
		}
	}

	class MyListPageProcessor {
		private final Pattern PATTERN = Pattern.compile("&page=(\\d+)");

		public void process(Page page) throws Exception {
			page.setSkip(true);// 列表页不产生结果只抓取商品链接并尝试进入下一页
			List<String> skus = page.getHtml().$("#J_goodsList li[data-sku]").regex("<li\\sdata-sku=\"(\\d+)\"").all();
			if (skus != null && skus.size() > 0) {
				for (String sku : skus) {
					page.addTargetRequest("https://item.jd.com/" + sku + ".html");
					Progress.addTotal(1);
				}
				String currPageUrl = page.getUrl().get();
				Matcher matcher = PATTERN.matcher(currPageUrl);
				if (matcher.find()) {
					String currPage = matcher.group(1);
					int nextPage = Integer.valueOf(currPage) + 1;
					String nextPageUrl = matcher.replaceAll("&page=" + nextPage);
					page.addTargetRequest(nextPageUrl);
					Progress.addTotal(1);
				} else {
					String nextPageUrl = currPageUrl + "&page=2";
					page.addTargetRequest(nextPageUrl);
					Progress.addTotal(1);
				}
			} else {
				throw new IllegalStateException("没有检测到商品列表！");
			}
		}
	}

}