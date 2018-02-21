package cwm.usingwearablesdk;

/**
 * Created by user on 2017/9/10.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cwm.wearablesdk.settings.BodySettings;

public class PersonalInfoFragment extends Fragment {
    private View mView;
    private Button ensure;
    private Button request;
    private Button sync;
    private EditText oldEdit;
    private EditText highEdit;
    private EditText sexEdit;
    private EditText weightEdit;

    private int mOld = 0;
    private int mHigh = 0;
    private String mSex = null;
    private int mWeight = 0;

    ListenForPersonalInfoFragment mCallback;

    // Container Activity must implement this interface
    public interface ListenForPersonalInfoFragment {
        public void onPersonalInfoSaveToUserConfig();
        public void onPersonalRequest();
        public void onSycRequest();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            mCallback = (ListenForPersonalInfoFragment) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ListenForPersonalInfoFragment");
        }
    }

    // lift-cycle ----------------------------------------------------------------------------------
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((!oldEdit.getText().toString().matches("")) &&
                        (!highEdit.getText().toString().matches("")) &&
                        (!sexEdit.getText().toString().matches("")) &&
                        (!weightEdit.getText().toString().matches(""))) {

                    mOld = Integer.parseInt(oldEdit.getText().toString());
                    mHigh = Integer.parseInt(highEdit.getText().toString());
                    mSex = sexEdit.getText().toString();
                    mWeight = Integer.parseInt(weightEdit.getText().toString());

                    mCallback.onPersonalInfoSaveToUserConfig();

                    Toast.makeText(getContext(),"已儲存設定在手機中",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(),"請正確填妥資料!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPersonalRequest();
            }
        });
        sync = (Button)mView.findViewById(R.id.sync);
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onSycRequest();
            }
        });

    } // onActivityCreated()

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    } // onCreate()

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.layout_personal_info, null);
        }
        oldEdit = (EditText)mView.findViewById(R.id.old_edit);
        highEdit = (EditText)mView.findViewById(R.id.high_edit);
        sexEdit = (EditText)mView.findViewById(R.id.sex_edit);
        weightEdit = (EditText)mView.findViewById(R.id.weight_edit);
        ensure = (Button)mView.findViewById(R.id.ensure);
        request = (Button)mView.findViewById(R.id.request);

        return mView;
    } // onCreateView()

    @Override
    public  void onPause() {
        super.onPause();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("OLD", Integer.parseInt(oldEdit.getText().toString()));
        editor.putInt("HIGH", Integer.parseInt(highEdit.getText().toString()));
        editor.putString("SEX", sexEdit.getText().toString());
        editor.putInt("WEIGHT", Integer.parseInt(weightEdit.getText().toString()));
        editor.apply();
    }

    @Override
    public void onResume(){
        super.onResume();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        oldEdit.setText(Integer.toString(preferences.getInt("OLD", 0)));
        highEdit.setText(Integer.toString(preferences.getInt("HIGH", 0)));
        sexEdit.setText(preferences.getString("SEX", ""));
        weightEdit.setText(Integer.toString(preferences.getInt("WEIGHT", 0)));

    }
    // function
    public int getOld(){ return mOld;}

    public int getHigh(){ return mHigh;}

    public String getSex() { return mSex;}

    public int getWeight() { return mWeight;}

    public void updateSetting(BodySettings body){
        oldEdit.setText(Integer.toString(body.getOld()));
        highEdit.setText(Integer.toString(body.getHight()));
        if(body.getSex() == 0)
         sexEdit.setText("M");
        else
            sexEdit.setText("F");
        weightEdit.setText(Integer.toString(body.getWeight()));

    }
}
