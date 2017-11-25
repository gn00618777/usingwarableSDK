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

public class GestureRequestFragment extends Fragment {

    private ListenerForGestureRequestFragment mCallback;
    private View mView;
    private Button requestButton;

    public interface ListenerForGestureRequestFragment{

        void onRequestGesture();

    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        try {
            mCallback = (ListenerForGestureRequestFragment) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ListenForGestureRequestFragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mView == null){
            mView = inflater.inflate(R.layout.layout_request_gesture, null);
        }
        requestButton = (Button)mView.findViewById(R.id.request_gesture);
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onRequestGesture();
            }
        });

        return mView;
    } // onCreateView()

}
