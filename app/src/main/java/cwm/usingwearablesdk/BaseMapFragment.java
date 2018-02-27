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
import android.widget.Toast;

public class BaseMapFragment extends Fragment {

    private View mView;
    private Button updateButton;
    private Button eraseOledButton;
    private Button eraseBitMapButton;
    private Button eraseFontButton;

    ListenForBaseMapFragment mCallback;

    // Container Activity must implement this interface
    public interface ListenForBaseMapFragment {
         void onStartUpdateBaseMap();
         void onPressEraseOLEDButton();
         void onPressEraseBitMapButton();
         void onPressEraseFontButton();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            mCallback = (ListenForBaseMapFragment) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ListenForBaseMapFragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_basemap, null);
        }

        updateButton = (Button) mView.findViewById(R.id.update);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onStartUpdateBaseMap();
            }
        });
        eraseOledButton = (Button)mView.findViewById(R.id.oled_erase);
        eraseBitMapButton = (Button)mView.findViewById(R.id.bitmap_erase);
        eraseFontButton = (Button)mView.findViewById(R.id.font_erase);
        eraseOledButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressEraseOLEDButton();
            }
        });

        eraseBitMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressEraseBitMapButton();
            }
        });

        eraseFontButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressEraseFontButton();
            }
        });

        return mView;
    }
}
