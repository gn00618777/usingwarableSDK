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
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.util.Log;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import cwm.wearablesdk.TabataSettings;

public class TabataIntervalFragment extends Fragment {

    private View mView;

    private TextView intervalStartView;
    private TextView intervalCountView;

    private Button intervalStartButton;
    private Button intervalCountButton;
    private Button intervalEndButton;

    private String intervalStart = "";
    private String intervalCount = "";

    // Container Activity must implement this interface
    public interface ListenForTabataIntervalFragment {
        public void onPressItervalStartButton();
        public void onPressIntervalCountButton();
        public void onPressIntervalEndButton();

    }

    private ListenForTabataIntervalFragment mCallback;


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            mCallback = (ListenForTabataIntervalFragment) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ListenForTabataPrepareFragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_tabata_interval, null);
        }
        intervalStartView = (TextView)mView.findViewById(R.id.interval_start_view);
        intervalStartView.setText(intervalStart);
        intervalCountView = (TextView)mView.findViewById(R.id.interval_count_view);
        intervalCountView.setText(intervalCount);

        intervalStartButton = (Button)mView.findViewById(R.id.interval_start);
        intervalCountButton = (Button)mView.findViewById(R.id.interval_count);
        intervalEndButton = (Button)mView.findViewById(R.id.interval_end);

        intervalStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressItervalStartButton();
            }
        });
        intervalCountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressIntervalCountButton();
            }
        });
        intervalEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressIntervalEndButton();
            }
        });

        return mView;
    }

    public void setIntervalStartView(String s){
        intervalStart = s;
    }
    public void setIntervalCountView(String s){
        intervalCount = s;
    }



}
