package net.cb21.cbmagazine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;

/**
 * html로부터 매거진에 필요한 데이터를 추려내 저장하는 클래스
 * 
 * @since 2013. 9. 12.
 * @author Jung
 */
public class InfoContent {
	private int				key;
	private String			title;
	private String			address;
	private String			phoneNum;
	private String			summary;
	private Bitmap			imgSrc;

	public static String	BASE_URL	= "http://m.chungbuknadri.net";
	public static String	CONTENT_URL	= BASE_URL
												+ "/mobile/tour/view.do?tourKey=";
	public static String	IMG_URL		= BASE_URL + "/mobile/img.do?key=";

	// TODO: Constructor
	/**
	 * Default constructor, 외부 호출 금지
	 */
	private InfoContent() {
	} // InfoContent()

	// TODO: Getters
	/**
	 * @return key : 관광지 정보 페이지 키
	 */
	public int getKey() {
		return key;
	} // getKey()

	/**
	 * @return title : 관광지 제목(명칭)
	 */
	public String getTitle() {
		return title;
	} // getTitle()

	/**
	 * @return address :관광지 주소
	 */
	public String getAddress() {
		return address;
	} // getAddress()

	/**
	 * @return phoneNum : 관광지 전화번호
	 */
	public String getPhoneNum() {
		return phoneNum;
	} // getPhoneNum()

	/**
	 * @return summary : 관광지 정보(개요)
	 */
	public String getSummary() {
		return summary;
	} // getSummary()

	/**
	 * @return imgSrc : 이미지 소스
	 */
	public Bitmap getImgSrc() {
		return imgSrc;
	} // getImgSrc()

	// TODO: Setters
	/**
	 * @param key : 관광지 정보 페이지 키
	 */
	public void setKey(int key) {
		this.key = key;
	} // setKey(int key)

	/**
	 * @param title : 관광지 제목(명칭)
	 */
	public void setTitle(String title) {
		this.title = title;
	} // setTitle(String title)

	/**
	 * @param address : 관광지 주소
	 */
	public void setAddress(String address) {
		this.address = address;
	} // setAddress(String address)

	/**
	 * @param phoneNum : 관광지 전화번호
	 */
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	} // setPhoneNum(String phoneNum)

	/**
	 * @param summary : 관광지 정보(개요)
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	} // setSummary(String summary)

	/**
	 * @param imgSrc : 이미지 소스
	 */
	public void setImgSrc(Bitmap imgSrc) {
		this.imgSrc = imgSrc;
	} // setImgSrc(String imgSrc)

	/**
	 * 빈 데이터가 없는지를 체크하고 알려주는 메소드, 값의 무결성은 검사하지 않음
	 * 
	 * @return result : 문제 없을 경우 true
	 */
	public boolean isValidContent() {
		boolean result = true;

		if (getKey() <= 0)
			result = false;
		if (getTitle() == null || getTitle().trim().length() == 0)
			result = false;
		if (getAddress() == null || getAddress().trim().length() == 0)
			result = false;
		if (getPhoneNum() == null || getPhoneNum().trim().length() == 0)
			result = false;
		if (getSummary() == null || getSummary().trim().length() == 0)
			result = false;
		if (getImgSrc() == null)
			result = false;

		return result;
	} // isValidContent()

	// TODO: static Method
	/**
	 * InfoContent 생성을 위한 메소드, 키값을 받아 컨텐츠 페이지와 이미지 페이지를 분석하고 필요한 정보를 추출 및 저장한다.
	 * 
	 * @param key : 광고지 정보 페이지 접근을 위한 키
	 * @return mContent : 정보를 저장한 후의 InfoContent 객체
	 */
	public static InfoContent getContentFromKey(int key) {
		// 저장할 컨텐츠 생성
		InfoContent mContent = new InfoContent();
		mContent.setKey(key);
		Log.d("getContentFromKey", "initialize content by " + key + " : "
				+ mContent);

		AsyncTask<Void, Void, Boolean> contentCreator = mContent.new ContentCreator();
		contentCreator.execute();
		
		try {
			if (contentCreator.get(100, TimeUnit.SECONDS)) // 컨텐츠 작성 완료 대기 Timeout 100초
				return mContent; // 작성 완료시 컨텐츠 리턴
		} catch (Exception e) {
			Log.e("contentCreator.get", "ERROR : " + e.getMessage());
			return null; // 예외 발생시 null 리턴
		} // try get content
		
		return null; // 컨텐ㅊ
	} // getContentFromWeb(int key)

	/**
	 * InfoContent's inner class.</br> execute(key)를 통해 사용 private 클래스로, 외부에서는
	 * InfoContent 클래스를 통해 활용
	 * 
	 * @since 2013. 9. 15.
	 * @author Jung
	 */
	private class ContentCreator extends AsyncTask<Void, Void, Boolean> {
		@Override
		/**
		 * execute시 실행되는 문장, 상세 내용은 private 메소드를 이용
		 */
		protected Boolean doInBackground(Void... params) {
			Log.d("ContentCreator execute", "execute with key " + key);
			try {
				// 컨텐츠 정보 저장
				String contentPath = CONTENT_URL + key;
				URL contentUrl = new URL(contentPath);
				parseContent(contentUrl);

				// 이미지 정보 저장
				String imgPath = IMG_URL + key + "&type=tour";
				URL imgUrl = new URL(imgPath);
				parseImg(imgUrl);
			} catch (Exception e) {
				Log.e("ContentCreator execute",
						"execute Failure! - " + e.getMessage());
				return false;
			} // try create content
			return true;
		} // doInBackground(Integer... key)

		/**
		 * 컨텐츠 페이지를 파징해 infoContent에 저장한다.
		 * 
		 * @param contentUrl : 컨텐츠 페이지의 URL
		 */
		private void parseContent(URL contentUrl) throws Exception {
			Log.d("parseContent", "parse content start with : " + contentUrl);

			BufferedReader br = null;
			br = new BufferedReader(new InputStreamReader(
					contentUrl.openStream(), "UTF-8"));

			boolean encountTag = false;
			String line;
			int areaCount = 0;
			while ((line = br.readLine()) != null) {
				line = line.trim(); // 좌우 여백 제거 및 공백줄 제거
				if (line.contains("<div id=\"subject\">")) { // 제목 영역 접근
					Log.d("parseContent", "encount subject area"); // TODO : encount subject
					
					String title = line;
					title = title.replace("<div id=\"subject\">", ""); // 시작 태그 제거
					title = title.replace("</div>", ""); // 종료 태그 제거
					setTitle(title);
				} else if (line.contains("<div id=\"form_1\"")) { // 광고지 정보 영역 접근
					Log.d("parseContent", "encount info area"); // TODO : start info area
					
					encountTag = true;
				} else if (encountTag && line.contains("<li>")) { // 광고지 정보 영역 내의 <li>태그 접근
					if (areaCount == 0) { // 첫번째 <li>태그 : 주소
						Log.d("parseContent", "encount address area"); // TODO : encount address
						
						String address = ""; // 주소 정보 저장을 위한 임시 변수
						String addrStr; // 주소 스트링을 읽어들일 버퍼
						while (!((addrStr = br.readLine()).contains("</li>"))) { // 태그 종료시까지 정보 저장
							addrStr = addrStr.trim(); // 좌우 여백 제거 및 공백줄 제거
							if (addrStr.contains("<br />")) // 첫줄은 항상 <br>태그만을 포함
								continue; // 그냥 넘어감

							else if (addrStr.length() != 0) // 공백줄이 아닐 경우
								address += addrStr; // 주소 정보 추가
						} // while end of tag
						setAddress(address); // 저장된 주소 정보 적용
					} else if (areaCount == 1) { // 두번째 <li>태그 : 전화번호, 처리방식은 주소와 동일
						Log.d("parseContent", "encount phoneNum area"); // TODO : encount phoneNum
						
						String phoneNum = "";
						String phoneStr;
						while (!((phoneStr = br.readLine()).contains("</li>"))) {
							phoneStr = phoneStr.trim();
							if (phoneStr.contains("<br />"))
								continue;

							else if (phoneStr.length() != 0)
								phoneNum += phoneStr;
						} // while end of tag
						setPhoneNum(phoneNum);
					} else if (areaCount == 2) { // 세번째 <li>태그 : 개요, 처리방식은 주소와 동일
						Log.d("parseContent", "encount summary area"); // TODO : encount summary
						
						String summary = "";
						String sumStr;
						while (!((sumStr = br.readLine()).contains("</li>"))) {
							sumStr = sumStr.trim();
							if (sumStr.contains("<br />"))
								continue;

							else if (sumStr.length() != 0)
								summary += sumStr;
						} // while end of tag
						setSummary(summary);
						break; // 개요까지 저장 후 읽기 종료
					} // if-else if check <li> area
					areaCount++;
				} // if-else if 1_depth tag check
			} // while page not end
		} // parseContent(URL contentUrl)

		/**
		 * 이미지 페이지를 파징해 infoContent에 저장한다.
		 * 
		 * @param contentUrl : 컨텐츠 페이지의 URL
		 */
		private void parseImg(URL imgUrl) throws Exception {
			BufferedReader br = null;
			br = new BufferedReader(new InputStreamReader(imgUrl.openStream(),
					"UTF-8"));

			String line;
			while ((line = br.readLine()) != null) {
				line = line.trim(); // 좌우 여백 제거 및 공백줄 제거
				if (line.contains("<img")) { // 첫번째 <img 태그 접근
					Log.d("parseImg", "encount img tag"); // TODO : encount image
					
					String src = line.split(" ")[2]; // src 속성정보
					src = src.replace("src=", ""); // 경로값 이외 정보 제거
					src = src.replace("\"", ""); // 경로값 이외 정보 제거
					String srcPath = BASE_URL + src; // 부분 경로 -> 전체 경로
					
					URL srcUrl = new URL(srcPath);
					setImgSrc(BitmapFactory.decodeStream(srcUrl.openStream()));
					break; // 이미지는 하나만 저장하고 종료
				} // catch tag!
			} // while page not end
		} // parseImg(URL imgUrl)
	} // ContentCreator END
} // TourInfo END