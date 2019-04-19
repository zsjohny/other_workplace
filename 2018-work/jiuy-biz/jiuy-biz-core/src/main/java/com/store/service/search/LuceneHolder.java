package com.store.service.search;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleDocValuesField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.springframework.beans.factory.annotation.Autowired;

import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.SearchConstants;
import com.jiuyuan.entity.search.SearchProductVO;
import com.jiuyuan.entity.search.SearchWeight;
import com.jiuyuan.service.common.ShopGlobalSettingService;


/*
 * @Scope("singleton")
 * 
 * @Component
 */
//@Component
public class LuceneHolder {

	// @Autowired(required = false)
	private IndexWriter indexWriter;

	@Autowired(required = false)
	private Analyzer analyzer;
	
	@Autowired
	private ShopGlobalSettingService globalSettingService;

	private static QueryParser _parser = null;
	private static final String prefixHTML = "<font color='red'>";
	private static final String suffixHTML = "</font>";

	protected synchronized QueryParser getQueryParser(SearchWeight weight) {
		if (_parser == null) {
			Map<String, Float> boosts = new HashMap<String, Float>();
			List<String> fieldList = new ArrayList<String>();

			weight.getSearch_objects().forEach((vo) -> {
				boosts.put(vo.getMatch_name(), vo.getWeight());
				fieldList.add(vo.getMatch_name());
			});

			String[] fields = new String[fieldList.size()];
			fieldList.toArray(fields);

			_parser = new MultiFieldQueryParser(fields, analyzer, boosts);
//	        boosts.put(SearchConstants.FCATEGORY, 1.0f);
//	        boosts.put(SearchConstants.NAME, 1.0f);
//			_parser = new MultiFieldQueryParser(new String[]{SearchConstants.FCATEGORY, SearchConstants.NAME}, analyzer, boosts);
			
		}
		return _parser;
	}

	public Map<String, Object> search(String keywords, int start, int size, Sort sort, SearchWeight weight)
			throws IOException, ParseException, InvalidTokenOffsetsException {

		Map<String,Object> data = new HashMap<>();
		
		List<SearchProductVO> list = new ArrayList<SearchProductVO>();
		IndexSearcher searcher = getSearcher();

		QueryParser parser = getQueryParser(weight);
		// parser.setAllowLeadingWildcard(true);

		Query query = parser.parse(keywords);
		TopDocs topDocs = null;
		if (sort != null) {
			long time = System.currentTimeMillis();
			topDocs = searcher.search(query, start + size, sort);
		} else {
			topDocs = searcher.search(query, start + size);
		}
		
		

		data.put("totalHits", topDocs.totalHits);
		
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		int end = size + start < topDocs.totalHits ? size + start : topDocs.totalHits;
		// SimpleHTMLFormatter simpleHtmlFormatter = new
		// SimpleHTMLFormatter(prefixHTML, suffixHTML);
		// Highlighter highlighter = new Highlighter(simpleHtmlFormatter, new
		// QueryScorer(query));
		// TokenStream tokenStream = null;
		Document doc;
		SearchProductVO vo = null;
		String deductStr = (String) globalSettingService.getJsonObject(GlobalSettingName.JIUCOIN_DEDUCTION_SETTING).get("activityText");//玖币抵扣文本
		for (int i = start; i < end; i++) {
			vo = new SearchProductVO();
			int docId = scoreDocs[i].doc;
			doc = searcher.doc(docId);

			Explanation exp = searcher.explain(query, docId);

			vo.setScore(exp.getValue());

			vo.setId(Long.parseLong(doc.get(SearchConstants.ID)));
			vo.setName(doc.get(SearchConstants.NAME));
			vo.setImage(doc.get(SearchConstants.IMAGE));
			vo.setCash(Double.parseDouble(doc.get(SearchConstants.CASH)));
			vo.setIsPromotion((doc.get(SearchConstants.ISPROMOTION)).equals("true") ? true : false);
			vo.setPromotionCash(Double.parseDouble(doc.get(SearchConstants.PROMOTIONCASH)));
			vo.setJiuCoin(Integer.parseInt(doc.get(SearchConstants.FJIUCOIN)));
			vo.setMarketPrice(Double.parseDouble(doc.get(SearchConstants.MARKETPRICE)));
			vo.setSubscriptLogo(doc.get(SearchConstants.SUBSCRIPTLOGO));
			if(Double.parseDouble(doc.get(SearchConstants.DEDUCTPERCENT)) > 0){
				vo.setDeductDesc(deductStr + doc.get(SearchConstants.DEDUCTPERCENT)+ "%");
				
			}
			// tokenStream = analyzer.tokenStream("title", new
			// StringReader(goods));
			// goods = highlighter.getBestFragment(tokenStream, goods);
			// vo.setfTitle(goods == null ? doc.get("goods") : goods);
			vo.setCreateTime(Long.parseLong(doc.get(SearchConstants.CREATETIME)));
			vo.setSaleTotalCount(Integer.parseInt(doc.get(SearchConstants.SALETOTALCOUNT)));
			vo.setVisitCount(Integer.parseInt(doc.get(SearchConstants.VISITCOUNT)));
			vo.setWeight(Integer.parseInt(doc.get(SearchConstants.WEIGHT)));
			vo.setType(0); // 商品搜索
			
			//测试用
			vo.setfBrandName(doc.get(SearchConstants.FBRANDNAME));
			vo.setfCategoryName(doc.get(SearchConstants.FCATEGORY));
			vo.setfColor(doc.get(SearchConstants.FCOLOR));
			vo.setfSeason(doc.get(SearchConstants.FSEASON));
			vo.setfSize(doc.get(SearchConstants.FSIZE));
			vo.setfTag(doc.get(SearchConstants.FTAG));
			vo.setfTitle(doc.get(SearchConstants.FTITLE));

			list.add(vo);
		}
		
		data.put("resultList", list);
		return data;
	}

	/**
	 * 根据页码和分页大小获取上一次的最后一个ScoreDoc
	 */
	private ScoreDoc getLastScoreDoc(int pageIndex, int pageSize, Query query, IndexSearcher searcher)
			throws IOException {
		if (pageIndex == 1)
			return null;// 如果是第一页就返回空
		int num = pageSize * (pageIndex - 1);// 获取上一页的数量
		TopDocs tds = searcher.search(query, num);
		return tds.scoreDocs[num - 1];
	}

//	public List<SearchProductVO> search(String params, int pageNo, int pageSize, double begin_price, double end_price,
//			ScoreDoc after, Sort sort, SearchWeight weight) {
//		IndexSearcher searcher = null;
//		List<SearchProductVO> vo_list = null;
//		try {
//			searcher = getSearcher();
//			QueryParser parser = getQueryParser(weight);
//			parser.setAllowLeadingWildcard(true);
//			Query query = parser.parse(params);
//			TopDocs topDocs = searcher.searchAfter(after, query, pageSize);
//			int pages = (topDocs.totalHits + pageSize - 1) / pageSize;
//			int intPageNo = pageNo > pages ? pages : pageNo;
//			if (intPageNo < 1) {
//				intPageNo = 1;
//			}
//			vo_list = search(params, (intPageNo - 1) * pageSize, pageSize, sort, weight);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//		return vo_list;
//	}

	public boolean writeIndex(List<SearchProductVO> list) {
		boolean flag = true;
		try {
			for (SearchProductVO vo : list) {
				Document document = builderDocument(vo);
				indexWriter.addDocument(document);
			}
			indexWriter.commit();
		} catch (IOException e) {
			try {
				indexWriter.rollback();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			flag = false;
		}
		return flag;
	}

	public boolean writeIndex(SearchProductVO vo) {
		boolean flag = true;
		try {
			Document document = builderDocument(vo);
			indexWriter.addDocument(document);
			indexWriter.commit();
		} catch (IOException e) {
			e.printStackTrace();
			try {
				indexWriter.rollback();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			flag = false;
		}
		return flag;

	}

	/**
	 * update index
	 * 
	 * @return
	 * @throws IOException
	 */
	public void update(String gid, SearchProductVO vo) {
		try {
			Document doc = builderDocument(vo);
			org.apache.lucene.index.Term term = new org.apache.lucene.index.Term("gid", doc.get("gid"));
			indexWriter.updateDocument(term, doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除指定索引 根据商品id
	 * 
	 * @return true if delete successfully or false if failed.
	 * @throws IOException
	 */
	public boolean deleteIndex(String id) {
		boolean flag = true;
		try {
			Term term = new Term("gid", id);
			indexWriter.deleteDocuments(term);
			indexWriter.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				indexWriter.rollback();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			flag = false;
		}
		return flag;
	}

	/**
	 * Delete all index .
	 * 
	 * @param isdeletefile
	 * @return
	 */
	public boolean deleteAllIndex(boolean isdeletefile) {
		boolean flag = true;
		try {
			indexWriter.deleteAll();
			indexWriter.commit();
		} catch (IOException e) {
			e.printStackTrace();
			try {
				indexWriter.rollback();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			flag = false;
		}
		return flag;
	}

	private IndexSearcher getSearcher() throws IOException {
		return new IndexSearcher(DirectoryReader.open(indexWriter.getDirectory()));
	}

	private Document builderDocument(SearchProductVO vo) {
		Document doc = new Document();
		DecimalFormat    df   = new DecimalFormat("##.##"); 
		if (vo.getName() == null || vo.getImage() == null) return doc;
		// store for display
		
		Field name = new StoredField(SearchConstants.NAME, vo.getName());
		Field image = new StoredField(SearchConstants.IMAGE, vo.getImage());
		Field cash = new StoredField(SearchConstants.CASH, vo.getCash());
		Field isPromotion = new StoredField(SearchConstants.ISPROMOTION, vo.getIsPromotion() ? "true" : "false");
		Field promotionCash = new StoredField(SearchConstants.PROMOTIONCASH, vo.getPromotionCash());
		Field jiuCoin = new StoredField(SearchConstants.FJIUCOIN, vo.getJiuCoin());
		Field marketPrice = new StoredField(SearchConstants.MARKETPRICE, vo.getMarketPrice());
		Field subscriptLogo = new StoredField(SearchConstants.SUBSCRIPTLOGO, vo.getSubscriptLogo());
		Field deductPercent = new StoredField(SearchConstants.DEDUCTPERCENT, df.format(vo.getDeductPercent()));

		// store for test
		Field createTime = new StoredField(SearchConstants.CREATETIME, vo.getCreateTime());
		Field weight = new StoredField(SearchConstants.WEIGHT, vo.getWeight());
		Field saleTotalCount = new StoredField(SearchConstants.SALETOTALCOUNT, vo.getSaleTotalCount());
		Field visitCount = new StoredField(SearchConstants.VISITCOUNT, vo.getVisitCount());

		// sort
		Field fCreateTime = new NumericDocValuesField(SearchConstants.FCREATETIME, vo.getCreateTime());
		Field fWeight = new NumericDocValuesField(SearchConstants.FWEIGHT, vo.getWeight());
		Field fCash = new DoubleDocValuesField(SearchConstants.FCASH, vo.getCurrentCash());
		Field fSaleTotalCount = new NumericDocValuesField(SearchConstants.FSALETOTALCOUNT, vo.getSaleTotalCount());
		Field fVisitCount = new NumericDocValuesField(SearchConstants.FVISITCOUNT, vo.getVisitCount());

		// index
		Field id = new StringField(SearchConstants.ID, String.valueOf(vo.getId()), Store.YES);
//		Field fTitle = new TextField(SearchConstants.FTITLE, vo.getfTitle(), Store.NO);
		
//	Field fBrandName = new TextField(SearchConstants.FBRANDNAME, vo.getfBrandName(), Store.NO);
//		Field fColor = new TextField(SearchConstants.FCOLOR, vo.getfColor(), Store.NO);
//		Field fSize = new StringField(SearchConstants.FSIZE, vo.getfSize(), Store.NO);
//		Field fSeason = new TextField(SearchConstants.FSEASON, vo.getfSeason(), Store.NO);
//		Field fCategory = new TextField(SearchConstants.FCATEGORY, vo.getfCategoryName(), Store.NO);
//		Field fTag = new TextField(SearchConstants.FTAG, vo.getfTag(), Store.NO);

		Field fTitle = new TextField(SearchConstants.FTITLE, vo.getfTitle(), Store.YES);
		Field fBrandName = new TextField(SearchConstants.FBRANDNAME, vo.getfBrandName(), Store.YES);
		fBrandName.setBoost(vo.getfCategoryWeight());
		Field fColor = new TextField(SearchConstants.FCOLOR, vo.getfColor(), Store.YES);
		Field fSize = new StringField(SearchConstants.FSIZE, vo.getfSize(), Store.YES);
		Field fSeason = new TextField(SearchConstants.FSEASON, vo.getfSeason(), Store.YES);
		Field fTag = new TextField(SearchConstants.FTAG, vo.getfTag(), Store.YES);
		
		Field fCategory = new TextField(SearchConstants.FCATEGORY, vo.getfCategoryName(), Store.YES);
		fCategory.setBoost(vo.getfBrandWeight());
		fTitle.setBoost(vo.getfBrandWeight()+vo.getfCategoryWeight());

		doc.add(id);
		doc.add(name);
		doc.add(image);
		doc.add(cash);
		doc.add(isPromotion);
		doc.add(promotionCash);
		doc.add(jiuCoin);
		doc.add(marketPrice);
		doc.add(subscriptLogo);
		doc.add(deductPercent);
		doc.add(createTime);
		doc.add(weight);
		doc.add(saleTotalCount);
		doc.add(visitCount);
		doc.add(fCreateTime);
		doc.add(fWeight);
		doc.add(fSaleTotalCount);
		doc.add(fVisitCount);
		doc.add(fCash);
		doc.add(fTitle);
		doc.add(fBrandName);
		doc.add(fColor);
		doc.add(fSize);
		doc.add(fSeason);
		doc.add(fTag);
		doc.add(fCategory);
		
		return doc;
	}

	public IndexWriter getIndexWriter() {
		return indexWriter;
	}

	public void setIndexWriter(IndexWriter indexWriter) {
		this.indexWriter = indexWriter;
	}

	public Analyzer getAnalyzer() {
		return analyzer;
	}

	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}

}
