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

public class CurrentFragment extends Fragment {

    //UI
    private View mView;
    private Button synButton;

    private float mVersion = 0;

    public interface ListenForCurrentFragment {
        public void onRequestCurrentLife();
    }

    ListenForCurrentFragment mCallback;

    // lift-cycle ----------------------------------------------------------------------------------
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            mCallback = (ListenForCurrentFragment) context;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ListenForCurrentFragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.layout_current, null);
        }

        synButton = (Button)mView.findViewById(R.id.current);


        return mView;
    } // onCreateView()

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        synButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onRequestCurrentLife();
            }
        });


    } // onActivityCreated()

}
