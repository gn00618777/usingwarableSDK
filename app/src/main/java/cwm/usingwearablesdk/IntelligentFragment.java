package cwm.usingwearablesdk;

import android.content.Context;
import android.os.Bundle;
import com.github.machinarius.preferencefragment.PreferenceFragment;

import android.preference.EditTextPreference;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.widget.Toast;

import cwm.wearablesdk.settings.IntelligentSettings;

public class IntelligentFragment extends PreferenceFragment{

    private Boolean isSendtaryRemind = true;
    private Boolean isHandUp = true;
    private Boolean isOnWear = true;
    private Boolean isDoubleTap = false;
    private Boolean isWristScroll = true;
    private Boolean isShake = false;
    private Boolean isSignificant = false;
    private int stepAim = 0;
    private int sedentaryT = 0;

    ListenerForIntellignetFragment mCallback;

    public interface ListenerForIntellignetFragment{

        void onIntelligentSaveToRing();
        void onIntelligentRequest();
        void onSycRequest();

    }

    // lift-cycle ----------------------------------------------------------------------------------
    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        try {
            mCallback = (ListenerForIntellignetFragment) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ListenForPersonalInfoFragment");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    } // onActivityCreated()

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.intellignet_settings);

        final SwitchPreference sedentarySwitch = (SwitchPreference) findPreference("sedentary_remind");
        final SwitchPreference handUpSwitch = (SwitchPreference) findPreference("hand_up");
        final SwitchPreference onWearSwitch = (SwitchPreference) findPreference("on-wear");
        final SwitchPreference wristScrollSwitch = (SwitchPreference) findPreference("wrist-scroll");
        final EditTextPreference editTextPreference = (EditTextPreference) findPreference("step_number_aim");
        final EditTextPreference editTextPreference1 = (EditTextPreference) findPreference("sedentary_remind_time");
        final Preference save = findPreference("intelligent_save");
        final Preference request = findPreference("intelligent_request");
        final Preference sync = findPreference("intelligent_sync");

        if (sedentarySwitch != null){
            sedentarySwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                @Override
                public boolean onPreferenceChange(Preference arg0, Object isVibrateOnObject){
                    boolean isEnabled = false;
                    isEnabled = sedentarySwitch.isChecked();
                    if(isEnabled == false)
                        isSendtaryRemind = true;
                    else
                        isSendtaryRemind = false;

                    return true;
                }
            });
        }
        if(handUpSwitch != null) {
            handUpSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference arg0, Object isVibrateOnObject) {
                    Boolean isEnabled = false;
                    isEnabled = handUpSwitch.isChecked();
                    if (isEnabled == false)
                        isHandUp = true;
                    else
                        isHandUp = false;

                    return true;

                }
            });
        }

        if(onWearSwitch != null) {
            onWearSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference arg0, Object isVibrateOnObject) {
                    Boolean isEnabled = false;
                    isEnabled = onWearSwitch.isChecked();
                    if (isEnabled == false)
                        isOnWear = true;
                    else
                        isOnWear = false;

                    return true;

                }
            });
        }

        if(wristScrollSwitch != null) {
            wristScrollSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference arg0, Object isVibrateOnObject) {
                    Boolean isEnabled = false;
                    isEnabled = wristScrollSwitch.isChecked();
                    if (isEnabled == false)
                        isWristScroll = true;
                    else
                        isWristScroll = false;

                    return true;
                }
            });
        }

        if(editTextPreference != null){
            editTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange(Preference arg0, Object isVibrateOnObject) {
                    stepAim = Integer.valueOf(editTextPreference.getEditText().getText().toString());
                   return true;
                }
            });
        }

        if(editTextPreference1 != null){
            editTextPreference1.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                public boolean onPreferenceChange(Preference arg0, Object isVibrateOnObject) {
                    sedentaryT = Integer.valueOf(editTextPreference1.getEditText().getText().toString());
                    return true;
                }
            });
        }

        if(save != null) {
            save.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){

                @Override
                public boolean onPreferenceClick(Preference  preference){
                    Toast.makeText(getContext(),"已儲存設定在手機中",Toast.LENGTH_SHORT).show();
                    mCallback.onIntelligentSaveToRing();
                    return true;
                }
            });
        }
        if(request != null) {
            request.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){

                @Override
                public boolean onPreferenceClick(Preference  preference){

                    mCallback.onIntelligentRequest();
                    return true;
                }
            });
        }
        if(sync != null) {
            sync.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){

                @Override
                public boolean onPreferenceClick(Preference  preference){

                    mCallback.onSycRequest();
                    return true;
                }
            });
        }
    } // onCreate()

    @Override
    public void onPause(){
        super.onPause();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("INTELLIGENT  SYNC AIM", stepAim);
        editor.commit();
    }
    @Override
    public void onResume(){
        super.onResume();

        final SwitchPreference sedentarySwitch = (SwitchPreference) findPreference("sedentary_remind");
        final SwitchPreference handUpSwitch = (SwitchPreference) findPreference("hand_up");
        final SwitchPreference onWearSwitch = (SwitchPreference) findPreference("on-wear");
        final SwitchPreference wristScrollSwitch = (SwitchPreference) findPreference("wrist-scroll");

        EditTextPreference aim = (EditTextPreference) findPreference("step_number_aim");
        EditTextPreference time = (EditTextPreference) findPreference("sedentary_remind_time");

        isSendtaryRemind = sedentarySwitch.isChecked();
        isHandUp = handUpSwitch.isChecked();
        isOnWear = onWearSwitch.isChecked();
        isWristScroll = wristScrollSwitch.isChecked();

        stepAim = Integer.valueOf(aim.getText());
        sedentaryT = Integer.valueOf(time.getText());

    }

    //function
    public boolean getSendtaryRemindStatus(){ return isSendtaryRemind;}
    public boolean getHandUpStatus(){ return isHandUp;}
    public boolean getOnWearStatus() {return isOnWear;}
    public boolean getWritstScrollStatus() {return isWristScroll;}
    public int getAim(){return stepAim;}
    public int getSedentartTime(){return sedentaryT;}

    public void updateSetting(IntelligentSettings settings){

        final SwitchPreference sedentarySwitch = (SwitchPreference) findPreference("sedentary_remind");
        final SwitchPreference handUpSwitch = (SwitchPreference) findPreference("hand_up");
        final SwitchPreference onWearSwitch = (SwitchPreference) findPreference("on-wear");
        final SwitchPreference wristScrollSwitch = (SwitchPreference) findPreference("wrist-scroll");

        EditTextPreference aim = (EditTextPreference) findPreference("step_number_aim");
        EditTextPreference time = (EditTextPreference) findPreference("sedentary_remind_time");

        boolean[] gesture = settings.getGesture();

        if(gesture[MainActivity.GESTURE.STEP_COINTER.ordinal()]){

        }
        else{

        }

        if(gesture[MainActivity.GESTURE.CUSTOMISED_PEDOMETER.ordinal()]){

        }
        else{

        }

        if(gesture[MainActivity.GESTURE.SIGNIFICANT_MOTION.ordinal()]){

        }
        else{

        }
        if(gesture[MainActivity.GESTURE.HAND_UP.ordinal()]){
             handUpSwitch.setChecked(true);
        }
        else{
             handUpSwitch.setChecked(false);
        }

        if(gesture[MainActivity.GESTURE.WATCH_TAKE_OFF.ordinal()]){
               onWearSwitch.setChecked(true);
        }
        else{
               onWearSwitch.setChecked(false);
        }


        if(gesture[MainActivity.GESTURE.ACTIVITY_RECOGNITION.ordinal()]){

        }
        else{

        }


        if(gesture[MainActivity.GESTURE.SLEEPING.ordinal()]){

        }
        else{

        }

        if(gesture[MainActivity.GESTURE.SEDENTARY.ordinal()]){
              sedentarySwitch.setChecked(true);
        }
        else{
              sedentarySwitch.setChecked(false);
        }

        if(gesture[MainActivity.GESTURE.WRIST_SCROLL.ordinal()]){
               wristScrollSwitch.setChecked(true);
        }
        else{
               wristScrollSwitch.setChecked(false);
        }

        if(gesture[MainActivity.GESTURE.SHAKE.ordinal()]){

        }
        else{

        }

        if(gesture[MainActivity.GESTURE.FALL.ordinal()]){

        }
        else{

        }

        if(gesture[MainActivity.GESTURE.FLOOR_CLIMBED.ordinal()]){

        }
        else{

        }

        if(gesture[MainActivity.GESTURE.SKIPPING.ordinal()]){

        }
        else{

        }

        aim.setText(Integer.toString(settings.getGoal()));
        time.setText(Integer.toString(settings.getTime()));

    }

}
