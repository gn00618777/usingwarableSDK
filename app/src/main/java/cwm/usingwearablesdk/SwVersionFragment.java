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
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;

import org.w3c.dom.Text;

public class SwVersionFragment extends Fragment {

    //UI
    private View mView;
    private Button synButton;
    private TextView versionText;

    private float mVersion = 0;

    public interface ListenForSwVersionFragment {
        public void onRequestSwVersion();
    }

    ListenForSwVersionFragment mCallback;

    // lift-cycle ----------------------------------------------------------------------------------
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            mCallback = (ListenForSwVersionFragment) context;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ListenForSwVersionFragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.layout_sw_version, null);
        }

        synButton = (Button)mView.findViewById(R.id.sync);
        versionText = (TextView)mView.findViewById(R.id.version);


        return mView;
    } // onCreateView()

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        synButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onRequestSwVersion();
            }
        });
        versionText.setText(Float.toString(mVersion));


    } // onActivityCreated()

    //function
    public void setVersion(float version){
        Log.d("bernie","veriosn:"+Float.toString(version));
        mVersion = version;
    }

}
