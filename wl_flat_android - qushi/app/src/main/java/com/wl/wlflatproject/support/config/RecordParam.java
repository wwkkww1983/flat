package com.wl.wlflatproject.support.config;

import com.wl.wlflatproject.support.FunLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
* 
* @ClassName: RecordParamBean 
* @Description: TODO(录像配置) 
* @author xxy 
* @date 2016年3月19日 下午4:47:20 
*
*/

public class RecordParam extends BaseConfig {
	
	public static final String CONFIG_NAME = JsonConfig.RECORD;
	
	public boolean redundancy;
	public int packetLength;
	public int preRecord;
	public String recordMode;
	public String[][] timeSection;
	public String[][] mask;

	@Override
	public String getConfigName() {
		return CONFIG_NAME;
	}
	
	public String getConfigNameOfChannel(){
		return Config_Name_ofchannel;
	}
	
	@Override
	public boolean onParse(String json) {
		if (!super.onParse(json))
		{
			return false;
		}

		try {
			Config_Name_ofchannel = mJsonObj.getString("Name");
			JSONObject c_jsonobj = mJsonObj.getJSONObject(mJsonObj.getString("Name"));
			return onParse(c_jsonobj);
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

	}
	
	public boolean onParse(JSONObject obj) throws JSONException {
		if (null == obj) {
			return false;
		}
		
		try {
			redundancy = obj.optBoolean("Redundancy");
			packetLength = obj.optInt("PacketLength");
			preRecord = obj.optInt("PreRecord");
			recordMode = obj.optString("RecordMode");
				
			JSONArray timeSectionArray = obj.getJSONArray("TimeSection");
			if ( null != timeSectionArray ) {
				timeSection = new String[timeSectionArray.length()][];
				
				for ( int i = 0; i < timeSectionArray.length(); i ++ ) {
					JSONArray pArray = timeSectionArray.getJSONArray(i);
					if ( pArray.length() > 0 ) {
						String[] timeSecs = new String[pArray.length()];
						for ( int j = 0; j < pArray.length(); j ++ ) {
							timeSecs[j] = pArray.getString(j);
						}
						timeSection[i] = timeSecs;
					}
				}
			}
			
			JSONArray maskArray = obj.getJSONArray("Mask");
			if ( null != maskArray ) {
				mask = new String[maskArray.length()][];
				
				for ( int i = 0; i < maskArray.length(); i ++ ) {
					JSONArray pArray = maskArray.getJSONArray(i);
					if ( pArray.length() > 0 ) {
						String[] maskTemp = new String[pArray.length()];
						for ( int j = 0; j < pArray.length(); j ++ ) {
							maskTemp[j] = pArray.getString(j);
						}
						mask[i] = maskTemp;
					}
				}
			}
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		return true;
	}
	
	@Override
	public String getSendMsg() {
		super.getSendMsg();
		try {
			mJsonObj.put("Name", getConfigNameOfChannel());
			
			JSONObject c_json = null;
			if ( mJsonObj.isNull(getConfigNameOfChannel()) ) {
				c_json = new JSONObject();
			} else {
				c_json = mJsonObj.getJSONObject(getConfigNameOfChannel());
			}

			JSONArray maskArray = new JSONArray();
			if ( null != mask ) {
				for ( int i = 0; i < mask.length; i ++ ) {
					if ( null != mask[i] && mask[i].length > 0 ) {
						JSONArray pArray = new JSONArray();
						for ( int j = 0; j < mask[i].length; j ++ ) {
							pArray.put(mask[i][j]);
						}
						maskArray.put(pArray);
					}
				}
			}
			c_json.put("Mask", maskArray);
			
			
			c_json.put("PacketLength", packetLength);
			c_json.put("PreRecord", preRecord);
			c_json.put("RecordMode", recordMode);
			c_json.put("Redundancy", redundancy);
						
			JSONArray timeSectionArray = new JSONArray();
			if ( null != timeSection ) {
				for ( int i = 0; i < timeSection.length; i ++ ) {
					if ( null != timeSection[i] && timeSection[i].length > 0 ) {
						JSONArray pArray = new JSONArray();
						for ( int j = 0; j < timeSection[i].length; j ++ ) {
							pArray.put(timeSection[i][j]);
						}
						timeSectionArray.put(pArray);
					}
				}
			}
			c_json.put("TimeSection", timeSectionArray);
			
			mJsonObj.put(getConfigNameOfChannel(), c_json);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		FunLog.e(getConfigNameOfChannel(), "json:" + mJsonObj.toString());
		return mJsonObj.toString();
	}
	
	public int getPreRecordTime() {
		return preRecord;
	}
	
	public void setPreRecordTime(int preRecordTime) {
		preRecord = preRecordTime;
	}

	public int getPacketLength() {
		return packetLength;
	}
	
	public void setPacketLength(int pacLen) {
		packetLength = pacLen;
	}

	public String getRecordMode() {
		return recordMode;
	}
	
	public void setRecordMode(String mode) {
		recordMode = mode;
	}
}
