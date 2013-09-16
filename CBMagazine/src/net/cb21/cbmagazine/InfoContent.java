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
 * html�κ��� �Ű����� �ʿ��� �����͸� �߷��� �����ϴ� Ŭ����
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
	 * Default constructor, �ܺ� ȣ�� ����
	 */
	private InfoContent() {
	} // InfoContent()

	// TODO: Getters
	/**
	 * @return key : ������ ���� ������ Ű
	 */
	public int getKey() {
		return key;
	} // getKey()

	/**
	 * @return title : ������ ����(��Ī)
	 */
	public String getTitle() {
		return title;
	} // getTitle()

	/**
	 * @return address :������ �ּ�
	 */
	public String getAddress() {
		return address;
	} // getAddress()

	/**
	 * @return phoneNum : ������ ��ȭ��ȣ
	 */
	public String getPhoneNum() {
		return phoneNum;
	} // getPhoneNum()

	/**
	 * @return summary : ������ ����(����)
	 */
	public String getSummary() {
		return summary;
	} // getSummary()

	/**
	 * @return imgSrc : �̹��� �ҽ�
	 */
	public Bitmap getImgSrc() {
		return imgSrc;
	} // getImgSrc()

	// TODO: Setters
	/**
	 * @param key : ������ ���� ������ Ű
	 */
	public void setKey(int key) {
		this.key = key;
	} // setKey(int key)

	/**
	 * @param title : ������ ����(��Ī)
	 */
	public void setTitle(String title) {
		this.title = title;
	} // setTitle(String title)

	/**
	 * @param address : ������ �ּ�
	 */
	public void setAddress(String address) {
		this.address = address;
	} // setAddress(String address)

	/**
	 * @param phoneNum : ������ ��ȭ��ȣ
	 */
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	} // setPhoneNum(String phoneNum)

	/**
	 * @param summary : ������ ����(����)
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	} // setSummary(String summary)

	/**
	 * @param imgSrc : �̹��� �ҽ�
	 */
	public void setImgSrc(Bitmap imgSrc) {
		this.imgSrc = imgSrc;
	} // setImgSrc(String imgSrc)

	/**
	 * �� �����Ͱ� �������� üũ�ϰ� �˷��ִ� �޼ҵ�, ���� ���Ἲ�� �˻����� ����
	 * 
	 * @return result : ���� ���� ��� true
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
	 * InfoContent ������ ���� �޼ҵ�, Ű���� �޾� ������ �������� �̹��� �������� �м��ϰ� �ʿ��� ������ ���� �� �����Ѵ�.
	 * 
	 * @param key : ������ ���� ������ ������ ���� Ű
	 * @return mContent : ������ ������ ���� InfoContent ��ü
	 */
	public static InfoContent getContentFromKey(int key) {
		// ������ ������ ����
		InfoContent mContent = new InfoContent();
		mContent.setKey(key);
		Log.d("getContentFromKey", "initialize content by " + key + " : "
				+ mContent);

		AsyncTask<Void, Void, Boolean> contentCreator = mContent.new ContentCreator();
		contentCreator.execute();
		
		try {
			if (contentCreator.get(100, TimeUnit.SECONDS)) // ������ �ۼ� �Ϸ� ��� Timeout 100��
				return mContent; // �ۼ� �Ϸ�� ������ ����
		} catch (Exception e) {
			Log.e("contentCreator.get", "ERROR : " + e.getMessage());
			return null; // ���� �߻��� null ����
		} // try get content
		
		return null; // ���٤�
	} // getContentFromWeb(int key)

	/**
	 * InfoContent's inner class.</br> execute(key)�� ���� ��� private Ŭ������, �ܺο�����
	 * InfoContent Ŭ������ ���� Ȱ��
	 * 
	 * @since 2013. 9. 15.
	 * @author Jung
	 */
	private class ContentCreator extends AsyncTask<Void, Void, Boolean> {
		@Override
		/**
		 * execute�� ����Ǵ� ����, �� ������ private �޼ҵ带 �̿�
		 */
		protected Boolean doInBackground(Void... params) {
			Log.d("ContentCreator execute", "execute with key " + key);
			try {
				// ������ ���� ����
				String contentPath = CONTENT_URL + key;
				URL contentUrl = new URL(contentPath);
				parseContent(contentUrl);

				// �̹��� ���� ����
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
		 * ������ �������� ��¡�� infoContent�� �����Ѵ�.
		 * 
		 * @param contentUrl : ������ �������� URL
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
				line = line.trim(); // �¿� ���� ���� �� ������ ����
				if (line.contains("<div id=\"subject\">")) { // ���� ���� ����
					Log.d("parseContent", "encount subject area"); // TODO : encount subject
					
					String title = line;
					title = title.replace("<div id=\"subject\">", ""); // ���� �±� ����
					title = title.replace("</div>", ""); // ���� �±� ����
					setTitle(title);
				} else if (line.contains("<div id=\"form_1\"")) { // ������ ���� ���� ����
					Log.d("parseContent", "encount info area"); // TODO : start info area
					
					encountTag = true;
				} else if (encountTag && line.contains("<li>")) { // ������ ���� ���� ���� <li>�±� ����
					if (areaCount == 0) { // ù��° <li>�±� : �ּ�
						Log.d("parseContent", "encount address area"); // TODO : encount address
						
						String address = ""; // �ּ� ���� ������ ���� �ӽ� ����
						String addrStr; // �ּ� ��Ʈ���� �о���� ����
						while (!((addrStr = br.readLine()).contains("</li>"))) { // �±� ����ñ��� ���� ����
							addrStr = addrStr.trim(); // �¿� ���� ���� �� ������ ����
							if (addrStr.contains("<br />")) // ù���� �׻� <br>�±׸��� ����
								continue; // �׳� �Ѿ

							else if (addrStr.length() != 0) // �������� �ƴ� ���
								address += addrStr; // �ּ� ���� �߰�
						} // while end of tag
						setAddress(address); // ����� �ּ� ���� ����
					} else if (areaCount == 1) { // �ι�° <li>�±� : ��ȭ��ȣ, ó������� �ּҿ� ����
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
					} else if (areaCount == 2) { // ����° <li>�±� : ����, ó������� �ּҿ� ����
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
						break; // ������� ���� �� �б� ����
					} // if-else if check <li> area
					areaCount++;
				} // if-else if 1_depth tag check
			} // while page not end
		} // parseContent(URL contentUrl)

		/**
		 * �̹��� �������� ��¡�� infoContent�� �����Ѵ�.
		 * 
		 * @param contentUrl : ������ �������� URL
		 */
		private void parseImg(URL imgUrl) throws Exception {
			BufferedReader br = null;
			br = new BufferedReader(new InputStreamReader(imgUrl.openStream(),
					"UTF-8"));

			String line;
			while ((line = br.readLine()) != null) {
				line = line.trim(); // �¿� ���� ���� �� ������ ����
				if (line.contains("<img")) { // ù��° <img �±� ����
					Log.d("parseImg", "encount img tag"); // TODO : encount image
					
					String src = line.split(" ")[2]; // src �Ӽ�����
					src = src.replace("src=", ""); // ��ΰ� �̿� ���� ����
					src = src.replace("\"", ""); // ��ΰ� �̿� ���� ����
					String srcPath = BASE_URL + src; // �κ� ��� -> ��ü ���
					
					URL srcUrl = new URL(srcPath);
					setImgSrc(BitmapFactory.decodeStream(srcUrl.openStream()));
					break; // �̹����� �ϳ��� �����ϰ� ����
				} // catch tag!
			} // while page not end
		} // parseImg(URL imgUrl)
	} // ContentCreator END
} // TourInfo END