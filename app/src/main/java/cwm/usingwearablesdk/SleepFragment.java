package cwm.usingwearablesdk;

/**
 * Created by user on 2017/9/10.
 */
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.content.Context;

public class SleepFragment extends Fragment {
    //UI
    private View mView;
    private TextView rawView;
    private TextView parserView;
    private Button requesrSleep;
    private String mRaw="";
    private String mParser = "";

    private String mDevice = null;
    private String mDeviceAddress = null;
    private Boolean mDeviceStatus = false;

    // Container Activity must implement this interface
    public interface ListenForSleepFragment {
        public void onRequestSleep();
    }

    ListenForSleepFragment mCallback;

    // lift-cycle ----------------------------------------------------------------------------------
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            mCallback = (ListenForSleepFragment) context;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ListenForSleepFragment");
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        requesrSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onRequestSleep();
            }
        });


    } // onActivityCreated()

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.activity_sleep, null);
        }
        requesrSleep = (Button)mView.findViewById(R.id.request);
        //rawView = (TextView)mView.findViewById(R.id.raw);
        parserView = (TextView)mView.findViewById(R.id.parser);
       // rawView.setText(mRaw);
        parserView.setText(mParser);
        return mView;
    } // onCreateView()

    public void setRawValue(String info){
        mRaw = info;
    }
    public void setParserValue(String info){
        mParser = info;
    }

}
