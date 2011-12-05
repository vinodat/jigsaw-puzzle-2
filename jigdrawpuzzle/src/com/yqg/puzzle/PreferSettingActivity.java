package com.yqg.puzzle;


import com.yqg.puzzle.utils.Constants;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.widget.Toast;

public class PreferSettingActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener{


	private static final String TAG = "PreferSettingActivity";

	private ListPreference mListPreferences = null;
	private CheckBoxPreference mCheckBoxPreferences = null;
	
	private Intent mResultData = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Load the XML preferences file
        addPreferencesFromResource(R.xml.setting_preferences);
        mListPreferences = (ListPreference) getPreferenceScreen().findPreference(Constants.KEY_PREFER_LEVEL);
        mCheckBoxPreferences = (CheckBoxPreference) getPreferenceScreen().findPreference(Constants.KEY_PREFER_AUDIO);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// Set up a listener whenever a key changes
	    getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if(mResultData == null){
			mResultData = new Intent();
		}
		if(key.equals(Constants.KEY_PREFER_LEVEL) ){
			mResultData.putExtra(Constants.KEY_PREFERENCES_LEVEL_SETTING_MODIFIED, true);
		}else if(key.equals(Constants.KEY_PREFER_AUDIO)){
			mResultData.putExtra(Constants.KEY_PREFERENCES_AUDIO_SETTING_MODIFIED, true);
		}
		setResult(RESULT_OK, mResultData);
	}
}
